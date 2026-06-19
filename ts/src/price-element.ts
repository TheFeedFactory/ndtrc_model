import { z } from "zod";
import { LangCodeSchema } from "./internal/lang-code.js";

/**
 * Standard NDTRC-derived price-type values shipped as defaults/fallback.
 * Accounts may define their own values, so `value` is a free-form string.
 */
export const StandardPriceDescriptionValues = [
  "Adults", "Children", "Groups", "CJP", "Pasholders", "Lastminute",
] as const;

export const PriceDescriptionValueSchema = z.string();

export type PriceDescriptionValue = z.infer<typeof PriceDescriptionValueSchema>;

export const PriceElementDescriptionTranslationSchema = z
  .object({
    lang: LangCodeSchema.optional(),
    text: z.string().optional(),
  })
  .passthrough();

export type PriceElementDescriptionTranslation = z.infer<
  typeof PriceElementDescriptionTranslationSchema
>;

export const PriceElementExtraPriceInformationSchema = z
  .object({
    lang: LangCodeSchema.optional(),
    text: z.string().optional(),
  })
  .passthrough();

export type PriceElementExtraPriceInformation = z.infer<
  typeof PriceElementExtraPriceInformationSchema
>;

export const PriceValueSchema = z
  .object({
    from: z.number().optional(),
    until: z.number().optional(),
  })
  .passthrough();

export type PriceValue = z.infer<typeof PriceValueSchema>;

export const PriceElementCommentSchema = z
  .object({
    text: z.string().optional(),
  })
  .passthrough();

export type PriceElementComment = z.infer<typeof PriceElementCommentSchema>;

export const PriceElementDescriptionSchema = z
  .object({
    value: PriceDescriptionValueSchema.optional(),
    descriptionTranslations: z
      .array(PriceElementDescriptionTranslationSchema)
      .optional(),
  })
  .passthrough();

export type PriceElementDescription = z.infer<
  typeof PriceElementDescriptionSchema
>;

export const PriceElementSchema = z
  .object({
    freeentrance: z.boolean().optional(),
    priceValue: PriceValueSchema.optional(),
    description: PriceElementDescriptionSchema.optional(),
    comments: z.array(PriceElementCommentSchema).optional(),
    extraPriceInformations: z
      .array(PriceElementExtraPriceInformationSchema)
      .optional(),
  })
  .passthrough();

export type PriceElement = z.infer<typeof PriceElementSchema>;
