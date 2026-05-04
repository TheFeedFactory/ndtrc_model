# TypeScript + Zod port of the NDTRC domain model

Labels: `needs-triage`, `enhancement`, `typescript`

## Problem Statement

The Feed Factory's canonical domain model (events, locations, venues,
routes, calendars, contact info, pricing, translations, promotions, SEO)
lives in this repository as Groovy classes published to Maven Central as
`nl.eventconnectors.ff:ff-model`. Today's consumers are JVM-only.

Several Feed Factory projects are TypeScript-based (`ff-doc`, `ff-mcp`,
`ff-shared-components`, `ff-feed-gui`, `ff-dashboard`, `ff-gui`,
`multifeed`). When they integrate with the FF API they either:

- hand-roll local TypeScript types that drift from the Java side over
  time;
- fall back to `unknown` / `any` and lose type safety; or
- shell out to bash + `jq` (e.g. `multifeed/scripts/lib/ec-api.sh`).

The first concrete adopter, `multifeed`, scrapes external sources into
its own simpler `simple-model` shape, then maps those records into FF
API payloads. Without a shared, validated TypeScript model, every
mapping is fragile, every API response is parsed by hand, and breakages
caused by Groovy-side model changes go undetected until production data
fails.

## Solution

Add a hand-maintained TypeScript port of the NDTRC domain model, shipped
as the npm package `@eventconnectors/ndtrc-model`. The package exposes
**Zod v4 schemas** that validate the JSON wire format, plus the
**inferred TypeScript types**, with the Groovy classes remaining the
source of truth.

The port colocates inside this repository under `ts/`, so a single
commit changes both the Java and TypeScript sides of the model and a
single git tag releases both artifacts. Drift between the two is caught
mechanically by a static field-name parity test plus a growing JSON
fixture corpus seeded from real API responses.

Field names match the wire verbatim, dates remain ISO-8601 strings,
unknown wire keys survive parsing through `.passthrough()`, and each
entity exposes a permissive primary schema (suitable for outgoing
request bodies) plus a curated response variant where
server-guaranteed fields (`trcid`, `entitytype`, `creationdate`,
`lastupdated`, `wfstatus`, …) are required.

## User Stories

1. As a TypeScript service developer integrating with the FF API, I want
   a typed Zod schema for `TRCItem` so I can validate incoming JSON
   without hand-rolling types.
2. As a TypeScript service developer, I want `z.infer`-derived
   TypeScript types for every NDTRC entity so my code has compile-time
   safety against the model.
3. As a TypeScript service developer building a request body for an FF
   API create endpoint, I want a permissive schema where server-managed
   fields (`trcid`, `creationdate`, etc.) are optional so the schema
   does not reject valid create payloads.
4. As a TypeScript service developer parsing a successful FF API
   response, I want a stricter response-variant schema where
   server-guaranteed fields are required so I can rely on
   `response.trcid` being a `string`, not `string | undefined`.
5. As a multifeed maintainer, I want to validate the TRCItem-shaped
   payloads my mapper produces before POSTing to the FF API so mapping
   bugs surface in tests, not at the API boundary.
6. As a multifeed maintainer, I want to parse FF API responses into
   typed objects so my downstream code can rely on field shapes.
7. As a maintainer of any FF TypeScript project, I want to install the
   package from public npm with a single dependency so adoption has zero
   internal-registry friction.
8. As a maintainer of an FF TypeScript project on Zod v3, I want a clear
   migration path to consume the package on Zod v4.
9. As a model maintainer (Groovy side), I want to be unable to ship a
   Groovy-side field change without also updating the TypeScript port,
   because CI fails when the two sides diverge in field names.
10. As a model maintainer, I want a single version number across the
    Maven artifact and the npm package so consumers never have to ask
    "is npm 1.2.5 compatible with Java 1.2.5?".
11. As a model maintainer, I want releases of both artifacts to be
    triggered by a single git tag so the lockstep rule is mechanical,
    not manual.
