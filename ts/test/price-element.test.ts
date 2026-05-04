import { describe, it, expect } from "vitest";
import {
  ExtraPriceInformationSchema,
  PriceDescriptionValueSchema,
  PriceElementSchema,
} from "../src/index.js";

describe("PriceDescriptionValueSchema", () => {
  it("accepts all valid values with correct capitalisation", () => {
    for (const v of ["Adults", "Children", "Groups", "CJP", "Pasholders", "Lastminute"]) {
      expect(PriceDescriptionValueSchema.parse(v)).toBe(v);
    }
  });

  it("rejects lowercase variants", () => {
    expect(() => PriceDescriptionValueSchema.parse("adults")).toThrow();
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
