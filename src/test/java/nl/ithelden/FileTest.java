package nl.ithelden

import nl.ithelden.model.ndtrc.File
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FileTest {

    @Test
    void testYouTube() {
        String url1 = "https://www.youtube.com/watch?v=hVlT0PN3y-k"
        String n1 = File.normalizeYouTubeURL(url1)

        String url2 = "https://youtu.be/hVlT0PN3y-k"
        String n2 = File.normalizeYouTubeURL(url2)

        String url3 = "https://www.youtube.com/embed/hVlT0PN3y-k"
        String n3 = File.normalizeYouTubeURL(url3)

        String url4 = "https://youtu.be/nMc6Kzy186s"
        String n4 = File.normalizeYouTubeURL(url4)

        Assertions.assertEquals("https://www.youtube.com/watch?v=hVlT0PN3y-k", n1)
        Assertions.assertEquals("https://www.youtube.com/watch?v=hVlT0PN3y-k", n2)
        Assertions.assertEquals("https://www.youtube.com/watch?v=hVlT0PN3y-k", n3)
        Assertions.assertEquals("https://www.youtube.com/watch?v=nMc6Kzy186s", n4)
    }
}
