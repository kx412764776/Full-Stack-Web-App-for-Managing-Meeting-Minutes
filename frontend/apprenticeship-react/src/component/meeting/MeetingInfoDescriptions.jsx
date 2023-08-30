import {Descriptions, Space} from "antd";

const MeetingInfoDescriptions = ({ meetingInfo }) => {

    // Clone the meetingInfo object and remove the 'meetingId' property
    const modifiedMeetingInfo = { ...meetingInfo };
    delete modifiedMeetingInfo.meetingId;
    const meetingDate = new Date(modifiedMeetingInfo.meetingDate);
    modifiedMeetingInfo.meetingDate = meetingDate.toLocaleDateString() + ' ' + meetingDate.toLocaleTimeString();

    const formatKey = (key) => {
        const splitKey = key.split(/(?=[A-Z])/);
        return splitKey.map((word) => word.charAt(0).toUpperCase() + word.slice(1)).join(' ');
    }

    const items = Object.entries(modifiedMeetingInfo).map(([key, value]) => ({
        key,
        label: formatKey(key),
        children: value,
    }));


    return (
        <Space
            direction="vertical"
            style={{
                width: '100%',
                padding: '0 50px',
                marginTop: '10px',
            }}

        >
            <Descriptions
                title="Meeting Information"
                bordered={true}
                column={1}
                items={items}
                size={"small"}
            />
        </Space>
    );
}

export default MeetingInfoDescriptions;