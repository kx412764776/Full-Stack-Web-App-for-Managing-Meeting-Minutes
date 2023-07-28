package com.apprenticeship.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
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
public class Member implements UserDetails {

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
    private String email;

    @Column(
            nullable = false
    )
    private String password;

    @Column(
            nullable = false
    )
    private String memberRole;

    public Member(Integer memberId,
                  String firstName,
                  String lastName,
                  String email,
                  String password,
                  String memberRole) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.memberRole = memberRole;
    }

    public Member(String firstName,
                  String lastName,
                  String email,
                  String password,
                  String memberRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.memberRole = memberRole;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(memberRole));
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(String memberRole) {
        this.memberRole = memberRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(memberId, member.memberId) && Objects.equals(firstName, member.firstName) && Objects.equals(lastName, member.lastName) && Objects.equals(email, member.email) && Objects.equals(password, member.password) && Objects.equals(memberRole, member.memberRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, firstName, lastName, email, password, memberRole);
    }

    @Override
    public String toString() {
        return "Member{" +
               "memberId=" + memberId +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", email='" + email + '\'' +
               ", password='" + password + '\'' +
               ", memberRole='" + memberRole + '\'' +
               '}';
    }
}
