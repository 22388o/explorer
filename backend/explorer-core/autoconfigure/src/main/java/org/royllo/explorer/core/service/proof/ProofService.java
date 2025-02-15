package org.royllo.explorer.core.service.proof;

import org.royllo.explorer.core.dto.proof.ProofDTO;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Proof service.
 */
public interface ProofService {

    /**
     * Add a new proof to the database.
     *
     * @param proof        proof
     * @param decodedProof decoded proof
     * @return the proof created
     */
    ProofDTO addProof(String proof,
                      DecodedProofResponse decodedProof);

    /**
     * Get a proof by its proof id.
     *
     * @param proofId proof id
     * @return proof
     */
    Optional<ProofDTO> getProofByProofId(String proofId);

    /**
     * Returns the proof of a specific asset.
     *
     * @param assetId  asset id
     * @param page     the page number we want to retrieve (First page is page 1)
     * @param pageSize the page size
     * @return proofs
     */
    Page<ProofDTO> getProofByAssetId(String assetId, int page, int pageSize);

}
