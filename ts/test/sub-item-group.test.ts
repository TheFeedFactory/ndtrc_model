import { describe, it, expect } from "vitest";
import { SubItemGroupSchema } from "../src/index.js";

describe("SubItemGroupSchema", () => {
  it("parses valid sub-item group", () => {
    const result = SubItemGroupSchema.parse({
      trcid: "sub-123",
      type: { catid: "room" },
      subItemTranslations: [{ lang: "nl", title: "Kamer 1" }],
    });
    expect(result.trcid).toBe("sub-123");
  });

  it("accepts an empty object", () => {
    expect(SubItemGroupSchema.parse({})).toEqual({});
  });

  it("rejects invalid lang in translations", () => {
    expect(() =>
      SubItemGroupSchema.parse({
        subItemTranslations: [{ lang: "NL" }],
      }),
    ).toThrow();
  });

  it("preserves unknown keys", () => {
    const result = SubItemGroupSchema.parse({ extra: true });
    expect((result as Record<string, unknown>).extra).toBe(true);
  });
});
