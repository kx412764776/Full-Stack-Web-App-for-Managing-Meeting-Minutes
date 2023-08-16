package com.apprenticeship.repository;

import com.apprenticeship.model.MeetingTable;
import com.apprenticeship.model.Member;
import com.apprenticeship.model.MinutesTable;
import com.apprenticeship.model.SignatureInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("signatureJpaRepository")
public interface SignatureRepository extends JpaRepository<SignatureInfo, Integer> {

    boolean existsByMemberIdAndMinutesId(Member memberId, MinutesTable minutesId);

    boolean existsByMemberIdAndMinutesId_MeetingId(Member memberId, MeetingTable meetingId);

    Optional<List<SignatureInfo>> findAllByMinutesId_MeetingId(MeetingTable meetingId);

    SignatureInfo findByMinutesId_MeetingIdAndMemberId_MemberId(MeetingTable minutesId_meetingId, Integer memberId);
}
