import {useEffect, useState} from "react";
import {Button, Checkbox, Col, Divider, List, message, Row, Space} from "antd";
import {deleteAttendeeInfoByMeetingIdAndEmail, getAttendeeInfoByMeetingId} from "../../services/client.js";
import {LoginAuth} from "../context/LoginContext.jsx";

const ViewParticipantDrawer = ({selectedMeetingId, onClose}) => {

    const {memberInfo} = LoginAuth();
    const [participantList, setParticipantList] = useState([]);
    const [selectedParticipant, setSelectedParticipant] = useState([]);
    const indeterminate = selectedParticipant.length > 0 && selectedParticipant.length < participantList.length;
    const selectAll = selectedParticipant.length === participantList.length;

    const handleRemoveParticipant = async () => {
        // Remove selected participant from the meeting
        try {
            await deleteAttendeeInfoByMeetingIdAndEmail(selectedMeetingId, selectedParticipant);
            setSelectedParticipant([]);
            message.success("Participant with email " + selectedParticipant + " removed successfully");
            await getParticipantList();
        } catch (error) {
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


    const handleSelectAll = (e) => {
        const checked = e.target.checked;
        if (checked) {
            const allEmails = participantList.map((attendee) => attendee.email);
            setSelectedParticipant(allEmails);
            console.log(allEmails);
        } else {
            setSelectedParticipant([]);
        }
    };

    const isAcademic = memberInfo.memberRoles == "ACADEMIC";

    useEffect(() => {
        getParticipantList();
    }, [selectedMeetingId]);

    return (
        <>
            {/* If the user is academic, show the checkbox to select all participants
                If the member is not academic, show the list of participants*/}
            {isAcademic === true ? (
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
                        style={{display: "block", whiteSpace: "normal" }}
                        options={participantList.map((attendee) => ({
                            label: attendee.email + ", " + attendee.nameWithRole,
                            value: attendee.email,
                        }))}
                        value={selectedParticipant}
                        onChange={(email) => setSelectedParticipant(email)}
                    />
                </>
            ) : (
                <List
                    itemLayout="horizontal"
                    dataSource={participantList}
                    renderItem={(item) => (
                        <List.Item>
                            <Row gutter={8}>
                                <Col span={12}>{item.email}</Col>
                                <Col span={12} style={{whiteSpace: 'nowrap'}}>{item.nameWithRole}</Col>
                            </Row>
                        </List.Item>
                    )}
                />
            )}

            {/* If the user is academic, show the remove participant button */}
            { isAcademic &&
                <Space style={{position: "absolute", bottom: 20, left: 10}}>
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
            }
        </>
    )
}

export default ViewParticipantDrawer;