package com.apprenticeship.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class SignatureInfoTest {

    private SignatureInfo signatureInfo;

    @BeforeEach
    void setUp() {
        signatureInfo = new SignatureInfo();
    }

    @Test
    void testConstructorAndGetter() {
        Date date = new Date();
        signatureInfo = new SignatureInfo(
                1,
                new MinutesTable(),
                new Member(),
                1,
                date
        );

        assertThat(signatureInfo).isNotNull();
        assertThat(signatureInfo.getSignatureId()).isEqualTo(1);
        assertThat(signatureInfo.getMinutesId()).isNotNull();
        assertThat(signatureInfo.getMemberId()).isNotNull();
        assertThat(signatureInfo.getIsSigned()).isEqualTo(1);
        assertThat(signatureInfo.getSignedDate()).isEqualTo(date);

        SignatureInfo signatureInfo2 = new SignatureInfo(
                new MinutesTable(),
                new Member(),
                1,
                date
        );

        assertThat(signatureInfo2).isNotNull();
        assertThat(signatureInfo2.getMinutesId()).isNotNull();
        assertThat(signatureInfo2.getMemberId()).isNotNull();
        assertThat(signatureInfo2.getIsSigned()).isEqualTo(1);
        assertThat(signatureInfo2.getSignedDate()).isEqualTo(date);

    }

    @Test
    void testSetters() {
        Date date = new Date();
        signatureInfo.setSignatureId(1);
        signatureInfo.setMinutesId(new MinutesTable());
        signatureInfo.setMemberId(new Member());
        signatureInfo.setIsSigned(1);
        signatureInfo.setSignedDate(date);

        assertThat(signatureInfo).isNotNull();
        assertThat(signatureInfo.getSignatureId()).isEqualTo(1);
        assertThat(signatureInfo.getMinutesId()).isNotNull();
        assertThat(signatureInfo.getMemberId()).isNotNull();
        assertThat(signatureInfo.getIsSigned()).isEqualTo(1);
        assertThat(signatureInfo.getSignedDate()).isEqualTo(date);
    }


}