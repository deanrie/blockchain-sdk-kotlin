package com.tangem.blockchain.common.memo

import com.google.common.truth.Truth.assertThat
import com.tangem.blockchain.extensions.Result
import kotlinx.coroutines.test.runTest
import org.junit.Test

class NumericMemoValidatorTest {

    @Test
    fun `GIVEN empty or decimal memo WHEN validateMemo THEN returns Valid`() = runTest {
        listOf("", "0", "42", "9223372036854775807").forEach { memo ->
            val result = NumericMemoValidator.validateMemo(memo)

            assertThat((result as Result.Success).data).isEqualTo(MemoState.Valid)
        }
    }

    @Test
    fun `GIVEN non-numeric or out-of-range memo WHEN validateMemo THEN returns Invalid`() = runTest {
        listOf("abc", "12a", "1.5", "+1", "-1", " 1", "9223372036854775808", "１２").forEach { memo ->
            val result = NumericMemoValidator.validateMemo(memo)

            assertThat((result as Result.Success).data).isEqualTo(MemoState.Invalid)
        }
    }

    @Test
    fun `GIVEN any address WHEN isMemoRequired THEN returns false`() = runTest {
        val result = NumericMemoValidator.isMemoRequired("anyAddress")

        assertThat((result as Result.Success).data).isFalse()
    }
}
