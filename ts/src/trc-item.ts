import { z } from "zod";
import type { Calendar } from "./calendar.js";
import { CalendarSchema } from "./calendar.js";
import type { Contactinfo } from "./contactinfo.js";
import { ContactinfoSchema } from "./contactinfo.js";
import type { ExtraPriceInformation } from "./extra-price-information.js";
import { ExtraPriceInformationSchema } from "./extra-price-information.js";
import type { File } from "./file.js";
import { FileSchema } from "./file.js";
import type { Location } from "./location.js";
import { LocationSchema } from "./location.js";
import type { Performer } from "./performer.js";
import { PerformerSchema } from "./performer.js";
import type { PriceElement } from "./price-element.js";
import { PriceElementSchema } from "./price-element.js";
import type { Promotion } from "./promotion.js";
import { PromotionSchema } from "./promotion.js";
import type { RouteInfo } from "./route-info.js";
import { RouteInfoSchema } from "./route-info.js";
import type { SeoMetadata } from "./seo-metadata.js";
import { SeoMetadataSchema } from "./seo-metadata.js";
import type { TRCItemCategories } from "./trc-item-categories.js";
import { TRCItemCategoriesSchema } from "./trc-item-categories.js";
import type { TRCItemDetail } from "./trc-item-detail.js";
import { TRCItemDetailSchema } from "./trc-item-detail.js";
import type { TrcitemRelation } from "./trcitem-relation.js";
import { TrcitemRelationSchema } from "./trcitem-relation.js";
import type { Translations } from "./translations.js";
import { TranslationsSchema } from "./translations.js";

export const WFStatusSchema = z.enum([
  "draft", "readyforvalidation", "approved", "rejected", "deleted", "archived",
]);

export type WFStatus = z.infer<typeof WFStatusSchema>;

export const EntityTypeSchema = z.enum([
  "EVENEMENT", "LOCATIE", "EVENTGROUP", "ROUTE", "VENUE",
]);

export type EntityType = z.infer<typeof EntityTypeSchema>;

export interface TRCItem {
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
  slug?: string | undefined;
  validator?: string | undefined;
  /** @deprecated */
  validatedby?: string | undefined;
  wfstatus?: WFStatus | undefined;
  cidn?: string | undefined;
  published?: boolean | undefined;
  deleted?: boolean | undefined;
  /** @deprecated */
  offline?: boolean | undefined;
  /** @deprecated */
  isprivate?: boolean | undefined;
  entitytype?: EntityType | undefined;
  /** @deprecated */
  productiontrcid?: string | undefined;
  calendar?: Calendar | undefined;
  contactinfo?: Contactinfo | undefined;
  trcItemCategories?: TRCItemCategories | undefined;
  performers?: Performer[] | undefined;
  files?: File[] | undefined;
  trcItemDetails?: TRCItemDetail[] | undefined;
  trcitemRelation?: TrcitemRelation | undefined;
  keywords?: string | undefined;
  markers?: string | undefined;
  location?: Location | undefined;
  locationRef?: string | undefined;
  userorganisation?: string | undefined;
  priceElements?: PriceElement[] | undefined;
  extrapriceinformations?: ExtraPriceInformation[] | undefined;
  routeInfo?: RouteInfo | undefined;
  translations?: Translations | undefined;
  promotions?: Promotion[] | undefined;
  seoMetadata?: SeoMetadata | undefined;
  [key: string]: unknown;
}

export interface TRCItemResponse extends Omit<TRCItem, "trcid" | "entitytype" | "creationdate" | "lastupdated" | "wfstatus"> {
  trcid: string;
  entitytype: EntityType;
  creationdate: string;
  lastupdated: string;
  wfstatus: WFStatus;
  [key: string]: unknown;
}

const _trcItemBase = z
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
    slug: z.string().optional(),
    validator: z.string().optional(),
    validatedby: z.string().optional(),
    wfstatus: WFStatusSchema.optional(),
    cidn: z.string().optional(),
    published: z.boolean().optional(),
    deleted: z.boolean().optional(),
    offline: z.boolean().optional(),
    isprivate: z.boolean().optional(),
    entitytype: EntityTypeSchema.optional(),
    productiontrcid: z.string().optional(),
    calendar: CalendarSchema.optional(),
    contactinfo: ContactinfoSchema.optional(),
    trcItemCategories: TRCItemCategoriesSchema.optional(),
    performers: z.array(PerformerSchema).optional(),
    files: z.array(FileSchema).optional(),
    trcItemDetails: z.array(TRCItemDetailSchema).optional(),
    trcitemRelation: TrcitemRelationSchema.optional(),
    keywords: z.string().optional(),
    markers: z.string().optional(),
    location: LocationSchema.optional(),
    locationRef: z.string().optional(),
    userorganisation: z.string().optional(),
    priceElements: z.array(PriceElementSchema).optional(),
    extrapriceinformations: z.array(ExtraPriceInformationSchema).optional(),
    routeInfo: RouteInfoSchema.optional(),
    translations: TranslationsSchema.optional(),
    promotions: z.array(PromotionSchema).optional(),
    seoMetadata: SeoMetadataSchema.optional(),
  })
  .passthrough();

export const TRCItemSchema: z.ZodType<TRCItem> = _trcItemBase;

export const TRCItemResponseSchema: z.ZodType<TRCItemResponse> =
  _trcItemBase.extend({
    trcid: z.string(),
    entitytype: EntityTypeSchema,
    creationdate: z.string(),
    lastupdated: z.string(),
    wfstatus: WFStatusSchema,
  });

export type Event = TRCItem & { entitytype: "EVENEMENT" };
export type LocationItemEntity = TRCItem & { entitytype: "LOCATIE" };
export type Venue = TRCItem & { entitytype: "VENUE" };
export type Route = TRCItem & { entitytype: "ROUTE" };
export type EventGroup = TRCItem & { entitytype: "EVENTGROUP" };
