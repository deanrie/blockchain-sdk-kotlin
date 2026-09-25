package com.tangem.blockchain.blockchains.xdc

import com.google.common.truth.Truth
import org.junit.Test

class XDCAddressTest {

    private val addressService = XDCAddressService()

    @Test
    fun validateAddressWhoseHexBodyStartsWithDc() {
        // "0xdc…" contains the substring "xdc" at index 1; a replace-all would turn it into "00x…".
        val ethForm = "0xdc5a4e7b2c1f3a9d8e6b0c2f1a3d5e7b9c1d2e3f"
        val xdcForm = "xdcdc5a4e7b2c1f3a9d8e6b0c2f1a3d5e7b9c1d2e3f"

        Truth.assertThat(addressService.validate(ethForm)).isTrue()
        Truth.assertThat(addressService.validate(xdcForm)).isTrue()
        Truth.assertThat(XDCAddressService.formatWith0xPrefix(xdcForm)).isEqualTo(ethForm)
        Truth.assertThat(XDCAddressService.formatWithXdcPrefix(ethForm)).isEqualTo(xdcForm)
    }

    @Test
    fun validateRegularAddressInBothForms() {
        val ethForm = "0x0000000000000000000000000000000000000001"
        val xdcForm = "xdc0000000000000000000000000000000000000001"

        Truth.assertThat(addressService.validate(ethForm)).isTrue()
        Truth.assertThat(addressService.validate(xdcForm)).isTrue()
    }

    @Test
    fun rejectMalformedAddress() {
        Truth.assertThat(addressService.validate("xdc12")).isFalse()
        Truth.assertThat(addressService.validate("0xzz5a4e7b2c1f3a9d8e6b0c2f1a3d5e7b9c1d2e3f")).isFalse()
    }
}
