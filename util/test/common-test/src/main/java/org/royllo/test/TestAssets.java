package org.royllo.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.MatchType;
import org.mockserver.model.JsonBody;
import org.royllo.test.tapd.AssetValue;
import org.royllo.test.tapd.DecodedProofValue;
import org.royllo.test.tapd.DecodedProofValueRequest;
import org.royllo.test.tapd.DecodedProofValueResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.MediaType.APPLICATION_JSON;

/**
 * Test assets data.
 */
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class TestAssets {

    /* ============================================================================================================== */

    /** Royllo coin asset id (exists in royllo database). */
    public static final String ROYLLO_COIN_ASSET_ID = "f9dd292bb211dae8493645150b36efa990841b11038d026577440d2616d1ec32";

    /** Royllo coin raw proof ID. */
    public static final String ROYLLO_COIN_PROOF_ID = "09fcd6349cceea648dc00545846e40b50efdf3c9e27e3d7feb43103f6e593576";

    /** Royllo coin raw proof. */
    public static final String ROYLLO_COIN_RAW_PROOF = "0000000001fd051400241101b19e0dd323f86840976a161bf807fd1dbc30e793bb74aff307e1e9edfa570000000101500000d6224f34b13ded7327694942bf00a9060938f759a544e98e7b0139120000000000002d2c483ab11c46d36e302cae0e225c790ce201449138186b3dc7e83e72652a594816a764ffff001d274d807202fd0189020000000001021101b19e0dd323f86840976a161bf807fd1dbc30e793bb74aff307e1e9edfa570100000000ffffffffdc62e81401abfec546a45213c65c9910466f06ea1b831b017c13edfa71d775b10200000000ffffffff02e80300000000000022512040b3ddefc0df09b89fd1379a3bd246bf2827ed461506a5899dc230324d0aa158bc05000000000000225120d7ab0deb6185088278a66f5599be41510db531fd7f2df39e3b8558e2d2b65b0602473044022023f92ca5b7289d5cfb2ae415b91efa8bb9758d97b78b8ad17c81ce43fa9d262a02205c790874cbd4dae139f1d3e2b029bb97c4bf13bff0bc80127dacf9e917a37ed5012102cf948a447b6b49ccf83754b2455b7c6f77a9daeb1bb192f17ae34d3f52182a1a024630430220698a8f0543c97775ece1ad26a63f99d837ff9fd3b136e5c43cc984c37a75b3dd021f4d183f9811ac7a7cde904c795f68f96bda845efb5493a95582b60a93ca55c201210305c9ac7185c67621b7534e700d9592863f07ae5b9add38da9576e4a06a66c1000000000003c206e47b7ffac0a46c638ebd66bd72c07c93059cb9dcacd5474b4d29bb0f1cc661ef12dc3d91a992c4d4800fadc4593e665783966e9ea18257e7d1d187ee3f70a3a53f660f84796da9dccf2521f2bc0caddb627789062cca22e8de926f1e2327370f38e8899c9da3fcc32ccbccb65ccac88e253398c0d447323a456813bf08f7b22bd1af29b3bec54a12010c5bcd4a545e0bc461507236adc7fef624b70042f53a0ebf264c2dfde363f7e68820f57767962e412238ce2ee06ace92795f496ee1c5eb2e04fd015600010001541101b19e0dd323f86840976a161bf807fd1dbc30e793bb74aff307e1e9edfa57000000010a726f796c6c6f436f696e0c482467dfb29000804e044c4c6044b487c0280082002d4611a8ba7f22703d6300000000000201000303fd03e70669016700650000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080200000921025d615b377761a5bfcfe84f0f11afd35837a68f702dd8a0cac0a2a4052b20d2110a6102260e9ffbe7fdabe746b4ba4c3b86c8f237d7946f116949e93db77d5e0357c13dd0d86f5c646fcca990b8ceaf7a480762c92900351ae84e8135740fa92a66c8c0a4e509c8d10df8913924a583d74e9cf25ccd56eb2552c3b92a8c3c9ebe8cca97059f0004000000000121026ad322cc8a05cf5723bf8aeb5c778c6462146e573af182ba20c6bed53ea29ae60274004900010001209e4f59a9e6c363a266472043f21f2f11b0b7f206f2abd8760d555832f16cf63f02220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0630012e00040000000101210264e26d9bcccf4e442e08a8bab1fc2a0b64fcb83b34cbba1fe4b0404c3fd110980303020101081c0001000117526f796c6c6f20636f696e20666f7220746573746e657401bc6c08537fd418062422c4c2803bca5cc7edba3fd5693f5d3f6370a45d0d7d";

    /** Royllo coin asset state id n°1. */
    public static final String ROYLLO_COIN_ASSET_STATE_ID = "2c3f6a53f1f7d5c42970f0586c89bd767cde919fc28ec46f1d5831db4d6dfc3a";

    /* ============================================================================================================== */

    /** Unknown royllo coin asset id (exists on testnet). */
    public static final String UNKNOWN_ROYLLO_COIN_ASSET_ID = "852054161032646e45cbaebc8c316660f5d4abb3f7ef9b367dd7d91889e0a5ba";

    /** Unknown royllo coin raw proof. */
    public static final String UNKNOWN_ROYLLO_COIN_RAW_PROOF = "0000000001fd048600246df2da40de9f02d38ff0bec73399553a78c4ddaaa43e6e112fdc68545d2755410000000101500000e020c2f35f015c63c678d1c7c32779d2ae0f03bf2f02038d9e1b1400000000000000fcc189fd4aff61f3df407bcc93a964e70eaadd5d3c928c9b59e647635389c31d8520ab64ffff001d69e568a302f6020000000001016df2da40de9f02d38ff0bec73399553a78c4ddaaa43e6e112fdc68545d2755410100000000ffffffff02e402000000000000225120d9c5d97fb55abcdf85102d90fb4c7b76d9f1656ae900d3127c861d0e814fa699e803000000000000225120d68d892bc7813c8012e295fe5e2abe94433c4fddaddd3cf57a7ee30dd14e15350247304402206a0c9ff04deb771651745a694a990c56e89213093cd686807f8796fb336311c302207e30d5709696443e527cb94d0e840802a77cc5915dbe28536585194c795886d7012102cf948a447b6b49ccf83754b2455b7c6f77a9daeb1bb192f17ae34d3f52182a1a0000000003c2062c9a2969fd95baf2bbf2fef77027d589e6886411d44694e99877680894108f292e3ac8594e660d4525ea0ad4debd7a72a10104c0213e7856c2b0d86aa405ebc71aeb9a14a9f4a1fa3219da74352ce9df8392a8cddd31a552ea69211d475350a1bd371e31431605795e628ad367d15be0302d176979966b79ec6e653492a91a3d85b96db6515fd44c87b9b062770bea138b93e99110294dc8f4312e315bde589d1b4f4f800498be8de0cbaed9720dd9edb27e622e2c79e5ae861a1bbb358e4c983104fd015d000100015b6df2da40de9f02d38ff0bec73399553a78c4ddaaa43e6e112fdc68545d2755410000000111756e6b6e6f776e526f796c6c6f436f696e0c482467dfb29000804e044c4c6044b487c0280082002d4611a8ba7f22703d6300000001000201000303fd03e8066901670065000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000008020000092102372e365b746b7215b9f037fe354aca49b01177e91fd1f0172fb6053e2dd0eff20a61033961286ebb47abce4167ba070546270ea756242a50978b0e38b73c28c125aa8f2c3f92006f40226157229fe7e29a917d9c7dcdf826b1af396ff7416d9947ac189ef25c34be098411c2e6cae1a2c7a9846b61b499c5b4bbfca2daf73bac7858d3059f000400000001012102103a3be4f8d8aa275382ccf5898dcc27d0b10fcf1f40a29ff5548d98676ebba50274004900010001200c83db7f00ffe9e1fe2e6acb5d7de3cca391be4be35837d9a81a087976c8525c02220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0630012e0004000000000121023b5987e9f7c02c2502ce518a6b0f5cc18aae4579a45268105c0b2d0d1c324e950303020101081c0001000117526f796c6c6f20636f696e20666f7220746573746e6574c8254216b4fd05ea9c648b9db7e10e6b5a2f02ae8fe95a7e6320282df0d97915";

    /* ============================================================================================================== */

    /** Test coin asset id (several proof files). */
    public static final String TEST_COIN_ASSET_ID = "981dd27089b3bebc0ea0cd17e78e69ca8d582e7b9695ac53f97ea10723a852f8";

    /* ============================================================================================================== */

    /** Active Royllo coin. */
    public static final String ACTIVE_ROYLLO_COIN_ASSET_ID = "1781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413";

    /** Active Royllo coin - Raw proof 1 id. */
    public static final String ACTIVE_ROYLLO_COIN_PROOF_1_PROOF_ID = "14e2075827c687217bede3f703cfbc94345717213f4fd34d83b68f8268040691";

    /** Active Royllo coin - Raw proof 1. */
    public static final String ACTIVE_ROYLLO_COIN_PROOF_1_RAW_PROOF = "0000000001fd0393002439481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db00000001015004e00020e6c99a0dff85ede53ea46fef1c2c7c06fb0e2f4a904e43f21f00000000000000dd5aa62693ed7a74bb2fe8c449fc358b48717b4ba7c2fa000a91e199b6d112926b7dff63bcfe3319ba3fe85f02ea0200000000010139481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0100000000ffffffff026f02000000000000160014b3cb6391af7e94b41475cb925e1eafed57c318cae803000000000000225120541c599eae0b80c2c7c5c7a17b75b147a0fe20663277bbbd49247e87b9b1c1370247304402204afb9a04135bc36e9062e8e0cafad11a58fc2e13f6dcf01447938faced44b53e02202492dc504f6b5577d4add1ea550fad8f7357f7c7215a7d5ec21e14269f270a5e0121027ebfbaf2f6612b4819b188f3b80386d5ddf3d4c55c5f4af8c688d97b8984b0d60000000003a205e44c946dc58fca94bfc32e4c55b7f4aad4a3742246453617d4e56a314f9949c95cc123946cb7bc245bc43c8e0b3c29317c690504a0cfa7b2e0c790731184d0f5b7fb34b383e47b12e5335ef4e37d3dbe1eca30d9ba62805ac8344efee4871ff684af935b1aff57d8aef1dc0fed4862a2965529a0c9f8a7d3572d8fa513303c744f7ba713278679e08bb0fd955df359de9605b6e97b6e78cc44ce67bfc84c76151104e8000100014939481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000001000201000303fd03eb0669016700650000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080200000921024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc059f000400000001012103bea9941963648cfaaa2981d68ebf209e20b3e68287d94371805832e9624014290274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8d2726a957285fe1e6f4c9916deb2086f5cd3fa67327caa039dbc85d57c9576f";

    /** Active Royllo coin - Raw proof 2 id. */
    public static final String ACTIVE_ROYLLO_COIN_PROOF_2_PROOF_ID = "23a6c9e1db87a8993490c7578c7ae6d85fee3bc16b9fc7d3c4c756f7452262e1";

    /** Active Royllo coin - Raw proof 2. */
    public static final String ACTIVE_ROYLLO_COIN_PROOF_2_RAW_PROOF = "0000000002fd0393002439481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db00000001015004e00020e6c99a0dff85ede53ea46fef1c2c7c06fb0e2f4a904e43f21f00000000000000dd5aa62693ed7a74bb2fe8c449fc358b48717b4ba7c2fa000a91e199b6d112926b7dff63bcfe3319ba3fe85f02ea0200000000010139481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0100000000ffffffff026f02000000000000160014b3cb6391af7e94b41475cb925e1eafed57c318cae803000000000000225120541c599eae0b80c2c7c5c7a17b75b147a0fe20663277bbbd49247e87b9b1c1370247304402204afb9a04135bc36e9062e8e0cafad11a58fc2e13f6dcf01447938faced44b53e02202492dc504f6b5577d4add1ea550fad8f7357f7c7215a7d5ec21e14269f270a5e0121027ebfbaf2f6612b4819b188f3b80386d5ddf3d4c55c5f4af8c688d97b8984b0d60000000003a205e44c946dc58fca94bfc32e4c55b7f4aad4a3742246453617d4e56a314f9949c95cc123946cb7bc245bc43c8e0b3c29317c690504a0cfa7b2e0c790731184d0f5b7fb34b383e47b12e5335ef4e37d3dbe1eca30d9ba62805ac8344efee4871ff684af935b1aff57d8aef1dc0fed4862a2965529a0c9f8a7d3572d8fa513303c744f7ba713278679e08bb0fd955df359de9605b6e97b6e78cc44ce67bfc84c76151104e8000100014939481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000001000201000303fd03eb0669016700650000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080200000921024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc059f000400000001012103bea9941963648cfaaa2981d68ebf209e20b3e68287d94371805832e9624014290274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8d2726a957285fe1e6f4c9916deb2086f5cd3fa67327caa039dbc85d57c9576ffd05a60024d7bd7a50f78b9e6a0621c34175bc390b091070e3532908660ad19ad2061cb1600000000101500000002056e41b012000c3de4265eac3671a8278ab65cbb05dfd66586a8178bf0000000094b2a4d08f54f11f05276326894ba9cc7a27a3d99247e669bd505422e8e8330c308bff63ffff001de1101dd102fd0180020000000001024186b51fb5050ce20332a18c940934faaf7b7f80874a9d9889b456a8927a20050200000000ffffffffd7bd7a50f78b9e6a0621c34175bc390b091070e3532908660ad19ad2061cb16001000000000000000003e8030000000000002251207d84b124b559f1a0ab396cec2677d5a27a140caaef680cfdbecd7a16a4593762e8030000000000002251209588124b01b2a1f00db66c22b3c5560d58a7f6f8c09a98878723e87ee6eb10a3c90c0000000000001600148cb54fe42666c5e008316c342aa58c0d803392ca0247304402203b09ac49a17faf2c1bed11faccba212cfcfb01eb962656b9fd28f4a60fdc81980220561c9053d536e8cdcb771c13ba5957497e421c5ac6456b0c5be76ef908fc60c8012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c01407eb8a1f6bf214a8471f72a894cc99e6fe756b8f2258a6c786cb825767fc2762d2e1320da2eecabaa92872fa03d584d52e5ff5c80056be1488f44cc0aeb50559c0000000003e207938006468f02d2d507be06a19e95dcb745c0c80d2f909b699b1e9be7d51ab20f013260064752b181c2b7211d73f34b6a898c7728eed82e4fa837d3032a0f9f940b660b9ab0110750f38696f8afb8c9e1efe82f134bb264ccb624d2a89afde4d9eb44c9b74161a524df3f8d0cee247be9984f6694ca31766d2a12470bfc4e0b5c33b2659a8b50f188deb330af74218788c0717f66f4f56832b2ec568e571e97dd0fc080ed0b6465878f8547163ddf71a6825290b08846484fab9ab9f84895feedd0ff6506a12b5c5e718cd7ee09f8474c852587b7a6c693819fd4add29752f6796304fd0156000100014939481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000001000201000303fd038706ad01ab0065d7bd7a50f78b9e6a0621c34175bc390b091070e3532908660ad19ad2061cb160000000011781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc014201402b9b32468fc0a3e8809763fdd191a720aba0f6fc0e46642e192484114cf1b002ac82e56cb4e8aaf8fcd9c7742e2b4fa6c195af91cedaa1a670ab46cb4a3811920728b48931d124c3c1911c5671ffabc9477c82b62cd62d3920d2cadd5878d31f882100000000000003eb08020000092102b0664f13bfc71ddcb8e0eb004b5b486cda6d0dfd33340b85d43f618b484e1517059f000400000000012102d180fa5fde1f070a9df166d1cc4c0ec8fd3ccd57da8744a4fed8a1a1578cf1ae0274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff06c901c70004000000010121030411db4d023a8d55607fedc562d519b1854af7752d9e1f00279213380d439e88029c007100010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413024a000151f716888109ae4abf80fa5120d1eaca3e36ec8a2a45849e573001373de715170000000000000064ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffbf012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff48740a7a6841d86a30a38719fce36e4e30393e63acd4e5132ee2544dce048d37";

    /** Active Royllo coin - Raw proof 3 id. */
    public static final String ACTIVE_ROYLLO_COIN_PROOF_3_PROOF_ID = "e537eddf83dcb34723121860b49579eb4e766ace01bbb81fc7fec233835f2e1e";

    /** Active Royllo coin - Raw proof 3. */
    public static final String ACTIVE_ROYLLO_COIN_PROOF_3_RAW_PROOF = "0000000002fd0393002439481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db00000001015004e00020e6c99a0dff85ede53ea46fef1c2c7c06fb0e2f4a904e43f21f00000000000000dd5aa62693ed7a74bb2fe8c449fc358b48717b4ba7c2fa000a91e199b6d112926b7dff63bcfe3319ba3fe85f02ea0200000000010139481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0100000000ffffffff026f02000000000000160014b3cb6391af7e94b41475cb925e1eafed57c318cae803000000000000225120541c599eae0b80c2c7c5c7a17b75b147a0fe20663277bbbd49247e87b9b1c1370247304402204afb9a04135bc36e9062e8e0cafad11a58fc2e13f6dcf01447938faced44b53e02202492dc504f6b5577d4add1ea550fad8f7357f7c7215a7d5ec21e14269f270a5e0121027ebfbaf2f6612b4819b188f3b80386d5ddf3d4c55c5f4af8c688d97b8984b0d60000000003a205e44c946dc58fca94bfc32e4c55b7f4aad4a3742246453617d4e56a314f9949c95cc123946cb7bc245bc43c8e0b3c29317c690504a0cfa7b2e0c790731184d0f5b7fb34b383e47b12e5335ef4e37d3dbe1eca30d9ba62805ac8344efee4871ff684af935b1aff57d8aef1dc0fed4862a2965529a0c9f8a7d3572d8fa513303c744f7ba713278679e08bb0fd955df359de9605b6e97b6e78cc44ce67bfc84c76151104e8000100014939481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000001000201000303fd03eb0669016700650000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080200000921024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc059f000400000001012103bea9941963648cfaaa2981d68ebf209e20b3e68287d94371805832e9624014290274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8d2726a957285fe1e6f4c9916deb2086f5cd3fa67327caa039dbc85d57c9576ffd07830024d7bd7a50f78b9e6a0621c34175bc390b091070e3532908660ad19ad2061cb1600000000101500000002056e41b012000c3de4265eac3671a8278ab65cbb05dfd66586a8178bf0000000094b2a4d08f54f11f05276326894ba9cc7a27a3d99247e669bd505422e8e8330c308bff63ffff001de1101dd102fd0180020000000001024186b51fb5050ce20332a18c940934faaf7b7f80874a9d9889b456a8927a20050200000000ffffffffd7bd7a50f78b9e6a0621c34175bc390b091070e3532908660ad19ad2061cb16001000000000000000003e8030000000000002251207d84b124b559f1a0ab396cec2677d5a27a140caaef680cfdbecd7a16a4593762e8030000000000002251209588124b01b2a1f00db66c22b3c5560d58a7f6f8c09a98878723e87ee6eb10a3c90c0000000000001600148cb54fe42666c5e008316c342aa58c0d803392ca0247304402203b09ac49a17faf2c1bed11faccba212cfcfb01eb962656b9fd28f4a60fdc81980220561c9053d536e8cdcb771c13ba5957497e421c5ac6456b0c5be76ef908fc60c8012103219e2594f427e338690c3f51409706439d8ab83fde02d18d0c28ec55c285801c01407eb8a1f6bf214a8471f72a894cc99e6fe756b8f2258a6c786cb825767fc2762d2e1320da2eecabaa92872fa03d584d52e5ff5c80056be1488f44cc0aeb50559c0000000003e207938006468f02d2d507be06a19e95dcb745c0c80d2f909b699b1e9be7d51ab20f013260064752b181c2b7211d73f34b6a898c7728eed82e4fa837d3032a0f9f940b660b9ab0110750f38696f8afb8c9e1efe82f134bb264ccb624d2a89afde4d9eb44c9b74161a524df3f8d0cee247be9984f6694ca31766d2a12470bfc4e0b5c33b2659a8b50f188deb330af74218788c0717f66f4f56832b2ec568e571e97dd0fc080ed0b6465878f8547163ddf71a6825290b08846484fab9ab9f84895feedd0ff6506a12b5c5e718cd7ee09f8474c852587b7a6c693819fd4add29752f6796304fd0292000100014939481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f000000010002010003016406fd021301fd020f0065000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002fd01a44a00018205ee72be16f3b195b465cb095b97e811edddabb0dcec197f9e682d05a31ca00000000000000387ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff7ffd0156000100014939481e35ca088d991a4e6d7296b22c650545f0bbfe0850d3ae48a214318f84db0000000110616374697665526f796c6c6f436f696e0e5573656420627920526f796c6c6f00000001000201000303fd038706ad01ab0065d7bd7a50f78b9e6a0621c34175bc390b091070e3532908660ad19ad2061cb160000000011781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413024e9d77ff1df871af183419a6cfd308235f512717f13da57dbf045a4a8c2ca5cc014201402b9b32468fc0a3e8809763fdd191a720aba0f6fc0e46642e192484114cf1b002ac82e56cb4e8aaf8fcd9c7742e2b4fa6c195af91cedaa1a670ab46cb4a3811920728b48931d124c3c1911c5671ffabc9477c82b62cd62d3920d2cadd5878d31f882100000000000003eb08020000092102b0664f13bfc71ddcb8e0eb004b5b486cda6d0dfd33340b85d43f618b484e151708020000092102576e6cf95f0d7724f2e17afcd74a690231bf4e1ecb1963229af3fe33edcd58ca059f0004000000010121030411db4d023a8d55607fedc562d519b1854af7752d9e1f00279213380d439e880274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff06c901c7000400000000012102d180fa5fde1f070a9df166d1cc4c0ec8fd3ccd57da8744a4fed8a1a1578cf1ae029c007100010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e5413024a00019fe1a55b2c9813569b7c7a3bf514bafc0cf43748d646a2bf39848870b06677a70000000000000387ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffbf012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff079f000400000000012102d180fa5fde1f070a9df166d1cc4c0ec8fd3ccd57da8744a4fed8a1a1578cf1ae0274004900010001201781a8879353ab2f8bb70dcf96f5b0ff620a987cf1044b924d6e3c382e1e541302220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff012700010001220000ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe362126b922539fc3451739b11745035fcc950ee86d0edace720399be6951c7e";

    /* ============================================================================================================== */

    /** Contains all assets and contains decoded proofs (request and response). */
    private static final Map<String, AssetValue> ASSETS = new LinkedHashMap<>();

    static {
        try {
            // TODO find files automatically instead of specifying them manually.

            // =========================================================================================================
            // RoylloCoin.
            AssetValue roylloCoin = new AssetValue();
            // Decoded proof 1.
            roylloCoin.addDecodedProofValue(new DecodedProofValue(
                    getDecodedProofRequestFromFile("/tapd/roylloCoin/decodeProof-proofFile1-proofAtDepth0-request.json"),
                    getDecodedProofResponseFromFile("/tapd/roylloCoin/decodeProof-proofFile1-proofAtDepth0-response.json")));
            // Adding the asset
            ASSETS.put(roylloCoin.getAssetId(), roylloCoin);

            // =========================================================================================================
            // UnknownRoylloCoin.
            AssetValue unknownRoylloCoin = new AssetValue();
            // Decoded proof 1.
            unknownRoylloCoin.addDecodedProofValue(new DecodedProofValue(
                    getDecodedProofRequestFromFile("/tapd/unknownRoylloCoin/decodeProof-proofFile1-proofAtDepth0-request.json"),
                    getDecodedProofResponseFromFile("/tapd/unknownRoylloCoin/decodeProof-proofFile1-proofAtDepth0-response.json")));
            // Adding the asset
            ASSETS.put(unknownRoylloCoin.getAssetId(), unknownRoylloCoin);

            // =========================================================================================================
            // TestCoin.
            AssetValue testCoin = new AssetValue();
            testCoin.addDecodedProofValue(new DecodedProofValue(
                    getDecodedProofRequestFromFile("/tapd/testCoin/decodeProof-proofFile1-proofAtDepth0-request.json"),
                    getDecodedProofResponseFromFile("/tapd/testCoin/decodeProof-proofFile1-proofAtDepth0-response.json")));
            testCoin.addDecodedProofValue(new DecodedProofValue(
                    getDecodedProofRequestFromFile("/tapd/testCoin/decodeProof-proofFile2-proofAtDepth0-request.json"),
                    getDecodedProofResponseFromFile("/tapd/testCoin/decodeProof-proofFile2-proofAtDepth0-response.json")));
            testCoin.addDecodedProofValue(new DecodedProofValue(
                    getDecodedProofRequestFromFile("/tapd/testCoin/decodeProof-proofFile2-proofAtDepth1-request.json"),
                    getDecodedProofResponseFromFile("/tapd/testCoin/decodeProof-proofFile2-proofAtDepth1-response.json")));
            testCoin.addDecodedProofValue(new DecodedProofValue(
                    getDecodedProofRequestFromFile("/tapd/testCoin/decodeProof-proofFile3-proofAtDepth0-request.json"),
                    getDecodedProofResponseFromFile("/tapd/testCoin/decodeProof-proofFile3-proofAtDepth0-response.json")));
            testCoin.addDecodedProofValue(new DecodedProofValue(
                    getDecodedProofRequestFromFile("/tapd/testCoin/decodeProof-proofFile3-proofAtDepth1-request.json"),
                    getDecodedProofResponseFromFile("/tapd/testCoin/decodeProof-proofFile3-proofAtDepth1-response.json")));
            ASSETS.put(testCoin.getAssetId(), testCoin);

        } catch (IOException e) {
            throw new RuntimeException("TestAssets loading error: " + e);
        }
    }

    /**
     * Returns an asset value by asset id.
     *
     * @param assetId asset id
     * @return asset value
     */
    public static AssetValue findAssetValueByAssetId(final String assetId) {
        return ASSETS.get(assetId);
    }

    /**
     * Returns an asset state by asset state id.
     *
     * @param assetStateId asset state id
     * @return asset state
     */
    public static Optional<DecodedProofValueResponse.DecodedProof> findAssetStateByAssetStateId(final String assetStateId) {
        return ASSETS.values().stream()
                .flatMap(assetValue -> assetValue.getDecodedProofValues().stream())
                .filter(decodedProofValue -> decodedProofValue.getAssetStateId().equals(assetStateId))
                .map(DecodedProofValue::getResponse)
                .map(DecodedProofValueResponse::getDecodedProof)
                .findFirst();
    }

    /**
     * Set mock server rules.
     *
     * @param mockServer mock server
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    public static void setMockServerRules(final ClientAndServer mockServer) {
        // Adding all assets to the mock server.
        for (Map.Entry<String, AssetValue> entry : ASSETS.entrySet()) {
            // For each decoded proof values, we mock the request.
            AtomicInteger i = new AtomicInteger(0);
            entry.getValue().getDecodedProofValues().forEach(decodedProofValue -> {
                int index = i.getAndIncrement();
                mockServer.when(request().withBody(
                                JsonBody.json(
                                        "{"
                                                + "\"raw_proof\" : \"" + entry.getValue().getDecodedProofValues().get(index).getRequest().getRawProof() + "\","
                                                + "\"proof_at_depth\" : " + entry.getValue().getDecodedProofValues().get(index).getRequest().getProofAtDepth()
                                                + "}",
                                        MatchType.ONLY_MATCHING_FIELDS
                                )
                        ))
                        .respond(response().withStatusCode(200)
                                .withContentType(APPLICATION_JSON)
                                .withBody(entry.getValue().getDecodedProofValues().get(index).getJSONResponse()));
            });
        }
    }

    /**
     * Returns a proof request loaded from a file.
     *
     * @param filePath file path
     * @return proof request
     * @throws IOException file not found
     */
    private static DecodedProofValueRequest getDecodedProofRequestFromFile(final String filePath) throws IOException {
        InputStream inputStream = TestAssets.class.getResourceAsStream(filePath);
        return new ObjectMapper().readValue(inputStream, DecodedProofValueRequest.class);
    }

    /**
     * Returns a decoded proof response loaded from a file.
     *
     * @param filePath file path
     * @return decoded proof
     * @throws IOException file not found
     */
    private static DecodedProofValueResponse getDecodedProofResponseFromFile(final String filePath) throws IOException {
        InputStream inputStream = TestAssets.class.getResourceAsStream(filePath);
        return new ObjectMapper().readValue(inputStream, DecodedProofValueResponse.class);
    }

}
