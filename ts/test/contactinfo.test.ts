import { describe, it, expect } from "vitest";
import {
  ContactinfoSchema,
  MailSchema,
  PhoneSchema,
  UrlSchema,
  URLServiceTypeSchema,
} from "../src/index.js";

describe("URLServiceTypeSchema", () => {
  it("accepts all valid enum values", () => {
    for (const v of [
      "general", "booking", "review", "video", "webshop",
      "socialmedia", "lastminute", "virtualtour", "dmo",
      "sustainability", "venuefinder", "travelbase", "homepage",
    ]) {
      expect(URLServiceTypeSchema.parse(v)).toBe(v);
    }
  });

  it("rejects invalid enum value", () => {
    expect(() => URLServiceTypeSchema.parse("invalid")).toThrow();
  });
});

describe("MailSchema", () => {
  it("parses a valid mail", () => {
    const result = MailSchema.parse({
      email: "info@example.com",
      reservations: true,
    });
    expect(result.email).toBe("info@example.com");
  });

  it("rejects invalid email", () => {
    expect(() => MailSchema.parse({ email: "not-an-email" })).toThrow();
  });
});

describe("UrlSchema", () => {
  it("parses a valid url entry", () => {
    const result = UrlSchema.parse({
      url: "https://example.com",
      targetLanguage: "nl",
      urlServiceType: "booking",
    });
    expect(result.url).toBe("https://example.com");
  });

  it("rejects invalid URL", () => {
    expect(() => UrlSchema.parse({ url: "not-a-url" })).toThrow();
  });

  it("rejects invalid targetLanguage", () => {
    expect(() =>
      UrlSchema.parse({
        url: "https://example.com",
        targetLanguage: "NL",
      }),
    ).toThrow();
  });

  it("rejects invalid urlServiceType", () => {
    expect(() =>
      UrlSchema.parse({
        url: "https://example.com",
        urlServiceType: "invalid",
      }),
    ).toThrow();
  });
});

describe("ContactinfoSchema", () => {
  it("parses a full contact info object", () => {
    const result = ContactinfoSchema.parse({
      label: "Main office",
      mail: { email: "info@example.com" },
      phone: { number: "+31301234567" },
      urls: [
        {
          url: "https://example.com",
          urlServiceType: "general",
          targetLanguage: "nl",
        },
      ],
      address: { city: "Utrecht", country: "NL" },
    });
    expect(result.label).toBe("Main office");
    expect(result.mail?.email).toBe("info@example.com");
  });

  it("accepts an empty object (all fields optional)", () => {
    expect(ContactinfoSchema.parse({})).toEqual({});
  });

  it("accepts deprecated list fields", () => {
    const result = ContactinfoSchema.parse({
      mails: [{ email: "old@example.com" }],
      phones: [{ number: "+31123456789" }],
    });
    expect(result.mails).toHaveLength(1);
  });

  it("preserves unknown keys via passthrough", () => {
    const result = ContactinfoSchema.parse({ extra: "data" });
    expect((result as Record<string, unknown>).extra).toBe("data");
  });

  it("rejects unknown keys in strict mode", () => {
    expect(() =>
      ContactinfoSchema.strict().parse({ label: "x", typo: true }),
    ).toThrow();
  });
});
