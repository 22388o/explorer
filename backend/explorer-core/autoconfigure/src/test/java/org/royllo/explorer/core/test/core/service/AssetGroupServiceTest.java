package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetGroupDTO;
import org.royllo.explorer.core.service.asset.AssetGroupService;
import org.royllo.explorer.core.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@DisplayName("AssetGroupService tests")
public class AssetGroupServiceTest extends BaseTest {

    @Autowired
    private AssetGroupService assetGroupService;

    @Test
    @DisplayName("addAssetGroup()")
    public void addAssetGroup() {
        // =============================================================================================================
        // First constraint test - Trying to save an asset group with an ID.
        AssertionError e = assertThrows(AssertionError.class, () -> assetGroupService.addAssetGroup(AssetGroupDTO.builder().id(1L).build()));
        assertEquals("Asset group id must be null", e.getMessage());

        // =============================================================================================================
        // Second constraint test - Asset group key id is required.
        e = assertThrows(AssertionError.class, () -> assetGroupService.addAssetGroup(AssetGroupDTO.builder().build()));
        assertEquals("Asset group key id is required", e.getMessage());

        // =============================================================================================================
        // Third constraint test - Asset group key already registered.
        e = assertThrows(AssertionError.class, () -> assetGroupService.addAssetGroup(AssetGroupDTO.builder().rawGroupKey("RAW_GROUP_KEY_10000").build()));
        assertEquals("Asset group key already registered", e.getMessage());

        // =============================================================================================================
        // Now creating a real asset group.
        AssetGroupDTO assetGroupDTO = assetGroupService.addAssetGroup(AssetGroupDTO.builder()
                .assetIdSig("NEW-ASSET-ID-SIG")
                .rawGroupKey("NEW-ASSET-GROUP-KEY")
                .tweakedGroupKey("NEW-ASSET-GROUP-KEY-TWEAKED")
                .build());

        assertNotNull(assetGroupDTO);
        assertNotNull(assetGroupDTO.getId());
        assertEquals("NEW-ASSET-ID-SIG", assetGroupDTO.getAssetIdSig());
        assertEquals("NEW-ASSET-GROUP-KEY", assetGroupDTO.getRawGroupKey());
        assertEquals("NEW-ASSET-GROUP-KEY-TWEAKED", assetGroupDTO.getTweakedGroupKey());
    }

    @Test
    @DisplayName("getAssetGroupByRawGroupKey()")
    public void getAssetGroupByRawGroupKey() {
        // =============================================================================================================
        // Non-existing asset group.
        Optional<AssetGroupDTO> assetGroup = assetGroupService.getAssetGroupByRawGroupKey("NON-EXISTING");
        assertFalse(assetGroup.isPresent());

        // =============================================================================================================
        // Existing asset group on testnet and in our database initialization script ("roylloCoin") .
        assetGroup = assetGroupService.getAssetGroupByRawGroupKey("RAW_GROUP_KEY_10000");
        assertTrue(assetGroup.isPresent());
        assertEquals(10000, assetGroup.get().getId());
        assertEquals("ASSET_ID_SIG_10000", assetGroup.get().getAssetIdSig());
        assertEquals("RAW_GROUP_KEY_10000", assetGroup.get().getRawGroupKey());
        assertEquals("TWEAKED_GROUP_KEY_10000", assetGroup.get().getTweakedGroupKey());
    }

}
