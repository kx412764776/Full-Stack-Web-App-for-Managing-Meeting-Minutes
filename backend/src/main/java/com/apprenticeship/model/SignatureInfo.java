package com.apprenticeship.model;

import jakarta.persistence.*;

import java.util.Date;

/**
 * This class is used to store if member signed the minutes or not.
 */

@Entity
@Table(
        name = "signatureInfo",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "signature_id_unique",
                        columnNames = "signatureId"
                )
        }
)
public class SignatureInfo {

    @Id
    @SequenceGenerator(
            name = "signature_id_seq",
            sequenceName = "signature_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "signature_id_seq"
    )
    private Integer signatureId;

    // foreign key to minutes table (minutesId)
    @ManyToOne
    @JoinColumn(
            name = "minutesId",
            referencedColumnName = "minutesId",
            foreignKey = @ForeignKey(
                    name = "minutesId_fk"
            ),
            nullable = false
    )
    private MinutesTable minutesId;

    // foreign key to member table (memberId)
    @ManyToOne
    @JoinColumn(
            name = "memberId",
            referencedColumnName = "memberId",
            foreignKey = @ForeignKey(
                    name = "memberId_fk"
            ),
            nullable = false
    )
    private Member memberId;

    // 0: not signed, 1: signed
    @Column(
            name = "isSigned",
            nullable = false
    )
    private Integer isSigned;

    private Date signedDate;

    public SignatureInfo() {
    }

    public SignatureInfo(Integer signatureId,
                         MinutesTable minutesId,
                         Member memberId,
                         Integer isSigned,
                         Date signedDate
    ) {
        this.signatureId = signatureId;
        this.minutesId = minutesId;
        this.memberId = memberId;
        this.isSigned = isSigned;
        this.signedDate = signedDate;
    }

    public SignatureInfo(MinutesTable minutesId, Member memberId, Integer isSigned, Date signedDate) {
        this.minutesId = minutesId;
        this.memberId = memberId;
        this.isSigned = isSigned;
        this.signedDate = signedDate;
    }

    public Integer getSignatureId() {
        return signatureId;
    }

    public void setSignatureId(Integer signatureId) {
        this.signatureId = signatureId;
    }

    public MinutesTable getMinutesId() {
        return minutesId;
    }

    public void setMinutesId(MinutesTable minutesId) {
        this.minutesId = minutesId;
    }

    public Member getMemberId() {
        return memberId;
    }

    public void setMemberId(Member memberId) {
        this.memberId = memberId;
    }

    public Integer getIsSigned() {
        return isSigned;
    }

    public void setIsSigned(Integer isSigned) {
        this.isSigned = isSigned;
    }

    public Date getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(Date signedDate) {
        this.signedDate = signedDate;
    }
}
