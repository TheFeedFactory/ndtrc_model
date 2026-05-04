import { type ZodTypeAny, ZodObject } from "zod";

export function zodKeys(schema: ZodTypeAny): Set<string> {
  const unwrapped = unwrapSchema(schema);
  if (unwrapped instanceof ZodObject) {
    return new Set(Object.keys(unwrapped.shape));
  }
  throw new Error(
    `Expected a ZodObject schema, got ${unwrapped.constructor.name}`,
  );
}

function unwrapSchema(schema: ZodTypeAny): ZodTypeAny {
  const def = schema._def as Record<string, unknown>;

  if ("innerType" in def) {
    return unwrapSchema(def.innerType as ZodTypeAny);
  }
  if ("schema" in def && def.schema instanceof ZodObject) {
    return unwrapSchema(def.schema);
  }
  return schema;
}
