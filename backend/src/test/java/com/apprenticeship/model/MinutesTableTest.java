package com.apprenticeship.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MinutesTableTest {

    private MinutesTable minutesTable;
    @BeforeEach
    void setUp() {
        minutesTable = new MinutesTable();
    }

    @Test
    void testConstructorAndGetter() {
        minutesTable = new MinutesTable(
                1,
                new MeetingTable(),
                "minutesFilename",
                "minutesContent"
        );

        assertThat(minutesTable).isNotNull();
        assertThat(minutesTable.getMinutesFilename()).isEqualTo("minutesFilename");
        assertThat(minutesTable.getMinutesContent()).isEqualTo("minutesContent");

        MinutesTable minutesTable2 = new MinutesTable(
                new MeetingTable(),
                "minutesFilename",
                "minutesContent"
        );

        assertThat(minutesTable2).isNotNull();
        assertThat(minutesTable2.getMinutesFilename()).isEqualTo("minutesFilename");
        assertThat(minutesTable2.getMinutesContent()).isEqualTo("minutesContent");
    }

    @Test
    void testSetters() {
        minutesTable.setMinutesId(1);
        minutesTable.setMinutesFilename("minutesFilename");
        minutesTable.setMinutesContent("minutesContent");

        assertThat(minutesTable).isNotNull();
        assertThat(minutesTable.getMinutesId()).isEqualTo(1);
        assertThat(minutesTable.getMinutesFilename()).isEqualTo("minutesFilename");
        assertThat(minutesTable.getMinutesContent()).isEqualTo("minutesContent");
    }


}