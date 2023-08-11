import {Formik} from "formik";
import * as Yup from "yup";
import {saveMeeting} from "../../services/client.js";
import {Button, DatePicker, Form, Input, Space} from 'antd';

const CreateMeetingForm = () => {

    const {TextArea} = Input;
    return (
        <Formik
            initialValues={{
                meetingTopic: '',
                meetingName: '',
                meetingDate: '',
                meetingDuration: '',
                meetingDescription: '',
            }}
            validationSchema={Yup.object({
                meetingTopic: Yup.string()
                    .max(15, 'Must be 15 characters or less')
                    .required('Required'),
                meetingName: Yup.string()
                    .max(50, 'Must be 20 characters or less')
                    .required('Required'),
                meetingDate: Yup.date()
                    .required('Required'),
                meetingDuration: Yup.string()
                    .required('Required'),
                meetingDescription: Yup.string()
            })}
            onSubmit={(meeting, {setSubmitting}) => {
                console.log("Form submitted!")
                setSubmitting(true);
                // transform meeting date to timestamp and save meeting to database
                saveMeeting({
                    ...meeting,
                    meetingDate: meeting.meetingDate.toISOString()
                })
                    .then(res => {
                        console.log(res);

                    }).catch(err => {
                    console.log(err);
                }).finally(() => {
                    setSubmitting(false);
                    window.location.reload();
                })
            }}
        >
            {(formikProps) => (
                <Form
                    layout="horizontal"
                    size={"large"}
                    onFinish={formikProps.handleSubmit}
                >
                    <Form.Item
                        label="Meeting Topic"
                        name="meetingTopic"
                        rules={[{required: true, message: 'Please input your meeting topic!'}]}
                    >
                        <Input
                            placeholder="Meeting Topic"
                            onChange={formikProps.handleChange}
                            value={formikProps.values.meetingTopic}
                        />
                    </Form.Item>
                    <Form.Item
                        label="Meeting Name"
                        name="meetingName"
                        rules={[{required: true, message: 'Please input your meeting name!'}]}
                    >
                        <Input
                            placeholder="Meeting Name"
                            onChange={formikProps.handleChange}
                            value={formikProps.values.meetingName}
                        />
                    </Form.Item>
                    <Form.Item
                        label="Meeting Date"
                        name="meetingDate"
                        rules={[{required: true}]}
                    >
                        <DatePicker
                            showTime
                            value={formikProps.values.meetingDate}
                            onChange={value => formikProps.setFieldValue("meetingDate", value)}
                        />
                    </Form.Item>
                    <Form.Item
                        label="Meeting Duration"
                        name="meetingDuration"
                        rules={[{required: true, message: 'Please input your meeting duration!'}]}
                    >
                        <Input
                            placeholder="Meeting Duration"
                            onChange={formikProps.handleChange}
                            value={formikProps.values.meetingDuration}
                        />
                    </Form.Item>
                    <Form.Item
                        label="Meeting Description"
                        name="meetingDescription"
                        rules={[{message: 'Please input your meeting description!'}]}
                    >
                        <TextArea
                            rows={4}
                            placeholder="Meeting Description"
                            onChange={formikProps.handleChange}
                        />
                    </Form.Item>
                    <Form.Item>
                        <Space>
                            <Button
                                type="primary"
                                htmlType="submit"
                                disabled={formikProps.isSubmitting || !formikProps.isValid}
                            >
                                Submit
                            </Button>
                            <Button htmlType="reset" onClick={formikProps.resetForm}>
                                Reset
                            </Button>
                        </Space>
                    </Form.Item>
                </Form>
            )}
        </Formik>
    )
}

export default CreateMeetingForm;