# Capability Evidence and Authority

Status: Accepted

## Context

Current support is inferred from processor registration, constant `supports…` methods, missing providers, explicit declarations, presentation fields, and direct type checks. A central matrix that copies those answers would reduce lookup cost without reducing the number of truths that must remain synchronized.

Phase 1 needs deterministic type-level evidence without flattening contextual support or claiming that every behavior is a product capability.

## Decision

Capability authority follows the kind of evidence:

- Registration of an operational provider is the authoritative evidence for a provider-backed fundamental capability. The same support fact must not also require a boolean declaration.
- A stable type-wide fact may be declared explicitly only when no operational provider can prove it. Such intrinsic evidence must be colocated with type composition, uniquely owned, and validated against contradictory provider evidence.
- Universal Entry behavior is shared feature policy, not a capability every type must opt into. A type-specific provider is justified only by a genuine specialization.
- Contextual support is evaluated from its runtime inputs and is not copied into the deterministic type-level report as unconditional support.
- Derived behavior is computed by the consuming feature and is never stored as another fundamental capability declaration.
- Presentation metadata owns wording, imagery, and layout choices only. It cannot authorize or suppress behavior.
- Storage, backup wire format, reader/player mechanics, media conversion, and compatibility branches remain outside capability authority unless they independently gate a product feature.

The deterministic report is assembled from authoritative evidence. It is a projection for inspection and validation, not another manually maintained matrix.

## Consequences

- Registering a provider automatically contributes its provider-backed capability evidence.
- Constant false/no-op providers cannot masquerade as supported implementations.
- Explicit intrinsic declarations remain possible but are exceptional and validated.
- Separate provider systems, such as library progress calculators, must either contribute evidence through composition or be surfaced as missing obligations; they cannot remain an invisible second type catalog.
- Existing compatibility APIs may temporarily project the new evidence during migration, but they do not remain independent authorities.

## Alternatives Rejected

- A central per-type boolean matrix: it duplicates operational truth and can drift.
- Treating every processor category as supported merely because a provider is registered: some current providers are contextual evaluators or all-no-op implementations.
- Treating every direct `EntryType` branch as a capability: this would absorb legitimate media and compatibility boundaries.
- Making presentation metadata the convenient central catalog: behavioral availability would remain coupled to UI maintenance.

## Affected Capabilities and Phases

- All provider-backed and type-wide capabilities
- Phase 1 foundation and deterministic reporting
- Phase 3 core migration
- Phase 7 documentation projection
- Phase 8 compatibility cleanup and enforcement
