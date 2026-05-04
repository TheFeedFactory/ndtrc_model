import { describe, it, expect } from "vitest";
import { join } from "node:path";
import {
  extractGroovyFields,
  parseGroovyFile,
} from "../src/internal/groovy-fields.js";

const FIXTURES_DIR = join(import.meta.dirname, "fixtures", "groovy");

describe("parseGroovyFile", () => {
  it("extracts simple fields with and without @JsonProperty", () => {
    const result = parseGroovyFile(`
class Simple {
    @JsonProperty String name
    Integer count
    Boolean active
}
`);
    expect(result["Simple"]).toEqual(new Set(["name", "count", "active"]));
  });

  it("extracts fields from nested static classes", () => {
    const result = parseGroovyFile(`
class Outer {
    String id

    static class Inner {
        String value

        static class Deep {
            String key
        }
    }
}
`);
    expect(result["Outer"]).toEqual(new Set(["id"]));
    expect(result["Outer.Inner"]).toEqual(new Set(["value"]));
    expect(result["Outer.Inner.Deep"]).toEqual(new Set(["key"]));
  });

  it("skips enum values but extracts enum-typed fields", () => {
    const result = parseGroovyFile(`
class WithEnum {
    String label
    Status status

    enum Status { active, inactive, archived }
}
`);
    expect(result["WithEnum"]).toEqual(new Set(["label", "status"]));
    expect(result["WithEnum.Status"]).toBeUndefined();
  });

  it("handles multi-line enum blocks", () => {
    const result = parseGroovyFile(`
class WithBlockEnum {
    String name

    static enum Type {
        simple,
        complex,
        composite
    }

    String other
}
`);
    expect(result["WithBlockEnum"]).toEqual(new Set(["name", "other"]));
  });

  it("extracts multi-field declarations (e.g. String lang, text)", () => {
    const result = parseGroovyFile(`
class Multi {
    Double lat, lng
    String label
    Address start, end
}
`);
    expect(result["Multi"]).toEqual(
      new Set(["lat", "lng", "label", "start", "end"]),
    );
  });

  it("skips methods and @JsonIgnore methods", () => {
    const result = parseGroovyFile(`
class WithMethods {
    String name

    @JsonIgnore
    boolean isEmpty() {
        return name == null
    }

    void normalize() {
        if (name) {
            name = name.trim()
        }
    }

    static String helper(String input) {
        return input?.toLowerCase()
    }
}
`);
    expect(result["WithMethods"]).toEqual(new Set(["name"]));
  });

  it("handles deprecated fields", () => {
    const result = parseGroovyFile(`
class WithDeprecated {
    String current
    @Deprecated @JsonProperty List<String> old = []
    @Deprecated String legacy
}
`);
    expect(result["WithDeprecated"]).toEqual(
      new Set(["current", "old", "legacy"]),
    );
  });

  it("handles fields with default values including constructor calls", () => {
    const result = parseGroovyFile(`
class WithDefaults {
    String country = 'NL'
    Boolean active = false
    List<String> items = []
    Translations translations = new Translations()
}
`);
    expect(result["WithDefaults"]).toEqual(
      new Set(["country", "active", "items", "translations"]),
    );
  });

  it("handles @JsonInclude annotations on fields", () => {
    const result = parseGroovyFile(`
class WithJsonInclude {
    @JsonInclude(JsonInclude.Include.NON_EMPTY) RouteInfo routeInfo
    @JsonInclude(JsonInclude.Include.NON_EMPTY) List<Promotion> promotions
    String name
}
`);
    expect(result["WithJsonInclude"]).toEqual(
      new Set(["routeInfo", "promotions", "name"]),
    );
  });

  it("handles fields with semicolons", () => {
    const result = parseGroovyFile(`
class WithSemicolons {
    String duration;
    Integer count
}
`);
    expect(result["WithSemicolons"]).toEqual(new Set(["duration", "count"]));
  });

  it("handles fields with trailing comments", () => {
    const result = parseGroovyFile(`
class WithComments {
    String validatedby // deprecated
    Boolean offline // deprecated
    String name
}
`);
    expect(result["WithComments"]).toEqual(
      new Set(["validatedby", "offline", "name"]),
    );
  });
});

