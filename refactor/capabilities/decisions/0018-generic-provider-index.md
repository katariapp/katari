# Generic Provider Index

Status: Accepted

## Context

Provider bindings became the single type-owned declaration during Milestones 4.1 through 4.2, but operational dispatch
still passed those bindings through a transitional `EntryInteractionRegistry`. That registry repeated one mutable map,
one `registerX` method, and one capability-owned installer callback for every provider contract. A future capability
therefore still required edits in two shared mechanisms even though a content type declared it only once.

The old registry-shaped tests repeated the same problem. They asserted duplicate registration separately for nearly
every provider category and encoded obsolete support booleans and current-type matrices rather than exercising provider
behavior or generic composition rules.

## Proposed Decision

- `EntryInteractionProviderBinding` is the only bridge between graph evidence and operational provider identity.
- Composition flattens discovered bindings into one generic typed provider index.
- Existing interaction facades request a typed map for a capability definition; the index contains no
  capability-specific registration methods and no concrete content-type list.
- Provider contracts are grouped by interaction family. `EntryInteractionPlugin` owns only binding/contribution
  primitives, while `EntryInteractionComposition` owns only assembly and facade wiring; operational dispatch lives with
  its cohesive interaction family.
- Duplicate providers are rejected by the generic contribution/index invariants, not by one hand-written check per
  capability.
- Missing bindings remain valid and continue to produce each facade's neutral or unavailable behavior.
- Download-setting translation remains a transitional facade projection until the Downloads feature owns settings
  consequences in Phase 5. Its operational setting metadata lives on each specialized setting-capability definition,
  and the index discovers those bindings without a central setting list or type matrix.
- Registry-shaped and support-label tests are removed. Focused composition tests cover valid partial contributions,
  generic dispatch, duplicate claims, and provider/type ownership. Media-specific tests continue to own media behavior.

## Consequences

- Adding a provider capability no longer requires adding a registry method or installer callback.
- Content-type declarations, graph evidence, and operational dispatch cannot drift into separate registration paths.
- Adding a provider contract or changing one interaction facade does not expand a shared catch-all contract or dispatch
  file.
- The remaining capability-specific code is in operational facades and feature policies. Phase 5 must move those
  consequences to feature-owned integrations; the provider index does not replace feature evaluation.
- Test failures above the SPI boundary remain useful migration obligations when they identify Phase 5 consumers of the
  deleted report or missing feature contributors.

## Alternatives Rejected

- Retaining `EntryInteractionRegistry` as a compatibility facade
- Generating or maintaining one registration method per capability
- Reconstructing operational maps from a separate support report
- Keeping registry-shaped tests merely to make the pre-migration test suite compile
- Treating every absent provider as an invalid or incomplete content type
