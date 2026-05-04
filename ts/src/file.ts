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
    hlink: z.string().url().optional(),
    filetype: FileTypeSchema.optional(),
    mediatype: MediaTypeSchema.optional(),
    targetLanguage: LangCodeSchema.optional(),
    title: TitleSchema.optional(),
  })
  .passthrough();

export type File = z.infer<typeof FileSchema>;
