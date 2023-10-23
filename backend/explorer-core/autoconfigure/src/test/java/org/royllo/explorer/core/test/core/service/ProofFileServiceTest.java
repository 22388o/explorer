package org.royllo.explorer.core.test.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.dto.proof.ProofFileDTO;
import org.royllo.explorer.core.provider.tapd.DecodedProofResponse;
import org.royllo.explorer.core.provider.tapd.TapdService;
import org.royllo.explorer.core.service.asset.AssetService;
import org.royllo.explorer.core.service.proof.ProofFileService;
import org.royllo.explorer.core.test.util.TestWithMockServers;
import org.royllo.explorer.core.util.exceptions.proof.ProofCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.royllo.explorer.core.util.constants.UserConstants.ANONYMOUS_ID;
import static org.royllo.test.TapdData.ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.ROYLLO_COIN_FROM_TEST;
import static org.royllo.test.TapdData.UNKNOWN_ROYLLO_COIN_ASSET_ID;
import static org.royllo.test.TapdData.UNKNOWN_ROYLLO_COIN_FROM_TEST;

@SpringBootTest
@DisplayName("ProofService tests")
public class ProofFileServiceTest extends TestWithMockServers {

    @Autowired
    private TapdService TAPDService;

    @Autowired
    private ProofFileService proofFileService;

    @Autowired
    private AssetService assetService;

    @Test
    @DisplayName("addProof()")
    public void addProof() {
        // Retrieved data from TAPD.
        final String UNKNOWN_ROYLLO_COIN_RAW_PROOF = UNKNOWN_ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String UNKNOWN_ROYLLO_COIN_PROOF_ID = sha256(UNKNOWN_ROYLLO_COIN_RAW_PROOF);

        // =============================================================================================================
        // Unknown Royllo coin.
        DecodedProofResponse unknownRoylloCoinDecodedProof = TAPDService.decode(UNKNOWN_ROYLLO_COIN_RAW_PROOF).block();
        assertNotNull(unknownRoylloCoinDecodedProof);

        // We add our proof but our an asset doesn't exist yet --> an error must occur.
        assertFalse(assetService.getAssetByAssetId(UNKNOWN_ROYLLO_COIN_ASSET_ID).isPresent());
        ProofCreationException e = assertThrows(ProofCreationException.class, () -> proofFileService.addProof(UNKNOWN_ROYLLO_COIN_RAW_PROOF, unknownRoylloCoinDecodedProof));
        assertEquals(e.getMessage(), "Asset " + UNKNOWN_ROYLLO_COIN_ASSET_ID + " is not registered in our database");

        // We add the asset of our proof, and then, our proof --> No error and proof should be added.
        final AssetDTO unknownRoylloCoin = assetService.addAsset(ASSET_MAPPER.mapToAssetDTO(unknownRoylloCoinDecodedProof.getDecodedProof()));
        assertNotNull(unknownRoylloCoin);
        verifyAsset(unknownRoylloCoin, UNKNOWN_ROYLLO_COIN_ASSET_ID);

        // Then, our proof that should be added without any problem.
        final ProofFileDTO proofAdded = proofFileService.addProof(UNKNOWN_ROYLLO_COIN_RAW_PROOF, unknownRoylloCoinDecodedProof);
        assertNotNull(proofAdded.getId());
        assertEquals(UNKNOWN_ROYLLO_COIN_PROOF_ID, proofAdded.getProofFileId());
        assertEquals(UNKNOWN_ROYLLO_COIN_RAW_PROOF, proofAdded.getRawProof());
        assertEquals(UNKNOWN_ROYLLO_COIN_ASSET_ID, proofAdded.getAsset().getAssetId());
        assertEquals(ANONYMOUS_ID, proofAdded.getCreator().getId());

        // We add again our proof as it's already in our database --> an error must occur.
        e = assertThrows(ProofCreationException.class, () -> proofFileService.addProof(UNKNOWN_ROYLLO_COIN_RAW_PROOF, unknownRoylloCoinDecodedProof));
        assertEquals(e.getMessage(), "This proof file is already registered with proof id: " + UNKNOWN_ROYLLO_COIN_PROOF_ID);
    }

