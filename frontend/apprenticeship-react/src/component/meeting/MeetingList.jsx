import {useEffect, useState} from 'react';
import {getMeetingInfo, getMeetingInfoByEmail} from "../../services/client.js";
import {LoginAuth} from "../context/LoginContext.jsx";
import Highlighter from 'react-highlight-words';
import {Button, Drawer, Input, Space, Table} from 'antd';
import {SearchOutlined} from '@ant-design/icons';
import AddParticipantForm from "./AddParticipantForm.jsx";
import ViewParticipantDrawer from "./ViewParticipantDrawer.jsx";

const MeetingList = () => {

    const {memberInfo} = LoginAuth();

    const [meetingInfoList, setMeetingInfoList] = useState([]);
    const [searchText, setSearchText] = useState('');
    const [searchedColumn, setSearchedColumn] = useState('');
    const [drawerVisible, setDrawerVisible] = useState(false);
    const [selectedMeetingId, setSelectedMeetingId] = useState(null);
    const [selectedDrawer, setSelectedDrawer] = useState('addParticipants');

    const onClose = () => {
        setDrawerVisible(false);
    }

    // Handle adding participant

    useEffect(() => {
        const fetchMeetingInfo = async () => {
            try {
                let res;
                console.log(memberInfo.memberRoles);
                if (memberInfo.memberRoles == 'ACADEMIC') {
                    res = await getMeetingInfo();
                } else {
                    const memberEmail = memberInfo.email;
                    res = await getMeetingInfoByEmail(memberEmail);
                }
                setMeetingInfoList(res.data);
            } catch (err) {
                console.log(err);
            }
        };
        fetchMeetingInfo();
    }, [memberInfo]);

    const getColumnSearchProps = (dataIndex) => ({
        filterDropdown: ({setSelectedKeys, selectedKeys, confirm, clearFilters}) => (
            <div style={{padding: 8}}>
                <Input
                    id="searchInput"
                    placeholder={`Search ${dataIndex}`}
                    value={selectedKeys[0]}
                    onChange={(e) => setSelectedKeys(e.target.value ? [e.target.value] : [])}
                    onPressEnter={() => handleSearch(selectedKeys, confirm, dataIndex)}
                    style={{marginBottom: 8, display: 'block'}}
                />
                <Space>
                    <Button
                        type="primary"
                        onClick={() => handleSearch(selectedKeys, confirm, dataIndex)}
                        icon={<SearchOutlined/>}
                        size="small"
                        style={{width: 90}}
                    >
                        Search
                    </Button>
                    <Button onClick={() => handleReset(clearFilters)} size="small" style={{width: 90}}>
                        Reset
                    </Button>
                </Space>
            </div>
        ),
        filterIcon: (filtered) => <SearchOutlined style={{color: filtered ? '#1890ff' : undefined}}/>,
        onFilter: (value, record) =>
            record[dataIndex].toString().toLowerCase().includes(value.toLowerCase()),
        onFilterDropdownOpenChange: (visible) => {
            if (visible) {
                setTimeout(() => document.getElementById('searchInput').select(), 100);
            }
        },
        render: (text) =>
            searchedColumn === dataIndex ? (
                <Highlighter
                    highlightStyle={{backgroundColor: '#ffc069', padding: 0}}
                    searchWords={[searchText]}
                    autoEscape
                    textToHighlight={text.toString()}
                />
            ) : (
                text
            ),
    });

    const handleSearch = (selectedKeys, confirm, dataIndex) => {
        confirm();
        setSearchText(selectedKeys[0]);
        setSearchedColumn(dataIndex);
    };

    const handleReset = (clearFilters) => {
        clearFilters();
        setSearchText('');
    };

    // Split each word of the title of the meeting list and capitalise the first letter.
    const formatKey = (key) => {
        const splitKey = key.split(/(?=[A-Z])/);
        return splitKey.map((word) => word.charAt(0).toUpperCase() + word.slice(1)).join(' ');
    }

    const columns = meetingInfoList.length > 0 ? Object.keys(meetingInfoList[0]).slice(1).map((key) => ({
        title: formatKey(key),
        dataIndex: key,

        //
        sorter: (a, b) => {
            const valueA = a[key];
            const valueB = b[key];
            if (typeof valueA === 'string') {
                return valueA.localeCompare(valueB);
            } else if (key === 'meetingDate') {
                return new Date(valueA) - new Date(valueB);
            } else {
                return valueA - valueB;
            }
        },
        ...getColumnSearchProps(key), // Apply search functionality to each column
    })) : [];

    // If Login member is ACADEMIC,
    // add columns for adding meeting minutes, add participants and edit meeting info
    if (memberInfo.memberRoles == 'ACADEMIC') {
        columns.push(
            {
                title: 'Meeting Minutes',
                dataIndex: 'meetingMinutes',
                render: (_, record) => (
                    <Space direction={"vertical"}>
                        <Button
                            type="primary"
                            onClick={() => {
                                window.location.href = (`/apprenticeship/meeting/${record.meetingId}/minutes`);
                            }}
                        >
                            Edit Meeting Minutes
                        </Button>
                        <Button
                            type="primary"
                            onClick={() => {
                                // TODO: View meeting minutes
                                window.location.href = ``;
                            }}
                        >
                            View Meeting Minutes
                        </Button>
                    </Space>
                )
            },
            {
                title: 'Participants',
                dataIndex: 'participants',
                render: (_, record) => {
                    return (
                        <Space direction={"vertical"}>
                            <Button
                                type="primary"
                                onClick={() => {
                                    setDrawerVisible(true)
                                    setSelectedMeetingId(record.meetingId)
                                    setSelectedDrawer('addParticipants');
                                }}
                            >
                                Add Participants
                            </Button>
                            <Button
                                type="primary"
                                onClick={() => {
                                    setDrawerVisible(true)
                                    setSelectedMeetingId(record.meetingId)
                                    setSelectedDrawer('viewParticipants');
                                }}
                            >
                                View Participants
                            </Button>
                        </Space>
                    );
                }
            }
        )
    }

    // change the format of meetingDate
    const modifiedData = meetingInfoList.map(item => {
        const {meetingDate, ...rest} = item;
        let date = new Date(meetingDate);
        // format the date to dd/mm/yyyy hh:mm:ss
        const modifiedMeetingDate =
            date.toLocaleString().split(',')[0] + ' ' + date.toLocaleString().split(',')[1];
        return {...rest, meetingDate: modifiedMeetingDate};
    });

    const modifiedDataWithKey =
        modifiedData.map((row, index) =>
            ({...row, key: row[0] || index}));

    return (
    <>
        <Table columns={columns} dataSource={modifiedDataWithKey}/>
        <Drawer
            title={selectedDrawer === 'addParticipants' ? 'Add Participants' : 'View Participants'}
            width={400}
            onClose={onClose}
            open={drawerVisible}
            bodyStyle={{paddingBottom: 80}}
        >
            {selectedDrawer === 'addParticipants' ? (
                <AddParticipantForm
                    selectedMeetingId={selectedMeetingId}
                    onClose={onClose}
                />
            ) : (
                <ViewParticipantDrawer
                    selectedMeetingId={selectedMeetingId}
                    onClose={onClose}
                />
            )}
        </Drawer>
    </>
    )
}

export default MeetingList;



