import { describe, it, expect } from "vitest";
import { LocationSchema } from "../src/index.js";

describe("LocationSchema", () => {
  it("parses a valid location", () => {
    const result = LocationSchema.parse({
      address: { city: "Utrecht", country: "NL" },
      label: "Domtoren",
      locationItem: { id: "loc-1", trcid: "trc-123", text: "Domtoren Utrecht" },
    });
    expect(result.label).toBe("Domtoren");
    expect(result.locationItem?.trcid).toBe("trc-123");
  });

  it("accepts an empty object", () => {
    expect(LocationSchema.parse({})).toEqual({});
  });

  it("validates nested address", () => {
    expect(() =>
      LocationSchema.parse({
        address: {
          gisCoordinates: [{ xcoordinate: "999" }],
        },
      }),
    ).toThrow();
  });

  it("preserves unknown keys", () => {
    const result = LocationSchema.parse({ extra: true });
    expect((result as Record<string, unknown>).extra).toBe(true);
  });
});
