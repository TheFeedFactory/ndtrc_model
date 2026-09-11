import { z } from "zod";
import { LangCodeSchema } from "./internal/lang-code.js";

export const FileTypeSchema = z.enum([
  "jpeg", "jpg", "gif", "png", "mp3", "pdf", "gpx", "kml",
  "youtube", "kmz", "vimeo", "tif", "bmp", "jfif", "tiff", "webp",
]);

export type FileType = z.infer<typeof FileTypeSchema>;

export const MediaTypeSchema = z.enum([
  "poster", "other", "audio", "brochure", "floorplan", "photo",
  "logo", "video", "roadmap", "text", "attachment", "qr",
]);

export type MediaType = z.infer<typeof MediaTypeSchema>;

export const TitleTranslationSchema = z
  .object({
    lang: LangCodeSchema.optional(),
    label: z.string().optional(),
  })
  .passthrough();

export type TitleTranslation = z.infer<typeof TitleTranslationSchema>;

export const TitleSchema = z
  .object({
    label: z.string().optional(),
    titleTranslations: z.array(TitleTranslationSchema).optional(),
  })
  .passthrough();

export type Title = z.infer<typeof TitleSchema>;

export const FileSchema = z
  .object({
    trcid: z.string().optional(),
    main: z.boolean().optional(),
    copyright: z.string().optional(),
    filename: z.string().optional(),
    /**
     * URL of the file. Required: a File without a link points at nothing, and every
     * producer in the API sets it (a file built from a blank URL is dropped rather than
     * stored). Validated as a URL, so an empty string or a bare path fails.
     */
    hlink: z.string().url(),
    filetype: FileTypeSchema.optional(),
    mediatype: MediaTypeSchema.optional(),
    targetLanguage: LangCodeSchema.optional(),
    title: TitleSchema.optional(),
  })
  .passthrough();

export type File = z.infer<typeof FileSchema>;
