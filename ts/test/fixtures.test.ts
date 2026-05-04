import { describe, it, expect } from "vitest";
import { readdirSync, readFileSync } from "node:fs";
import { join } from "node:path";
import type { ZodTypeAny } from "zod";
import {
  AddressSchema,
  CalendarSchema,
  ContactinfoSchema,
  ConvertedEntrySchema,
  ExtraPriceInformationSchema,
  FetchedEntrySchema,
  FileSchema,
  GISCoordinateSchema,
  LocationSchema,
  PerformerSchema,
  PriceElementSchema,
  PromotionSchema,
  RouteInfoSchema,
  SeoMetadataSchema,
  SubItemGroupSchema,
  TRCItemSchema,
  TRCItemCategoriesSchema,
  TRCItemDetailSchema,
  TRCItemGroupSchema,
  TrcitemRelationSchema,
  TranslationsSchema,
} from "../src/index.js";

const FIXTURES_DIR = join(import.meta.dirname, "fixtures");

const schemaByDir: Record<string, ZodTypeAny> = {
  address: AddressSchema,
  calendar: CalendarSchema,
  contactinfo: ContactinfoSchema,
  "converted-entry": ConvertedEntrySchema,
  "extra-price-information": ExtraPriceInformationSchema,
  "fetched-entry": FetchedEntrySchema,
  file: FileSchema,
  "gis-coordinate": GISCoordinateSchema,
  location: LocationSchema,
  performer: PerformerSchema,
  "price-element": PriceElementSchema,
  promotion: PromotionSchema,
  "route-info": RouteInfoSchema,
  "seo-metadata": SeoMetadataSchema,
  "sub-item-group": SubItemGroupSchema,
  "trc-item": TRCItemSchema,
  "trc-item-categories": TRCItemCategoriesSchema,
  "trc-item-detail": TRCItemDetailSchema,
  "trc-item-group": TRCItemGroupSchema,
  "trcitem-relation": TrcitemRelationSchema,
  translations: TranslationsSchema,
};

const fixtureDirs = readdirSync(FIXTURES_DIR, { withFileTypes: true })
  .filter((d) => d.isDirectory() && d.name in schemaByDir)
  .map((d) => d.name);

describe("fixture corpus", () => {
  for (const dir of fixtureDirs) {
    const schema = schemaByDir[dir]!;
    const dirPath = join(FIXTURES_DIR, dir);
    const files = readdirSync(dirPath).filter((f) => f.endsWith(".json"));

    for (const file of files) {
      it(`${dir}/${file} parses through schema`, () => {
        const raw = JSON.parse(readFileSync(join(dirPath, file), "utf-8"));
        expect(() => schema.parse(raw)).not.toThrow();
      });
    }
  }
});
