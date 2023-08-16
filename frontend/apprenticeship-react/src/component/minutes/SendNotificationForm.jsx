import {getAttendeeInfoByMeetingId, sendEmail} from "../../services/client.js";
import {useEffect, useState} from "react";
import {Button, Checkbox, Divider, message, Space} from "antd";

const SendNotificationForm = ({selectedMeetingId, onClose}) => {

    const [participantList, setParticipantList] = useState([]);
    const [selectedParticipant, setSelectedParticipant] = useState([]);
    const indeterminate = selectedParticipant.length > 0 && selectedParticipant.length < participantList.length;
    const selectAll = selectedParticipant.length === participantList.length;

    const handleSelectParticipant = (emails) => {
        setSelectedParticipant(emails);
    }

    const handleSelectAll = (e) => {
        setSelectedParticipant(e.target.checked ? participantList.map((attendee) => attendee.email) : []);
    };

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

    const handleSendNotification = async () => {
        try {
            await sendEmail(selectedMeetingId, selectedParticipant);
            onClose();
            message.success("Notification email sent to " + selectedParticipant.join(", "), 5);
        } catch (error) {
            message.error("Error sending notification email to " + selectedParticipant.join(", "), 5);
        }
    }

    useEffect(() => {
        getParticipantList();
    }, [selectedMeetingId]);

    return (
        <>
            <Checkbox
                style={{marginBottom: 10}}
                checked={selectAll}
                onChange={handleSelectAll}
                indeterminate={indeterminate}
            >
                Select All
            </Checkbox>
            <Divider/>
            <Checkbox.Group
                style={{display: "block", whiteSpace: "normal"}}
                options={participantList.map((attendee) => ({
                    label: attendee.email + ", " + attendee.nameWithRole,
                    value: attendee.email,
                }))}
                value={selectedParticipant}
                onChange={handleSelectParticipant}
            />
            <Space style={{position: "absolute", bottom: 20, left: 25}}>
                <Button

                    type="primary"
                    onClick={handleSendNotification}
                    disabled={selectedParticipant.length === 0}
                >
                    send
                </Button>
                <Button style={{marginLeft: 10}} onClick={onClose}>cancel</Button>
            </Space>
        </>
    );
};
export default SendNotificationForm;