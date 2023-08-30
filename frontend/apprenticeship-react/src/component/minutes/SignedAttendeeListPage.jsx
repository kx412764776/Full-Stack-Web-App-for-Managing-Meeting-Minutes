import {HomeNav} from "../shared/HomeNav.jsx";
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {getMeetingInfoByMeetingId, getSignatureTableByMeetingId} from "../../services/client.js";
import {Button, Col, Divider, Drawer, Row, Space, Table, Tag} from "antd";
import MeetingInfoDescriptions from "../meeting/MeetingInfoDescriptions.jsx";
import {CheckCircleOutlined, ClockCircleOutlined} from "@ant-design/icons";
import SendNotificationForm from "./SendNotificationForm.jsx";
import {LoginAuth} from "../context/LoginContext.jsx";

const SignedAttendeeListPage = () => {

    const {memberInfo} = LoginAuth();

    const {meetingId} = useParams();
    const [meetingInfo, setMeetingInfo] = useState({});
    const [signatureList, setSignatureList] = useState([]);
    const [drawerVisible, setDrawerVisible] = useState(false);

    const onCloseDrawer = () => {
        setDrawerVisible(false);
    }

    const fetchMeetingInfo = async () => {
        const res = await getMeetingInfoByMeetingId(meetingId);
        setMeetingInfo(res.data);

    }

    const fetchSignatureList = async () => {
        const signList = await getSignatureTableByMeetingId(meetingId);
        setSignatureList(signList.data);
        console.log(signList.data);
    }

    const columns = [
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Email',
            dataIndex: 'email',
            key: 'email'
        },
        {
            title: 'Member Role',
            dataIndex: 'memberRole',
            key: 'memberRole'
        },
        {
            title: 'Signature Status',
            dataIndex: 'signatureStatus',
            key: 'signatureStatus',
            render: (signatureStatus) => (
                <>
                    {signatureStatus === 1 ? (
                        <Tag icon={<CheckCircleOutlined />} color={"success"}>
                            Signed
                        </Tag>
                    ) : (
                        <Tag icon={<ClockCircleOutlined />} color={"default"}>
                            Waiting
                        </Tag>
                    )
                    }
                </>
            )
        }
    ]

    useEffect(() => {
        fetchMeetingInfo();
        fetchSignatureList();
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
                    <MeetingInfoDescriptions meetingInfo={meetingInfo}/>
                </Col>
                <Col span={8} align={"end"}>
                    <Space>
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
            </Row>

            <Drawer
                title={"Send Required to Sign Notification Email"}
                placement={"right"}
                open={drawerVisible}
                onClose={() => setDrawerVisible(false)}
                width={400}
            >
                <SendNotificationForm selectedMeetingId={meetingId} onClose={onCloseDrawer}/>
            </Drawer>

            <Divider/>
            <Row gutter={16}
                 style={{
                     padding: '0 50px',
                     marginTop: '10px',
                 }}
            >
                <Col span={24}>
                    <Table
                        columns={columns}
                        dataSource={signatureList.map((item, index) => ({ ...item, key: index }))}
                        title={() => 'Signature List'}
                    />
                </Col>
            </Row>
        </>
    )
}

export default SignedAttendeeListPage;