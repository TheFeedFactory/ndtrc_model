import { describe, it, expect } from "vitest";
import { join } from "node:path";
import { extractGroovyFields } from "../src/internal/groovy-fields.js";
import { zodKeys } from "../src/internal/zod-keys.js";
import {
  AddressSchema,
  ContactinfoSchema,
  DescriptionTranslationSchema,
  FaxSchema,
  FileSchema,
  GISCoordinateSchema,
  MailSchema,
  PerformerSchema,
  PhoneSchema,
  TitleSchema,
  TitleTranslationSchema,
  TranslationsSchema,
  UrlSchema,
} from "../src/index.js";
import type { ZodTypeAny } from "zod";

const GROOVY_DIR = join(
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

/**
 * Maps Groovy class names to their corresponding Zod schemas.
 * Add new entries here as entity schemas are implemented.
 */
const entityMap: Record<string, ZodTypeAny> = {
  Address: AddressSchema,
  Contactinfo: ContactinfoSchema,
  "Contactinfo.DescriptionTranslation": DescriptionTranslationSchema,
  "Contactinfo.Fax": FaxSchema,
  "Contactinfo.Mail": MailSchema,
  "Contactinfo.Phone": PhoneSchema,
  "Contactinfo.Url": UrlSchema,
  File: FileSchema,
  "File.Title": TitleSchema,
  "File.Title.TitleTranslation": TitleTranslationSchema,
  GISCoordinate: GISCoordinateSchema,
  Performer: PerformerSchema,
  Translations: TranslationsSchema,
};

/**
 * Fields present in Groovy but intentionally excluded from the Zod schema.
 * Each entry documents why the field is excluded.
 *
 * - TRCItem.forceoverwrite: workflow field, not part of the wire format
 */
const allowlist: Record<string, Set<string>> = {
  TRCItem: new Set(["forceoverwrite"]),
};

describe("Groovy ↔ Zod parity", () => {
  it("every mapped entity has matching field sets", async () => {
    const groovyFields = await extractGroovyFields(GROOVY_DIR);

    for (const [className, schema] of Object.entries(entityMap)) {
      const groovy = groovyFields[className];
      expect(
        groovy,
        `Groovy class "${className}" not found in ${GROOVY_DIR}`,
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
  });

  it("fails when a Groovy field is added without a TS counterpart", async () => {
    const groovyFields = await extractGroovyFields(GROOVY_DIR);
    const gisFields = groovyFields["GISCoordinate"]!;

    const fakeGisFields = new Set([...gisFields, "newField"]);
    const zodFields = zodKeys(GISCoordinateSchema);

    const missing = [...fakeGisFields].filter((f) => !zodFields.has(f));
    expect(missing).toContain("newField");
  });
});
