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

    void cleanupData() {
        if (filetype && !mediatype) {
            if (filetype == FileType.vimeo || filetype == FileType.youtube) {
                mediatype = MediaType.video
            }
            if (filetype == FileType.jpg || filetype == FileType.jpeg || filetype == FileType.gif ||
                    filetype == FileType.png || filetype == FileType.bmp || filetype == FileType.jfif ||

                    filetype == FileType.tiff || filetype == FileType.webp) {
                mediatype = MediaType.photo
            }
        }

        // if the filetype is video convert any url to the standard youtube format
        // al link the the format of
        if (hlink && filetype == FileType.youtube) {
            hlink = normalizeYouTubeURL(hlink)
            filename = hlink
        }
    }

    enum FileType { jpeg, jpg, gif, png, mp3, pdf, gpx, kml, youtube, kmz, vimeo, tif, bmp, jfif, tiff, webp }
    enum MediaType { poster, other, audio, brochure, photo, logo, video, roadmap, text, attachment, qr }

    static def normalizeYouTubeURL(String url) {
        if (!url?.trim()) return url

        // Regular expression patterns for different YouTube URL formats
        def standardPattern = ~/https:\/\/www\.youtube\.com\/watch\?v=([a-zA-Z0-9_-]+)/
        def shortenedPattern = ~/https:\/\/youtu\.be\/([a-zA-Z0-9_-]+)/
        def embedPattern = ~/https:\/\/www\.youtube\.com\/embed\/([a-zA-Z0-9_-]+)/

        def matcher = standardPattern.matcher(url)

        // Check if it's already in the desired format
        if (matcher.find()) {
            return "https://www.youtube.com/watch?v=${matcher.group(1)}"
        }

        // Check if it's in the shortened format
        matcher = shortenedPattern.matcher(url)
        if (matcher.find()) {
            return "https://www.youtube.com/watch?v=${matcher.group(1)}"
        }

        // Check if it's in the embed format
        matcher = embedPattern.matcher(url)
        if (matcher.find()) {
            return "https://www.youtube.com/watch?v=${matcher.group(1)}"
        }

        // If none of the patterns match, return the original URL (or you could return null or throw an exception if you prefer)
        return url
    }
}