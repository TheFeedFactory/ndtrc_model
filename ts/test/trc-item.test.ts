import { describe, it, expect } from "vitest";
import {
  TRCItemSchema,
  TRCItemResponseSchema,
  WFStatusSchema,
  EntityTypeSchema,
} from "../src/index.js";

describe("WFStatusSchema", () => {
  it("accepts all values", () => {
    for (const v of ["draft", "readyforvalidation", "approved", "rejected", "deleted", "archived"]) {
      expect(WFStatusSchema.parse(v)).toBe(v);
    }
  });

  it("rejects invalid value", () => {
    expect(() => WFStatusSchema.parse("pending")).toThrow();
  });
});

describe("EntityTypeSchema", () => {
  it("accepts all values", () => {
    for (const v of ["EVENEMENT", "LOCATIE", "EVENTGROUP", "ROUTE", "VENUE"]) {
      expect(EntityTypeSchema.parse(v)).toBe(v);
    }
  });

  it("rejects lowercase", () => {
    expect(() => EntityTypeSchema.parse("evenement")).toThrow();
  });
});

describe("TRCItemSchema", () => {
  it("parses a permissive create body", () => {
    const result = TRCItemSchema.parse({
      entitytype: "EVENEMENT",
      trcItemDetails: [
        { lang: "nl", title: "Testfestival", shortdescription: "Leuk festival" },
      ],
      calendar: { alwaysopen: true },
    });
    expect(result.entitytype).toBe("EVENEMENT");
  });

  it("accepts an empty object (permissive primary schema)", () => {
    expect(TRCItemSchema.parse({})).toEqual({});
  });

  it("preserves unknown keys via passthrough", () => {
    const result = TRCItemSchema.parse({ acl: ["admin"], links: {} });
    expect((result as Record<string, unknown>).acl).toEqual(["admin"]);
    expect((result as Record<string, unknown>).links).toEqual({});
  });

  it("rejects unknown keys in strict mode", () => {
    expect(() =>
      TRCItemSchema.strict().parse({
        trcid: "123",
        typo: "oops",
      }),
    ).toThrow();
  });
});

describe("TRCItemResponseSchema", () => {
  const validResponse = {
    trcid: "trc-123",
    entitytype: "EVENEMENT" as const,
    creationdate: "2026-01-01T00:00:00.000+01:00",
    lastupdated: "2026-05-01T12:00:00.000+02:00",
    wfstatus: "approved" as const,
    trcItemDetails: [{ lang: "nl", title: "Testfestival" }],
  };

  it("parses a populated response", () => {
    const result = TRCItemResponseSchema.parse(validResponse);
    expect(result.trcid).toBe("trc-123");
    expect(result.wfstatus).toBe("approved");
  });

  it("also passes the primary schema", () => {
    expect(() => TRCItemSchema.parse(validResponse)).not.toThrow();
  });

  it("rejects a create body missing server-guaranteed fields", () => {
    expect(() =>
      TRCItemResponseSchema.parse({
        entitytype: "EVENEMENT",
        trcItemDetails: [{ lang: "nl", title: "Test" }],
      }),
    ).toThrow();
  });
});
