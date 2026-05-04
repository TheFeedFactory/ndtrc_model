import { z } from "zod";
import { LangCodeSchema } from "./internal/lang-code.js";

export const OGTypeSchema = z.enum(["event", "place", "article", "organization"]);
export type OGType = z.infer<typeof OGTypeSchema>;

export const TwitterCardTypeSchema = z.enum(["summary", "summary_large_image"]);
export type TwitterCardType = z.infer<typeof TwitterCardTypeSchema>;

export const SeoDetailSchema = z
  .object({
    lang: LangCodeSchema.optional(),
    metaTitle: z.string().optional(),
    metaDescription: z.string().optional(),
    ogImageUrl: z.string().optional(),
    ogImageAlt: z.string().optional(),
    ogType: OGTypeSchema.optional(),
    twitterCardType: TwitterCardTypeSchema.optional(),
  })
  .passthrough();

export type SeoDetail = z.infer<typeof SeoDetailSchema>;

export const CanonicalConfigSchema = z
  .object({
    canonicalUrl: z.string().optional(),
  })
  .passthrough();

export type CanonicalConfig = z.infer<typeof CanonicalConfigSchema>;

export const SeoMetadataSchema = z
  .object({
    seoDetails: z.array(SeoDetailSchema).optional(),
    canonical: CanonicalConfigSchema.optional(),
    schemaOrgJsonLd: z.string().optional(),
  })
  .passthrough();

export type SeoMetadata = z.infer<typeof SeoMetadataSchema>;
