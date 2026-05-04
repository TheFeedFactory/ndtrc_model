import { z } from "zod";
import { LangCodeSchema } from "./internal/lang-code.js";

export const TRCItemDetailSchema = z
  .object({
    lang: LangCodeSchema.optional(),
    longdescription: z.string().optional(),
    shortdescription: z.string().optional(),
    title: z.string().optional(),
  })
  .passthrough();

export type TRCItemDetail = z.infer<typeof TRCItemDetailSchema>;
