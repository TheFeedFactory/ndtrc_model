import { describe, it, expect } from "vitest";
import {
  RouteInfoSchema,
  RouteTypeSchema,
  RouteDifficultySchema,
  SurfaceTypeSchema,
  PoiCategorySchema,
} from "../src/index.js";

describe("RouteTypeSchema", () => {
  it("accepts all values", () => {
    for (const v of ["driving_traffic", "driving", "walking", "cycling"]) {
      expect(RouteTypeSchema.parse(v)).toBe(v);
    }
  });
});

describe("RouteDifficultySchema", () => {
  it("accepts all values", () => {
    for (const v of ["easy", "moderate", "challenging", "difficult", "expert"]) {
      expect(RouteDifficultySchema.parse(v)).toBe(v);
    }
  });
});

describe("SurfaceTypeSchema", () => {
  it("accepts all values", () => {
    for (const v of ["paved", "gravel", "dirt", "sand", "grass", "cobblestone",
      "boardwalk", "rock", "snow", "water", "mixed"]) {
      expect(SurfaceTypeSchema.parse(v)).toBe(v);
    }
  });
});

describe("PoiCategorySchema", () => {
  it("accepts all values", () => {
    for (const v of ["museum", "monument", "castle", "church", "nature_area",
      "parking", "toilet", "rest_area", "picnic_area",
      "hotel", "camping", "hostel", "bed_breakfast",
      "restaurant", "cafe", "bar", "bakery",
      "swimming", "hiking_start", "cycling_start", "boat_rental",
      "bus_stop", "train_station", "ferry", "bike_rental",
      "viewpoint", "information_point", "other"]) {
      expect(PoiCategorySchema.parse(v)).toBe(v);
    }
  });
});

describe("RouteInfoSchema", () => {
  it("parses a valid route info", () => {
    const result = RouteInfoSchema.parse({
      type: "eventConnectors",
      routeType: "cycling",
      distanceInKilometers: 42.5,
      durationInMinutes: 150,
      difficulty: "moderate",
      primarySurface: "paved",
      routeCoordinates: [{ lat: 52.09, lng: 5.12 }],
      pois: [{ label: "Museum", category: "museum" }],
    });
    expect(result.distanceInKilometers).toBe(42.5);
    expect(result.pois).toHaveLength(1);
  });

  it("accepts an empty object", () => {
    expect(RouteInfoSchema.parse({})).toEqual({});
  });

  it("rejects invalid enum values", () => {
    expect(() => RouteInfoSchema.parse({ routeType: "flying" })).toThrow();
    expect(() => RouteInfoSchema.parse({ difficulty: "extreme" })).toThrow();
  });

  it("preserves unknown keys", () => {
    const result = RouteInfoSchema.parse({ extra: true });
    expect((result as Record<string, unknown>).extra).toBe(true);
  });
});
