import { z } from "zod";
import { LangCodeSchema } from "./internal/lang-code.js";
import { UrlSchema } from "./contactinfo.js";
import { FileSchema } from "./file.js";

export const ValidityStrategySchema = z.enum([
  "always", "dateRange", "earlyBird", "lastMinute",
]);

export type ValidityStrategy = z.infer<typeof ValidityStrategySchema>;

export const PromotionTypeSchema = z.enum([
  "none", "free", "discount", "gift", "allowance",
]);

export type PromotionType = z.infer<typeof PromotionTypeSchema>;

export const DiscountSchema = z
  .object({
    free: z.boolean().optional(),
    percentage: z.number().int().optional(),
    amount: z.number().optional(),
  })
  .passthrough();

export type Discount = z.infer<typeof DiscountSchema>;

export const PromotionTranslationSchema = z
  .object({
    lang: LangCodeSchema.optional(),
    description: z.string().optional(),
  })
  .passthrough();

export type PromotionTranslation = z.infer<typeof PromotionTranslationSchema>;

export const PromotionOpenSchema = z
  .object({
    month: z.number().int().optional(),
    weeknumber: z.number().int().optional(),
    daynumber: z.number().int().optional(),
    day: z.number().int().optional(),
    whens: z.array(z.any()).optional(),
  })
  .passthrough();

export const PromotionSchema = z
  .object({
    product: z.string().optional(),
    externalReference: z.string().optional(),
    promotionType: PromotionTypeSchema.optional(),
    discount: DiscountSchema.optional(),
    /**
     * Whether this promotion must carry a `discount.percentage` or `discount.amount`
     * to count as complete. Server default is `true`; set it to `false` for offers that
     * need no value (a gift, a present, free entrance). Primitive boolean on the Groovy
     * side, so a response always carries it — optional here because a request body may
     * omit it and take the default.
     */
    discountValueRequired: z.boolean().optional(),
    translations: z.array(PromotionTranslationSchema).optional(),
    detailsUrls: z.array(UrlSchema).optional(),
    /**
     * Optional image for the promotion, typically the logo of the offer, shown next to
     * it in the editor and on the publishing sites.
     */
    image: FileSchema.optional(),
    enabled: z.boolean().optional(),
    restrictedToRegisteredUsers: z.boolean().optional(),
    validityStrategy: ValidityStrategySchema.optional(),
    eventRelativeDuration: z.string().optional(),
    startDate: z.string().datetime({ offset: true }).optional(),
    endDate: z.string().datetime({ offset: true }).optional(),
    opens: z.array(PromotionOpenSchema).optional(),
  })
  .passthrough();

export type Promotion = z.infer<typeof PromotionSchema>;
