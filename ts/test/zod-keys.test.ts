import { describe, it, expect } from "vitest";
import { z } from "zod";
import { zodKeys } from "../src/internal/zod-keys.js";

describe("zodKeys", () => {
  it("returns keys from a plain object schema", () => {
    const schema = z.object({
      name: z.string(),
      age: z.number(),
    });
    expect(zodKeys(schema)).toEqual(new Set(["name", "age"]));
  });

  it("returns keys from a .passthrough() schema", () => {
    const schema = z
      .object({
        x: z.string(),
        y: z.string(),
      })
      .passthrough();
    expect(zodKeys(schema)).toEqual(new Set(["x", "y"]));
  });

  it("returns keys from a .merge() schema", () => {
    const base = z.object({ id: z.string() });
    const extension = z.object({ name: z.string(), value: z.number() });
    const merged = base.merge(extension);
    expect(zodKeys(merged)).toEqual(new Set(["id", "name", "value"]));
  });

  it("returns keys from nested object schemas (top-level only)", () => {
    const schema = z.object({
      address: z.object({
        city: z.string(),
        zip: z.string(),
      }),
      name: z.string(),
    });
    expect(zodKeys(schema)).toEqual(new Set(["address", "name"]));
  });

  it("returns keys from a schema with optional fields", () => {
    const schema = z.object({
      required: z.string(),
      optional: z.string().optional(),
    });
    expect(zodKeys(schema)).toEqual(new Set(["required", "optional"]));
  });

  it("returns keys from a .passthrough().merge() chain", () => {
    const base = z.object({ a: z.string() }).passthrough();
    const ext = z.object({ b: z.number() });
    const merged = base.merge(ext);
    expect(zodKeys(merged)).toEqual(new Set(["a", "b"]));
  });

  it("throws for non-object schemas", () => {
    expect(() => zodKeys(z.string())).toThrow("Expected a ZodObject");
    expect(() => zodKeys(z.number())).toThrow("Expected a ZodObject");
    expect(() => zodKeys(z.array(z.string()))).toThrow("Expected a ZodObject");
  });
});
