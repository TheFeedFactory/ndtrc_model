import { z } from "zod";

export const ExternalLocationInfoSchema = z
  .object({
    externalId: z.string().optional(),
    locationName: z.string().optional(),
    address: z.string().optional(),
    housenr: z.string().optional(),
    zipcode: z.string().optional(),
    city: z.string().optional(),
    latitude: z.string().optional(),
    longitude: z.string().optional(),
    trcid: z.string().optional(),
    ffId: z.string().optional(),
  })
  .passthrough();

export type ExternalLocationInfo = z.infer<typeof ExternalLocationInfoSchema>;

export const FetchedEntrySchema = z
  .object({
    feed: z.string().optional(),
    sourceId: z.string().optional(),
    sourceUrl: z.string().optional(),
    errorMessage: z.string().optional(),
    label: z.string().optional(),
    created: z.string().optional(),
    modified: z.string().optional(),
    externalId: z.string().optional(),
    data: z.record(z.unknown()).optional(),
    externalLocationInfo: ExternalLocationInfoSchema.optional(),
  })
  .passthrough();

export type FetchedEntry = z.infer<typeof FetchedEntrySchema>;
