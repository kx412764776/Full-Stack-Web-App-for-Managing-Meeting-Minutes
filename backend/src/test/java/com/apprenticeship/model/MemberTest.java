package com.apprenticeship.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member(
                1, // memberId
                "John", // firstName
                "Alex", // lastName
                "john@example.com", // email
                "password", // password
                "APPRENTICE" // memberRole
        );
    }

    @Test
    void testConstructorAndGetters() {
        assertNotNull(member);
        assertThat(member.getMemberId()).isEqualTo(1);
        assertThat(member.getFirstName()).isEqualTo("John");
        assertThat(member.getLastName()).isEqualTo("Alex");
        assertThat(member.getEmail()).isEqualTo("john@example.com");
        assertThat(member.getPassword()).isEqualTo("password");
        assertThat(member.getMemberRole()).isEqualTo("APPRENTICE");

        Member member2 = new Member(
                "Qizhao",
                "Chen",
                "qizhao@example.com",
                "password",
                "APPRENTICE"
        );
        assertNotNull(member2);
        assertThat(member2.getFirstName()).isEqualTo("Qizhao");
        assertThat(member2.getLastName()).isEqualTo("Chen");
        assertThat(member2.getEmail()).isEqualTo("qizhao@example.com");
        assertThat(member2.getPassword()).isEqualTo("password");
        assertThat(member2.getMemberRole()).isEqualTo("APPRENTICE");

    }

    @Test
    void testSetters() {
        member.setMemberId(2);
        member.setFirstName("Jane");
        member.setLastName("Smith");
        member.setEmail("jane@example.com");
        member.setPassword("password");
        member.setMemberRole("MENTOR");

        assertThat(member.getMemberId()).isEqualTo(2);
        assertThat(member.getFirstName()).isEqualTo("Jane");
        assertThat(member.getLastName()).isEqualTo("Smith");
        assertThat(member.getEmail()).isEqualTo("jane@example.com");
        assertThat(member.getPassword()).isEqualTo("password");
        assertThat(member.getMemberRole()).isEqualTo("MENTOR");
    }

    @Test
    void testGetAuthorities() {
        // test that the authorities are set correctly
        Collection<? extends GrantedAuthority> authorities = member.getAuthorities();
        // test that the authorities are not null
        assertThat(authorities).isNotNull();
        // test that the authorities are not empty
        assertThat(authorities.size()).isEqualTo(1);
        // test that the authorities contain the correct role
        assertThat(authorities.iterator().next().getAuthority()).isEqualTo("APPRENTICE");
    }

    @Test
    void testUsernameAndPassword() {
        assertThat(member.getUsername()).isEqualTo("john@example.com");
        assertThat(member.getPassword()).isEqualTo("password");
    }

    @Test
    void testAccountNonExpired() {
        assertThat(member.isAccountNonExpired()).isTrue();
    }

    @Test
    void testAccountNonLocked() {
        assertThat(member.isAccountNonLocked()).isTrue();
    }

    @Test
    void testCredentialsNonExpired() {
        assertThat(member.isCredentialsNonExpired()).isTrue();
    }

    @Test
    void testIsEnabled() {
        assertThat(member.isEnabled()).isTrue();
    }

    @Test
    void testEqualsAndHashCode() {
        Member sameMember = new Member(
                1,
                "John",
                "Alex",
                "john@example.com",
                "password",
                "APPRENTICE"
        );
        Member differentMember = new Member(
                2,
                "Jane",
                "Smith",
                "jane@example.com",
                "password",
                "MENTOR"
        );

        assertThat(member).isEqualTo(sameMember);
        assertThat(member).isNotEqualTo(differentMember);

        assertThat(member.hashCode()).isEqualTo(sameMember.hashCode());
    }

    @Test
    void testToString() {
        String expectedToString = "Member{" +
                                  "memberId=1" +
                                  ", firstName='John'" +
                                  ", lastName='Alex'" +
                                  ", email='john@example.com'" +
                                  ", password='password'" +
                                  ", memberRole='APPRENTICE'" +
                                  '}';
        assertThat(member.toString()).isEqualTo(expectedToString);
    }
}