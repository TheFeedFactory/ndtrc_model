package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includeNames = true)
class File {

    @JsonProperty String trcid
    @JsonProperty Boolean main

    @JsonProperty String copyright
    @JsonProperty String filename
    @JsonProperty String hlink

    @JsonProperty FileType filetype
    @JsonProperty MediaType mediatype
    @JsonProperty String targetLanguage
    @JsonProperty Title title

    @ToString(includeNames = true)
    static class Title {
        @JsonProperty String label
        @JsonProperty List<TitleTranslation> titleTranslations = []

        @ToString(includeNames = true)
        static class TitleTranslation {
            @JsonProperty String lang
            @JsonProperty String label
        }
    }

    enum FileType { jpeg, jpg, gif, png, mp3, pdf, gpx, kml, youtube, kmz, vimeo, tif, bmp, jfif, tiff}
    enum MediaType { poster, other, audio, brochure, photo, logo, video, roadmap, text, attachment, qr }
}