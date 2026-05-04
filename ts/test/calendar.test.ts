import { describe, it, expect } from "vitest";
import {
  CalendarSchema,
  CalendarResponseSchema,
  CalendarTypeSchema,
  RecurrencyTypeSchema,
  WhenSchema,
  SingleDateSchema,
  PatternDateSchema,
  ExceptionDateSchema,
} from "../src/index.js";

describe("CalendarTypeSchema", () => {
  it("accepts all values", () => {
    for (const v of ["NONE", "ALWAYSOPEN", "ONREQUEST", "OPENINGTIMES", "PATTERNDATES", "SINGLEDATES"]) {
      expect(CalendarTypeSchema.parse(v)).toBe(v);
    }
  });
});

describe("RecurrencyTypeSchema", () => {
  it("accepts all values", () => {
    for (const v of ["daily", "weekly", "monthlySimple", "monthlyComplex", "yearly"]) {
      expect(RecurrencyTypeSchema.parse(v)).toBe(v);
    }
  });
});

describe("WhenSchema", () => {
  it("accepts valid HH:mm times", () => {
    const result = WhenSchema.parse({ timestart: "09:00", timeend: "17:30" });
    expect(result.timestart).toBe("09:00");
  });

  it("rejects invalid HH:mm", () => {
    expect(() => WhenSchema.parse({ timestart: "25:00" })).toThrow();
    expect(() => WhenSchema.parse({ timestart: "9:00" })).toThrow();
    expect(() => WhenSchema.parse({ timestart: "09:60" })).toThrow();
    expect(() => WhenSchema.parse({ timeend: "not-a-time" })).toThrow();
  });

  it("accepts urls referencing Contactinfo.Url", () => {
    const result = WhenSchema.parse({
      timestart: "10:00",
      urls: [{ url: "https://tickets.example.com", urlServiceType: "booking" }],
    });
    expect(result.urls).toHaveLength(1);
  });
});

describe("CalendarSchema", () => {
  it("parses ALWAYSOPEN calendar", () => {
    const result = CalendarSchema.parse({
      alwaysopen: true,
      calendarType: "ALWAYSOPEN",
    });
    expect(result.alwaysopen).toBe(true);
  });

  it("parses ONREQUEST calendar", () => {
    const result = CalendarSchema.parse({
      onrequest: true,
      calendarType: "ONREQUEST",
    });
    expect(result.onrequest).toBe(true);
  });

  it("parses SINGLEDATES with multiple time slots", () => {
    const result = CalendarSchema.parse({
      singleDates: [
        {
          date: "2026-07-01T00:00:00.000+02:00",
          when: [
            { timestart: "10:00", timeend: "13:00" },
            { timestart: "15:00", timeend: "18:00" },
          ],
        },
      ],
      calendarType: "SINGLEDATES",
    });
    expect(result.singleDates?.[0]?.when).toHaveLength(2);
  });

  it("parses PATTERNDATES weekly", () => {
    const result = CalendarSchema.parse({
      patternDates: [
        {
          startdate: "2026-01-01T00:00:00.000+01:00",
          enddate: "2026-12-31T00:00:00.000+01:00",
          recurrencyType: "weekly",
          opens: [
            { day: 2, whens: [{ timestart: "09:00", timeend: "17:00" }] },
            { day: 6, whens: [{ timestart: "10:00", timeend: "14:00" }] },
          ],
        },
      ],
      calendarType: "PATTERNDATES",
    });
    expect(result.patternDates?.[0]?.opens).toHaveLength(2);
  });

  it("parses ExceptionDate", () => {
    const result = CalendarSchema.parse({
      closeds: [
        { date: "2026-12-25T00:00:00.000+01:00", whens: [] },
      ],
    });
    expect(result.closeds).toHaveLength(1);
  });

  it("accepts a sparse calendar (primary schema)", () => {
    expect(CalendarSchema.parse({})).toEqual({});
  });

  it("preserves unknown keys via passthrough", () => {
    const result = CalendarSchema.parse({ extra: true });
    expect((result as Record<string, unknown>).extra).toBe(true);
  });
});

describe("CalendarResponseSchema", () => {
  it("rejects sparse calendar missing required booleans", () => {
    expect(() =>
      CalendarResponseSchema.parse({
        calendarType: "ALWAYSOPEN",
        alwaysopen: true,
      }),
    ).toThrow();
  });

  it("accepts a full response calendar", () => {
    const result = CalendarResponseSchema.parse({
      alwaysopen: true,
      cancelled: false,
      soldout: false,
      excludeholidays: false,
      calendarType: "ALWAYSOPEN",
    });
    expect(result.calendarType).toBe("ALWAYSOPEN");
  });
});
