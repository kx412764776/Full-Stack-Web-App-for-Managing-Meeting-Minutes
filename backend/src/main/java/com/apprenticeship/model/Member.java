package com.apprenticeship.model;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * This class is a model for the Member table in the database.
 */
@Entity
@Table(
        name = "member",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "member_email_unique",
                        columnNames = "email"
                ),
                @UniqueConstraint(
                        name = "member_id_unique",
                        columnNames = "memberId"
                )
        })
public class Member {

    @Id
    @SequenceGenerator(
            name = "member_id_seq",
            sequenceName = "member_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "member_id_seq"
    )
    private Integer memberId;

    @Column(
            nullable = false
    )
    private String firstName;

    @Column(
            nullable = false
    )
    private String lastName;

    @Column(
            nullable = false
    )
    private String username;

    @Column(
            nullable = false
    )
    private String email;

    @Column(
            nullable = false
    )
    private String password;

    public Member(Integer memberId,
                  String firstName,
                  String lastName,
                  String username,
                  String email,
                  String password) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Member() {

    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer id) {
        this.memberId = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(memberId, member.memberId) && Objects.equals(firstName, member.firstName) && Objects.equals(lastName, member.lastName) && Objects.equals(username, member.username) && Objects.equals(email, member.email) && Objects.equals(password, member.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, firstName, lastName, username, email, password);
    }

    @Override
    public String toString() {
        return "Member{" +
               "memberId=" + memberId +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", username='" + username + '\'' +
               ", email='" + email + '\'' +
               ", password='" + password + '\'' +
               '}';
    }
}
