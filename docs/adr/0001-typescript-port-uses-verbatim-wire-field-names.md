# TypeScript port uses verbatim wire field names

The Groovy canonical model has historically inconsistent field naming
(`creationdate`, `availablefrom`, `lastupdated` are concatenated lowercase
while `trcItemDetails`, `priceElements`, `routeInfo`, `seoMetadata`,
`locationRef` are camelCase). Jackson serialises these straight to JSON, so
this is the actual wire format every Java and bash consumer of the FF API
already uses.

The TypeScript Zod schemas in `ts/src/` mirror those names **verbatim** —
no camelCase normalization, no transform layer, no separate "wire" vs
"domain" schemas. The package is a fidelity port of the canonical model;
hiding the legacy naming inside TS would force every TS consumer to either
re-discover the wire names anyway or maintain a translation layer.

Cleaning up the wire format is a separate cross-cutting project that would
update Java + TS together; until that happens, "the model" means "what
goes over the wire," and `creationdate` stays `creationdate`.

## Considered options

- **camelCase TS types with a Zod `.transform()` layer** — pretty TS, but
  asymmetric: the same schema can no longer encode outgoing JSON without
  an inverse transform, and grepping for a field name across Java + TS +
  OpenAPI stops working.
- **Two schemas (`Wire` and `Domain`) with bi-directional codecs** — most
  flexible, also the most surface area to keep in sync.
