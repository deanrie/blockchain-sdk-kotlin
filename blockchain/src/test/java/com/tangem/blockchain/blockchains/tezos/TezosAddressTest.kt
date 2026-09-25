package com.tangem.blockchain.blockchains.tezos

import com.google.common.truth.Truth
import com.tangem.common.card.EllipticCurve
import com.tangem.common.extensions.hexToBytes
import com.tangem.common.extensions.calculateSha256
import org.bitcoinj.core.Base58
import org.junit.Test

class TezosAddressTest {

    private val addressService = TezosAddressService()

    @Test
    fun makeAddressFromCorrectPublicKey() {
        val walletPublicKey = "98E0E504F3A5FDE704400302ABB0A2EFB0DF0F95C166C91D7F207DEDCE10CBA3"
            .hexToBytes()
        val expected = "tz1hhRdWDAvGsgEioZ9GAp4bUVQkd9ng2MMR"

        Truth.assertThat(addressService.makeAddress(walletPublicKey, EllipticCurve.Ed25519)).isEqualTo(expected)
    }

    @Test
    fun validateCorrectAddress() {
        val address = "tz1hhRdWDAvGsgEioZ9GAp4bUVQkd9ng2MMR"

        Truth.assertThat(addressService.validate(address)).isTrue()
    }

    @Test
    fun rejectChecksumValidAddressWithForeignPrefix() {
        // A 23-byte payload with the edpk (public key) prefix instead of tz1/tz2/tz3/KT1, correctly checksummed.
        val prefixedHash = "0D0F25".hexToBytes() + ByteArray(20) { it.toByte() }
        val checksum = prefixedHash.calculateSha256().calculateSha256().copyOfRange(0, 4)
        val address = Base58.encode(prefixedHash + checksum)

        Truth.assertThat(addressService.validate(address)).isFalse()
    }
}
