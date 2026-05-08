# @eventconnectors/ndtrc_model

Zod schemas for the **NDTRC tourism data model** — a TypeScript port of the Groovy [ndtrc_model](https://github.com/TheFeedFactory/ndtrc_model) library. Every entity, enum, and nested type from the Groovy source has a matching Zod schema with field-for-field parity, enforced by automated tests.

## Installation

```bash
npm install @eventconnectors/ndtrc_model
```

Requires `zod` as a peer dependency (v3.x).

## Quick start

### Validating an API response

Use the `*Response` schema variants for data coming back from the FeedFactory API. These require server-guaranteed fields (`trcid`, `entitytype`, `creationdate`, etc.) to be present:

```ts
import { TRCItemResponseSchema } from "@eventconnectors/ndtrc_model";

const response = await fetch("https://api.feedfactory.nl/...");
const json = await response.json();

const item = TRCItemResponseSchema.parse(json);
// item.trcid is string (required, not optional)
```

### Building a request body

Use the primary schema for constructing outbound payloads. All fields are optional — you only set what you need:

```ts
import { TRCItemSchema } from "@eventconnectors/ndtrc_model";

const draft = TRCItemSchema.parse({
  entitytype: "EVENEMENT",
  trcItemDetails: [{ lang: "nl", shortdescription: "Koningsdag" }],
});
```

## Entity type aliases

Convenience types narrow `entitytype` to a specific literal:

```ts
import type { Event, LocationItemEntity, Venue, Route, EventGroup } from "@eventconnectors/ndtrc_model";
```

These are intersection types (`TRCItem & { entitytype: "EVENEMENT" }`, etc.) and work with any Zod-parsed `TRCItem`.

## `.passthrough()` policy

All schemas use `.passthrough()` by default — unknown keys in the input are preserved, not stripped. This means the API can add new fields without breaking your code.

To opt into strict validation (reject unknown keys), call `.strict()` on any schema:

```ts
import { GISCoordinateSchema } from "@eventconnectors/ndtrc_model";

const strict = GISCoordinateSchema.strict();
strict.parse({ xcoordinate: "5.12", unknownField: true }); // throws ZodError
```

## Extending schemas

Use Zod's `.extend()` to add fields that exist in the wire format but aren't in the Groovy model:

```ts
import { TRCItemSchema } from "@eventconnectors/ndtrc_model";
import { z } from "zod";

const MyTRCItemSchema = TRCItemSchema.extend({
  customScore: z.number().optional(),
});
```

## Version lockstep

This package version tracks the Groovy `ndtrc_model` version. When the Groovy model adds or changes fields, a corresponding release of this package follows with the same version bump. Automated parity tests ensure no field drift between the two.

## Exported schemas

Every schema and its inferred TypeScript type are exported from the package root. Key entities:

| Schema | Description |
|---|---|
| `TRCItemSchema` / `TRCItemResponseSchema` | Core tourism item (event, location, venue, route) |
| `TRCItemGroupSchema` | Event group with event links |
| `CalendarSchema` / `CalendarResponseSchema` | Opening hours, single dates, patterns, exceptions |
| `ContactinfoSchema` | Phone, mail, fax, URLs |
| `FileSchema` | Media files and titles |
| `LocationSchema` | Location with address and GIS coordinates |
| `PriceElementSchema` | Pricing with descriptions and values |
| `PromotionSchema` | Promotions, discounts, validity strategies |
| `RouteInfoSchema` | Route details with POIs and coordinates |
| `SeoMetadataSchema` | SEO configuration and canonical URLs |
| `ConvertedEntrySchema` | Converter pipeline wrapper |
| `FetchedEntrySchema` | Fetcher pipeline wrapper |

See the [source index](./src/index.ts) for the full export list.

## License

Apache-2.0
