package com.apprenticeship.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(
        name = "member",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "member_email_unique",
                        columnNames = "email"
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
    private String name;

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
                  String name,
                  String username,
                  String email,
                  String password
    ) {
        this.memberId = memberId;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        Member user = (Member) o;
        return Objects.equals(memberId, user.memberId) && Objects.equals(name, user.name) && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, name, username, email, password);
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" + memberId +
               ", name='" + name + '\'' +
               ", username='" + username + '\'' +
               ", email='" + email + '\'' +
               ", password='" + password + '\'' +
               '}';
    }
}
