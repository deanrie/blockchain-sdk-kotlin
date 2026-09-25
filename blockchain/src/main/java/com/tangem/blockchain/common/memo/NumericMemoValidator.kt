package com.tangem.blockchain.common.memo

import com.tangem.blockchain.extensions.Result

/**
 * Validator for chains whose memo is an unsigned 64-bit integer carried as a `Long` in the transaction extras
 * (Casper `id`, Internet Computer `memo`). Anything that is not a non-negative decimal integer fitting into a `Long`
 * is rejected up front; without this the app silently dropped such a memo from the signed transfer.
 */
internal object NumericMemoValidator : MemoValidator {

    override suspend fun isMemoRequired(destinationAddress: String): Result<Boolean> = Result.Success(false)

    override suspend fun validateMemo(memo: String): Result<MemoState> {
        if (memo.isEmpty()) return Result.Success(MemoState.Valid)
        val isValid = memo.all { it in '0'..'9' } && memo.toLongOrNull()?.let { it >= 0 } == true
        return Result.Success(if (isValid) MemoState.Valid else MemoState.Invalid)
    }
}
