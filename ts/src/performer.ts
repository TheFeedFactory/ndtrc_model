import { z } from "zod";

export const PerformerSchema = z
  .object({
    roleid: z.string().optional(),
    label: z.string().optional(),
    rolelabel: z.string().optional(),
  })
  .passthrough();

export type Performer = z.infer<typeof PerformerSchema>;
