const PII_KEYS = new Set([
  "owner",
  "createdby",
  "lastupdatedby",
  "legalowner",
  "externalid",
  "slug",
]);

const CONTACT_PII_KEYS = new Set(["email", "phonenumber", "faxnumber"]);

const REDACTED_RE = /^redacted-[0-9a-f]{8}$/;

function stableHash(value: string): string {
  let h = 0;
  for (let i = 0; i < value.length; i++) {
    h = (Math.imul(31, h) + value.charCodeAt(i)) | 0;
  }
  return `redacted-${(h >>> 0).toString(16).padStart(8, "0")}`;
}

function sanitiseValue(key: string, value: unknown): unknown {
  if (typeof value !== "string") return value;
  if (PII_KEYS.has(key) || CONTACT_PII_KEYS.has(key)) {
    if (REDACTED_RE.test(value)) return value;
    return stableHash(value);
  }
  return value;
}

export function sanitise(json: unknown): unknown {
  if (json === null || json === undefined) return json;
  if (Array.isArray(json)) return json.map((item) => sanitise(item));
  if (typeof json === "object") {
    const result: Record<string, unknown> = {};
    for (const [key, value] of Object.entries(json as Record<string, unknown>)) {
      result[key] = sanitiseValue(key, value) === value
        ? sanitise(value)
        : sanitiseValue(key, value);
    }
    return result;
  }
  return json;
}
