package nl.ithelden;

import java.util.EnumSet;
import java.util.Set;
import nl.ithelden.model.ndtrc.File;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FileCleanupDataTest {
    private static final Set<File.FileType> PHOTO_TYPES = EnumSet.of(File.FileType.jpg, File.FileType.jpeg, File.FileType.gif,
            File.FileType.png, File.FileType.bmp, File.FileType.jfif, File.FileType.tiff, File.FileType.webp);

    @Test
    void mediatypeIsInferredFromFiletypeWhenMissing() {
        for (File.FileType type : File.FileType.values()) {
            File file = new File();
            file.setFiletype(type);
            file.cleanupData();

            File.MediaType expected = type == File.FileType.youtube || type == File.FileType.vimeo ? File.MediaType.video
                    : PHOTO_TYPES.contains(type) ? File.MediaType.photo : null;
            Assertions.assertEquals(expected, file.getMediatype(), type.name());
        }
    }

    @Test
    void tifIsNotInferredAsPhotoButTiffIs() {
        // a quirk of the original list, kept as is
        File tif = new File();
        tif.setFiletype(File.FileType.tif);
        tif.cleanupData();
        Assertions.assertNull(tif.getMediatype());
    }

    @Test
    void anExistingMediatypeIsKept() {
        File file = new File();
        file.setFiletype(File.FileType.png);
        file.setMediatype(File.MediaType.logo);
        file.cleanupData();
        Assertions.assertEquals(File.MediaType.logo, file.getMediatype());
    }

    @Test
    void youtubeLinksAreNormalisedAndTheVideoIdBecomesTheFilename() {
        File file = new File();
        file.setFiletype(File.FileType.youtube);
        file.setHlink("https://youtu.be/hVlT0PN3y-k");
        file.setFilename("original.mp4");
        file.cleanupData();

        Assertions.assertEquals("https://www.youtube.com/watch?v=hVlT0PN3y-k", file.getHlink());
        Assertions.assertEquals("hVlT0PN3y-k", file.getFilename());
        Assertions.assertEquals(File.MediaType.video, file.getMediatype());
    }

    @Test
    void unrecognisedYoutubeLinkKeepsHlinkButLosesFilename() {
        File file = new File();
        file.setFiletype(File.FileType.youtube);
        file.setHlink("https://m.youtube.com/watch?v=abc");
        file.setFilename("original");
        file.cleanupData();

        Assertions.assertEquals("https://m.youtube.com/watch?v=abc", file.getHlink());
        Assertions.assertNull(file.getFilename());
    }

    @Test
    void nonYoutubeAndEmptyLinksAreLeftAlone() {
        File vimeo = new File();
        vimeo.setFiletype(File.FileType.vimeo);
        vimeo.setHlink("https://youtu.be/abc");
        vimeo.setFilename("f");
        vimeo.cleanupData();
        Assertions.assertEquals("https://youtu.be/abc", vimeo.getHlink());
        Assertions.assertEquals("f", vimeo.getFilename());

        File empty = new File();
        empty.setFiletype(File.FileType.youtube);
        empty.setHlink("");
        empty.setFilename("f");
        empty.cleanupData();
        Assertions.assertEquals("", empty.getHlink());
        Assertions.assertEquals("f", empty.getFilename());
    }

    @Test
    void normalizeYouTubeURLEdgeCases() {
        Assertions.assertNull(File.normalizeYouTubeURL(null));
        Assertions.assertEquals("", File.normalizeYouTubeURL(""));
        Assertions.assertEquals("   ", File.normalizeYouTubeURL("   "));
        Assertions.assertEquals("https://www.youtube.com/watch?v=abc", File.normalizeYouTubeURL("https://www.youtube.com/watch?v=abc&t=10"));
        Assertions.assertEquals("https://www.youtube.com/watch?v=abc_DEF-1", File.normalizeYouTubeURL("https://www.youtube.com/embed/abc_DEF-1"));
        Assertions.assertEquals("https://www.youtube.com/watch?v=xyz", File.normalizeYouTubeURL("see https://youtu.be/xyz?si=1 now"));
        Assertions.assertEquals("http://youtu.be/abc", File.normalizeYouTubeURL("http://youtu.be/abc"));
        Assertions.assertEquals("https://www.youtube.com/watch?v=", File.normalizeYouTubeURL("https://www.youtube.com/watch?v="));
        Assertions.assertInstanceOf(String.class, File.normalizeYouTubeURL("https://youtu.be/abc"));
    }

    @Test
    void youtubeVideoIDEdgeCases() {
        Assertions.assertNull(File.youtubeVideoID(null));
        Assertions.assertNull(File.youtubeVideoID(" "));
        Assertions.assertNull(File.youtubeVideoID("https://vimeo.com/123"));
        Assertions.assertNull(File.youtubeVideoID("http://youtu.be/abc"));
        Assertions.assertEquals("abc", File.youtubeVideoID("https://www.youtube.com/watch?v=abc&t=10"));
        Assertions.assertEquals("lead", File.youtubeVideoID(" https://youtu.be/lead"));
    }
}
