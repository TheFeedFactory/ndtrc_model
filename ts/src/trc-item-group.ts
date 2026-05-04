import { z } from "zod";
import type { Calendar } from "./calendar.js";
import { CalendarSchema } from "./calendar.js";
import type { Contactinfo } from "./contactinfo.js";
import { ContactinfoSchema } from "./contactinfo.js";
import type { File } from "./file.js";
import { FileSchema } from "./file.js";
import type { PriceElement } from "./price-element.js";
import { PriceElementSchema } from "./price-element.js";
import type { Promotion } from "./promotion.js";
import { PromotionSchema } from "./promotion.js";
import type { TRCItemCategories } from "./trc-item-categories.js";
import { TRCItemCategoriesSchema } from "./trc-item-categories.js";
import type { TRCItemDetail } from "./trc-item-detail.js";
import { TRCItemDetailSchema } from "./trc-item-detail.js";
import type { Translations } from "./translations.js";
import { TranslationsSchema } from "./translations.js";
import { EntityTypeSchema, WFStatusSchema } from "./trc-item.js";
import type { EntityType, WFStatus } from "./trc-item.js";

export const EventLinkSchema = z
  .object({
    eventId: z.string().optional(),
  })
  .passthrough();

export type EventLink = z.infer<typeof EventLinkSchema>;

export interface TRCItemGroup {
  trcid?: string | undefined;
  creationdate?: string | undefined;
  availablefrom?: string | undefined;
  availableto?: string | undefined;
  lastupdated?: string | undefined;
  lastimportedon?: string | undefined;
  createdby?: string | undefined;
  lastupdatedby?: string | undefined;
  owner?: string | undefined;
  legalowner?: string | undefined;
  externalid?: string | undefined;
  validator?: string | undefined;
  wfstatus?: WFStatus | undefined;
  entitytype?: EntityType | undefined;
  calendar?: Calendar | undefined;
  contactinfo?: Contactinfo | undefined;
  trcItemCategories?: TRCItemCategories | undefined;
  files?: File[] | undefined;
  trcItemDetails?: TRCItemDetail[] | undefined;
  keywords?: string | undefined;
  markers?: string | undefined;
  userorganisation?: string | undefined;
  priceElements?: PriceElement[] | undefined;
  translations?: Translations | undefined;
  promotions?: Promotion[] | undefined;
  eventLinks?: EventLink[] | undefined;
  [key: string]: unknown;
}

const _trcItemGroupBase = z
  .object({
    trcid: z.string().optional(),
    creationdate: z.string().optional(),
    availablefrom: z.string().optional(),
    availableto: z.string().optional(),
    lastupdated: z.string().optional(),
    lastimportedon: z.string().optional(),
    createdby: z.string().optional(),
    lastupdatedby: z.string().optional(),
    owner: z.string().optional(),
    legalowner: z.string().optional(),
    externalid: z.string().optional(),
    validator: z.string().optional(),
    wfstatus: WFStatusSchema.optional(),
    entitytype: EntityTypeSchema.optional(),
    calendar: CalendarSchema.optional(),
    contactinfo: ContactinfoSchema.optional(),
    trcItemCategories: TRCItemCategoriesSchema.optional(),
    files: z.array(FileSchema).optional(),
    trcItemDetails: z.array(TRCItemDetailSchema).optional(),
    keywords: z.string().optional(),
    markers: z.string().optional(),
    userorganisation: z.string().optional(),
    priceElements: z.array(PriceElementSchema).optional(),
    translations: TranslationsSchema.optional(),
    promotions: z.array(PromotionSchema).optional(),
    eventLinks: z.array(EventLinkSchema).optional(),
  })
  .passthrough();

export const TRCItemGroupSchema: z.ZodType<TRCItemGroup> = _trcItemGroupBase;
