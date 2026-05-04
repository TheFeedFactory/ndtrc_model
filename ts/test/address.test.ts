import { describe, it, expect } from "vitest";
import { AddressSchema } from "../src/index.js";

describe("AddressSchema", () => {
  it("parses a valid address", () => {
    const result = AddressSchema.parse({
      main: true,
      city: "Utrecht",
      street: "Domplein",
      housenr: "1",
      zipcode: "3512 JC",
      country: "NL",
      province: "Utrecht",
      gisCoordinates: [
        { xcoordinate: "5.1214", ycoordinate: "52.0907", label: "Dom" },
      ],
    });
    expect(result.city).toBe("Utrecht");
    expect(result.gisCoordinates?.[0]?.xcoordinate).toBe("5.1214");
  });

  it("accepts an empty object (all fields optional)", () => {
    expect(AddressSchema.parse({})).toEqual({});
  });

  it("rejects non-string city", () => {
    expect(() => AddressSchema.parse({ city: 123 })).toThrow();
  });

  it("rejects non-boolean main", () => {
    expect(() => AddressSchema.parse({ main: "yes" })).toThrow();
  });

  it("validates nested GIS coordinates", () => {
    expect(() =>
      AddressSchema.parse({
        gisCoordinates: [{ xcoordinate: "999" }],
      }),
    ).toThrow();
  });

  it("preserves unknown keys via passthrough", () => {
    const result = AddressSchema.parse({ city: "Amsterdam", extra: true });
    expect((result as Record<string, unknown>).extra).toBe(true);
  });

  it("rejects unknown keys in strict mode", () => {
    expect(() =>
      AddressSchema.strict().parse({ city: "Amsterdam", typo: true }),
    ).toThrow();
  });
});
