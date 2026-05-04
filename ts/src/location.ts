import { z } from "zod";
import { AddressSchema } from "./address.js";

export const LocationItemSchema = z
  .object({
    id: z.string().optional(),
    trcid: z.string().optional(),
    text: z.string().optional(),
  })
  .passthrough();

export type LocationItem = z.infer<typeof LocationItemSchema>;

export const LocationSchema = z
  .object({
    address: AddressSchema.optional(),
    label: z.string().optional(),
    locationItem: LocationItemSchema.optional(),
    venueItem: LocationItemSchema.optional(),
  })
  .passthrough();

export type Location = z.infer<typeof LocationSchema>;
