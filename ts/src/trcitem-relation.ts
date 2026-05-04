import { z } from "zod";
import { SubItemGroupSchema } from "./sub-item-group.js";

export const TrcitemRelationSchema = z
  .object({
    subItemGroups: z.array(SubItemGroupSchema).optional(),
  })
  .passthrough();

export type TrcitemRelation = z.infer<typeof TrcitemRelationSchema>;
