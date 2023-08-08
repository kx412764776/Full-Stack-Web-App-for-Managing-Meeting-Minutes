import {useState} from 'react';
import {Form, message, Select, Button, Space} from "antd";
import {addParticipantAndMeetingToAttendeeTable, checkMemberByEmail} from "../../services/client.js";

const {Option} = Select;

const AddParticipantForm = ({onClose, selectedMeetingId}) => {
    const [form] = Form.useForm();
    // State to store selected emails
    const [selectedEmails, setSelectedEmails] = useState([]);


    const handleSearch = async (emailPrefix) => {
        try {
            // Call the function to get the response from the backend
            const response = await checkMemberByEmail(emailPrefix);
            const emailsWithName = response.data.map((emailAndName) => ({
                email: emailAndName.split(' ')[0],
                name: emailAndName.split(' ')[1]
            }));
            setSelectedEmails(emailsWithName.map((value) => JSON.stringify(value)));

        } catch (error) {
            console.log(error);
        }
    };

    const handleAddParticipant = async () => {
        const selectedEmailAddresses = selectedEmails.map((value) => JSON.parse(value).email);

        try {
            // Call the function to add participants and meetingId to the backend
            const response = await addParticipantAndMeetingToAttendeeTable(selectedEmailAddresses, selectedMeetingId);

            // Handle success or error
            if (response.status === 201) {
                message.success('Participants added successfully');
                onClose();
                form.resetFields();
            }
        } catch (error) {
            message.error('Members already exist in the meeting');
        }

    };


    return (
        <Form layout={"vertical"} form={form}>
            <Form.Item
                label="Email"
                name="email"
                rules={[
                    {
                        required: true,
                        message: "Please input the email of the participant!",
                    }
                ]}
            >
                <Select
                    mode="multiple"
                    placeholder="Search and select participant email"
                    style={{width: '100%'}}
                    onChange={setSelectedEmails} // Store selected emails in state
                    onSearch={handleSearch}
                >
                    {selectedEmails.map((value) => (
                        <Option key={value} value={value}>
                            {JSON.parse(value).email} ({JSON.parse(value).name})
                        </Option>
                    ))}
                </Select>
            </Form.Item>
            <Space
                style={{position: 'absolute', bottom: 20, left:10}}>
                <Button type="primary" onClick={handleAddParticipant}>
                    Add Participants
                </Button>
                <Button onClick={() => {
                    form.resetFields();
                    onClose();
                }}>

                    Cancel
                </Button>
            </Space>
        </Form>
    );

};
export default AddParticipantForm;
