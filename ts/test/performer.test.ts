import { describe, it, expect } from "vitest";
import { PerformerSchema } from "../src/index.js";

describe("PerformerSchema", () => {
  it("parses a valid performer", () => {
    const result = PerformerSchema.parse({
      roleid: "actor",
      label: "John Doe",
      rolelabel: "Lead Actor",
    });
    expect(result.label).toBe("John Doe");
  });

  it("accepts an empty object (all fields optional)", () => {
    expect(PerformerSchema.parse({})).toEqual({});
  });

  it("rejects non-string field", () => {
    expect(() => PerformerSchema.parse({ roleid: 123 })).toThrow();
  });

  it("preserves unknown keys via passthrough", () => {
    const result = PerformerSchema.parse({ label: "Jane", extra: true });
    expect((result as Record<string, unknown>).extra).toBe(true);
  });
});
