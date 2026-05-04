import { describe, it, expect } from "vitest";
import {
  DataTypeSchema,
  TRCItemCategoriesSchema,
  CategorySchema,
} from "../src/index.js";

describe("DataTypeSchema", () => {
  it("accepts all valid values", () => {
    for (const v of ["yes", "yesno", "nullableyesno", "choice", "multichoice",
      "freetext", "integer", "decimal", "date", "data", "url", "email", "phone"]) {
      expect(DataTypeSchema.parse(v)).toBe(v);
    }
  });

  it("rejects invalid value", () => {
    expect(() => DataTypeSchema.parse("boolean")).toThrow();
  });
});

describe("TRCItemCategoriesSchema", () => {
  it("parses valid categories", () => {
    const result = TRCItemCategoriesSchema.parse({
      types: [{ catid: "2.1.1", isDefault: true }],
      categories: [{ catid: "3.1.1", datatype: "freetext", value: "test" }],
      soldout: false,
      canceled: false,
    });
    expect(result.types).toHaveLength(1);
    expect(result.categories?.[0]?.datatype).toBe("freetext");
  });

  it("accepts an empty object", () => {
    expect(TRCItemCategoriesSchema.parse({})).toEqual({});
  });

  it("category IDs are open strings (not enumerated)", () => {
    expect(CategorySchema.parse({ catid: "99.99.99" }).catid).toBe("99.99.99");
  });

  it("preserves unknown keys via passthrough", () => {
    const result = TRCItemCategoriesSchema.parse({ extra: true });
    expect((result as Record<string, unknown>).extra).toBe(true);
  });
});
