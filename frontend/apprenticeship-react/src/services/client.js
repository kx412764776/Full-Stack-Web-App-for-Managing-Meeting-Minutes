import axios from 'axios';

const getAuthConfig = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem("access_token")}`
    }
})

export const login = async (usernameAndPassword) => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/login`,
            usernameAndPassword
        );
    } catch (error) {
        console.log(error);
    }
}

export const register = async (member) => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/register`,
            member
        );
    } catch (error) {
        console.log(error);
    }
}

export const getMemberInfo = async (memberEmail) => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/member/memberInfo/${memberEmail}`,
            memberEmail,
            getAuthConfig()
        )
    } catch (error) {
        console.log(error);
    }
}

export const getAllMemberInfo = async () => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/member/memberInfo`,
        )
    } catch (error) {
        console.log(error);
    }
}

export const checkMemberByEmail = async (memberEmailPrefix) => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/member/checkMember/${memberEmailPrefix}`,
            memberEmailPrefix,
            getAuthConfig()
        )
    } catch (error) {
        console.log(error);
    }
}

export const getMeetingInfo = async () => {
    try {
        return await axios.get(
            `http://localhost:8090/apprenticeship/meeting/meetingInfoList`,
            getAuthConfig()
        )
    } catch (error) {
        console.log(error);
    }
}

export const getMeetingInfoByMeetingId = async (meetingId) => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/meeting/meetingInfo/${meetingId}`,
            meetingId,
            getAuthConfig()
        )
    } catch (error) {
        console.log(error);
    }
}

export const getMeetingInfoByEmail = async (memberEmail) => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/meeting/${memberEmail}`,
            memberEmail,
            getAuthConfig()
        )
    } catch (error) {
        console.log(error);
    }
}

export const saveMeeting = async (meeting) => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/meeting`,
            meeting,
            getAuthConfig()
        );
    } catch (error) {
        console.log(error);
    }
}

export const updateMeetingByMeetingId = async (meetingId, updatedMeetingInfo) => {
    try {
        return await axios.put(
            `http://localhost:8090/apprenticeship/meeting/meetingInfo/${meetingId}`,
            updatedMeetingInfo,
            getAuthConfig()
        )
    } catch (error) {
        console.log(error);
    }
}

export const deleteMeetingByMeetingId = async (meetingId) => {
    try {
        return await axios.delete(
            `http://localhost:8090/apprenticeship/meeting/meetingInfo/${meetingId}`,
            getAuthConfig()
        )
    } catch (error) {
        console.log(error);
    }
}

export const addParticipantAndMeetingToAttendeeTable = async (emails, meetingId) => {
    const insertAttendee = {
        emails: emails,
        meetingId: meetingId
    };
    try {
        return await axios.post(
            'http://localhost:8090/apprenticeship/meeting/attendee',
            insertAttendee,
            getAuthConfig()
        );
    } catch (error) {
        console.log(error);
    }
}

export const getAttendeeInfoByMeetingId = async (meetingId) => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/meeting/attendee/${meetingId}`,
            meetingId,
            getAuthConfig()
        )
    } catch (error) {
        console.log(error);
    }
}

export const deleteAttendeeInfoByMeetingIdAndEmail = async (meetingId, memberEmails) => {
    try {
        return await axios.delete(
            `http://localhost:8090/apprenticeship/meeting/attendee/${meetingId}/${memberEmails}`,
            getAuthConfig()
        )
    } catch (error) {
        console.log(error);
    }
}

export const insertOrUpdateMinutesByMeetingId = async (meetingId, minutesContent) => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/minutes/${meetingId}`,
            {meetingId, minutesContent},
            {
                ...getAuthConfig(),
                'Content-Type': 'application/json'
            }
        )
    } catch (error) {
        console.log(error);
    }
}

export const getMinutesByMeetingId = async (meetingId) => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/minutes/meetingId/${meetingId}`,
            meetingId,
            getAuthConfig()
        )
    } catch (error) {
        console.log(error);
    }
}

export const sendEmail = async (meetingId, memberEmails) => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/notification/sendEmail`,
            {meetingId, memberEmails},
            getAuthConfig()
        )
    } catch (error) {
        console.log(error);
    }
}

export const updateSignature = async (meetingId, memberEmail) => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/minutes/signature`,
            {meetingId, memberEmail},
            getAuthConfig()
        )
    } catch (error) {
        console.log(error);
    }
}

export const checkIfSignatureExists = async (meetingId, memberEmail) => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/minutes/signature/status`,
            {meetingId, memberEmail},
            getAuthConfig()
        )
    } catch (error) {
        console.log(error);
    }
}

export const getFullSignedMeetingList = async () => {
    try {
        return await axios.get(
            `http://localhost:8090/apprenticeship/minutes/signature/signed`,
            getAuthConfig()
        )
    } catch (error) {
        console.log(error);
    }
}

export const getNotFullSignedMeetingList = async () => {
    try {
        return await axios.get(
            `http://localhost:8090/apprenticeship/minutes/signature/notFullSigned`,
            getAuthConfig()
        )
    } catch (error) {
        console.log(error);
    }
}

export const getSignatureTableByMeetingId = async (meetingId) => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/minutes/signature/${meetingId}`,
            meetingId,
            getAuthConfig()
        )
    } catch (error) {
        console.log(error);
    }
}

export const getSignedMeetingListByMemberEmail = async (memberEmail) => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/minutes/signature/signed/${memberEmail}`,
            memberEmail,
            getAuthConfig()
        )
    } catch (error) {
        console.log(error);
    }
}

export const getNotSignedMeetingListByMemberEmail = async (memberEmail) => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/minutes/signature/notSigned/${memberEmail}`,
            memberEmail,
            getAuthConfig()
        )
    } catch (error) {
        console.log(error);
    }
}
