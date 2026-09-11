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

  it("parses a file that carries nothing but its link", () => {
    const result = FileSchema.parse({ hlink: "https://example.com/photo.jpg" });
    expect(result.hlink).toBe("https://example.com/photo.jpg");
  });

  it("rejects a file without an hlink", () => {
    // A File without a link points at nothing. The API never produces one: a file built
    // from a blank URL is dropped rather than stored.
    expect(() => FileSchema.parse({})).toThrow();
    expect(() => FileSchema.parse({ filetype: "jpg", mediatype: "photo" })).toThrow();
  });

  it("rejects bad enum values", () => {
    const hlink = "https://example.com/photo.jpg";
    expect(() => FileSchema.parse({ hlink, filetype: "svg" })).toThrow();
    expect(() => FileSchema.parse({ hlink, mediatype: "unknown" })).toThrow();
  });

  it("rejects bad URL", () => {
    expect(() => FileSchema.parse({ hlink: "not-a-url" })).toThrow();
    expect(() => FileSchema.parse({ hlink: "" })).toThrow();
    expect(() => FileSchema.parse({ hlink: "/content/img/1/img.jpg" })).toThrow();
  });

  it("preserves unknown keys via passthrough", () => {
    const result = FileSchema.parse({
      hlink: "https://example.com/photo.jpg",
      extra: true,
    });
    expect((result as Record<string, unknown>).extra).toBe(true);
  });
});
