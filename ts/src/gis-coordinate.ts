import { z } from "zod";

const numericString = (min: number, max: number) =>
  z
    .string()
    .refine(
      (v) => {
        const n = Number(v);
        return !Number.isNaN(n) && n >= min && n <= max;
      },
      { message: `Must be a numeric string in [${min}, ${max}]` },
    );

export const GISCoordinateSchema = z
  .object({
    xcoordinate: numericString(-180, 180).optional(),
    ycoordinate: numericString(-90, 90).optional(),
    label: z.string().optional(),
  })
  .passthrough();

export type GISCoordinate = z.infer<typeof GISCoordinateSchema>;
