# Download Feature Parity Manifesto

## Context

Katari supports three content types: Manga, Anime, and Book. Each type needs different media-resolution, transfer, packaging, validation, and offline-consumption logic. Those differences are inherent to the media and should remain possible.

The download experience, however, is one product feature. A user should not need to learn different meanings for queueing, starting, pausing, retrying, cancelling, automatic downloading, cleanup, or offline availability based on the type of content being downloaded.

The current feature does not consistently meet that expectation. The three download paths have evolved separately, so sharing a screen or exposing a common command does not always produce shared behavior. The recent unification of download notifications demonstrates the intended direction: type-specific download logic can report structured facts while shared feature policy owns their common interpretation.

## The Problem

Download support currently has surface-level unification without complete behavioral unification.

The same user action can have different queue and execution consequences depending on content type. Shared preferences may be honored by some download paths and ignored by others. Lifecycle behavior such as surviving background execution, reacting to network constraints, or cleaning up consumed content is uneven. Profile identity, merged-entry ownership, renamed metadata, progress wording, and queue persistence do not receive the same guarantees everywhere.

This creates three forms of debt:

- **User-facing debt:** controls and settings do not reliably mean the same thing for every supported type.
- **Correctness debt:** fixes made in one download path do not automatically protect the others.
- **Expansion debt:** adding another content type risks creating another complete download feature instead of adding only the media-specific behavior that type requires.

Parity work is therefore not a cosmetic effort. It is a correction to the ownership model of the feature.

## Current Parity Gaps

The known gaps fall into broad behavioral categories rather than individual implementation defects.

### Queue semantics

Queueing, ordinary start, start-now priority, reordering, retrying, pausing, and cancellation are not governed by one consistent state model. In particular, an action affecting pending work must never restart or disrupt unrelated active work.

### Execution lifetime and constraints

Not every content type receives the same foreground, process-restoration, network-loss, and Wi-Fi-policy guarantees. Network constraints should describe a shared user policy, not an optional behavior implemented by only some media paths.

### Persistence and identity

Queued work is not uniformly scoped and restored by profile, content type, real owner entry, and child item. This matters especially for profile switching and merged entries, where the visible entry may not own the selected child or its source.

### Automatic download policy

Automatic downloading of newly discovered content does not consistently apply the same enabled state, category inclusion and exclusion, and consumed-item filtering. A shared setting must have shared semantics.

### Consumption-driven lifecycle

Removal after an item is marked consumed, removal after completing later items, category exclusions, bookmark protection, and download-ahead behavior are not available or interpreted consistently across the content types where they are meaningful.

### Storage lifecycle

Downloaded content does not have uniform guarantees around safe publication, temporary data, validation, reindexing, deletion, and continuity when a source or entry title changes. The physical formats may differ, but the observable promises must not.

### Presentation and settings

Queue phases, progress descriptions, errors, sorting, and settings mix shared concepts with content-specific assumptions. Shared presentation must consume semantic state and localize it consistently. Content-specific controls must be identified as capabilities rather than appearing to be universal preferences.

## What Parity Means

Parity means that every supported content type receives the same guarantees for every concept that belongs to the download feature itself.

Those guarantees include:

- The same action has the same queue-state meaning.
- Start now gives selected work meaningful priority.
- Pausing preserves queued work and resuming continues it.
- Cancelling pending work does not disturb unrelated active work.
- Queued work survives the lifecycle conditions the app claims to support.
- Network and Wi-Fi preferences apply consistently.
- Failures are retryable and represented consistently.
- Automatic-download preferences apply consistently.
- Consumption-driven cleanup and download-ahead policies behave consistently where applicable.
- Work is attributed to the correct profile, content type, owner entry, source, and child.
- Downloaded content remains discoverable after supported metadata changes.
- Reindexing, deletion, and offline availability have consistent outcomes.
- Queue, notification, and error presentation use shared localized semantics.
- Privacy-sensitive notification behavior is identical across content types.

Parity does not require identical transfer mechanics or storage formats. Manga may consist of image pages, Anime may consist of video streams and subtitles, and Book may consist of publication resources. Progress precision, resumability, validation details, and selectable media options may therefore differ.

Such differences must be explicit capabilities. They must not silently change the meaning of common controls or weaken common guarantees.

## Guiding Principles

### One feature, multiple media strategies

Katari has one download feature. Content types contribute only the behavior that is genuinely specific to resolving, transferring, packaging, validating, and opening their media.

### Shared policy has one owner

Queue semantics, execution policy, lifecycle policy, network constraints, retry behavior, automation, cleanup decisions, and user-facing state should not be independently reinterpreted by every content type.

### Structured facts cross the boundary

Type-specific logic should communicate facts such as phase changes, progress, completion, failure, warnings, and artifact state. Shared feature policy decides how those facts affect the queue and user experience.

### Identity follows the real content owner

The visible entry is not always the owner of a selected child. Download identity and storage operations must follow the actual profile, entry, source, and child that own the media. Presentation may still navigate through the visible merged entry.

### Capabilities are honest

A content-specific option is acceptable. A shared setting that only works for some content types is not. Unsupported behavior must be represented explicitly and must not be offered as though it were universal.

### Compatibility is preserved deliberately

Existing downloads and queued work are user data. Architectural improvement must account for established storage layouts and recovery paths rather than treating old media as disposable.

### Parity is protected by contracts

Every common guarantee should be verifiable against all supported content types. Type-specific tests remain necessary, but they cannot substitute for a shared behavioral contract.

## End Goal

The end state is a download feature in which shared behavior is defined once and media-specific logic is replaceable behind a narrow boundary.

From the user's perspective:

- The Downloads screen represents one coherent queue.
- All controls and preferences have predictable meanings.
- Backgrounding, process recreation, network changes, profile changes, and merged entries do not produce type-dependent surprises.
- Automatic downloads, cleanup, retry, and offline access behave according to the same declared policy.
- Type-specific choices appear only when the selected media supports or requires them.

From the project's perspective:

- A common behavioral fix protects every content type.
- A parity regression is caught by the same contract regardless of media.
- Adding a future content type requires implementing its media-specific download and offline-artifact capabilities, not recreating queue management, lifecycle policy, persistence, settings, notifications, and cleanup.
- Shared code does not need to understand image pages, video streams, subtitles, EPUB resources, or future media formats in order to manage downloads correctly.

## Success Criteria

The work is complete when:

- The same common download scenarios can be exercised for Manga, Anime, and Book with the same observable outcomes.
- Every shared download preference is either honored by all applicable types or clearly scoped as content-specific.
- Queue state and actions remain correct with mixed content types present simultaneously.
- Profile restoration and merged-entry downloads retain the real media owner.
- Network loss, pause, cancellation, retry, and process restoration cannot corrupt queue state or completed media.
- Consumed-content cleanup and download-ahead policies no longer depend on which reader or player happened to implement them.
- Renaming, reindexing, deleting, and opening downloaded content produce consistent results.
- User-facing progress, warnings, and errors are semantic, localized, and privacy-aware.
- A new content type can join the download feature without introducing another independent feature stack.

## Non-Goals

This manifesto does not prescribe a particular class structure, persistence technology, scheduling algorithm, migration sequence, concurrency model, or on-disk format.

It also does not require content types to expose concepts that their content model does not support. Bookmark-based behavior, stream selection, image packaging, publication validation, and similar features remain capability-dependent.

The requirement is that genuine media differences remain contained, while everything users reasonably understand as download-feature behavior reaches parity.
