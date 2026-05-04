import { describe, it, expect } from "vitest";
import { join } from "node:path";
import { extractGroovyFields } from "../src/internal/groovy-fields.js";
import { zodKeys } from "../src/internal/zod-keys.js";
import {
  AddressSchema,
  CalendarSchema,
  CalendarCommentSchema,
  CommentTranslationSchema,
  ConvertedEntrySchema,
  ExceptionDateSchema,
  ExternalLocationInfoSchema,
  ExtraInformationSchema,
  FetchedEntrySchema,
  OpenSchema,
  PatternDateSchema,
  SingleDateSchema,
  StatusTranslationSchema,
  TRCItemSchema,
  TRCItemGroupSchema,
  EventLinkSchema,
  WhenSchema,
  CanonicalConfigSchema,
  CategorySchema,
  CategoryTranslationSchema,
  CategoryValueSchema,
  ContactinfoSchema,
  DescriptionTranslationSchema,
  ExtraPriceInformationSchema,
  FaxSchema,
  FileSchema,
  GISCoordinateSchema,
  LatLngSchema,
  LocationSchema,
  LocationItemSchema,
  MailSchema,
  PerformerSchema,
  PhoneSchema,
  PoiSchema,
  PriceElementCommentSchema,
  PriceElementDescriptionSchema,
  PriceElementDescriptionTranslationSchema,
  PriceElementExtraPriceInformationSchema,
  PriceElementSchema,
  PriceValueSchema,
  PromotionSchema,
  DiscountSchema,
  PromotionTranslationSchema,
  RouteInfoSchema,
  SeoDetailSchema,
  SeoMetadataSchema,
  SubItemGroupSchema,
  SubItemTranslationSchema,
  TitleSchema,
  TitleTranslationSchema,
  TRCItemCategoriesSchema,
  TRCItemDetailSchema,
  TrcitemRelationSchema,
  TranslationsSchema,
  TypeSchema,
  UrlSchema,
} from "../src/index.js";
import type { ZodTypeAny } from "zod";

const GROOVY_NDTRC_DIR = join(
  import.meta.dirname,
  "..",
  "..",
  "src",
  "main",
  "groovy",
  "nl",
  "ithelden",
  "model",
  "ndtrc",
);

const GROOVY_MODEL_DIR = join(
  import.meta.dirname,
  "..",
  "..",
  "src",
  "main",
  "groovy",
  "nl",
  "ithelden",
  "model",
);

const entityMap: Record<string, ZodTypeAny> = {
  Address: AddressSchema,
  Calendar: CalendarSchema,
  "Calendar.Comment": CalendarCommentSchema,
  "Calendar.CommentTranslation": CommentTranslationSchema,
  "Calendar.ExceptionDate": ExceptionDateSchema,
  "Calendar.ExtraInformation": ExtraInformationSchema,
  "Calendar.PatternDate": PatternDateSchema,
  "Calendar.PatternDate.Open": OpenSchema,
  "Calendar.SingleDate": SingleDateSchema,
  "Calendar.StatusTranslation": StatusTranslationSchema,
  "Calendar.When": WhenSchema,
  Contactinfo: ContactinfoSchema,
  "Contactinfo.DescriptionTranslation": DescriptionTranslationSchema,
  "Contactinfo.Fax": FaxSchema,
  "Contactinfo.Mail": MailSchema,
  "Contactinfo.Phone": PhoneSchema,
  "Contactinfo.Url": UrlSchema,
  ExtraPriceInformation: ExtraPriceInformationSchema,
  File: FileSchema,
  "File.Title": TitleSchema,
  "File.Title.TitleTranslation": TitleTranslationSchema,
  GISCoordinate: GISCoordinateSchema,
  Location: LocationSchema,
  "Location.LocationItem": LocationItemSchema,
  Performer: PerformerSchema,
  PriceElement: PriceElementSchema,
  "PriceElement.Comment": PriceElementCommentSchema,
  "PriceElement.Description": PriceElementDescriptionSchema,
  "PriceElement.DescriptionTranslation": PriceElementDescriptionTranslationSchema,
  "PriceElement.ExtraPriceInformation": PriceElementExtraPriceInformationSchema,
  "PriceElement.PriceValue": PriceValueSchema,
  Promotion: PromotionSchema,
  "Promotion.Discount": DiscountSchema,
  "Promotion.PromotionTranslation": PromotionTranslationSchema,
  RouteInfo: RouteInfoSchema,
  "RouteInfo.LatLng": LatLngSchema,
  "RouteInfo.Poi": PoiSchema,
  SeoMetadata: SeoMetadataSchema,
  "SeoMetadata.CanonicalConfig": CanonicalConfigSchema,
  "SeoMetadata.SeoDetail": SeoDetailSchema,
  SubItemGroup: SubItemGroupSchema,
  "SubItemGroup.SubItemTranslation": SubItemTranslationSchema,
  TRCItem: TRCItemSchema,
  TRCItemCategories: TRCItemCategoriesSchema,
  "TRCItemCategories.Category": CategorySchema,
  "TRCItemCategories.CategoryTranslation": CategoryTranslationSchema,
  "TRCItemCategories.CategoryValue": CategoryValueSchema,
  "TRCItemCategories.Type": TypeSchema,
  TRCItemDetail: TRCItemDetailSchema,
  TRCItemGroup: TRCItemGroupSchema,
  "TRCItemGroup.EventLink": EventLinkSchema,
  TrcitemRelation: TrcitemRelationSchema,
  Translations: TranslationsSchema,
};

