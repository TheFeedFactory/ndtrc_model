import { describe, it, expect } from "vitest";
import { TrcitemRelationSchema } from "../src/index.js";

describe("TrcitemRelationSchema", () => {
  it("parses valid relation", () => {
    const result = TrcitemRelationSchema.parse({
      subItemGroups: [{ trcid: "grp-1" }],
    });
    expect(result.subItemGroups).toHaveLength(1);
  });

  it("accepts an empty object", () => {
    expect(TrcitemRelationSchema.parse({})).toEqual({});
  });

  it("preserves unknown keys", () => {
    const result = TrcitemRelationSchema.parse({ extra: true });
    expect((result as Record<string, unknown>).extra).toBe(true);
  });
});
