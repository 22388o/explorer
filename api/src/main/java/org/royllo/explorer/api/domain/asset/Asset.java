package org.royllo.explorer.api.domain.asset;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.royllo.explorer.api.domain.bitcoin.BitcoinTransactionOutput;
import org.royllo.explorer.api.domain.user.User;
import org.royllo.explorer.api.util.base.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * Taro asset.
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "ASSETS")
public class Asset extends BaseDomain {

    /** Unique identifier. */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /** The first outpoint of the transaction that created the asset (txid:vout). */
    @NotNull(message = "Bitcoin transaction output is required")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_BITCOIN_TRANSACTION_OUTPUT_GENESIS_POINT", updatable = false)
    private BitcoinTransactionOutput genesisPoint;

    /** Asset creator. */
    @NotNull(message = "Asset creator is required")
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_USER_CREATOR", nullable = false)
    private User creator;

    /** The name of the asset. */
    @NotBlank(message = "Asset name is required")
    @Column(name = "NAME", updatable = false)
    private String name;

    /** The hashed metadata of the asset. */
    @NotBlank(message = "Hashed metadata is required")
    @Column(name = "META", updatable = false)
    private String meta;

    /** The asset ID that uniquely identifies the asset. */
    @NotBlank(message = "Asset ID is required")
    @Column(name = "ASSET_ID", updatable = false)
    private String assetId;

    /** The index of the output that carries the unique Taro commitment in the genesis transaction. */
    @NotBlank(message = "Output index is required")
    @Column(name = "OUTPUT_INDEX", updatable = false)
    private int outputIndex;

}
