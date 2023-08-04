import {HomeNav} from "./component/shared/HomeNav.jsx";
import MeetingList from "./component/meeting/MeetingList.jsx";
import {Button, Modal, Space} from "antd";
import {PlusOutlined} from "@ant-design/icons";
import {useState} from "react";
import CreateMeetingForm from "./component/meeting/CreateMeetingForm.jsx";
import {LoginAuth} from "./component/context/LoginContext.jsx";

const MeetingPage = () => {

    const { memberInfo } = LoginAuth();
    const [showForm, setShowForm] = useState(false);
    const toggleForm = () => setShowForm(!showForm);
    const handleModalClose = () => {
        setShowForm(false);
    }

    return (
        <>
            <HomeNav/>
            <Space direction="vertical"
                   style={{width: '100%', marginTop: '10px'}}
            >
                {memberInfo?.memberRole === "ACADEMIC" && (
                    <Button type="primary" icon={<PlusOutlined />} onClick={toggleForm}>
                        Create new meeting
                    </Button>
                )}
                <Modal
                    title="Create New Meeting"
                    centered
                    open={showForm}
                    onCancel={handleModalClose}
                    footer={null}
                >
                    <CreateMeetingForm/>
                </Modal>
                <MeetingList/>
            </Space>

        </>
    )
}
export default MeetingPage;