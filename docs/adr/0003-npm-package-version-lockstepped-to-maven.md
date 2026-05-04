# `@eventconnectors/ndtrc-model` is version-lockstepped to the Maven artifact

`pom.xml` and `ts/package.json` always carry the same version string. They
bump together in the same commit; a single git tag (`vX.Y.Z`) triggers
both Maven Central and npm publishing. The first npm release is
**`1.2.x`** — matching the current Maven version — not `0.x`, because the
underlying model contract is mature.

A consumer asking "is the TS schema for `1.2.5` compatible with Java
client `1.2.5`?" must get an unambiguous yes. Splitting the version space
(independent semver per artifact, or major.minor lockstep with
independent patch) invites that question and forces a compatibility table
nobody will keep current.

## Considered options

- **Independent semver per artifact** — TS evolves on its own cadence,
  but breaks the "one model, one version" mental model that justified
  having a single repo in the first place.
- **Major.minor lockstep, independent patch** — TS-only fixes don't need
  a Maven release, but introduces ambiguity ("does TS 1.2.4 imply Java
  1.2.x compatibility?") that the simpler full-lockstep rule avoids.

## Consequences

- TS-only fixes (e.g. correcting a `.optional()` typo that doesn't affect
  the wire format) wait for the next legitimate model bump. Empirically
  rare; if it becomes painful we revisit.
- Anyone reading git log can match a Maven release to an npm release by
  version alone, no compatibility matrix needed.