12. As a TypeScript service developer doing a fetch / mutate / PUT
    roundtrip, I want unknown wire fields to survive parsing so my PUT
    body does not silently drop fields the server added in a newer API
    version.
13. As a TypeScript service developer building a strict request
    validator (e.g. for an internal admin tool), I want to opt into
    strict-key validation at the call site so typos in my outgoing
    payloads fail fast.
14. As a TypeScript developer working with `Calendar`, I want each of
    the 12 nested types (SingleDate, PatternDate, PatternDate.Open, When,
    Status, StatusTranslation, ExtraInformation, ExceptionDate, Comment,
    CommentTranslation, RecurrencyType, CalendarType) to be available as
    its own importable schema and type so I can validate calendar
    fragments in isolation.
15. As a TypeScript developer working with translations, I want `lang`
    fields to be validated as ISO 639-1 lowercase two-letter codes so
    obvious typos (`Dutch`, `NL`, empty string) are caught at parse
    time.
16. As a TypeScript developer working with geographic data, I want
    latitude/longitude validated against their physical ranges so
    `lat: 9999` is rejected.
17. As a TypeScript developer working with email and URL fields, I want
    them validated as emails and URLs respectively so structurally
    invalid values fail at the boundary.
18. As a TypeScript developer working with date/time fields, I want
    values validated as ISO-8601 strings (not coerced to JS `Date`) so
    a parse / mutate / re-emit roundtrip is byte-stable.
19. As a TypeScript developer working with enums (`WFStatus`,
    `EntityType`, `FileType`, `RouteType`, etc.), I want the literal
    wire values preserved exactly (`readyforvalidation` not
    `readyForValidation`, `EVENEMENT` not `evenement`) so my code
    matches what the API actually sends.
20. As a TypeScript developer narrowing a `TRCItem` by entity type, I
    want convenience type aliases `Event`, `Location`, `Venue`, `Route`,
    `EventGroup` so I can write `function handleEvent(e: Event)` without
    constructing the intersection myself.
21. As a TypeScript developer importing from the package, I want a flat
    export surface with predictable naming (`TRCItemSchema` for the
    runtime, `TRCItem` for the type) so autocomplete shows me everything
    I need without subpath knowledge.
22. As a maintainer doing a patch release for a TypeScript-only fix, I
    accept that I bundle the fix into the next legitimate model version
    bump rather than publishing an npm-only patch — the lockstep rule
    is non-negotiable.
23. As a maintainer adding a new field to `TRCItem.groovy`, I want the
    parity test to fail in CI until I add the matching field to the
    Zod schema so I cannot ship one without the other.
24. As a maintainer fixing a real-world parsing bug (the schema
    rejects a valid API response), I want the failing JSON to land in
    the fixture corpus as part of the fix so the same bug cannot
    re-emerge.
25. As a maintainer or contributor, I want the package readme on npm to
    show how to install, validate a TRCItem response, and build a
    request body so a new consumer can integrate in under 15 minutes.
26. As a TypeScript developer working with deprecated fields
    (`validatedby`, `offline`, `isprivate`, `productiontrcid`), I want
    those fields typed but marked `@deprecated` so existing code keeps
    working but new code gets an IDE strikethrough.
27. As a TypeScript developer encountering a wire field documented in
    the OpenAPI but absent from the Zod schema (`Event.acl`,
    `Event.links`), I want the field to survive parsing through
    `.passthrough()` and the project README to point me at how to
    extend the schema locally with `.merge()`.
28. As a release engineer, I want the GitHub Actions workflow to run
    the parity test and the fixture-corpus suite before publishing so
    a broken schema cannot reach npm.
29. As a security-conscious engineer, I want the npm publishing
    pipeline to use a stored `NPM_TOKEN` initially and to be capable of
    migrating to npm trusted-publishing / OIDC provenance later without
    rewriting the workflow.
30. As a contributor seeding the fixture corpus, I want a script that
    fetches real API responses via `tff-cli` using `FF_API_TOKEN` from
    the environment, sanitises identifiable fields (owner, createdby,
    contactinfo email/phone), and writes the result under
    `ts/test/fixtures/`.

