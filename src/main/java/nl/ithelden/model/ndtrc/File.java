package nl.ithelden.model.ndtrc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nl.ithelden.model.util.ToStringBuilder;

/**
 * Represents a file associated with a TRC item, containing metadata like TRC ID, copyright,
 * filename, hyperlink, type, media type, and title information.
 * Includes logic to normalize YouTube URLs and infer media types.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class File {

    @JsonProperty private String trcid;
    @JsonProperty private Boolean main;

    @JsonProperty private String copyright;
    @JsonProperty private String filename;
    @JsonProperty private String hlink;

    @JsonProperty private FileType filetype;
    @JsonProperty private MediaType mediatype;
    @JsonProperty private String targetLanguage;
    @JsonProperty private Title title;

    public String getTrcid() {
        return trcid;
    }

    public void setTrcid(String trcid) {
        this.trcid = trcid;
    }

    public Boolean getMain() {
        return main;
    }

    public Boolean isMain() {
        return main;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getHlink() {
        return hlink;
    }

    public void setHlink(String hlink) {
        this.hlink = hlink;
    }

    public FileType getFiletype() {
        return filetype;
    }

    public void setFiletype(FileType filetype) {
        this.filetype = filetype;
    }

    public MediaType getMediatype() {
        return mediatype;
    }

    public void setMediatype(MediaType mediatype) {
        this.mediatype = mediatype;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    /**
     * Represents the title of a file, including the main label and translations.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Title {
        @JsonProperty private String label;
        @JsonProperty private List<TitleTranslation> titleTranslations = new ArrayList<>();

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public List<TitleTranslation> getTitleTranslations() {
            return titleTranslations;
        }

        public void setTitleTranslations(List<TitleTranslation> titleTranslations) {
            this.titleTranslations = titleTranslations;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(Title.class, this)
                    .add("label", label)
                    .add("titleTranslations", titleTranslations)
                    .build();
        }

        /**
         * Represents a translation of a file title in a specific language.
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class TitleTranslation {
            @JsonProperty private String lang;
            @JsonProperty private String label;

            public String getLang() {
                return lang;
            }

            public void setLang(String lang) {
                this.lang = lang;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            @Override
            public String toString() {
                return new ToStringBuilder(TitleTranslation.class, this)
                        .add("lang", lang)
                        .add("label", label)
                        .build();
            }
        }
    }

    public void cleanupData() {
        if (filetype != null && mediatype == null) {
            if (filetype == FileType.vimeo || filetype == FileType.youtube) {
                mediatype = MediaType.video;
            }
            if (filetype == FileType.jpg || filetype == FileType.jpeg || filetype == FileType.gif ||
                    filetype == FileType.png || filetype == FileType.bmp || filetype == FileType.jfif ||

                    filetype == FileType.tiff || filetype == FileType.webp) {
                mediatype = MediaType.photo;
            }
        }

        // if the filetype is video convert any url to the standard youtube format
        // al link the the format of
        if (hlink != null && !hlink.isEmpty() && filetype == FileType.youtube) {
            hlink = (String) normalizeYouTubeURL(hlink);
            filename = youtubeVideoID(hlink);
        }
    }

    public enum FileType {
        jpeg, jpg, gif, png, mp3, pdf, gpx, kml, youtube, kmz, vimeo, tif, bmp, jfif, tiff, webp;

        // Groovy gives every enum these members; kept so the 1.x API is unchanged.
        public static final FileType MIN_VALUE = jpeg;
        public static final FileType MAX_VALUE = webp;

        public FileType next() {
            FileType[] values = values();
            int ordinal = ordinal() + 1;
            return values[ordinal >= values.length ? 0 : ordinal];
        }

        public FileType previous() {
            FileType[] values = values();
            int ordinal = ordinal() - 1;
            return values[ordinal < 0 ? values.length - 1 : ordinal];
        }
    }

    public enum MediaType {
        poster, other, audio, brochure, floorplan, photo, logo, video, roadmap, text, attachment, qr;

        // Groovy gives every enum these members; kept so the 1.x API is unchanged.
        public static final MediaType MIN_VALUE = poster;
        public static final MediaType MAX_VALUE = qr;

        public MediaType next() {
            MediaType[] values = values();
            int ordinal = ordinal() + 1;
            return values[ordinal >= values.length ? 0 : ordinal];
        }

        public MediaType previous() {
            MediaType[] values = values();
            int ordinal = ordinal() - 1;
            return values[ordinal < 0 ? values.length - 1 : ordinal];
        }
    }

    // Declared as returning Object because the 1.x Groovy method was `static def`; changing the
    // return type would break binary compatibility for callers compiled against 1.x. It always
    // returns a String (or null); 1.x returned a GString for a rewritten URL.
    public static Object normalizeYouTubeURL(String url) {
        if (url == null || url.trim().isEmpty()) return url;

        // Regular expression patterns for different YouTube URL formats
        Pattern standardPattern = Pattern.compile("https://www\\.youtube\\.com/watch\\?v=([a-zA-Z0-9_-]+)");
        Pattern shortenedPattern = Pattern.compile("https://youtu\\.be/([a-zA-Z0-9_-]+)");
        Pattern embedPattern = Pattern.compile("https://www\\.youtube\\.com/embed/([a-zA-Z0-9_-]+)");

        Matcher matcher = standardPattern.matcher(url);

        // Check if it's already in the desired format
        if (matcher.find()) {
            return "https://www.youtube.com/watch?v=" + matcher.group(1);
        }

        // Check if it's in the shortened format
        matcher = shortenedPattern.matcher(url);
        if (matcher.find()) {
            return "https://www.youtube.com/watch?v=" + matcher.group(1);
        }

        // Check if it's in the embed format
        matcher = embedPattern.matcher(url);
        if (matcher.find()) {
            return "https://www.youtube.com/watch?v=" + matcher.group(1);
        }

        // If none of the patterns match, return the original URL (or you could return null or throw an exception if you prefer)
        return url;
    }

    public static String youtubeVideoID(String url) {
        if (url == null || url.trim().isEmpty()) return null;

        // Regular expression patterns for different YouTube URL formats
        Pattern standardPattern = Pattern.compile("https://www\\.youtube\\.com/watch\\?v=([a-zA-Z0-9_-]+)");
        Pattern shortenedPattern = Pattern.compile("https://youtu\\.be/([a-zA-Z0-9_-]+)");
        Pattern embedPattern = Pattern.compile("https://www\\.youtube\\.com/embed/([a-zA-Z0-9_-]+)");

        Matcher matcher = standardPattern.matcher(url);

        // Check if it's already in the desired format
        if (matcher.find()) {
            return matcher.group(1);
        }

        // Check if it's in the shortened format
        matcher = shortenedPattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }

        // Check if it's in the embed format
        matcher = embedPattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(File.class, this)
                .add("trcid", trcid)
                .add("main", main)
                .add("copyright", copyright)
                .add("filename", filename)
                .add("hlink", hlink)
                .add("filetype", filetype)
                .add("mediatype", mediatype)
                .add("targetLanguage", targetLanguage)
                .add("title", title)
                .build();
    }
}
