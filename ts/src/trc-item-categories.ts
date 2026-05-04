import { z } from "zod";
import { LangCodeSchema } from "./internal/lang-code.js";

export const DataTypeSchema = z.enum([
  "yes", "yesno", "nullableyesno", "choice", "multichoice",
  "freetext", "integer", "decimal", "date", "data", "url", "email", "phone",
]);

export type DataType = z.infer<typeof DataTypeSchema>;

export const CategoryTranslationSchema = z
  .object({
    catid: z.string().optional(),
    lang: LangCodeSchema.optional(),
    label: z.string().optional(),
    unit: z.string().optional(),
    value: z.string().optional(),
    explanation: z.string().optional(),
  })
  .passthrough();

export type CategoryTranslation = z.infer<typeof CategoryTranslationSchema>;

export const CategoryValueSchema = z
  .object({
    catid: z.string().optional(),
    value: z.string().optional(),
    categorytranslations: z.array(CategoryTranslationSchema).optional(),
  })
  .passthrough();

export type CategoryValue = z.infer<typeof CategoryValueSchema>;

export const CategorySchema = z
  .object({
    catid: z.string().optional(),
    valueid: z.string().optional(),
    value: z.string().optional(),
    defaultValue: z.string().optional(),
    datatype: DataTypeSchema.optional(),
    categoryvalues: z.array(CategoryValueSchema).optional(),
    categoryTranslations: z.array(CategoryTranslationSchema).optional(),
    parentCategoryTranslations: z.array(CategoryTranslationSchema).optional(),
    valueCategoryTranslations: z.array(CategoryTranslationSchema).optional(),
  })
  .passthrough();

export type Category = z.infer<typeof CategorySchema>;

export const TypeSchema = z
  .object({
    catid: z.string().optional(),
    isDefault: z.boolean().optional(),
    categoryTranslations: z.array(CategoryTranslationSchema).optional(),
  })
  .passthrough();

export type Type = z.infer<typeof TypeSchema>;

export const TRCItemCategoriesSchema = z
  .object({
    types: z.array(TypeSchema).optional(),
    categories: z.array(CategorySchema).optional(),
    soldout: z.boolean().optional(),
    canceled: z.boolean().optional(),
  })
  .passthrough();

export type TRCItemCategories = z.infer<typeof TRCItemCategoriesSchema>;