const modelEntityMap: Record<string, ZodTypeAny> = {
  ConvertedEntry: ConvertedEntrySchema,
  FetchedEntry: FetchedEntrySchema,
  "FetchedEntry.ExternalLocationInfo": ExternalLocationInfoSchema,
};

/**
 * Fields present in Groovy but intentionally excluded from the Zod schema.
 *
 * - TRCItem.forceoverwrite: workflow field, not part of the wire format
 */
const allowlist: Record<string, Set<string>> = {
  TRCItem: new Set(["forceoverwrite"]),
};

function checkParity(
  groovyFields: Record<string, Set<string>>,
  map: Record<string, ZodTypeAny>,
  dirLabel: string,
) {
  for (const [className, schema] of Object.entries(map)) {
    const groovy = groovyFields[className];
    expect(
      groovy,
      `Groovy class "${className}" not found in ${dirLabel}`,
    ).toBeDefined();

    const zod = zodKeys(schema);
    const allowed = allowlist[className] ?? new Set<string>();

    const groovyMinusAllowlist = new Set(
      [...groovy!].filter((f) => !allowed.has(f)),
    );

    const missingInZod = [...groovyMinusAllowlist].filter(
      (f) => !zod.has(f),
    );
    const extraInZod = [...zod].filter((f) => !groovyMinusAllowlist.has(f));

    expect(
      missingInZod,
      `${className}: fields in Groovy but missing from Zod schema: ${missingInZod.join(", ")}`,
    ).toEqual([]);
    expect(
      extraInZod,
      `${className}: fields in Zod schema but missing from Groovy: ${extraInZod.join(", ")}`,
    ).toEqual([]);
  }
}

describe("Groovy ↔ Zod parity", () => {
  it("every mapped ndtrc entity has matching field sets", async () => {
    const groovyFields = await extractGroovyFields(GROOVY_NDTRC_DIR);
    checkParity(groovyFields, entityMap, GROOVY_NDTRC_DIR);
  });

  it("every mapped model entity has matching field sets", async () => {
    const groovyFields = await extractGroovyFields(GROOVY_MODEL_DIR);
    checkParity(groovyFields, modelEntityMap, GROOVY_MODEL_DIR);
  });

  it("fails when a Groovy field is added without a TS counterpart", async () => {
    const groovyFields = await extractGroovyFields(GROOVY_NDTRC_DIR);
    const gisFields = groovyFields["GISCoordinate"]!;

    const fakeGisFields = new Set([...gisFields, "newField"]);
    const zodFields = zodKeys(GISCoordinateSchema);

    const missing = [...fakeGisFields].filter((f) => !zodFields.has(f));
    expect(missing).toContain("newField");
  });
});
