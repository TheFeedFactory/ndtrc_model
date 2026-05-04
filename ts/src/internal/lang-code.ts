import { z } from "zod";

export const LangCodeSchema = z.string().regex(/^[a-z]{2}$/, {
  message: "Must be a lowercase ISO 639-1 two-letter language code",
});

export type LangCode = z.infer<typeof LangCodeSchema>;
