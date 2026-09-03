import { z } from "zod";
import { LangCodeSchema } from "./internal/lang-code.js";

export const OGTypeSchema = z.enum(["event", "place", "article", "organization"]);
export type OGType = z.infer<typeof OGTypeSchema>;

export const TwitterCardTypeSchema = z.enum([
  "summary",
  "summary_large_image",
  "app",
  "player",
]);
export type TwitterCardType = z.infer<typeof TwitterCardTypeSchema>;

export const SeoDetailSchema = z
  .object({
    lang: LangCodeSchema.optional(),
    metaTitle: z.string().optional(),
    metaDescription: z.string().optional(),
    ogImageUrl: z.string().optional(),
    ogImageAlt: z.string().optional(),
    /**
     * Free-form on purpose: widened from {@link OGTypeSchema} so ff-gui can curate its own
     * option list independently of this model's release cycle.
     */
    ogType: z.string().optional(),
    twitterCardType: TwitterCardTypeSchema.optional(),
    twitterImageUrl: z.string().optional(),
    twitterImageAlt: z.string().optional(),
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
    overwriteDefaults: z.boolean().optional(),
    schemaOrgJsonLd: z.string().optional(),
  })
  .passthrough();

export type SeoMetadata = z.infer<typeof SeoMetadataSchema>;
