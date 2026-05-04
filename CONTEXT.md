# Context — NDTRC Domain Model

This repository defines the canonical domain model for **eventconnectors** /
The Feed Factory: events, locations, venues, routes, and the metadata around
them (calendar, contact info, pricing, translations, promotions, SEO).

It is consumed as a **linked / shared model** by other Feed Factory projects.
Today consumers are JVM-based (Maven artifact `nl.eventconnectors.ff:ff-model`,
groupId published from this repo). A TypeScript + Zod port is being added so
JS/TS consumers can integrate and validate against the same model.

## Glossary

### `trcid`, `id`
Server-generated identifiers. **Read-only** from the consumer's perspective:
a create request may omit them and the server assigns them; a response always
includes them populated. This drives the request/response schema asymmetry
in the TypeScript port — see "TypeScript port → Schema variants" below.

## Source-of-truth rules

- **Groovy classes under `src/main/groovy/nl/ithelden/model/`** are canonical.
- The TypeScript + Zod port is a **hand-maintained mirror**. When the Groovy
  model changes, TS must be updated in the same change. Drift is caught by
  CI/test rather than codegen.

## TypeScript port

- Lives at `ts/` inside this repository (colocated, not a separate repo).
- Published as **`@eventconnectors/ndtrc-model`** to public npm.
- Field names are **verbatim wire format** — the Zod schemas validate the
  JSON exactly as Jackson emits it from the Groovy classes. No
  camelCase/snake_case translation, no transform layer. `creationdate` stays
  `creationdate`, `trcItemDetails` stays `trcItemDetails`. Cleanup of legacy
  wire-format inconsistencies is a separate cross-cutting project that would
  update Java + TS together.
- Date/time values are **validated ISO-8601 strings**, not coerced to JS
  `Date` objects. Roundtripping is preserved; date arithmetic is the
  consumer's job.

### Schema variants (request vs response)

Each entity has **one permissive primary schema** (`TRCItemSchema`,
`PromotionSchema`, …) where almost every field is `.optional()`, plus a
**curated response variant** (`TRCItemResponseSchema`, …) that promotes
server-guaranteed fields to required via `.required({ ... })`.

The primary schema doubles as the request-body schema (a consumer creating
or updating an entity sends only what they have). The response variant is
what an API consumer can rely on after a successful read.

Server-guaranteed fields for `TRCItem` (always present on response):
- `trcid`, `entitytype`, `creationdate`, `lastupdated`, `wfstatus`
- (case-by-case review per entity during implementation)

### Unknown keys

Object schemas use `.passthrough()` by default — unknown keys survive a
parse/emit roundtrip. Consumers who want strict validation of their own
outgoing requests opt in at the call site (`Schema.strict().parse(...)`);
no pre-built strict variants are exported.

### Business-rule depth

Schemas validate **shape, types, and immutable physical/format constraints**;
they do not enforce policy or relational invariants that the API itself may
relax over time.

- ✅ Encoded: ISO-8601 date-time string format; lat/lon ranges
  (lat ∈ [-90, 90], lon ∈ [-180, 180]); email shape on email fields; URL
  shape on URL fields; enum membership.
- ❌ Not encoded: cross-field date ordering (`availablefrom <= availableto`,
  `Promotion.startDate <= endDate`); Calendar `onrequest` ↔ structured-calendar
  mutual exclusion; deprecated-field warnings (those go in JSDoc).

### Language codes

All `lang` fields (16 sites across the model), plus `primaryLanguage`,
`availableLanguages[]`, and `targetLanguage`, are validated as
**ISO 639-1 two-letter lowercase codes**: `z.string().regex(/^[a-z]{2}$/)`.
Empty string, uppercase, locale-tagged forms (`nl-BE`), and three-letter
(`nld`) all fail.

### Enums and external vocabularies

- Groovy `enum` types map to Zod `z.enum([...])` with **verbatim** wire
  values (each enum's case is preserved — `WFStatus` is lowercase,
  `EntityType` is UPPERCASE, `PriceDescriptionValue` is Capitalized). No TS
  `enum` declarations; the Zod-derived string-literal union is the type.
- NDTRC category codes (the `2.3.1` "Braderie"-style taxonomy in
  `src/main/resources/schema/NDTRC-types-*.txt`) are validated as **open
  `z.string()`** with no further constraints. The taxonomy is owned by an
  external standards body and evolves independently.

### Build, runtime, distribution

- **Zod v4** (not v3). Existing FF consumers on Zod v3 will need a
  one-time upgrade to integrate; this is accepted to avoid a future
  migration of this package itself.
- Build via **`tsup`**, emitting **dual ESM + CJS** plus declarations.
  Target **Node 18+ / ES2022**.
