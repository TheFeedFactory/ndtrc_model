import { describe, it, expect } from "vitest";
import { FileSchema, FileTypeSchema, MediaTypeSchema } from "../src/index.js";

describe("FileTypeSchema", () => {
  it("accepts all valid file types", () => {
    for (const v of ["jpeg", "jpg", "gif", "png", "mp3", "pdf", "gpx", "kml",
      "youtube", "kmz", "vimeo", "tif", "bmp", "jfif", "tiff", "webp"]) {
      expect(FileTypeSchema.parse(v)).toBe(v);
    }
  });

  it("rejects invalid file type", () => {
    expect(() => FileTypeSchema.parse("svg")).toThrow();
  });
});

describe("MediaTypeSchema", () => {
  it("accepts all valid media types", () => {
    for (const v of ["poster", "other", "audio", "brochure", "floorplan",
      "photo", "logo", "video", "roadmap", "text", "attachment", "qr"]) {
      expect(MediaTypeSchema.parse(v)).toBe(v);
    }
  });

  it("rejects invalid media type", () => {
    expect(() => MediaTypeSchema.parse("unknown")).toThrow();
  });
});

describe("FileSchema", () => {
  it("parses a valid file", () => {
    const result = FileSchema.parse({
      trcid: "abc-123",
      main: true,
      hlink: "https://example.com/photo.jpg",
      filetype: "jpg",
      mediatype: "photo",
      targetLanguage: "nl",
      title: {
        label: "Main photo",
        titleTranslations: [{ lang: "en", label: "Main photo" }],
      },
    });
    expect(result.filetype).toBe("jpg");
    expect(result.title?.label).toBe("Main photo");
  });

  it("accepts an empty object", () => {
    expect(FileSchema.parse({})).toEqual({});
  });

  it("rejects bad enum values", () => {
    expect(() => FileSchema.parse({ filetype: "svg" })).toThrow();
    expect(() => FileSchema.parse({ mediatype: "unknown" })).toThrow();
  });

  it("rejects bad URL", () => {
    expect(() => FileSchema.parse({ hlink: "not-a-url" })).toThrow();
  });

  it("preserves unknown keys via passthrough", () => {
    const result = FileSchema.parse({ extra: true });
    expect((result as Record<string, unknown>).extra).toBe(true);
  });
});
