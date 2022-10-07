package org.royllo.explorer.api.service.bitcoin;

import org.royllo.explorer.api.dto.bitcoin.BitcoinTransactionOutputDTO;

import java.util.Optional;

/**
 * Bitcoin service.
 */
public interface BitcoinService {

    /**
     * Returns details about a specific transaction output.
     *
     * @param txId transaction id
     * @param vOut output index (starts at 0)
     * @return transaction details
     */
    Optional<BitcoinTransactionOutputDTO> getBitcoinTransactionOutput(String txId, int vOut);

}