## Implementation Decisions

### Architectural

- **Source of truth.** The Groovy classes under
  `src/main/groovy/nl/ithelden/model/` remain canonical. The
  TypeScript port is a hand-maintained mirror, not generated.
- **Colocation.** The TypeScript package lives under `ts/` in this
  repository. Releases ship from a single commit and a single git tag.
- **Distribution.** Published as `@eventconnectors/ndtrc-model` to
  public npm. Built with **tsup** as dual ESM + CJS plus declarations,
  targeting Node 18 / ES2022.
- **Zod v4** runtime. Existing FF consumers on Zod v3 are expected to
  migrate as part of adopting the package.
- **Versioning.** Lockstepped to the Maven artifact (`pom.xml` and
  `ts/package.json` carry identical version strings). First npm release
  is `1.2.x` to match the current model contract.
- **Release pipeline.** GitHub Actions, triggered on `v*` tag push,
  using a repo `NPM_TOKEN` secret. Future migration path: npm trusted
  publishing via OIDC.

### Schema design

- **Wire fidelity.** Field names mirror the Groovy classes verbatim —
  no camelCase normalisation, no `.transform()` rename layer.
- **Date handling.** ISO-8601 string validation, no coercion to JS
  `Date`. Roundtrips are byte-stable.
- **Optionality default.** Almost every field is `.optional()` on the
  primary schema, reflecting the wire reality where Jackson omits
  empty/null fields and the Groovy side enforces no required core.
- **Schema variants.** Each entity exports a permissive primary schema
  (doubles as the request schema) plus a curated response variant
  derived via `.required({ ... })`, which promotes
  server-guaranteed fields (`trcid`, `entitytype`, `creationdate`,
  `lastupdated`, `wfstatus`, plus per-entity additions) to required.
- **Unknown keys.** Object schemas use `.passthrough()` so unknown
  wire keys survive parse / emit roundtrips. No pre-built strict
  variant; consumers opt in at the call site.
- **Single TRCItem, not five.** One `TRCItemSchema` discriminated at
  runtime by `entitytype`, plus thin convenience type aliases for
  `Event`, `Location`, `Venue`, `Route`, `EventGroup`. The
  OpenAPI-style five-way schema split is treated as documentation, not
  contract.
- **Refinements.** Encode shape-level physical constraints only:
  lat/lon ranges, email format, URL format, ISO datetime format,
  HH:mm time-of-day format, ISO 639-1 lang codes, enum membership.
  Do **not** encode policy/relational invariants such as
  `availablefrom <= availableto`, Calendar `onrequest` mutex, or
  cross-field date ordering.
- **Enums.** Mapped to `z.enum([...])` with verbatim wire values per
  enum (case is preserved per enum — `WFStatus` lowercase,
  `EntityType` UPPERCASE, `PriceDescriptionValue` Capitalized).
- **External vocabularies.** NDTRC category codes (the `2.3.1`
  taxonomy) are validated as open `z.string()`; the taxonomy is owned
  by an external standards body.
- **Out-of-Groovy wire fields.** OpenAPI-documented fields that the
  Groovy model does not declare (`Event.acl`, `Event.links`) are
  **not** typed in the Zod schema. They survive parsing through
  `.passthrough()`. A separate follow-up will resolve whether these
  belong in the canonical Groovy model.
- **Deprecated fields.** Included in the schemas with `@deprecated`
  JSDoc tags. Removal is a coordinated Java + TS change.

### Public API surface

- **Flat exports** from the package root. No namespaces, no subpaths.
- **Naming convention:** `XxxSchema` for runtime schemas,
  `XxxResponseSchema` for response variants, `Xxx` for inferred types,
  `XxxResponse` for inferred response types. Enums exposed as both
  schema (`WFStatusSchema`) and type (`WFStatus`). Convenience
  entitytype aliases as plain types.

### Modules

