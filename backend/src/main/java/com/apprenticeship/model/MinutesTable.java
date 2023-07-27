package com.apprenticeship.model;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * This class is a model for the Minutes table to store meeting minutes in the database.
 */
@Entity
@Table(
        name = "minutesTable",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "minutes_table_id_unique",
                        columnNames = "minutesId"
                )
        }
)
public class MinutesTable {

    @Id
    @SequenceGenerator(
            name = "minutes_table_id_seq",
            sequenceName = "minutes_table_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "minutes_table_id_seq"
    )
    private Integer minutesId;

    // foreign key to the meeting table
    @JoinColumn(
            name = "meetingId",
            referencedColumnName = "meetingId",
            foreignKey = @ForeignKey(
                    name = "meetingId_fk"
            ),
            nullable = false
    )
    @OneToOne
    private MeetingTable meetingId;

    @Column(
            nullable = false
    )
    private String minutesFilename;

    // file path to the minutes file
    @Column(
            nullable = false
    )
    private String minutesFilepath;

    public MinutesTable() {
    }

    public MinutesTable(Integer minutesId,
                        MeetingTable meetingId,
                        String minutesFilename,
                        String minutesFilepath) {
        this.minutesId = minutesId;
        this.meetingId = meetingId;
        this.minutesFilename = minutesFilename;
        this.minutesFilepath = minutesFilepath;
    }

    public MinutesTable(MeetingTable meetingId,
                        String minutesFilename,
                        String minutesFilepath) {
        this.meetingId = meetingId;
        this.minutesFilename = minutesFilename;
        this.minutesFilepath = minutesFilepath;
    }

    public Integer getMinutesId() {
        return minutesId;
    }

    public void setMinutesId(Integer minutesId) {
        this.minutesId = minutesId;
    }

    public MeetingTable getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(MeetingTable meetingId) {
        this.meetingId = meetingId;
    }

    public String getMinutesFilename() {
        return minutesFilename;
    }

    public void setMinutesFilename(String minutesFilename) {
        this.minutesFilename = minutesFilename;
    }

    public String getMinutesFilepath() {
        return minutesFilepath;
    }

    public void setMinutesFilepath(String minutesFilepath) {
        this.minutesFilepath = minutesFilepath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinutesTable that = (MinutesTable) o;
        return Objects.equals(minutesId, that.minutesId) && Objects.equals(meetingId, that.meetingId) && Objects.equals(minutesFilename, that.minutesFilename) && Objects.equals(minutesFilepath, that.minutesFilepath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minutesId, meetingId, minutesFilename, minutesFilepath);
    }

    @Override
    public String toString() {
        return "MinutesTable{" +
               "minutesId=" + minutesId +
               ", meetingId=" + meetingId +
               ", minutesFilename='" + minutesFilename + '\'' +
               ", minutesFilepath='" + minutesFilepath + '\'' +
               '}';
    }
}
