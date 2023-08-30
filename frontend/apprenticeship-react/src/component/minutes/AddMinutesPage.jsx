import {HomeNav} from "../shared/HomeNav.jsx";
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {
    getMeetingInfoByMeetingId,
    insertOrUpdateMinutesByMeetingId,
    getMinutesByMeetingId
} from "../../services/client.js";
import MeetingInfoDescriptions from "../meeting/MeetingInfoDescriptions.jsx";
import {Button, Col, Divider, message, Row, Space} from "antd";

const AddMinutesPage = () => {

    const {meetingId} = useParams();
    const [minutes, setMinutes] = useState('');
    const [meetingInfo, setMeetingInfo] = useState({});
    const [editMode, setEditMode] = useState(false);

    const saveMinutes = async () => {
        const response = await insertOrUpdateMinutesByMeetingId(meetingId, minutes);
        if (response.status === 200) {
            message.success('Minutes saved successfully');
            setEditMode(false);
        } else {
            message.error('Failed to save minutes');
        }
    }

    useEffect(() => {
        const fetchMeetingInfo = async () => {
            const response = await getMeetingInfoByMeetingId(meetingId);
            setMeetingInfo(response.data);

            const minutesContent = await getMinutesByMeetingId(meetingId);
            setMinutes(minutesContent.data);
        }
        fetchMeetingInfo();
    }, [meetingId]);

    return (
        <>
            <HomeNav/>
            <Row
                gutter={16}
                style={{
                    padding: '0 50px',
                    marginTop: '10px',
                }}
            >
                <Col span={16}>
                    <MeetingInfoDescriptions meetingInfo={meetingInfo}/>
                </Col>
                <Col span={8} align="end">
                    <Button
                        type={"primary"}
                        onClick={() => window.location.href = `/apprenticeship/meeting`}
                        style={{
                            marginTop: '10px',
                            marginRight: '10px'
                        }}
                    >
                        Back to Meeting
                    </Button>
                </Col>
            </Row>
            <Divider/>
            <Space
                direction="vertical"
                style={{
                    width: '100%',
                    padding: '0 50px',
                    marginTop: '10px',
                    height: '400px',
                }}
            >
                <Space
                    direction={"horizontal"}
                    style={{
                        display: 'flex',
                        justifyContent: 'flex-end',
                    }}
                >
                    {editMode ? (
                        <Button onClick={() => setEditMode(false)}>Cancel</Button>
                    ) : (
                        <Button type={"primary"} onClick={() => setEditMode(true)}>Edit</Button>
                    )}
                    {editMode && (
                        <Button type={"primary"} onClick={saveMinutes}>Save</Button>
                    )}
                </Space>
                <ReactQuill
                    style={{height: '700px'}}
                    theme="snow"
                    modules={{
                        toolbar: [
                            [{'header': [1, 2, 3, false]}],
                            ['bold', 'italic', 'underline'],
                            [{'list': 'ordered'}, {'list': 'bullet'}, {'indent': '-1'}, {'indent': '+1'}],
                            ['clean']
                        ]
                    }}
                    value={minutes}
                    onChange={setMinutes}
                    readOnly={!editMode}
                />
            </Space>

        </>
    )
}

export default AddMinutesPage;