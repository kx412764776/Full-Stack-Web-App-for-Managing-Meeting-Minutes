package com.apprenticeship.model;

import jakarta.persistence.*;

import java.util.Date;

/**
 * This class is a model for the Feedback table in the database.
 */

@Entity
@Table(
        name = "feedback",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "feedback_id_unique",
                        columnNames = "feedbackId"
                )
        }
)
public class Feedback {

    @Id
    @SequenceGenerator(
            name = "feedback_id_seq",
            sequenceName = "feedback_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "feedback_id_seq"
    )
    private Integer feedbackId;

    // foreign key to the member
    @ManyToOne
    @JoinColumn(
            name = "memberId",
            referencedColumnName = "memberId",
            foreignKey = @ForeignKey(
                    name = "feedback_memberId_fk"
            ),
            nullable = false
    )
    private Member memberId;
    private Date feedbackDate;
    private String feedbackContent;

    public Feedback() {
    }

    public Feedback(Integer feedbackId, Member memberId, Date feedbackDate, String feedbackContent) {
        this.feedbackId = feedbackId;
        this.memberId = memberId;
        this.feedbackDate = feedbackDate;
        this.feedbackContent = feedbackContent;
    }

    public Feedback(Member memberId, Date feedbackDate, String feedbackContent) {
        this.memberId = memberId;
        this.feedbackDate = feedbackDate;
        this.feedbackContent = feedbackContent;
    }

    public Integer getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Member getMemberId() {
        return memberId;
    }

    public void setMemberId(Member memberId) {
        this.memberId = memberId;
    }

    public Date getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(Date feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }
}
