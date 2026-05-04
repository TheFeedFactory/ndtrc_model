import { z } from "zod";
import { LangCodeSchema } from "./internal/lang-code.js";
import { TypeSchema, CategorySchema } from "./trc-item-categories.js";
import { FileSchema } from "./file.js";

export const SubItemTranslationSchema = z
  .object({
    lang: LangCodeSchema.optional(),
    title: z.string().optional(),
  })
  .passthrough();

export type SubItemTranslation = z.infer<typeof SubItemTranslationSchema>;

export const SubItemGroupSchema = z
  .object({
    trcid: z.string().optional(),
    type: TypeSchema.optional(),
    categories: z.array(CategorySchema).optional(),
    subItemTranslations: z.array(SubItemTranslationSchema).optional(),
    media: z.array(FileSchema).optional(),
  })
  .passthrough();

export type SubItemGroup = z.infer<typeof SubItemGroupSchema>;
