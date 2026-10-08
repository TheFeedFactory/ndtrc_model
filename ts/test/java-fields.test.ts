import { describe, it, expect } from "vitest";
import { join } from "node:path";
import {
  extractJavaFields,
  parseJavaFile,
} from "../src/internal/java-fields.js";

const FIXTURES_DIR = join(import.meta.dirname, "fixtures", "java");

describe("parseJavaFile", () => {
  it("extracts simple fields with and without @JsonProperty", () => {
    const result = parseJavaFile(`
public class Simple {
    @JsonProperty private String name;
    private Integer count;
    Boolean active;
}
`);
    expect(result["Simple"]).toEqual(new Set(["name", "count", "active"]));
  });

  it("extracts fields from nested static classes", () => {
    const result = parseJavaFile(`
public class Outer {
    private String id;

    public static class Inner {
        private String value;

        public static class Deep {
            private String key;
        }
    }
}
`);
    expect(result["Outer"]).toEqual(new Set(["id"]));
    expect(result["Outer.Inner"]).toEqual(new Set(["value"]));
    expect(result["Outer.Inner.Deep"]).toEqual(new Set(["key"]));
  });

  it("skips enum values but extracts enum-typed fields", () => {
    const result = parseJavaFile(`
public class WithEnum {
    private String label;
    private Status status;

    public enum Status { active, inactive, archived }
}
`);
    expect(result["WithEnum"]).toEqual(new Set(["label", "status"]));
    expect(result["WithEnum.Status"]).toBeUndefined();
  });

  it("handles multi-line enum blocks with members", () => {
    const result = parseJavaFile(`
public class WithBlockEnum {
    private String name;

    public static enum Type {
        simple,
        complex,
        composite;

        public static final Type MIN_VALUE = simple;

        public Type next() {
            return values()[0];
        }
    }

    private String other;
}
`);
    expect(result["WithBlockEnum"]).toEqual(new Set(["name", "other"]));
  });

  it("extracts multi-field declarations (e.g. String lang, text)", () => {
    const result = parseJavaFile(`
public class Multi {
    private Double lat, lng;
    private String label;
    private Address start, end;
}
`);
    expect(result["Multi"]).toEqual(
      new Set(["lat", "lng", "label", "start", "end"]),
    );
  });

  it("skips methods, accessors and @JsonIgnore methods", () => {
    const result = parseJavaFile(`
public class WithMethods {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    @JsonIgnore
    public boolean isEmpty() {
        return name == null;
    }

    public static String helper(String input) {
        return input == null ? null : input.toLowerCase();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(WithMethods.class, this)
                .add("name", name)
                .build();
    }
}
`);
    expect(result["WithMethods"]).toEqual(new Set(["name"]));
  });

  it("skips static fields such as constants and patterns", () => {
    const result = parseJavaFile(`
public class WithStatics {
    private static final Pattern WORD = Pattern.compile("(\\w)(\\w*)");
    public static final String NAME = "x";
    static int counter;
    private String kept;
}
`);
    expect(result["WithStatics"]).toEqual(new Set(["kept"]));
  });

  it("ignores Javadoc and comments, including ones that mention a class", () => {
    const result = parseJavaFile(`
/**
 * Represents something; see {@link Open} class for details.
 */
public class WithDocs {
    /**
     * The value. Example: {@code { "a": 1 }}
     * private String notAField;
     */
    private String value; // trailing; private String alsoNotAField;
    /* private String blockCommented; */
}
`);
    expect(result["WithDocs"]).toEqual(new Set(["value"]));
    expect(result["for"]).toBeUndefined();
  });

  it("handles deprecated fields", () => {
    const result = parseJavaFile(`
public class WithDeprecated {
    private String current;
    @Deprecated @JsonProperty private List<String> old = new ArrayList<>();
    @Deprecated private String legacy;
}
`);
    expect(result["WithDeprecated"]).toEqual(
      new Set(["current", "old", "legacy"]),
    );
  });

  it("handles fields with default values including constructor calls", () => {
    const result = parseJavaFile(`
public class WithDefaults {
    private String country = "NL";
    private Boolean active = false;
    private List<String> items = new ArrayList<>(List.of("nl"));
    private Map<String, List<Integer>> nested = new LinkedHashMap<>();
    private Translations translations = new Translations();
}
`);
    expect(result["WithDefaults"]).toEqual(
      new Set(["country", "active", "items", "nested", "translations"]),
    );
  });

  it("handles @JsonInclude annotations on fields", () => {
    const result = parseJavaFile(`
public class WithJsonInclude {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private RouteInfo routeInfo;
    @JsonInclude(JsonInclude.Include.NON_EMPTY) private List<Promotion> promotions;
    private String name;
}
`);
    expect(result["WithJsonInclude"]).toEqual(
      new Set(["routeInfo", "promotions", "name"]),
    );
  });
});

describe("extractJavaFields (from fixture files)", () => {
  it("extracts fields from all fixture java files", async () => {
    const result = await extractJavaFields(FIXTURES_DIR);

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

describe("extractJavaFields (from the real Java sources)", () => {
  const JAVA_DIR = join(
    import.meta.dirname,
    "..",
    "..",
    "src",
    "main",
    "java",
    "nl",
    "ithelden",
    "model",
    "ndtrc",
  );

  it("extracts GISCoordinate fields correctly", async () => {
    const result = await extractJavaFields(JAVA_DIR);
    expect(result["GISCoordinate"]).toEqual(
      new Set(["xcoordinate", "ycoordinate", "label"]),
    );
  });

  it("extracts Address fields correctly", async () => {
    const result = await extractJavaFields(JAVA_DIR);
    expect(result["Address"]).toEqual(
      new Set([
        "main",
        "reservation",
        "title",
        "city",
        "citytrcid",
        "country",
        "continent",
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
    const result = await extractJavaFields(JAVA_DIR);
    expect(result["Performer"]).toEqual(
      new Set(["roleid", "label", "rolelabel"]),
    );
  });

  it("extracts TRCItem fields including forceoverwrite", async () => {
    const result = await extractJavaFields(JAVA_DIR);
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
    const result = await extractJavaFields(JAVA_DIR);
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
    const result = await extractJavaFields(JAVA_DIR);
    expect(result["Contactinfo"]).toBeDefined();
    expect(result["Contactinfo.Mail"]).toBeDefined();
    expect(result["Contactinfo.Phone"]).toBeDefined();
    expect(result["Contactinfo.Fax"]).toBeDefined();
    expect(result["Contactinfo.Url"]).toBeDefined();
    expect(result["Contactinfo.DescriptionTranslation"]).toBeDefined();
  });

  it("does not include enum classes in the result", async () => {
    const result = await extractJavaFields(JAVA_DIR);
    expect(result["TRCItem.WFStatus"]).toBeUndefined();
    expect(result["TRCItem.EntityType"]).toBeUndefined();
    expect(result["Calendar.CalendarType"]).toBeUndefined();
    expect(result["File.FileType"]).toBeUndefined();
    expect(result["File.MediaType"]).toBeUndefined();
  });
});
