import { describe, it, expect } from "vitest";
import { TRCItemDetailSchema } from "../src/index.js";

describe("TRCItemDetailSchema", () => {
  it("parses valid detail", () => {
    const result = TRCItemDetailSchema.parse({
      lang: "nl",
      title: "Dom van Utrecht",
      shortdescription: "Beroemde toren",
      longdescription: "De Domtoren is het bekendste monument van Utrecht.",
    });
    expect(result.title).toBe("Dom van Utrecht");
  });

  it("accepts an empty object", () => {
    expect(TRCItemDetailSchema.parse({})).toEqual({});
  });

  it("rejects invalid lang", () => {
    expect(() => TRCItemDetailSchema.parse({ lang: "NL" })).toThrow();
  });

  it("preserves unknown keys", () => {
    const result = TRCItemDetailSchema.parse({ extra: true });
    expect((result as Record<string, unknown>).extra).toBe(true);
  });
});
