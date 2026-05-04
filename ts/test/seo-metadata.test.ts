import { describe, it, expect } from "vitest";
import {
  SeoMetadataSchema,
  OGTypeSchema,
  TwitterCardTypeSchema,
} from "../src/index.js";

describe("OGTypeSchema", () => {
  it("accepts all values", () => {
    for (const v of ["event", "place", "article", "organization"]) {
      expect(OGTypeSchema.parse(v)).toBe(v);
    }
  });

  it("rejects invalid value", () => {
    expect(() => OGTypeSchema.parse("blog")).toThrow();
  });
});

describe("TwitterCardTypeSchema", () => {
  it("accepts all values", () => {
    for (const v of ["summary", "summary_large_image"]) {
      expect(TwitterCardTypeSchema.parse(v)).toBe(v);
    }
  });
});

describe("SeoMetadataSchema", () => {
  it("parses valid SEO metadata", () => {
    const result = SeoMetadataSchema.parse({
      seoDetails: [
        {
          lang: "nl",
          metaTitle: "Dom van Utrecht",
          metaDescription: "Bezoek de Domtoren",
          ogType: "place",
          twitterCardType: "summary_large_image",
        },
      ],
      canonical: { canonicalUrl: "https://example.com/dom" },
      schemaOrgJsonLd: '{"@context":"https://schema.org"}',
    });
    expect(result.seoDetails).toHaveLength(1);
    expect(result.canonical?.canonicalUrl).toBe("https://example.com/dom");
  });

  it("accepts an empty object", () => {
    expect(SeoMetadataSchema.parse({})).toEqual({});
  });

  it("validates lang codes in seo details", () => {
    expect(() =>
      SeoMetadataSchema.parse({ seoDetails: [{ lang: "NL" }] }),
    ).toThrow();
  });

  it("preserves unknown keys", () => {
    const result = SeoMetadataSchema.parse({ extra: true });
    expect((result as Record<string, unknown>).extra).toBe(true);
  });
});
