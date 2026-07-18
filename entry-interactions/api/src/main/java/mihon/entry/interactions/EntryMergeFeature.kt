package mihon.entry.interactions

/**
 * Application boundary for preparing and executing shared Merge workflows.
 *
 * Callers provide user intent. The Merge feature resolves authoritative membership, validates the intent, and owns
 * every resulting consequence. Raw membership reads and persistence operations are deliberately absent from this
 * contract.
 */
interface EntryMergeFeature {
    suspend fun prepare(intent: EntryMergePrepareIntent): EntryMergePreparationResult

    suspend fun execute(intent: EntryMergeWorkflowIntent): EntryMergeExecutionResult
}
