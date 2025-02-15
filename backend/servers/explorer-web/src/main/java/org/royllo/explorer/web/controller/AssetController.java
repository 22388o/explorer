package org.royllo.explorer.web.controller;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.asset.AssetStateDTO;
import org.royllo.explorer.core.dto.proof.ProofDTO;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.asset.AssetStateService;
import org.royllo.explorer.core.service.proof.ProofService;
import org.royllo.explorer.web.util.base.BaseController;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.royllo.explorer.web.configuration.WebConfiguration.ASSET_PROOFS_DEFAULT_PAGE_SIZE;
import static org.royllo.explorer.web.configuration.WebConfiguration.ASSET_STATES_DEFAULT_PAGE_SIZE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.ASSET_ID_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.ASSET_STATES_LIST_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.PROOF_FILES_LIST_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.PROOF_FILE_ID_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.ModelAttributeConstants.RESULT_ATTRIBUTE;
import static org.royllo.explorer.web.util.constants.PagesConstants.ASSET_PAGE;
import static org.royllo.explorer.web.util.constants.PagesConstants.ASSET_PAGE_FRAGMENT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Asset controller.
 */
@Controller
@RequiredArgsConstructor
public class AssetController extends BaseController {

    /** Asset service. */
    private final AssetService assetService;

    /** Asset state service. */
    private final AssetStateService assetStateService;

    /** Proof file service. */
    private final ProofService proofService;

    /**
     * Page displaying an asset.
     *
     * @param model   model
     * @param request request
     * @param assetId asset id
     * @return view asset page
     */
    @SuppressWarnings("SameReturnValue")
    @GetMapping(value = {"/asset", "/asset/", "/asset/{assetId}"})
    public String getAssetByAssetId(final Model model,
                                    final HttpServletRequest request,
                                    @PathVariable(value = ASSET_ID_ATTRIBUTE, required = false) final String assetId) {
        // If assetId is present, we retrieve it.
        if (StringUtils.isNotBlank(assetId)) {
            model.addAttribute(ASSET_ID_ATTRIBUTE, assetId);

            // =========================================================================================================
            // We retrieve the asset and the assert group.
            final Optional<AssetDTO> asset = assetService.getAssetByAssetId(assetId.trim());
            if (asset.isPresent()) {
                model.addAttribute(RESULT_ATTRIBUTE, asset.get());

                // =========================================================================================================
                // We retrieve the asset states.
                final Page<AssetStateDTO> assetStates = assetStateService.getAssetStatesByAssetId(assetId.trim(), 1,
                        ASSET_STATES_DEFAULT_PAGE_SIZE);
                model.addAttribute(ASSET_STATES_LIST_ATTRIBUTE, assetStates.getContent());

                // =========================================================================================================
                // We retrieve the proof files.
                model.addAttribute(PROOF_FILES_LIST_ATTRIBUTE,
                        proofService.getProofByAssetId(assetId.trim(),
                                1,
                                ASSET_PROOFS_DEFAULT_PAGE_SIZE));
            }
        }

        // If it's an HTMX request, we return the fragment.
        if (isHtmxRequest(request)) {
            return ASSET_PAGE_FRAGMENT;
        }
        return ASSET_PAGE;
    }

    /**
     * Returns a proof.
     *
     * @param proofFileId proof file id
     * @return proof file
     */
    @GetMapping(value = "/asset/{assetId}/proof_file/{proofFileId}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody byte[] getProofFile(@PathVariable(value = PROOF_FILE_ID_ATTRIBUTE) final String proofFileId) {
        final Optional<ProofDTO> proofFile = proofService.getProofByProofId(proofFileId);
        if (proofFile.isPresent()) {
            return proofFile.get().getProof().getBytes();
        } else {
            throw new ResponseStatusException(NOT_FOUND, "Proof file not found");
        }
    }

}
