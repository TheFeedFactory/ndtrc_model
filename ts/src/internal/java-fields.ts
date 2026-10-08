import { readdir, readFile } from "node:fs/promises";
import { join } from "node:path";

/**
 * Extracts the instance field names of every class in the `.java` files of a
 * directory (not recursive), keyed by the dotted nested class name
 * (`Calendar.PatternDate.Open`). Enums are not included; static fields
 * (constants, loggers, patterns) are skipped because they are not part of the
 * wire format.
 *
 * This is a deliberately small line-based parser for the plain-bean style the
 * model is written in (one class per file, fields declared at the top level of
 * a class body), not a Java parser.
 */
export async function extractJavaFields(
  dirPath: string,
): Promise<Record<string, Set<string>>> {
  const entries = await readdir(dirPath);
  const javaFiles = entries.filter((f: string) => f.endsWith(".java")).sort();

  const result: Record<string, Set<string>> = {};

  for (const file of javaFiles) {
    const content = await readFile(join(dirPath, file), "utf-8");
    const classFields = parseJavaFile(content);
    for (const [className, fields] of Object.entries(classFields)) {
      result[className] = fields;
    }
  }

  return result;
}

export function parseJavaFile(content: string): Record<string, Set<string>> {
  const lines = stripComments(content).split("\n");
  const result: Record<string, Set<string>> = {};

  const classStack: string[] = [];
  const classBraceDepths: number[] = [];
  let braceDepth = 0;
  let inEnum = false;
  let enumBraceDepth = 0;

  for (const rawLine of lines) {
    const line = rawLine.trim();
    if (!line) continue;

    const preBraceDepth = braceDepth;
    let isDeclaration = false;

    const classMatch = line.match(/\bclass\s+(\w+)/);
    if (classMatch) {
      isDeclaration = true;
      classStack.push(classMatch[1]!);
      classBraceDepths.push(preBraceDepth);
      const fullName = classStack.join(".");
      if (!result[fullName]) result[fullName] = new Set();
    }

    const enumMatch = line.match(/\benum\s+(\w+)/);
    if (enumMatch && !classMatch) {
      isDeclaration = true;
      const openCount = (line.match(/\{/g) ?? []).length;
      const closeCount = (line.match(/\}/g) ?? []).length;
      if (!(openCount > 0 && openCount === closeCount)) {
        inEnum = true;
        enumBraceDepth = preBraceDepth;
      }
    }

    if (
      !isDeclaration &&
      classStack.length > 0 &&
      !inEnum &&
      preBraceDepth === classBraceDepths[classBraceDepths.length - 1]! + 1
    ) {
      const fields = extractFieldNames(line);
      if (fields.length > 0) {
        const fullName = classStack.join(".");
        if (!result[fullName]) result[fullName] = new Set();
        for (const f of fields) result[fullName]!.add(f);
      }
    }

    for (const ch of line) {
      if (ch === "{") braceDepth++;
      if (ch === "}") braceDepth--;
    }

    if (inEnum && braceDepth <= enumBraceDepth) {
      inEnum = false;
    }
    while (
      classBraceDepths.length > 0 &&
      braceDepth <= classBraceDepths[classBraceDepths.length - 1]!
    ) {
      classStack.pop();
      classBraceDepths.pop();
    }
  }

  return result;
}

/**
 * Removes `//` and `/* *\/` comments (Javadoc included) while keeping string
 * and char literals and every newline, so line structure survives. Javadoc
 * regularly says things like "see {@link Open} class for details", which
 * would otherwise read as a class declaration.
 */
function stripComments(source: string): string {
  let out = "";
  let i = 0;
  while (i < source.length) {
    const ch = source[i]!;
    const next = source[i + 1];
    if (ch === "/" && next === "/") {
      while (i < source.length && source[i] !== "\n") i++;
      continue;
    }
    if (ch === "/" && next === "*") {
      i += 2;
      while (i < source.length && !(source[i] === "*" && source[i + 1] === "/")) {
        if (source[i] === "\n") out += "\n";
        i++;
      }
      i += 2;
      continue;
    }
    if (ch === '"' || ch === "'") {
      out += ch;
      i++;
      while (i < source.length && source[i] !== ch && source[i] !== "\n") {
        if (source[i] === "\\") {
          out += source[i]!;
          i++;
        }
        if (i < source.length) {
          out += source[i]!;
          i++;
        }
      }
      if (i < source.length) {
        out += source[i]!;
        i++;
      }
      continue;
    }
    out += ch;
    i++;
  }
  return out;
}

function extractFieldNames(line: string): string[] {
  let s = line;

  s = s.replace(/@\w+(?:\([^)]*\))?\s*/g, "").trim();
  if (!s) return [];

  if (!s.endsWith(";")) return [];
  s = s.replace(/;/g, "").trim();

  const modifierRe = /^(?:public|private|protected|static|final|volatile|transient)\s+/;
  let isStatic = false;
  while (modifierRe.test(s)) {
    if (s.startsWith("static")) isStatic = true;
    s = s.replace(modifierRe, "").trim();
  }
  if (isStatic) return [];

  if (
    s.startsWith("return ") ||
    s.startsWith("throw ") ||
    s.startsWith("package ") ||
    s.startsWith("import ") ||
    s.startsWith("this.") ||
    s === ""
  ) {
    return [];
  }

  const equalsIndex = s.indexOf("=");
  const parenIndex = s.indexOf("(");
  if (parenIndex !== -1 && (equalsIndex === -1 || parenIndex < equalsIndex)) {
    return [];
  }

  let namesPart = s;
  if (equalsIndex !== -1) {
    namesPart = s.substring(0, equalsIndex).trim();
  }

  const match = namesPart.match(
    /^(\w[\w.<>,?\[\]\s]*?)\s+(\w+(?:\s*,\s*\w+)*)$/,
  );
  if (!match) return [];

  return match[2]!.split(/\s*,\s*/).filter((n) => n.length > 0);
}
