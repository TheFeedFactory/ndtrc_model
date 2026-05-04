# One permissive schema per entity, plus a curated response variant

The FF API is asymmetric between request and response: identifiers
(`trcid`, nested `id`s) and several other fields (`creationdate`,
`lastupdated`, `createdby`, `lastupdatedby`, `wfstatus`) are
**server-generated** — a create request may omit them, but a successful
response always includes them. A naïve "one schema per entity" approach
either over-relaxes (response loses safety) or over-tightens (create
breaks).

For each entity we ship **one permissive primary schema** (`TRCItemSchema`
etc.) where almost every field is `.optional()`, plus a curated
`XxxResponseSchema` derived via `.required({ ... })` that promotes
server-guaranteed fields to required. The primary schema doubles as the
request-body schema; the response variant is what an API consumer can
rely on after a read.

## Considered options

- **Two separate schemas per entity (`XxxRequestSchema` +
  `XxxResponseSchema`)** — explicit but doubles maintenance for every
  Groovy change.
- **A `withServerFields()` builder that turns the base schema into a
  response variant** — elegant in theory; in practice still needs a
  per-entity list of read-only fields, and adds a builder API to
  maintain.

## Consequences

- The OpenAPI does not currently document a separate request vs response
  schema for these entities — we are inventing a stricter contract on the
  consumer side than the API itself promises. That's accepted: the
  response variant exists for ergonomics, not as a contract guarantee.
- The list of "server-guaranteed" fields per entity must be reviewed
  case-by-case during implementation rather than inferred mechanically.
