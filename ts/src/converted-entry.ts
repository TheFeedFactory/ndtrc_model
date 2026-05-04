import { z } from "zod";
import { TRCItemSchema } from "./trc-item.js";

export const ConvertedEntrySchema = z
  .object({
    label: z.string().optional(),
    created: z.string().optional(),
    modified: z.string().optional(),
    errorMessage: z.string().optional(),
    externalId: z.string().optional(),
    trcItem: TRCItemSchema.optional(),
  })
  .passthrough();

export type ConvertedEntry = z.infer<typeof ConvertedEntrySchema>;