describe("extractGroovyFields (from fixture files)", () => {
  it("extracts fields from all fixture groovy files", async () => {
    const result = await extractGroovyFields(FIXTURES_DIR);

    expect(result["SimpleEntity"]).toEqual(
      new Set(["name", "count", "active"]),
    );

    expect(result["NestedEntity"]).toEqual(new Set(["id", "items"]));
    expect(result["NestedEntity.Inner"]).toEqual(
      new Set(["value", "nested"]),
    );
    expect(result["NestedEntity.Inner.Deep"]).toEqual(new Set(["key"]));

    expect(result["EntityWithEnums"]).toEqual(
      new Set(["label", "status", "detailType"]),
    );
    expect(result["EntityWithEnums.Detail"]).toEqual(
      new Set(["lang", "text"]),
    );

    expect(result["EntityWithMethods"]).toEqual(
      new Set(["name", "country", "tags", "translations"]),
    );

    expect(result["MultiFieldEntity"]).toEqual(
      new Set(["lat", "lng", "label", "start", "end", "eventRelativeDuration"]),
    );
  });
});

describe("extractGroovyFields (from real Groovy sources)", () => {
  const GROOVY_DIR = join(
    import.meta.dirname,
    "..",
    "..",
    "src",
    "main",
    "groovy",
    "nl",
    "ithelden",
    "model",
    "ndtrc",
  );

  it("extracts GISCoordinate fields correctly", async () => {
    const result = await extractGroovyFields(GROOVY_DIR);
    expect(result["GISCoordinate"]).toEqual(
      new Set(["xcoordinate", "ycoordinate", "label"]),
    );
  });

  it("extracts Address fields correctly", async () => {
    const result = await extractGroovyFields(GROOVY_DIR);
    expect(result["Address"]).toEqual(
      new Set([
        "main",
        "reservation",
        "title",
        "city",
        "citytrcid",
        "country",
        "housenr",
        "street",
        "streettrcid",
        "zipcode",
        "province",
        "neighbourhood",
        "district",
        "gisCoordinates",
      ]),
    );
  });

  it("extracts Performer fields correctly", async () => {
    const result = await extractGroovyFields(GROOVY_DIR);
    expect(result["Performer"]).toEqual(
      new Set(["roleid", "label", "rolelabel"]),
    );
  });

  it("extracts TRCItem fields including forceoverwrite", async () => {
    const result = await extractGroovyFields(GROOVY_DIR);
    expect(result["TRCItem"]).toBeDefined();
    const fields = result["TRCItem"]!;
    expect(fields.has("trcid")).toBe(true);
    expect(fields.has("entitytype")).toBe(true);
    expect(fields.has("wfstatus")).toBe(true);
    expect(fields.has("forceoverwrite")).toBe(true);
    expect(fields.has("calendar")).toBe(true);
    expect(fields.has("contactinfo")).toBe(true);
    expect(fields.has("performers")).toBe(true);
    expect(fields.has("seoMetadata")).toBe(true);
  });

  it("extracts Calendar nested classes", async () => {
    const result = await extractGroovyFields(GROOVY_DIR);
    expect(result["Calendar"]).toBeDefined();
    expect(result["Calendar.SingleDate"]).toBeDefined();
    expect(result["Calendar.PatternDate"]).toBeDefined();
    expect(result["Calendar.PatternDate.Open"]).toBeDefined();
    expect(result["Calendar.When"]).toBeDefined();
    expect(result["Calendar.StatusTranslation"]).toBeDefined();
    expect(result["Calendar.ExtraInformation"]).toBeDefined();
    expect(result["Calendar.ExceptionDate"]).toBeDefined();
    expect(result["Calendar.Comment"]).toBeDefined();
    expect(result["Calendar.CommentTranslation"]).toBeDefined();
  });

  it("extracts Contactinfo nested classes", async () => {
    const result = await extractGroovyFields(GROOVY_DIR);
    expect(result["Contactinfo"]).toBeDefined();
    expect(result["Contactinfo.Mail"]).toBeDefined();
    expect(result["Contactinfo.Phone"]).toBeDefined();
    expect(result["Contactinfo.Fax"]).toBeDefined();
    expect(result["Contactinfo.Url"]).toBeDefined();
    expect(result["Contactinfo.DescriptionTranslation"]).toBeDefined();
  });

  it("does not include enum classes in the result", async () => {
    const result = await extractGroovyFields(GROOVY_DIR);
    expect(result["TRCItem.WFStatus"]).toBeUndefined();
    expect(result["TRCItem.EntityType"]).toBeUndefined();
    expect(result["Calendar.CalendarType"]).toBeUndefined();
    expect(result["File.FileType"]).toBeUndefined();
    expect(result["File.MediaType"]).toBeUndefined();
  });
});
