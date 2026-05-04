import { z } from "zod";
import { LangCodeSchema } from "./internal/lang-code.js";
import { AddressSchema } from "./address.js";

export const URLServiceTypeSchema = z.enum([
  "general",
  "booking",
  "review",
  "video",
  "webshop",
  "socialmedia",
  "lastminute",
  "virtualtour",
  "dmo",
  "sustainability",
  "venuefinder",
  "travelbase",
  "homepage",
]);

export type URLServiceType = z.infer<typeof URLServiceTypeSchema>;

export const DescriptionTranslationSchema = z
  .object({
    lang: LangCodeSchema.optional(),
    label: z.string().optional(),
  })
  .passthrough();

export type DescriptionTranslation = z.infer<
  typeof DescriptionTranslationSchema
>;

export const MailSchema = z
  .object({
    email: z.string().email().optional(),
    descriptioncode: z.string().optional(),
    reservations: z.boolean().optional(),
    descriptionTranslations: z
      .array(DescriptionTranslationSchema)
      .optional(),
  })
  .passthrough();

export type Mail = z.infer<typeof MailSchema>;

export const PhoneSchema = z
  .object({
    number: z.string().optional(),
    descriptioncode: z.string().optional(),
    reservations: z.boolean().optional(),
    descriptionTranslations: z
      .array(DescriptionTranslationSchema)
      .optional(),
  })
  .passthrough();

export type Phone = z.infer<typeof PhoneSchema>;

export const FaxSchema = z
  .object({
    number: z.string().optional(),
    descriptioncode: z.string().optional(),
    reservations: z.boolean().optional(),
    descriptionTranslations: z
      .array(DescriptionTranslationSchema)
      .optional(),
  })
  .passthrough();

export type Fax = z.infer<typeof FaxSchema>;

export const UrlSchema = z
  .object({
    url: z.string().url().optional(),
    descriptioncode: z.string().optional(),
    targetLanguage: LangCodeSchema.optional(),
    reservations: z.boolean().optional(),
    urlServiceType: URLServiceTypeSchema.optional(),
    descriptionTranslations: z
      .array(DescriptionTranslationSchema)
      .optional(),
  })
  .passthrough();

export type Url = z.infer<typeof UrlSchema>;

export const ContactinfoSchema = z
  .object({
    label: z.string().optional(),
    /** @deprecated Use mail instead */
    mails: z.array(MailSchema).optional(),
    /** @deprecated Use phone instead */
    phones: z.array(PhoneSchema).optional(),
    /** @deprecated Use fax instead */
    faxes: z.array(FaxSchema).optional(),
    urls: z.array(UrlSchema).optional(),
    /** @deprecated Use address instead */
    addresses: z.array(AddressSchema).optional(),
    mail: MailSchema.optional(),
    phone: PhoneSchema.optional(),
    fax: FaxSchema.optional(),
    address: AddressSchema.optional(),
  })
  .passthrough();

export type Contactinfo = z.infer<typeof ContactinfoSchema>;
