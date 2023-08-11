package com.apprenticeship.model;

import jakarta.persistence.*;


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

    // Store the minutes file in the database
    @Column(
            columnDefinition = "TEXT"
    )
    private String minutesContent;

    public MinutesTable() {
    }

    public MinutesTable(Integer minutesId,
                        MeetingTable meetingId,
                        String minutesFilename,
                        String minutesContent) {
        this.minutesId = minutesId;
        this.meetingId = meetingId;
        this.minutesFilename = minutesFilename;
        this.minutesContent = minutesContent;
    }



    public MinutesTable(MeetingTable meetingId,
                        String minutesFilename,
                        String minutesContent) {
        this.meetingId = meetingId;
        this.minutesFilename = minutesFilename;
        this.minutesContent = minutesContent;
    }

    public String getMinutesContent() {
        return minutesContent;
    }

    public void setMinutesContent(String minutesContent) {
        this.minutesContent = minutesContent;
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


}
