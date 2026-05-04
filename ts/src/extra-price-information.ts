import { z } from "zod";
import { LangCodeSchema } from "./internal/lang-code.js";

export const ExtraPriceInformationSchema = z
  .object({
    lang: LangCodeSchema.optional(),
    text: z.string().optional(),
  })
  .passthrough();

export type ExtraPriceInformation = z.infer<
  typeof ExtraPriceInformationSchema
>;
