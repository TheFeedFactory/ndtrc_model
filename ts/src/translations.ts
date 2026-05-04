import { z } from "zod";
import { LangCodeSchema } from "./internal/lang-code.js";

export const TranslationsSchema = z
  .object({
    primaryLanguage: LangCodeSchema.optional(),
    availableLanguages: z.array(LangCodeSchema).optional(),
  })
  .passthrough();

export type Translations = z.infer<typeof TranslationsSchema>;
