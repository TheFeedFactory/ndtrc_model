import { z } from "zod";
import { LangCodeSchema } from "./internal/lang-code.js";
import { UrlSchema } from "./contactinfo.js";

export const CalendarTypeSchema = z.enum([
  "NONE", "ALWAYSOPEN", "ONREQUEST", "OPENINGTIMES", "PATTERNDATES", "SINGLEDATES",
]);

export type CalendarType = z.infer<typeof CalendarTypeSchema>;

export const RecurrencyTypeSchema = z.enum([
  "daily", "weekly", "monthlySimple", "monthlyComplex", "yearly",
]);

export type RecurrencyType = z.infer<typeof RecurrencyTypeSchema>;

export const WhenStatusSchema = z.enum([
  "normal", "cancelled", "soldout", "movedto", "premiere", "reprise",
]);

export type WhenStatus = z.infer<typeof WhenStatusSchema>;

const HHmm = z.string().regex(/^([01]\d|2[0-3]):[0-5]\d$/, {
  message: "Must be a 24-hour time in HH:mm format",
});

export const StatusTranslationSchema = z
  .object({
    lang: LangCodeSchema.optional(),
    text: z.string().optional(),
  })
  .passthrough();

export type StatusTranslation = z.infer<typeof StatusTranslationSchema>;

export const ExtraInformationSchema = z
  .object({
    lang: LangCodeSchema.optional(),
    text: z.string().optional(),
  })
  .passthrough();

export type ExtraInformation = z.infer<typeof ExtraInformationSchema>;

export const WhenSchema = z
  .object({
    timestart: HHmm.optional(),
    timeend: HHmm.optional(),
    status: WhenStatusSchema.optional(),
    valid: z.boolean().optional(),
    statustranslations: z.array(StatusTranslationSchema).optional(),
    extrainformations: z.array(ExtraInformationSchema).optional(),
    urls: z.array(UrlSchema).optional(),
  })
  .passthrough();

export type When = z.infer<typeof WhenSchema>;

export const SingleDateSchema = z
  .object({
    date: z.string().optional(),
    /** Wire-format wart: singular "when" holds a list of When, cf. ExceptionDate.whens */
    when: z.array(WhenSchema).optional(),
  })
  .passthrough();

export type SingleDate = z.infer<typeof SingleDateSchema>;

export const OpenSchema = z
  .object({
    month: z.number().int().optional(),
    weeknumber: z.number().int().optional(),
    daynumber: z.number().int().optional(),
    day: z.number().int().optional(),
    /** Wire-format wart: plural "whens" holds a list of When, cf. SingleDate.when */
    whens: z.array(WhenSchema).optional(),
  })
  .passthrough();

export type Open = z.infer<typeof OpenSchema>;

export const PatternDateSchema = z
  .object({
    startdate: z.string().optional(),
    enddate: z.string().optional(),
    recurrencyType: RecurrencyTypeSchema.optional(),
    occurrence: z.number().int().optional(),
    recurrence: z.number().int().optional(),
    opens: z.array(OpenSchema).optional(),
  })
  .passthrough();

export type PatternDate = z.infer<typeof PatternDateSchema>;

export const ExceptionDateSchema = z
  .object({
    date: z.string().optional(),
    /** Wire-format wart: plural "whens" holds a list of When, cf. SingleDate.when */
    whens: z.array(WhenSchema).optional(),
  })
  .passthrough();

export type ExceptionDate = z.infer<typeof ExceptionDateSchema>;

export const CommentTranslationSchema = z
  .object({
    label: z.string().optional(),
    lang: LangCodeSchema.optional(),
  })
  .passthrough();

export type CommentTranslation = z.infer<typeof CommentTranslationSchema>;

export const CalendarCommentSchema = z
  .object({
    label: z.string().optional(),
    commentTranslations: z.array(CommentTranslationSchema).optional(),
  })
  .passthrough();

export type CalendarComment = z.infer<typeof CalendarCommentSchema>;

export const CalendarSchema = z
  .object({
    singleDates: z.array(SingleDateSchema).optional(),
    patternDates: z.array(PatternDateSchema).optional(),
    opens: z.array(ExceptionDateSchema).optional(),
    closeds: z.array(ExceptionDateSchema).optional(),
    soldouts: z.array(ExceptionDateSchema).optional(),
    cancelleds: z.array(ExceptionDateSchema).optional(),
    excludeholidays: z.boolean().optional(),
    cancelled: z.boolean().optional(),
    soldout: z.boolean().optional(),
    onrequest: z.boolean().optional(),
    alwaysopen: z.boolean().optional(),
    comment: CalendarCommentSchema.optional(),
    calendarType: CalendarTypeSchema.optional(),
  })
  .passthrough();

export const CalendarResponseSchema = CalendarSchema.extend({
  cancelled: z.boolean(),
  soldout: z.boolean(),
  excludeholidays: z.boolean(),
  calendarType: CalendarTypeSchema,
});

export type Calendar = z.infer<typeof CalendarSchema>;
export type CalendarResponse = z.infer<typeof CalendarResponseSchema>;
