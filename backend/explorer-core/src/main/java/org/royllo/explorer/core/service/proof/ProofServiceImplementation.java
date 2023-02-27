package org.royllo.explorer.core.service.proof;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.dto.proof.ProofDTO;
import org.royllo.explorer.core.provider.tarod.DecodedProofResponse;
import org.royllo.explorer.core.repository.proof.ProofRepository;
import org.royllo.explorer.core.util.base.BaseService;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Proof implementation service.
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("checkstyle:DesignForExtension")
public class ProofServiceImplementation extends BaseService implements ProofService {

    /** Proof repository. */
    private final ProofRepository proofRepository;

    @Override
    public ProofDTO addProof(@NonNull final String rawProof,
                             final long proofIndex,
                             @NonNull final DecodedProofResponse decodedProof) {
        logger.info("addProof - Adding {}", decodedProof);
        // We check that the proof is not in our database.
        // We check that the asset exists in our database.

        // We create the proof (link to asset and user).
/*        Proof proof = Proof.builder()
                .proofId(getSHA256(rawProof + ":" + proofIndex))
                .creator(ANONYMOUS_USER)
                .rawProof(rawProof)UserConstants
                .proofIndex(proofIndex)
                .txMerkleProof(decodedProof.getDecodedProof().getTxMerkleProof())
                .inclusionProof(decodedProof.getDecodedProof().getInclusionProof())
                .exclusionProofs(decodedProof.getDecodedProof().getExclusionProofs())
                .build();*/

        return null;
    }

    /**
     * Returns the sha256 value calculated with the parameter.
     *
     * @param value value
     * @return sha256 of value
     */
    private String getSHA256(final String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(value.getBytes(StandardCharsets.UTF_8));
            return DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 is not available: " + e.getMessage());
        }
    }

}
