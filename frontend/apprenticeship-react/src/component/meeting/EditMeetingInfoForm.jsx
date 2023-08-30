import {Button, DatePicker, Form, Input, Space, message, Spin} from "antd";
import {Formik} from "formik";
import {getMeetingInfoByMeetingId, updateMeetingByMeetingId} from "../../services/client.js";
import * as Yup from "yup";
import {useEffect, useState} from "react";

const EditMeetingInfoForm = ({meetingId, onClose}) => {

    const {TextArea} = Input;
    const [meetingTopic, setMeetingTopic] = useState("");
    const [meetingName, setMeetingName] = useState("");
    const [meetingDate, setMeetingDate] = useState("");
    const [meetingDuration, setMeetingDuration] = useState("");
    const [meetingDescription, setMeetingDescription] = useState("");
    const [isInfoLoaded, setIsInfoLoaded] = useState(false);

    const fetchMeetingInfo = async () => {
        try {
            await getMeetingInfoByMeetingId(meetingId)
                .then(res => {
                    setMeetingTopic(res.data.meetingTopic);
                    setMeetingName(res.data.meetingName);
                    setMeetingDate(res.data.meetingDate);
                    setMeetingDuration(res.data.meetingDuration);
                    setMeetingDescription(res.data.meetingDescription);
                })
        } catch (e) {
            console.log(e);
        }
    }

    useEffect(() => {
        fetchMeetingInfo().then(() => {
            setIsInfoLoaded(true);
        });
    }, [meetingId])


    if (!isInfoLoaded) {
        return (
            <Spin tip={"Loading"} size={"large"}/>
        )
    }
    return (
        <>
            <Formik
                initialValues={{
                    meetingTopic: meetingTopic,
                    meetingName: meetingName,
                    meetingDate: meetingDate,
                    meetingDuration: meetingDuration,
                    meetingDescription: meetingDescription,
                }}
                enableReinitialize={true}
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
                    setSubmitting(true);
                    // According to meetingId,
                    // transforming meeting date to timestamp and save meeting to database
                    updateMeetingByMeetingId(meetingId, {
                        ...meeting,
                        meetingDate: meeting.meetingDate.toISOString()
                    })
                        .then(() => {
                                message.success(`Meeting ${meeting.meetingName} updated successfully`);
                            }
                        ).catch(() => {
                            message.error(`Meeting ${meeting.meetingName} updated failed`);
                        }
                    ).finally(() => {
                        setSubmitting(false);
                        onClose();
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
                            initialValue={meetingTopic}
                        >
                            <Input
                                onChange={formikProps.handleChange}
                                value={formikProps.values.meetingTopic}
                            />
                        </Form.Item>
                        <Form.Item
                            label="Meeting Name"
                            name="meetingName"
                            rules={[{required: true, message: 'Please input your meeting name!'}]}
                            initialValue={meetingName}
                        >
                            <Input
                                onChange={formikProps.handleChange}
                                value={formikProps.values.meetingName}
                            />
                        </Form.Item>
                        <Form.Item
                            label="Meeting Date"
                            name="meetingDate"
                            rules={[{required: true, message: 'Please choose your meeting date!'}]}
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
                            initialValue={meetingDuration}
                        >
                            <Input
                                onChange={formikProps.handleChange}
                                value={formikProps.values.meetingDuration}
                                initialValue={meetingDuration}
                            />
                        </Form.Item>
                        <Form.Item
                            label="Meeting Description"
                            name="meetingDescription"
                            rules={[{message: 'Please input your meeting description!'}]}
                            initialValue={meetingDescription}
                        >
                            <TextArea
                                rows={4}
                                onChange={formikProps.handleChange}
                            />
                        </Form.Item>
                        <Form.Item>
                            <Space
                                style={{float: "right", marginTop: "20px"}}

                            >
                                <Button
                                    type="primary"
                                    htmlType="submit"
                                    disabled={formikProps.isSubmitting || !formikProps.isValid}
                                >
                                    Update
                                </Button>
                                <Button
                                    htmlType="reset"
                                    onClick={onClose}
                                >
                                    Cancel
                                </Button>
                            </Space>
                        </Form.Item>
                    </Form>
                )}
            </Formik>
        </>
    )
}

export default EditMeetingInfoForm;