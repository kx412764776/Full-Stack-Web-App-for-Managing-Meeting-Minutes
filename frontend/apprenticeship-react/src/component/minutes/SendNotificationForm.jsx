import {getAttendeeInfoByMeetingId} from "../../services/client.js";
import {useEffect, useState} from "react";
import {Button, Checkbox, Divider, Space} from "antd";

const SendNotificationForm = ({selectedMeetingId, onClose}) => {

    const [participantList, setParticipantList] = useState([]);
    const [selectedParticipant, setSelectedParticipant] = useState([]);
    const [selectAll, setSelectAll] = useState(false);

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

    const handleSendNotification = () => {

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
            >
                Select All
            </Checkbox>
            <Divider/>
            <Checkbox.Group
                style={{display: "block", whiteSpace: "normal"}}
                options={participantList.map((attendee) => ({
                    label: (
                        <Space direction="vertical" >
                            {attendee.email}{attendee.nameWithRole}
                        </Space>
                    ),
                    value: attendee.email,
                }))}
                value={selectedParticipant}
                onChange={(email) => {
                    setSelectedParticipant(email);
                }}
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