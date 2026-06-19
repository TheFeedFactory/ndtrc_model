import { describe, it, expect } from "vitest";
import {
  ExtraPriceInformationSchema,
  PriceDescriptionValueSchema,
  PriceElementSchema,
  StandardPriceDescriptionValues,
} from "../src/index.js";

describe("PriceDescriptionValueSchema", () => {
  it("accepts the standard default values", () => {
    for (const v of StandardPriceDescriptionValues) {
      expect(PriceDescriptionValueSchema.parse(v)).toBe(v);
    }
  });

  it("accepts free-form, account-defined values (price types are configurable per account)", () => {
    expect(PriceDescriptionValueSchema.parse("adults")).toBe("adults");
    expect(PriceDescriptionValueSchema.parse("RotterdamCityCard")).toBe("RotterdamCityCard");
  });

  it("rejects non-string values", () => {
    expect(() => PriceDescriptionValueSchema.parse(123)).toThrow();
  });
});

describe("ExtraPriceInformationSchema", () => {
  it("parses valid extra price info", () => {
    const result = ExtraPriceInformationSchema.parse({ lang: "nl", text: "Gratis voor kinderen" });
    expect(result.text).toBe("Gratis voor kinderen");
  });

  it("rejects invalid lang", () => {
    expect(() => ExtraPriceInformationSchema.parse({ lang: "NL" })).toThrow();
  });
});

describe("PriceElementSchema", () => {
  it("parses a full price element", () => {
    const result = PriceElementSchema.parse({
      freeentrance: false,
      priceValue: { from: 10.0, until: 15.0 },
      description: {
        value: "Adults",
        descriptionTranslations: [{ lang: "nl", text: "Volwassenen" }],
      },
      comments: [{ text: "Including tour" }],
      extraPriceInformations: [{ lang: "nl", text: "Excl. garderobe" }],
    });
    expect(result.priceValue?.from).toBe(10.0);
    expect(result.description?.value).toBe("Adults");
  });

  it("accepts an empty object", () => {
    expect(PriceElementSchema.parse({})).toEqual({});
  });

  it("preserves unknown keys via passthrough", () => {
    const result = PriceElementSchema.parse({ extra: true });
    expect((result as Record<string, unknown>).extra).toBe(true);
  });
});
