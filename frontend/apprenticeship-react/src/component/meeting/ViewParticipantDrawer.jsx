import {useEffect, useState} from "react";
import {Button, Checkbox, Divider, message, Space} from "antd";
import {deleteAttendeeInfoByMeetingIdAndEmail, getAttendeeInfoByMeetingId} from "../../services/client.js";

const ViewParticipantDrawer = ({selectedMeetingId, onClose}) => {

    const [participantList, setParticipantList] = useState([]);
    const [selectedParticipant, setSelectedParticipant] = useState([]);
    const [selectAll, setSelectAll] = useState(false);

    const handleRemoveParticipant = async () => {
        // Remove selected participant from the meeting
        try {
            await deleteAttendeeInfoByMeetingIdAndEmail(selectedMeetingId, selectedParticipant);
            setSelectedParticipant([]);
            setSelectAll(false);
            message.success("Participant with email " + selectedParticipant + " removed successfully");
            await getParticipantList();
        } catch (error) {
            console.log(error);
            message.error("Error removing participant with email " + selectedParticipant);
        } finally {
            onClose();
        }

    }

    const getParticipantList = async () => {

        try {
            // Fetch attendee list for the selected meeting here using getAttendeeInfoByMeetingId
            const response = await getAttendeeInfoByMeetingId(selectedMeetingId);
            const participantList = response.data.map((attendee) => ({
                email: attendee.split(",")[0],
                nameWithRole: attendee.split(",")[1],
            }));
            setParticipantList(participantList);
        } catch (error) {
            console.log(error);
        }
    };

    useEffect(() => {
        getParticipantList();
    }, [selectedMeetingId]);

    const handleSelectAll = (e) => {
        const checked = e.target.checked;
        setSelectAll(checked);
        if (checked) {
            const allEmails = participantList.map((attendee) => attendee.email);
            console.log(allEmails);
            setSelectedParticipant(allEmails);
        } else {
            setSelectedParticipant([]);
        }
    };

    return (
        <>
            <Checkbox
                style={{ marginBottom: 10 }}
                checked={selectAll}
                onChange={handleSelectAll}
            >
                Select All
            </Checkbox>
            <Divider />
            <Checkbox.Group
                style={{ display: "block" }}
                options={participantList.map((attendee) => ({
                    label: attendee.email + " " + attendee.nameWithRole,
                    value: attendee.email,
                }))}
                value={selectedParticipant}
                onChange={(email) => setSelectedParticipant(email)}
            />
            <Space style={{ position: "absolute", bottom: 20, left: 10 }}>
                <Button
                    type="primary"
                    danger
                    onClick={handleRemoveParticipant}
                >
                    Remove Participant
                </Button>
                <Button
                    onClick={onClose}
                >
                    Cancel
                </Button>
            </Space>
        </>
    )
}

export default ViewParticipantDrawer;