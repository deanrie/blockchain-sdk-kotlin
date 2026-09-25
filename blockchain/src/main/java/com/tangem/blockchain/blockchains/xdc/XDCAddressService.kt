package com.tangem.blockchain.blockchains.xdc

import com.tangem.blockchain.blockchains.ethereum.EthereumAddressService
import com.tangem.blockchain.common.address.Address
import com.tangem.blockchain.common.address.AddressType
import com.tangem.common.card.EllipticCurve

internal class XDCAddressService : EthereumAddressService() {

    override fun makeAddresses(walletPublicKey: ByteArray, curve: EllipticCurve?): Set<Address> {
        return setOf(
            Address(makeAddressWithXdcPrefix(walletPublicKey, curve), AddressType.Default),
            Address(makeAddressWith0xPrefix(walletPublicKey, curve), AddressType.Legacy),
        )
    }

    override fun makeAddress(walletPublicKey: ByteArray, curve: EllipticCurve?): String {
        return makeAddressWithXdcPrefix(walletPublicKey, curve)
    }

    override fun validate(address: String): Boolean {
        return super.validate(formatWith0xPrefix(address))
    }

    private fun makeAddressWith0xPrefix(walletPublicKey: ByteArray, curve: EllipticCurve?): String {
        return super.makeAddress(walletPublicKey, curve)
    }

    private fun makeAddressWithXdcPrefix(walletPublicKey: ByteArray, curve: EllipticCurve?): String {
        val ethAddress = makeAddressWith0xPrefix(walletPublicKey, curve)

        return formatWithXdcPrefix(ethAddress)
    }

    companion object {
        private const val ETH_PREFIX = "0x"
        private const val XDC_PREFIX = "xdc"

        /** Only the leading prefix is swapped: `replace` would also rewrite an `xdc`/`0x` inside the hex body. */
        fun formatWith0xPrefix(address: String): String {
            return if (address.startsWith(XDC_PREFIX)) {
                ETH_PREFIX + address.removePrefix(XDC_PREFIX)
            } else {
                address
            }
        }

        fun formatWithXdcPrefix(address: String): String {
            return if (address.startsWith(ETH_PREFIX)) {
                XDC_PREFIX + address.removePrefix(ETH_PREFIX)
            } else {
                address
            }
        }
    }
}