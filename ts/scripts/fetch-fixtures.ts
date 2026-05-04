#!/usr/bin/env npx tsx
/**
 * Fetches sample entities from the NDTRC API via tff-cli, sanitises PII,
 * and writes them as JSON fixtures under ts/test/fixtures/<entity>/.
 *
 * Requirements:
 *   - FF_API_TOKEN env var set to a valid FeedFactory API token
 *   - tff-cli binary available at ../tff-cli/tff (relative to repo root)
 *
 * Usage:
 *   FF_API_TOKEN=<token> npx tsx ts/scripts/fetch-fixtures.ts
 */

import { execSync } from "node:child_process";
import { mkdirSync, writeFileSync } from "node:fs";
import { join, dirname } from "node:path";
import { fileURLToPath } from "node:url";
import { sanitise } from "../src/internal/sanitise.js";

const __dirname = dirname(fileURLToPath(import.meta.url));
const TFF_CLI = join(__dirname, "..", "..", "tff-cli", "tff");
const FIXTURES_DIR = join(__dirname, "..", "test", "fixtures");

const token = process.env.FF_API_TOKEN;
if (!token) {
  console.error("ERROR: FF_API_TOKEN environment variable is not set.");
  console.error("Set it to a valid FeedFactory API token and try again.");
  process.exit(1);
}

interface FetchSpec {
  dir: string;
  query: string;
  limit: number;
}

const specs: FetchSpec[] = [
  { dir: "trc-item", query: "entitytype:EVENEMENT", limit: 3 },
  { dir: "trc-item", query: "entitytype:LOCATIE", limit: 2 },
  { dir: "trc-item", query: "entitytype:VENUE", limit: 1 },
  { dir: "trc-item", query: "entitytype:ROUTE", limit: 1 },
];

function fetchEntities(query: string, limit: number): unknown[] {
  const cmd = `${TFF_CLI} search --token "${token}" --query "${query}" --limit ${limit} --format json`;
  const output = execSync(cmd, { encoding: "utf-8", maxBuffer: 10 * 1024 * 1024 });
  return JSON.parse(output) as unknown[];
}

let counter = 0;

for (const spec of specs) {
  const outDir = join(FIXTURES_DIR, spec.dir);
  mkdirSync(outDir, { recursive: true });

  console.log(`Fetching ${spec.limit} entities for "${spec.query}"...`);
  const entities = fetchEntities(spec.query, spec.limit);

  for (const entity of entities) {
    counter++;
    const sanitised = sanitise(entity);
    const filename = `sample-${String(counter).padStart(2, "0")}.json`;
    writeFileSync(join(outDir, filename), JSON.stringify(sanitised, null, 2) + "\n");
    console.log(`  → ${spec.dir}/${filename}`);
  }
}

console.log(`Done. Wrote ${counter} fixture(s).`);
