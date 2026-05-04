import { describe, it, expect } from "vitest";
import { sanitise } from "../src/internal/sanitise.js";

describe("sanitise", () => {
  const input = {
    trcid: "abc-123",
    owner: "john@example.com",
    createdby: "admin",
    lastupdatedby: "editor",
    legalowner: "ACME Corp",
    externalid: "ext-999",
    slug: "my-event-slug",
    contactinfo: {
      mail: [{ email: "secret@example.com", emailServiceType: "general" }],
      phone: [{ phonenumber: "+31612345678" }],
      fax: [{ faxnumber: "+31201234567" }],
    },
    nested: {
      owner: "nested-owner",
      safe: "kept",
    },
  };

  it("replaces all PII fields", () => {
    const result = sanitise(input) as Record<string, unknown>;
    expect(result.owner).not.toBe("john@example.com");
    expect(result.createdby).not.toBe("admin");
    expect(result.lastupdatedby).not.toBe("editor");
    expect(result.legalowner).not.toBe("ACME Corp");
    expect(result.externalid).not.toBe("ext-999");
    expect(result.slug).not.toBe("my-event-slug");

    const contactinfo = result.contactinfo as Record<string, unknown>;
    const mail = (contactinfo.mail as Record<string, unknown>[])[0];
    expect(mail.email).not.toBe("secret@example.com");
    const phone = (contactinfo.phone as Record<string, unknown>[])[0];
    expect(phone.phonenumber).not.toBe("+31612345678");
    const fax = (contactinfo.fax as Record<string, unknown>[])[0];
    expect(fax.faxnumber).not.toBe("+31201234567");
  });

  it("preserves non-PII fields", () => {
    const result = sanitise(input) as Record<string, unknown>;
    expect(result.trcid).toBe("abc-123");
    const contactinfo = result.contactinfo as Record<string, unknown>;
    const mail = (contactinfo.mail as Record<string, unknown>[])[0];
    expect(mail.emailServiceType).toBe("general");
  });

  it("replaces PII in nested objects", () => {
    const result = sanitise(input) as Record<string, unknown>;
    const nested = result.nested as Record<string, unknown>;
    expect(nested.owner).not.toBe("nested-owner");
    expect(nested.safe).toBe("kept");
  });

  it("produces stable output (same input → same output)", () => {
    const a = sanitise(input);
    const b = sanitise(input);
    expect(a).toEqual(b);
  });

  it("is idempotent (sanitising sanitised output is a no-op)", () => {
    const once = sanitise(input);
    const twice = sanitise(once);
    expect(twice).toEqual(once);
  });

  it("handles null, undefined, and primitives", () => {
    expect(sanitise(null)).toBe(null);
    expect(sanitise(undefined)).toBe(undefined);
    expect(sanitise(42)).toBe(42);
    expect(sanitise("hello")).toBe("hello");
  });

  it("handles arrays", () => {
    const arr = [{ owner: "x" }, { owner: "y" }];
    const result = sanitise(arr) as Record<string, unknown>[];
    expect(result[0].owner).not.toBe("x");
    expect(result[1].owner).not.toBe("y");
    expect(result[0].owner).not.toEqual(result[1].owner);
  });
});
