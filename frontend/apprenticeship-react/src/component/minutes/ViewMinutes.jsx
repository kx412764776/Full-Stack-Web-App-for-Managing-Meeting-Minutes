import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {
    getMeetingInfoByMeetingId,
    getMinutesByMeetingId
} from "../../services/client.js";
import {Button, Col, Divider, Drawer, Radio, Row, Space} from "antd";
import {HomeNav} from "../shared/HomeNav.jsx";
import MeetingInfoDescriptions from "../meeting/MeetingInfoDescriptions.jsx";
import ReactQuill from "react-quill";
import {LoginAuth} from "../context/LoginContext.jsx";
import SendNotificationForm from "./SendNotificationForm.jsx";

const ViewMinutes = () => {

    const {memberInfo} = LoginAuth();
    const {meetingId} = useParams();
    const [minutes, setMinutes] = useState('');
    const [meetingInfo, setMeetingInfo] = useState({});
    const [drawerVisible, setDrawerVisible] = useState(false);

    const onClose = () => {
        setDrawerVisible(false);
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

            <Row gutter={16}
                 style={{
                     padding: '0 50px',
                     marginTop: '10px',
                 }}
            >
                <Col span={16}>
                    <MeetingInfoDescriptions meetingInfo={meetingInfo} />
                </Col>
                <Col span={8} align="end">
                    <Space direction={"vertical"}>
                        <Button
                            type="primary"
                            onClick={() => window.location.href = '/apprenticeship/meeting'}
                            style={{
                                marginTop: '10px',
                            }}
                        >
                            Back to Meeting
                        </Button>

                        <Button
                            type="primary"
                            onClick={() =>{
                                setDrawerVisible(true);
                            }}
                            style={{
                                marginTop: '10px'
                            }}
                        >
                            Send Notification
                        </Button>
                    </Space>
                </Col>

                <Drawer
                    title={"Send Notification"}
                    placement={"right"}
                    open={drawerVisible}
                    onClose={() => setDrawerVisible(false)}
                    width={400}
                >
                    <SendNotificationForm selectedMeetingId={meetingId} onClose={onClose} />
                </Drawer>

                {/* When the member is a mentor or apprenticeship, show the approve button and back button */}
                {memberInfo.memberRoles == 'MENTOR' || memberInfo.memberRoles == 'APPRENTICE' && (
                    <>
                    <Col span={10} style={{ marginTop: '20px'}} offset={1}>
                        <Radio>Approve</Radio>
                        <Button
                            type="primary"
                            style={{ marginLeft: '10px' }}
                        >
                            Submit
                        </Button>
                    </Col>
                    </>
                )}
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
                <ReactQuill
                    style={{height: '700px'}}
                    theme="snow"
                    modules={{toolbar: false}}
                    value={minutes}
                    readOnly={true}
                />
            </Space>
        </>
    );
};

export default ViewMinutes;