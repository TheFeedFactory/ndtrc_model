import { describe, it, expect } from "vitest";
import { join } from "node:path";
import { extractJavaFields } from "../src/internal/java-fields.js";
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

const JAVA_NDTRC_DIR = join(
  import.meta.dirname,
  "..",
  "..",
  "src",
  "main",
  "java",
  "nl",
  "ithelden",
  "model",
  "ndtrc",
);

const JAVA_MODEL_DIR = join(
  import.meta.dirname,
  "..",
  "..",
  "src",
  "main",
  "java",
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
 * Fields present in Java but intentionally excluded from the Zod schema.
 *
 * - TRCItem.forceoverwrite: workflow field, not part of the wire format
 */
const allowlist: Record<string, Set<string>> = {
  TRCItem: new Set(["forceoverwrite"]),
};

function checkParity(
  javaFields: Record<string, Set<string>>,
  map: Record<string, ZodTypeAny>,
  dirLabel: string,
) {
  for (const [className, schema] of Object.entries(map)) {
    const java = javaFields[className];
    expect(
      java,
      `Java class "${className}" not found in ${dirLabel}`,
    ).toBeDefined();

    const zod = zodKeys(schema);
    const allowed = allowlist[className] ?? new Set<string>();

    const javaMinusAllowlist = new Set(
      [...java!].filter((f) => !allowed.has(f)),
    );

    const missingInZod = [...javaMinusAllowlist].filter(
      (f) => !zod.has(f),
    );
    const extraInZod = [...zod].filter((f) => !javaMinusAllowlist.has(f));

    expect(
      missingInZod,
      `${className}: fields in Java but missing from Zod schema: ${missingInZod.join(", ")}`,
    ).toEqual([]);
    expect(
      extraInZod,
      `${className}: fields in Zod schema but missing from Java: ${extraInZod.join(", ")}`,
    ).toEqual([]);
  }
}

describe("Java ↔ Zod parity", () => {
  it("every mapped ndtrc entity has matching field sets", async () => {
    const javaFields = await extractJavaFields(JAVA_NDTRC_DIR);
    checkParity(javaFields, entityMap, JAVA_NDTRC_DIR);
  });

  it("every mapped model entity has matching field sets", async () => {
    const javaFields = await extractJavaFields(JAVA_MODEL_DIR);
    checkParity(javaFields, modelEntityMap, JAVA_MODEL_DIR);
  });

  it("fails when a Java field is added without a TS counterpart", async () => {
    const javaFields = await extractJavaFields(JAVA_NDTRC_DIR);
    const gisFields = javaFields["GISCoordinate"]!;

    const fakeGisFields = new Set([...gisFields, "newField"]);
    const zodFields = zodKeys(GISCoordinateSchema);

    const missing = [...fakeGisFields].filter((f) => !zodFields.has(f));
    expect(missing).toContain("newField");
  });
});