- Published from **GitHub Actions** triggered on `v*` tag push, using a
  repo `NPM_TOKEN` secret. Migrate to npm trusted-publishing / OIDC
  provenance once the workflow is stable.

### Versioning

`@eventconnectors/ndtrc-model` is **lockstepped to the Maven version**.
`pom.xml` and `ts/package.json` always carry the same version string;
they bump together in the same commit, with a single git tag releasing
both. The first npm release is `1.2.x` (matching the current Maven
version), not `0.x` — the underlying contract is mature.

### Public API surface

Flat exports from the package root — no namespacing, no subpaths.
Naming convention:

- Runtime schemas: `XxxSchema` (e.g. `TRCItemSchema`, `CalendarSchema`)
- Response variants: `XxxResponseSchema`
- Inferred types: `Xxx` (e.g. `TRCItem`, `Calendar`)
- Inferred response types: `XxxResponse`
- Entitytype convenience aliases: `Event`, `Location`, `Venue`, `Route`, `EventGroup` (per the one-TRCItem rule)
- Enums exposed as both schema (`WFStatusSchema`) and type (`WFStatus`)

File layout under `ts/src/`: one file per entity (e.g. `trc-item.ts`,
`calendar.ts`, `address.ts`), mirroring `nl/ithelden/model/ndtrc/` 1:1,
re-exported through `ts/src/index.ts`. The 1:1 mapping is what makes the
parity test mechanical.

### Out-of-Groovy wire fields

When the OpenAPI documents wire fields the Groovy model doesn't have
(`Event.acl`, `Event.links`, possibly others), the TS port follows the
**strict canonical rule**: those fields are NOT in the Zod schema or the
inferred type, but they survive parsing through `.passthrough()`.
Consumers who need typed access either extend the schema locally with
`.merge()` / `.extend()`, or open an issue here to resolve the divergence
canonically (probably by adding the field to `TRCItem.groovy`).

This keeps the parity test mechanical: TS keys = Groovy fields, no
allowlist exceptions for "things documented elsewhere."

### Drift detection

Drift between the Groovy canonical model and the TS port is caught by:

1. **Static field-name parity test** (enforced): a test under `ts/test/`
   parses each `src/main/groovy/nl/ithelden/model/ndtrc/*.groovy` class,
   extracts field names, and asserts they match the corresponding Zod
   schema's known keys (modulo a small allowlist).
2. **JSON fixture corpus** (additive): `ts/test/fixtures/` holds real API
   responses fetched via `tff-cli` and sanitized of identifiable data.
   Each fixture must `Schema.parse()` cleanly. The corpus grows
   organically — every real-world parse failure adds a fixture.

### Deprecated fields

Deprecated wire fields (`validatedby`, `offline`, `isprivate`,
`productiontrcid`, …) are **included** in the TS schemas with a
`@deprecated` JSDoc tag. The wire still carries them; the TS surface
matches the wire (per the wire-fidelity rule above) and IDE strike-through
discourages new use.

### Calendar-specific notes

- **`SingleDate.when` (singular) vs `ExceptionDate.whens` (plural)** — both
  are `List<When>`; the plurality difference is a wire-format legacy wart.
  Documented inline on both fields so consumers don't think it's a typo.
- **Primitive booleans on Calendar (`cancelled`, `soldout`,
  `excludeholidays`)** are always present on the wire (Jackson can't omit
  primitives). Treated as `.optional()` on the primary/request schema
  (server defaults to `false` on omission) and required `z.boolean()` on
  the response variant.
- **`calendarType`** is server-derived from the other Calendar fields via
  `determineCalendarType()`. `.optional()` on the primary schema; required
  on the response variant. Same pattern as `wfstatus`.
- **Time-of-day strings (`When.timestart`, `When.timeend`)** validate as
  24-hour `HH:mm` — regex `^([01]\d|2[0-3]):[0-5]\d$`, optional. The
  Groovy-side tolerance for empty/whitespace times is treated as a bug
  not worth perpetuating in the schema.

### Entity shape: one TRCItem, not five

Following the Groovy canonical model, the TS port exposes one
`TRCItemSchema` (and one `TRCItemResponseSchema`) discriminated at runtime
by the `entitytype` field — not a `z.discriminatedUnion` of `Event`,
`Location`, `Venue`, `Route`, `EventGroup` like the OpenAPI doc does. Thin
type aliases (`type Event = z.infer<typeof TRCItemSchema> & { entitytype:
'EVENEMENT' }`, etc.) are exported as conveniences, but no separate
schemas — the OpenAPI's split is treated as a documentation aid, not a
contract divergence.
