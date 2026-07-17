# Phase 7 — Documentation and Capability Reporting

## Objective

Make capability documentation and developer reporting reflect deterministic executable truth rather than independently maintained support decisions.

## Scope

- [ ] Define user-facing metadata for documented capabilities.
- [ ] Represent supported, unavailable, and source-dependent documentation states.
- [ ] Generate or verify `docs/features/content-type-reference.md`.
- [ ] Produce a developer-readable capability and integration report.
- [ ] Show fundamental evidence, derived consequences, contextual conditions, and unmet specialized obligations.
- [ ] Integrate verification with normal repository validation.
- [ ] Update contributor documentation for maintaining capability metadata.

## Constraints

- Documentation validation must be deterministic.
- It must not start the Android application.
- It must not resolve live sources or trackers.
- Public documentation must remain user-facing.
- Internal provider and test-fixture details belong only in developer reports.

## Exit Gate

- Validation fails when the content-type reference disagrees with executable metadata.
- Source-dependent claims remain conditional.
- The report answers which types and integrations a change affects.
- Documentation no longer acts as an independent behavioral authority.

## Validation

- Capability metadata/report tests
- Documentation-reference verification
- VitePress production build
- Redirect generation when navigation changes
- `git diff --check`

## Manifesto Review

Confirm that documentation projects product truth and does not become another declaration developers must remember to update.
