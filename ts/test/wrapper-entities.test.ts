import { describe, it, expect } from "vitest";
import {
  TRCItemGroupSchema,
  ConvertedEntrySchema,
  FetchedEntrySchema,
} from "../src/index.js";

describe("TRCItemGroupSchema", () => {
  it("parses a valid group", () => {
    const result = TRCItemGroupSchema.parse({
      trcid: "grp-1",
      wfstatus: "approved",
      entitytype: "EVENTGROUP",
      eventLinks: [{ eventId: "evt-1" }],
    });
    expect(result.trcid).toBe("grp-1");
    expect(result.eventLinks).toHaveLength(1);
  });

  it("accepts an empty object", () => {
    expect(TRCItemGroupSchema.parse({})).toEqual({});
  });

  it("preserves unknown keys", () => {
    const result = TRCItemGroupSchema.parse({ extra: true });
    expect((result as Record<string, unknown>).extra).toBe(true);
  });
});

describe("ConvertedEntrySchema", () => {
  it("parses a valid entry", () => {
    const result = ConvertedEntrySchema.parse({
      label: "Test entry",
      externalId: "ext-1",
      trcItem: { trcid: "trc-1", entitytype: "EVENEMENT" },
    });
    expect(result.trcItem?.trcid).toBe("trc-1");
  });

  it("accepts an empty object", () => {
    expect(ConvertedEntrySchema.parse({})).toEqual({});
  });

  it("preserves unknown keys", () => {
    const result = ConvertedEntrySchema.parse({ extra: true });
    expect((result as Record<string, unknown>).extra).toBe(true);
  });
});

describe("FetchedEntrySchema", () => {
  it("parses a valid entry", () => {
    const result = FetchedEntrySchema.parse({
      feed: "test-feed",
      sourceId: "src-1",
      externalId: "ext-1",
      data: { key: "value" },
      externalLocationInfo: {
        city: "Utrecht",
        latitude: "52.09",
        longitude: "5.12",
      },
    });
    expect(result.feed).toBe("test-feed");
    expect(result.externalLocationInfo?.city).toBe("Utrecht");
  });

  it("accepts an empty object", () => {
    expect(FetchedEntrySchema.parse({})).toEqual({});
  });

  it("preserves unknown keys", () => {
    const result = FetchedEntrySchema.parse({ extra: true });
    expect((result as Record<string, unknown>).extra).toBe(true);
  });
});
