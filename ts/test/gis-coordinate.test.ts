import { describe, it, expect } from "vitest";
import { GISCoordinateSchema } from "../src/gis-coordinate.js";

describe("GISCoordinateSchema", () => {
  it("parses a valid coordinate", () => {
    const result = GISCoordinateSchema.parse({
      xcoordinate: "5.1214",
      ycoordinate: "52.0907",
      label: "Utrecht",
    });
    expect(result.xcoordinate).toBe("5.1214");
    expect(result.ycoordinate).toBe("52.0907");
    expect(result.label).toBe("Utrecht");
  });

  it("accepts an empty object (all fields optional)", () => {
    const result = GISCoordinateSchema.parse({});
    expect(result).toEqual({});
  });

  it("accepts boundary values", () => {
    expect(() =>
      GISCoordinateSchema.parse({ xcoordinate: "-180", ycoordinate: "-90" }),
    ).not.toThrow();
    expect(() =>
      GISCoordinateSchema.parse({ xcoordinate: "180", ycoordinate: "90" }),
    ).not.toThrow();
    expect(() =>
      GISCoordinateSchema.parse({ xcoordinate: "0", ycoordinate: "0" }),
    ).not.toThrow();
  });

  it("rejects out-of-range longitude (xcoordinate)", () => {
    expect(() =>
      GISCoordinateSchema.parse({ xcoordinate: "181" }),
    ).toThrow();
    expect(() =>
      GISCoordinateSchema.parse({ xcoordinate: "-181" }),
    ).toThrow();
  });

  it("rejects out-of-range latitude (ycoordinate)", () => {
    expect(() =>
      GISCoordinateSchema.parse({ ycoordinate: "91" }),
    ).toThrow();
    expect(() =>
      GISCoordinateSchema.parse({ ycoordinate: "-91" }),
    ).toThrow();
  });

  it("rejects non-numeric coordinate strings", () => {
    expect(() =>
      GISCoordinateSchema.parse({ xcoordinate: "not-a-number" }),
    ).toThrow();
  });

  it("rejects a non-string xcoordinate", () => {
    expect(() =>
      GISCoordinateSchema.parse({ xcoordinate: 5.1214 }),
    ).toThrow();
  });

  it("rejects a non-string ycoordinate", () => {
    expect(() =>
      GISCoordinateSchema.parse({ ycoordinate: 52.0907 }),
    ).toThrow();
  });

  it("preserves unknown keys via passthrough", () => {
    const result = GISCoordinateSchema.parse({
      xcoordinate: "5.1214",
      ycoordinate: "52.0907",
      srid: "EPSG:4326",
    });
    expect((result as Record<string, unknown>).srid).toBe("EPSG:4326");
  });

  it("rejects unknown keys in strict mode", () => {
    expect(() =>
      GISCoordinateSchema.strict().parse({
        xcoordinate: "5.1214",
        ycoordinate: "52.0907",
        typo: "oops",
      }),
    ).toThrow();
  });
});