    @Test
    @DisplayName("getProofFilesByAssetId()")
    public void getProofFilesByAssetId() {
        // Retrieved data from TAPD.
        final String ROYLLO_COIN_RAW_PROOF = ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String ROYLLO_COIN_PROOF_ID = sha256(ROYLLO_COIN_RAW_PROOF);

        // =============================================================================================================
        // First case: asset id not found in database.
        AssertionError e = assertThrows(AssertionError.class, () -> proofFileService.getProofFilesByAssetId(ROYLLO_COIN_ASSET_ID, 0, 1));
        assertEquals(e.getMessage(), "Page number starts at page 1");

        // =============================================================================================================
        // Second case: asset id not found in database.
        e = assertThrows(AssertionError.class, () -> proofFileService.getProofFilesByAssetId("NON_EXISTING_ASSET_ID", 1, 1));
        assertEquals(e.getMessage(), "Asset ID not found");

        // =============================================================================================================
        // Getting proofs of "activeRoylloCoin".
        // There are three proofs of this assets in test-data-proof_file.xml.
        final Page<ProofFileDTO> activeRoylloCoinProofs = proofFileService.getProofFilesByAssetId(ACTIVE_ROYLLO_COIN_ASSET_ID, 1, 10);
        assertEquals(3, activeRoylloCoinProofs.getTotalElements());

        // Proof 1.
        Optional<ProofFileDTO> proof1 = activeRoylloCoinProofs.getContent().stream().filter(proofDTO -> proofDTO.getId() == 10001).findFirst();
        assertTrue(proof1.isPresent());
        assertEquals(ANONYMOUS_ID, proof1.get().getCreator().getId());
        assertEquals(ACTIVE_ROYLLO_COIN_ASSET_ID, proof1.get().getAsset().getAssetId());
        assertEquals("14e2075827c687217bede3f703cfbc94345717213f4fd34d83b68f8268040691", proof1.get().getProofFileId());
        assertEquals("0000000001fd0393002439481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db00000001015004e00020e6c99a0dff85ede53ea46fef1c2c7c06fb0e2f4a904e43f21f00000000000000dd5aa62693ed7a74bb2fe8c449fc358b48717b4ba7c2fa000a91e199b6d112926b7dff63bcfe3319ba3fe85f02ea0200000000010139481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0100000000ffffffff026f02000000000000160014b3cb6391af7e94b41475cb925e1eafed57c318cae803000000000000225120541c599eae0b80c2c7c5c7a17b75b147a0fe20663277bbbd49247e87b9b1c1370247304402204afb9a04135bc36e9062e8e0cafad11a58fc2e13f6dcf01447938faced44b53e02202492dc504f6b5577d4add1ea550fad8f7357f7c7215a7d5ec21e14269f270a5e0121027ebfbaf2f6612b4819b188f3b80386d5ddf3d4c55c5f4af8c688d97b8984b0d60000000003a205e44c946dc58fca94bfc32e4c55b7f4aad4a3742246453617d4e56a314f9949c95cc123946cb7bc245bc43c8e0b3c29317c690504a0cfa7b2e0c790731184d0f5b7fb34b383e47b12e5335ef4e37d3dbe1eca30d9ba62805ac8344efee4871ff684af935b1aff57d8aef1dc0fed4862a2965529a0c9f8a7d3572d8fa513303c744f7ba713278679e08bb0fd955df359de9605b6e97b6e78cc44ce67bfc84c76151104e8000100014939481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000001000201000303fd03eb0669016700650000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080200000921024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc059f000400000001012103bea9941963648cfaaa2981d68ebf209e20b3e68287d94371805832e9624014290274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8d2726a957285fe1e6f4c9916deb2086f5cd3fa67327caa039dbc85d57c9576f", proof1.get().getRawProof());

        // Proof 2.
        Optional<ProofFileDTO> proof2 = activeRoylloCoinProofs.getContent().stream().filter(proofDTO -> proofDTO.getId() == 10002).findFirst();
        assertTrue(proof2.isPresent());
        assertEquals(ANONYMOUS_ID, proof2.get().getCreator().getId());
        assertEquals(ACTIVE_ROYLLO_COIN_ASSET_ID, proof2.get().getAsset().getAssetId());
        assertEquals("23a6c9e1db87a8993490c7578c7ae6d85fee3bc16b9fc7d3c4c756f7452262e1", proof2.get().getProofFileId());
        assertEquals("0000000002fd0393002439481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db00000001015004e00020e6c99a0dff85ede53ea46fef1c2c7c06fb0e2f4a904e43f21f00000000000000dd5aa62693ed7a74bb2fe8c449fc358b48717b4ba7c2fa000a91e199b6d112926b7dff63bcfe3319ba3fe85f02ea0200000000010139481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0100000000ffffffff026f02000000000000160014b3cb6391af7e94b41475cb925e1eafed57c318cae803000000000000225120541c599eae0b80c2c7c5c7a17b75b147a0fe20663277bbbd49247e87b9b1c1370247304402204afb9a04135bc36e9062e8e0cafad11a58fc2e13f6dcf01447938faced44b53e02202492dc504f6b5577d4add1ea550fad8f7357f7c7215a7d5ec21e14269f270a5e0121027ebfbaf2f6612b4819b188f3b80386d5ddf3d4c55c5f4af8c688d97b8984b0d60000000003a205e44c946dc58fca94bfc32e4c55b7f4aad4a3742246453617d4e56a314f9949c95cc123946cb7bc245bc43c8e0b3c29317c690504a0cfa7b2e0c790731184d0f5b7fb34b383e47b12e5335ef4e37d3dbe1eca30d9ba62805ac8344efee4871ff684af935b1aff57d8aef1dc0fed4862a2965529a0c9f8a7d3572d8fa513303c744f7ba713278679e08bb0fd955df359de9605b6e97b6e78cc44ce67bfc84c76151104e8000100014939481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000001000201000303fd03eb0669016700650000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080200000921024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc059f000400000001012103bea9941963648cfaaa2981d68ebf209e20b3e68287d94371805832e9624014290274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8d2726a957285fe1e6f4c9916deb2086f5cd3fa67327caa039dbc85d57c9576ffd05a60024d7bd7a50f78b9e6a0621c34175bc390b091070e3532908660ad19ad2061cb1600000000101500000002056e41b012000c3de4265eac3671a8278ab65cbb05dfd66586a8178bf0000000094b2a4d08f54f11f05276326894ba9cc7a27a3d99247e669bd505422e8e8330c308bff63ffff001de1101dd102fd0180020000000001024186b51fb5050ce20332a18c940934faaf7b7f80874a9d9889b456a8927a20050200000000ffffffffd7bd7a50f78b9e6a0621c34175bc390b091070e3532908660ad19ad2061cb16001000000000000000003e8030000000000002251207d84b124b559f1a0ab396cec2677d5a27a140caaef680cfdbecd7a16a4593762e8030000000000002251209588124b01b2a1f00db66c22b3c5560d58a7f6f8c09a98878723e87ee6eb10a3c90c0000000000001600148cb54fe42666c5e008316c342aa58c0d803392ca0247304402203b09ac49a17faf2c1bed11faccba212cfcfb01eb962656b9fd28f4a60fdc81980220561c9053d536e8cdcb771c13ba5957497e421c5ac6456b0c5be76ef908fc60c8012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c01407eb8a1f6bf214a8471f72a894cc99e6fe756b8f2258a6c786cb825767fc2762d2e1320da2eecabaa92872fa03d584d52e5ff5c80056be1488f44cc0aeb50559c0000000003e207938006468f02d2d507be06a19e95dcb745c0c80d2f909b699b1e9be7d51ab20f013260064752b181c2b7211d73f34b6a898c7728eed82e4fa837d3032a0f9f940b660b9ab0110750f38696f8afb8c9e1efe82f134bb264ccb624d2a89afde4d9eb44c9b74161a524df3f8d0cee247be9984f6694ca31766d2a12470bfc4e0b5c33b2659a8b50f188deb330af74218788c0717f66f4f56832b2ec568e571e97dd0fc080ed0b6465878f8547163ddf71a6825290b08846484fab9ab9f84895feedd0ff6506a12b5c5e718cd7ee09f8474c852587b7a6c693819fd4add29752f6796304fd0156000100014939481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000001000201000303fd038706ad01ab0065d7bd7a50f78b9e6a0621c34175bc390b091070e3532908660ad19ad2061cb160000000011781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc014201402b9b32468fc0a3e8809763fdd191a720aba0f6fc0e46642e192484114cf1b002ac82e56cb4e8aaf8fcd9c7742e2b4fa6c195af91cedaa1a670ab46cb4a3811920728b48931d124c3c1911c5671ffabc9477c82b62cd62d3920d2cadd5878d31f882100000000000003eb08020000092102b0664f13bfc71ddcb8e0eb004b5b486cda6d0dfd33340b85d43f618b484e1517059f000400000000012102d180fa5fde1f070a9df166d1cc4c0ec8fd3ccd57da8744a4fed8a1a1578cf1ae0274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff06c901c70004000000010121030411db4d023a8d55607fedc562d519b1854af7752d9e1f00279213380d439e88029c007100010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413024a000151f716888109ae4abf80fa5120d1eaca3e36ec8a2a45849e573001373de715170000000000000064ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffbf012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff48740a7a6841d86a30a38719fce36e4e30393e63acd4e5132ee2544dce048d37", proof2.get().getRawProof());

        // Proof 3.
        Optional<ProofFileDTO> proof3 = activeRoylloCoinProofs.getContent().stream().filter(proofDTO -> proofDTO.getId() == 10003).findFirst();
        assertTrue(proof3.isPresent());
        assertEquals(ANONYMOUS_ID, proof3.get().getCreator().getId());
        assertEquals(ACTIVE_ROYLLO_COIN_ASSET_ID, proof3.get().getAsset().getAssetId());
        assertEquals("e537eddf83dcb34723121860b49579eb4e766ace01bbb81fc7fec233835f2e1e", proof3.get().getProofFileId());
        assertEquals("0000000002fd0393002439481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db00000001015004e00020e6c99a0dff85ede53ea46fef1c2c7c06fb0e2f4a904e43f21f00000000000000dd5aa62693ed7a74bb2fe8c449fc358b48717b4ba7c2fa000a91e199b6d112926b7dff63bcfe3319ba3fe85f02ea0200000000010139481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0100000000ffffffff026f02000000000000160014b3cb6391af7e94b41475cb925e1eafed57c318cae803000000000000225120541c599eae0b80c2c7c5c7a17b75b147a0fe20663277bbbd49247e87b9b1c1370247304402204afb9a04135bc36e9062e8e0cafad11a58fc2e13f6dcf01447938faced44b53e02202492dc504f6b5577d4add1ea550fad8f7357f7c7215a7d5ec21e14269f270a5e0121027ebfbaf2f6612b4819b188f3b80386d5ddf3d4c55c5f4af8c688d97b8984b0d60000000003a205e44c946dc58fca94bfc32e4c55b7f4aad4a3742246453617d4e56a314f9949c95cc123946cb7bc245bc43c8e0b3c29317c690504a0cfa7b2e0c790731184d0f5b7fb34b383e47b12e5335ef4e37d3dbe1eca30d9ba62805ac8344efee4871ff684af935b1aff57d8aef1dc0fed4862a2965529a0c9f8a7d3572d8fa513303c744f7ba713278679e08bb0fd955df359de9605b6e97b6e78cc44ce67bfc84c76151104e8000100014939481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000001000201000303fd03eb0669016700650000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080200000921024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc059f000400000001012103bea9941963648cfaaa2981d68ebf209e20b3e68287d94371805832e9624014290274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8d2726a957285fe1e6f4c9916deb2086f5cd3fa67327caa039dbc85d57c9576ffd07830024d7bd7a50f78b9e6a0621c34175bc390b091070e3532908660ad19ad2061cb1600000000101500000002056e41b012000c3de4265eac3671a8278ab65cbb05dfd66586a8178bf0000000094b2a4d08f54f11f05276326894ba9cc7a27a3d99247e669bd505422e8e8330c308bff63ffff001de1101dd102fd0180020000000001024186b51fb5050ce20332a18c940934faaf7b7f80874a9d9889b456a8927a20050200000000ffffffffd7bd7a50f78b9e6a0621c34175bc390b091070e3532908660ad19ad2061cb16001000000000000000003e8030000000000002251207d84b124b559f1a0ab396cec2677d5a27a140caaef680cfdbecd7a16a4593762e8030000000000002251209588124b01b2a1f00db66c22b3c5560d58a7f6f8c09a98878723e87ee6eb10a3c90c0000000000001600148cb54fe42666c5e008316c342aa58c0d803392ca0247304402203b09ac49a17faf2c1bed11faccba212cfcfb01eb962656b9fd28f4a60fdc81980220561c9053d536e8cdcb771c13ba5957497e421c5ac6456b0c5be76ef908fc60c8012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c01407eb8a1f6bf214a8471f72a894cc99e6fe756b8f2258a6c786cb825767fc2762d2e1320da2eecabaa92872fa03d584d52e5ff5c80056be1488f44cc0aeb50559c0000000003e207938006468f02d2d507be06a19e95dcb745c0c80d2f909b699b1e9be7d51ab20f013260064752b181c2b7211d73f34b6a898c7728eed82e4fa837d3032a0f9f940b660b9ab0110750f38696f8afb8c9e1efe82f134bb264ccb624d2a89afde4d9eb44c9b74161a524df3f8d0cee247be9984f6694ca31766d2a12470bfc4e0b5c33b2659a8b50f188deb330af74218788c0717f66f4f56832b2ec568e571e97dd0fc080ed0b6465878f8547163ddf71a6825290b08846484fab9ab9f84895feedd0ff6506a12b5c5e718cd7ee09f8474c852587b7a6c693819fd4add29752f6796304fd0292000100014939481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f000000010002010003016406fd021301fd020f0065000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002fd01a44a00018205ee72be16f3b195b465cb095b97e811edddabb0dcec197f9e682d05a31ca00000000000000387ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7ffd0156000100014939481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000001000201000303fd038706ad01ab0065d7bd7a50f78b9e6a0621c34175bc390b091070e3532908660ad19ad2061cb160000000011781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc014201402b9b32468fc0a3e8809763fdd191a720aba0f6fc0e46642e192484114cf1b002ac82e56cb4e8aaf8fcd9c7742e2b4fa6c195af91cedaa1a670ab46cb4a3811920728b48931d124c3c1911c5671ffabc9477c82b62cd62d3920d2cadd5878d31f882100000000000003eb08020000092102b0664f13bfc71ddcb8e0eb004b5b486cda6d0dfd33340b85d43f618b484e151708020000092102576e6cf95f0d7724f2e17afcd74a690231bf4e1ecb1963229af3fe33edcd58ca059f0004000000010121030411db4d023a8d55607fedc562d519b1854af7752d9e1f00279213380d439e880274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff06c901c7000400000000012102d180fa5fde1f070a9df166d1cc4c0ec8fd3ccd57da8744a4fed8a1a1578cf1ae029c007100010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413024a00019fe1a55b2c9813569b7c7a3bf514bafc0cf43748d646a2bf39848870b06677a70000000000000387ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffbf012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff079f000400000000012102d180fa5fde1f070a9df166d1cc4c0ec8fd3ccd57da8744a4fed8a1a1578cf1ae0274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe362126b922539fc3451739b11745035fcc950ee86d0edace720399be6951c7e", proof3.get().getRawProof());

        // =============================================================================================================
        // Getting proofs of "roylloCoin" - One page.
        final Page<ProofFileDTO> roylloCoinProofs = proofFileService.getProofFilesByAssetId(ROYLLO_COIN_ASSET_ID, 1, 10);
        assertEquals(1, roylloCoinProofs.getTotalElements());

        // Proof 1.
        Optional<ProofFileDTO> roylloCoinProof1 = roylloCoinProofs.getContent().stream().filter(proofDTO -> proofDTO.getId() == 1).findFirst();
        assertEquals(ANONYMOUS_ID, roylloCoinProof1.get().getCreator().getId());
        assertEquals(ROYLLO_COIN_ASSET_ID, roylloCoinProof1.get().getAsset().getAssetId());
        assertEquals(ROYLLO_COIN_PROOF_ID, roylloCoinProof1.get().getProofFileId());
        assertEquals(ROYLLO_COIN_RAW_PROOF, roylloCoinProof1.get().getRawProof());
    }

    @Test
    @DisplayName("getProofFileByProofFileId()")
    public void getProofFileByProofFileId() {
        // Retrieved data from TAPD.
        final String ROYLLO_COIN_RAW_PROOF = ROYLLO_COIN_FROM_TEST.getDecodedProofRequest(0).getRawProof();
        final String ROYLLO_COIN_PROOF_ID = sha256(ROYLLO_COIN_RAW_PROOF);

        final Optional<ProofFileDTO> roylloCoinProof = proofFileService.getProofFileByProofFileId(ROYLLO_COIN_PROOF_ID);

        // Checking proof.
        assertTrue(roylloCoinProof.isPresent());
        assertEquals(ROYLLO_COIN_PROOF_ID, roylloCoinProof.get().getProofFileId());
        assertEquals(ROYLLO_COIN_RAW_PROOF, roylloCoinProof.get().getRawProof());

        // Checking asset.
        assertEquals(1, roylloCoinProof.get().getAsset().getId());
        verifyAsset(roylloCoinProof.get().getAsset(), ROYLLO_COIN_ASSET_ID);
    }

}
