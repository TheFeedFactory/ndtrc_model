import { readdir, readFile } from "node:fs/promises";
import { join } from "node:path";

export async function extractGroovyFields(
  dirPath: string,
): Promise<Record<string, Set<string>>> {
  const entries = await readdir(dirPath);
  const groovyFiles = entries.filter((f: string) => f.endsWith(".groovy")).sort();

  const result: Record<string, Set<string>> = {};

  for (const file of groovyFiles) {
    const content = await readFile(join(dirPath, file), "utf-8");
    const classFields = parseGroovyFile(content);
    for (const [className, fields] of Object.entries(classFields)) {
      result[className] = fields;
    }
  }

  return result;
}

export function parseGroovyFile(content: string): Record<string, Set<string>> {
  const lines = content.split("\n");
  const result: Record<string, Set<string>> = {};

  const classStack: string[] = [];
  const classBraceDepths: number[] = [];
  let braceDepth = 0;
  let inEnum = false;
  let enumBraceDepth = 0;

  for (const rawLine of lines) {
    const line = stripLineComment(rawLine).trim();
    if (!line) continue;

    const preBraceDepth = braceDepth;
    let isDeclaration = false;

    const classMatch = line.match(/\b(?:static\s+)?class\s+(\w+)/);
    if (classMatch) {
      isDeclaration = true;
      classStack.push(classMatch[1]!);
      classBraceDepths.push(preBraceDepth);
      const fullName = classStack.join(".");
      if (!result[fullName]) result[fullName] = new Set();
    }

    const enumMatch = line.match(/\b(?:static\s+)?enum\s+(\w+)/);
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

function stripLineComment(line: string): string {
  let inString = false;
  let stringChar = "";
  for (let i = 0; i < line.length; i++) {
    const ch = line[i]!;
    if (inString) {
      if (ch === stringChar && line[i - 1] !== "\\") inString = false;
      continue;
    }
    if (ch === '"' || ch === "'") {
      inString = true;
      stringChar = ch;
      continue;
    }
    if (ch === "/" && line[i + 1] === "/") {
      return line.substring(0, i);
    }
  }
  return line;
}

function extractFieldNames(line: string): string[] {
  let s = line;

  s = s.replace(/@\w+(?:\([^)]*\))?\s*/g, "").trim();
  if (!s) return [];

  s = s.replace(/;/g, "").trim();

  const modifierRe = /^(?:public|private|protected|static|final|volatile|transient)\s+/;
  while (modifierRe.test(s)) {
    s = s.replace(modifierRe, "").trim();
  }

  if (
    s.startsWith("class ") ||
    s.startsWith("enum ") ||
    s.startsWith("interface ") ||
    s.startsWith("void ") ||
    s.startsWith("def ") ||
    s.startsWith("return ") ||
    s.startsWith("if ") ||
    s.startsWith("if(") ||
    s.startsWith("for ") ||
    s.startsWith("for(") ||
    s.startsWith("while ") ||
    s.startsWith("while(") ||
    s.startsWith("this.") ||
    s.startsWith("throw ") ||
    s.startsWith("package ") ||
    s.startsWith("import ") ||
    s === "{" ||
    s === "}" ||
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
    /^(\w[\w.<>,?\s]*?)\s+(\w+(?:\s*,\s*\w+)*)$/,
  );
  if (!match) return [];

  return match[2]!.split(/\s*,\s*/).filter((n) => n.length > 0);
}
