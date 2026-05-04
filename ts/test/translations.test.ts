import { describe, it, expect } from "vitest";
import {
  LangCodeSchema,
  TranslationsSchema,
} from "../src/index.js";

describe("LangCodeSchema", () => {
  it("accepts valid ISO 639-1 codes", () => {
    expect(LangCodeSchema.parse("nl")).toBe("nl");
    expect(LangCodeSchema.parse("en")).toBe("en");
    expect(LangCodeSchema.parse("de")).toBe("de");
    expect(LangCodeSchema.parse("fr")).toBe("fr");
  });

  it("rejects uppercase codes", () => {
    expect(() => LangCodeSchema.parse("NL")).toThrow();
  });

  it("rejects three-letter codes", () => {
    expect(() => LangCodeSchema.parse("nld")).toThrow();
  });

  it("rejects locale tags", () => {
    expect(() => LangCodeSchema.parse("nl-BE")).toThrow();
  });

  it("rejects empty string", () => {
    expect(() => LangCodeSchema.parse("")).toThrow();
  });

  it("rejects full language names", () => {
    expect(() => LangCodeSchema.parse("Dutch")).toThrow();
  });
});

describe("TranslationsSchema", () => {
  it("parses a valid translations object", () => {
    const result = TranslationsSchema.parse({
      primaryLanguage: "nl",
      availableLanguages: ["nl", "en", "de"],
    });
    expect(result.primaryLanguage).toBe("nl");
    expect(result.availableLanguages).toEqual(["nl", "en", "de"]);
  });

  it("accepts an empty object (all fields optional)", () => {
    const result = TranslationsSchema.parse({});
    expect(result).toEqual({});
  });

  it("rejects invalid primary language", () => {
    expect(() =>
      TranslationsSchema.parse({ primaryLanguage: "NL" }),
    ).toThrow();
  });

  it("rejects invalid language in available list", () => {
    expect(() =>
      TranslationsSchema.parse({ availableLanguages: ["nl", "nld"] }),
    ).toThrow();
  });

  it("preserves unknown keys via passthrough", () => {
    const result = TranslationsSchema.parse({
      primaryLanguage: "nl",
      extra: "data",
    });
    expect((result as Record<string, unknown>).extra).toBe("data");
  });

  it("rejects unknown keys in strict mode", () => {
    expect(() =>
      TranslationsSchema.strict().parse({
        primaryLanguage: "nl",
        typo: "oops",
      }),
    ).toThrow();
  });
});