The implementation breaks down into the following modules. Items
marked **deep** encapsulate non-trivial behaviour behind a small
interface and are explicitly designed to be testable in isolation:

1. **Per-entity schema modules** — one module per Groovy entity,
   mirroring the Groovy package layout 1:1. Each module owns its
   primary schema, response variant, inferred types, and any nested
   schemas (e.g. `Calendar` owns 11 nested types). Re-exported from
   the package root.

2. **Groovy field extractor** *(deep)* — given a directory of Groovy
   source files, returns a map from class name to the set of
   declared field names. Pure function over filesystem reads.
   Encapsulates Groovy's small surface (`<modifier>* <Type> <name>`,
   nested static classes, `@JsonProperty` annotations). Tested in
   isolation against fixture Groovy files.

3. **Zod schema introspector** *(deep)* — given a Zod object schema,
   returns the set of known keys. Drives the parity assertion. Pure
   function. Tested in isolation against synthetic Zod schemas.

4. **Parity test runner** — uses (2) and (3) to assert
   `groovyFields(class) === zodKeys(schema) ∪ allowlist(class)` for
   every entity. Fails the build on drift. Allowlist seeded with
   `forceoverwrite` (TRCItem-only, documented as not part of the wire).

5. **Fixture corpus loader** — walks `ts/test/fixtures/`, loads each
   JSON, parses it through the corresponding schema, and asserts
   success. Grows organically — every new real-world parse failure
   adds a fixture.

6. **Fixture fetcher script** — invokes `tff-cli` with
   `FF_API_TOKEN`, fetches a representative slice of entities,
   sanitises identifiable fields, and writes JSON files under
   `ts/test/fixtures/<entity>/<sample>.json`. Not part of the
   shipped package — lives in `ts/scripts/`.

7. **Fixture sanitiser** *(deep)* — given raw API JSON, returns
   sanitised JSON with identifiable fields (owner, createdby,
   contactinfo email/phone, externalid, slug) replaced by stable
   anonymised placeholders. Pure function. Tested in isolation.

8. **Build pipeline** — `tsup` config emitting dual ESM+CJS plus
   declarations, with `package.json` `exports` map. Not a deep
   module; configuration only.

9. **Release workflow** — GitHub Actions YAML triggered on `v*` tag
   push; runs typecheck, parity test, fixture corpus, then
   `npm publish`. Not a deep module; configuration only.

## Testing Decisions

### What makes a good test

- Tests assert **external behaviour**: a JSON input produces a parsed
  output, or fails to parse with a specific error. Tests do not assert
  internals like schema construction order or which Zod combinators
  are used.
- Tests are **data-driven** wherever possible: a fixture file plus
  the schema it should parse against, rather than imperative assembly
  in the test body.
- Refinement tests assert **the boundary**: e.g. for lat/lon, both a
  valid value parses and an out-of-range value fails. Single-direction
  assertions (only happy path or only failure) are insufficient.

### Modules to be tested

- **Per-entity schemas:** for each Groovy entity, a smoke test that
  asserts the primary schema parses a representative valid JSON and
  rejects a JSON that violates one of its physical-format constraints
  (e.g. a `GISCoordinate` with `lat: 9999` fails; a `Translation` with
  `lang: "Dutch"` fails; a `When` with `timestart: "25:00"` fails).
- **Response variants:** for every entity that has one, a test that
  asserts the response variant requires the server-guaranteed fields
  while the primary schema accepts them as absent.
- **Roundtrip stability:** for each fixture in the corpus, parsing
  through the schema and re-emitting via `JSON.stringify` produces a
  structurally equal object (same keys, same values) — this guards
  against accidental coercion.
- **Strict-mode opt-in:** a test that demonstrates
  `Schema.strict().parse(...)` rejects unknown keys while the default
  schema accepts them.
- **Groovy field extractor** *(deep)*: tested against a fixture set of
  Groovy classes that exercise simple fields, nested static classes,
  `@JsonProperty` annotations, deprecated fields, and the
  `forceoverwrite`-style "not on the wire" comment marker.
