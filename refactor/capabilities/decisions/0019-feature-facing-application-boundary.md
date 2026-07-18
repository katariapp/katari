# Feature-Facing Application Boundary

Status: Accepted

## Context

F01 replaced application use of `EntryOpenInteraction` with `EntryOpenFeature`, but the raw interaction interface still
lived in the API exported to the application. Removing its dependency-injection binding made accidental use less
convenient without making it impossible. A future consumer could import the raw facade, restore a binding, or query the
production graph directly and reconstruct feature applicability.

Naming guidance is not an architectural boundary. The provider and feature layers need a compile-time dependency cut
before the remaining application consumers migrate.

## Proposed Decision

- The application-facing Entry-interactions API contains feature-owned contracts, shared data models, and host ports
  that application composition implements for type-owned runtimes. Host ports do not expose provider dispatch.
- Provider contracts, provider-backed operational facades, the `EntryInteractions` aggregate, composition, graph
  evaluation, and artifact selection remain behind the provider SPI/root boundary.
- The root Entry-interactions module exports its application API but depends on SPI only as an implementation detail.
- The application depends only on the root Entry-interactions module. It cannot depend directly on API, SPI, concrete
  type modules, or Feature Graph.
- Root composition injects feature-owned application contracts. It does not inject raw operational facades or graph
  evaluation objects for application lookup.
- Feature coordinators obtain matched operational dispatch from internal composition and enforce the applicability
  selected by their owned graph consequences.
- A fundamental provider may participate in multiple feature coordinators. The access boundary does not require a
  one-to-one `Interaction`-to-`Feature` replacement or one large feature facade.
- Boundary validation discovers public SPI declarations generically and rejects their use outside root composition,
  owned type modules, and tests. It does not enumerate current interaction names.
- Boundary validation also rejects any raw `Entry*Interaction`/`EntryInteractions` contract declared in the
  application-facing API, so moving a facade back across the module cut is not a compilation workaround.
- Existing application imports of raw facades become intentional migration failures. They are restored only through
  their owning `F02`–`F27` feature contracts.

## Consequences

- Choosing a raw interaction instead of a feature is no longer an available application-code shortcut.
- Adding another raw provider or dispatch contract to SPI automatically places it behind the same boundary.
- Exporting SPI or Feature Graph, adding a direct application dependency, or referencing an SPI type from application
  source fails boundary validation.
- Lower API, SPI, and type-module compilation remains independently verifiable while the application is intentionally
  broken by unported feature consumers.
- A developer can still deliberately redesign module dependencies, as with any architectural boundary, but doing so is
  an explicit build-file and enforcement change rather than an accidental API choice.

## Alternatives Rejected

- Relying on `Feature` versus `Interaction` naming conventions
- Keeping raw facade DI bindings until each application consumer migrates
- Exporting SPI temporarily to preserve application compilation
- Giving application code direct graph evaluation and asking each consumer to interpret it correctly
- Creating one giant `EntryFeature` facade or requiring one feature for every provider contract
- Maintaining an allowlist of raw interaction names forbidden to the application
