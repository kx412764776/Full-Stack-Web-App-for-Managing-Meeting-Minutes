package com.apprenticeship.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * This class is responsible for storing files information
 */
@Entity
@Table(
        name = "fileStorage",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "file_storage_id_unique",
                        columnNames = "fileId"
                )
        }
)
public class FileStorage {

    @Id
    @SequenceGenerator(
            name = "file_id_seq",
            sequenceName = "file_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "file_id_seq"
    )
    private Integer fileId;

    @ManyToOne
    @JoinColumn(
            name = "meetingId",
            referencedColumnName = "meetingId",
            foreignKey = @ForeignKey(
                    name = "meeting_file_fk"
            ),
            nullable = false
    )
    private MeetingTable meetingId;

    @Column(
            nullable = false
    )
    private String fileName;

    @Column(
            nullable = false
    )
    private String fileType;

    @Column(
            nullable = false
    )
    private String fileUrl;

    private LocalDateTime uploadDateTime;

    public FileStorage() {
    }

    public FileStorage(MeetingTable meetingId,
                       String fileName,
                       String fileType,
                       String fileUrl,
                       LocalDateTime uploadDateTime) {
        this.meetingId = meetingId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileUrl = fileUrl;
        this.uploadDateTime = uploadDateTime;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public MeetingTable getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(MeetingTable meetingId) {
        this.meetingId = meetingId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDateTime getUploadDateTime() {
        return uploadDateTime;
    }

    public void setUploadDateTime(LocalDateTime uploadDateTime) {
        this.uploadDateTime = uploadDateTime;
    }
}
