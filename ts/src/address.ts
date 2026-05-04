import { z } from "zod";
import { GISCoordinateSchema } from "./gis-coordinate.js";

export const AddressSchema = z
  .object({
    main: z.boolean().optional(),
    reservation: z.boolean().optional(),
    title: z.string().optional(),
    city: z.string().optional(),
    citytrcid: z.string().optional(),
    country: z.string().optional(),
    housenr: z.string().optional(),
    street: z.string().optional(),
    streettrcid: z.string().optional(),
    zipcode: z.string().optional(),
    province: z.string().optional(),
    neighbourhood: z.string().optional(),
    district: z.string().optional(),
    gisCoordinates: z.array(GISCoordinateSchema).optional(),
  })
  .passthrough();

export type Address = z.infer<typeof AddressSchema>;