- **Zod schema introspector** *(deep)*: tested against synthetic Zod
  schemas covering plain objects, `.passthrough()`, `.merge()`, and
  nested schemas.
- **Fixture sanitiser** *(deep)*: tested against synthetic API JSON
  exercising every PII field family (owner, createdby, email, phone,
  externalid, slug); asserts stability (the same input always yields
  the same anonymised output) and idempotence (sanitising a sanitised
  fixture is a no-op).
- **Parity test runner:** integration test asserts that the real
  Groovy classes and real Zod schemas in this repository agree on
  field names — this is the load-bearing CI gate.

### Prior art

- `multifeed/simple-model/` is the closest existing prior art for a
  Zod-schema-as-shipped-artifact in this organisation; its structure
  (single schema file plus a generator that produces a
  reference markdown) informs the README/docs approach but not the
  schema design (it's a deliberately simpler intermediate model, not
  a TRCItem mirror).
- `ff-doc` uses Vitest; we adopt the same runner for consistency.
- The Java side has three Groovy unit tests
  (`FileTest`, `AddressNormaliseTest`, `PromotionTest`) — useful as a
  sanity reference for what behaviours the Groovy side considers
  worth asserting, but the TS port writes its own tests rather than
  porting these.

## Out of Scope

- Generating the TypeScript schemas from Groovy or from the OpenAPI.
  This PRD ships a hand-written port, with drift caught by tests.
- Generating Groovy from TypeScript. The Groovy side remains the
  source of truth.
- Modifying the canonical Groovy model. Resolving the `Event.acl` /
  `Event.links` divergence between Groovy and the OpenAPI is tracked
  separately.
- Migrating existing FF consumers (`ff-doc`, `ff-mcp`,
  `ff-shared-components`, etc.) to use the new package. Each
  consumer's adoption is a separate piece of work, scoped per
  consumer.
- Rewriting `multifeed/simple-model/`. The simple-model and the
  NDTRC model coexist as complementary layers (scraper-friendly
  intermediate vs canonical wire format).
- Coordinating the Zod v3 → v4 upgrade in consuming projects.
- Adding a CHANGELOG generator. The git history plus the lockstep
  Maven version is sufficient for the first release; revisit if
  consumers ask for a structured changelog.
- Publishing JSON-Schema or OpenAPI artifacts derived from the Zod
  schemas. Possible follow-up if downstream tooling needs them.
- npm trusted-publishing / OIDC provenance for the first release.
  Workflow uses a stored `NPM_TOKEN` initially; OIDC is a
  hardening follow-up.
- A web-rendered docs site for the npm package. README on npm is
  sufficient for v1.

## Further Notes

- **First adopter is multifeed.** The first integration test for
  the package is a multifeed-shaped scenario: a sample
  `simple-model` record, mapper-transformed to a TRCItem-shaped
  payload, validated by `TRCItemSchema.strict().parse(...)`.
- **API token shared in chat.** The token used during this design
  session to fetch fixtures should be rotated after the fixture
  corpus is seeded.
- **Open follow-up issues to file separately:**
  - Resolve `Event.acl` and `Event.links` divergence between
    Groovy `TRCItem` and the OpenAPI Event schema.
  - Coordinate Zod v3 → v4 upgrade across `ff-doc`, `ff-mcp`,
    `multifeed`, and any other Zod-using FF projects.
  - Migrate the npm publish workflow from stored `NPM_TOKEN` to
    npm trusted-publishing / OIDC provenance.
  - Investigate auditing the wire format for naming consistency
    (cleanup project that touches Java + TS together).
- **Reference documentation produced during design:**
  - `CONTEXT.md` — design rationale and conventions.
  - `docs/adr/0001-typescript-port-uses-verbatim-wire-field-names.md`
  - `docs/adr/0002-one-permissive-schema-plus-curated-response-variant.md`
  - `docs/adr/0003-npm-package-version-lockstepped-to-maven.md`
