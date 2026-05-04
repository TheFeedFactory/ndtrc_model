import { describe, it, expect } from "vitest";
import {
  PromotionSchema,
  PromotionTypeSchema,
  ValidityStrategySchema,
} from "../src/index.js";

describe("PromotionTypeSchema", () => {
  it("accepts all valid values", () => {
    for (const v of ["none", "free", "discount", "gift", "allowance"]) {
      expect(PromotionTypeSchema.parse(v)).toBe(v);
    }
  });

  it("rejects invalid value", () => {
    expect(() => PromotionTypeSchema.parse("bogus")).toThrow();
  });
});

describe("ValidityStrategySchema", () => {
  it("accepts all valid values", () => {
    for (const v of ["always", "dateRange", "earlyBird", "lastMinute"]) {
      expect(ValidityStrategySchema.parse(v)).toBe(v);
    }
  });
});

describe("PromotionSchema", () => {
  it("parses a full promotion", () => {
    const result = PromotionSchema.parse({
      product: "Museum ticket",
      externalReference: "EXT-123",
      promotionType: "discount",
      discount: { percentage: 20 },
      translations: [{ lang: "nl", description: "20% korting" }],
      enabled: true,
      validityStrategy: "dateRange",
      startDate: "2026-01-01T00:00:00+01:00",
      endDate: "2026-12-31T23:59:59+01:00",
    });
    expect(result.promotionType).toBe("discount");
    expect(result.discount?.percentage).toBe(20);
  });

  it("accepts an empty object", () => {
    expect(PromotionSchema.parse({})).toEqual({});
  });

  it("rejects invalid date string", () => {
    expect(() =>
      PromotionSchema.parse({ startDate: "not-a-date" }),
    ).toThrow();
  });

  it("includes externalReference field (Maven 1.2.1)", () => {
    const result = PromotionSchema.parse({ externalReference: "ref-456" });
    expect(result.externalReference).toBe("ref-456");
  });

  it("preserves unknown keys via passthrough", () => {
    const result = PromotionSchema.parse({ extra: true });
    expect((result as Record<string, unknown>).extra).toBe(true);
  });
});
