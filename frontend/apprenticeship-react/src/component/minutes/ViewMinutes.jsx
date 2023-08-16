import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {
    checkIfSignatureExists,
    getMeetingInfoByMeetingId,
    getMinutesByMeetingId, updateSignature
} from "../../services/client.js";
import {Button, Col, Divider, Drawer, message, Radio, Row, Space} from "antd";
import {HomeNav} from "../shared/HomeNav.jsx";
import MeetingInfoDescriptions from "../meeting/MeetingInfoDescriptions.jsx";
import ReactQuill from "react-quill";
import {LoginAuth} from "../context/LoginContext.jsx";
import SendNotificationForm from "./SendNotificationForm.jsx";

const ViewMinutes = () => {

    const {memberInfo} = LoginAuth();
    const {meetingId} = useParams();
    const [minutes, setMinutes] = useState(null);
    const [meetingInfo, setMeetingInfo] = useState({});
    const [drawerVisible, setDrawerVisible] = useState(false);
    // Whether the member has approved the minutes
    const [whetherApprove, setWhetherApprove] = useState(false);
    // control if the approve submit button is disabled
    const [disabledApprove, setDisabledApprove] = useState(true);
    // control if the approve radio button been chosen
    const [isChoseApproveRadio, setIsChoseApproveRadio] = useState(false);



    const onClose = () => {
        setDrawerVisible(false);
    }

    // Submit the approval of the minutes
    const submitApproveMinutes = async () => {
        const memberEmail = memberInfo.email;
        const response = await updateSignature(meetingId, memberEmail);
        if (response.status === 200) {
            message.success('Successfully approved the minutes!');
            setWhetherApprove(true);
        } else {
            message.error('Failed to approve the minutes!');
        }
    }

    // Check if the minutes exists or the member has already approved the minutes
    const setApproveStatus = async () => {
        try {
            // Check if the member has already approved the minutes
            const memberEmail = memberInfo.email;
            const res = await checkIfSignatureExists(meetingId, memberEmail);
            if (res.status === 200) {
                setWhetherApprove(true);
            }
        } catch (error) {
            setWhetherApprove(false);
            console.log(error.message);
        } finally {
            // Check if the minutes exists
            if (minutes === null) {
                setDisabledApprove(true);
            } else if (whetherApprove === true) {
                setDisabledApprove(true);
            } else {
                setDisabledApprove(false);
            }
        }
    }


    useEffect(() => {
        const fetchMeetingInfo = async () => {
            const response = await getMeetingInfoByMeetingId(meetingId);
            setMeetingInfo(response.data);

            const minutesContent = await getMinutesByMeetingId(meetingId);
            setMinutes(minutesContent.data);

            setApproveStatus();
        }
        fetchMeetingInfo();

    }, [meetingId, minutes, whetherApprove]);


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
                    <MeetingInfoDescriptions meetingInfo={meetingInfo}/>
                </Col>
                <Col span={8} align="end">
                    <Space direction={"vertical"}>
                        <Button
                            type="primary"
                            onClick={() => window.location.href = '/apprenticeship/meeting'}
                            style={{
                                marginTop: '10px',
                                width: '100%'
                            }}
                        >
                            Back to Meeting
                        </Button>

                        {memberInfo.memberRoles == 'ACADEMIC' && (
                        <Button
                            type="primary"
                            onClick={() => {
                                setDrawerVisible(true);
                            }}
                            style={{
                                marginTop: '10px',
                                width: '100%'
                            }}
                        >
                            Send Notification
                        </Button>
                        )}
                    </Space>
                </Col>

                <Drawer
                    title={"Send Required to Sign Notification Email"}
                    placement={"right"}
                    open={drawerVisible}
                    onClose={() => setDrawerVisible(false)}
                    width={400}
                >
                    <SendNotificationForm selectedMeetingId={meetingId} onClose={onClose}/>
                </Drawer>

                {/* When the member is a mentor or apprenticeship, show the approve button and back button */}
                {memberInfo.memberRoles == 'MENTOR' || memberInfo.memberRoles == 'APPRENTICE' && (
                    <>
                        <Col span={10} style={{marginTop: '20px'}} offset={1}>
                            <Radio
                                onChange={() => {
                                    setIsChoseApproveRadio(true);
                                }}
                                checked={isChoseApproveRadio}
                                disabled={disabledApprove}
                            >
                                Approve
                            </Radio>
                            <Button
                                type="primary"
                                style={{marginLeft: '10px'}}
                                onClick={submitApproveMinutes}
                                disabled={!isChoseApproveRadio || disabledApprove}
                            >
                                Submit
                            </Button>
                            {whetherApprove === true && minutes != null && (
                                <Space style={{color: "red", marginLeft: 10}}>
                                    You have already approved the minutes.
                                </Space>
                            )}
                            {minutes === null && (
                                <Space style={{color: "red", marginLeft: 10}}>
                                    Please wait for the minutes to be uploaded.
                                </Space>
                            )}
                            {!whetherApprove && minutes != null && (
                                <Space style={{color: "red", marginLeft: 10}}>
                                    You need to approve the minutes.
                                </Space>
                            )}
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