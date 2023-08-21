import {useEffect, useState} from 'react';
import {deleteMeetingByMeetingId, getMeetingInfo, getMeetingInfoByEmail} from "../../services/client.js";
import {LoginAuth} from "../context/LoginContext.jsx";
import Highlighter from 'react-highlight-words';
import {Button, Divider, Drawer, Input, message, Modal, Space, Table} from 'antd';
import {SearchOutlined} from '@ant-design/icons';
import AddParticipantForm from "./AddParticipantForm.jsx";
import ViewParticipantDrawer from "./ViewParticipantDrawer.jsx";
import EditMeetingInfoForm from "./EditMeetingInfoForm.jsx";

const MeetingList = () => {

    const {memberInfo} = LoginAuth();

    const [meetingInfoList, setMeetingInfoList] = useState([]);
    const [searchText, setSearchText] = useState('');
    const [searchedColumn, setSearchedColumn] = useState('');
    const [drawerVisible, setDrawerVisible] = useState(false);
    const [selectedMeetingId, setSelectedMeetingId] = useState(null);
    const [selectedDrawer, setSelectedDrawer] = useState('addParticipants');

    // delete meeting variable and confirm modal
    const [deleteMeetingId, setDeleteMeetingId] = useState(null);
    const [showConfirmDeleteModal, setShowConfirmDeleteModal] = useState(false);

    // set edit meeting id and edit meeting modal
    const [editMeetingId, setEditMeetingId] = useState(null);
    const [showEditMeetingModal, setShowEditMeetingModal] = useState(false);

    // Open edit meeting modal
    const handleShowEditMeetingModal = (meetingId) => {
        setEditMeetingId(meetingId);
        setShowEditMeetingModal(true);
    }

    // Close edit meeting modal
    const handleCloseEditMeetingModal = () => {
        setShowEditMeetingModal(false);
    }

    // when choose delete meeting, show confirm modal
    const showConfirmDeleteDialog = (meetingId) => {
        setDeleteMeetingId(meetingId);
        setShowConfirmDeleteModal(true);
    }

    const handleDeleteMeeting = async () => {
        try {
            console.log(deleteMeetingId);
            await deleteMeetingByMeetingId(deleteMeetingId);
            setShowConfirmDeleteModal(false);
            message.success('Meeting deleted successfully');
            await fetchMeetingInfo();
        } catch (err) {
            message.error('Failed to delete meeting');
        }
    }

    const handleCancelDeleteMeeting = () => {
        setShowConfirmDeleteModal(false);
    }

    const onClose = () => {
        setDrawerVisible(false);
    }

    const fetchMeetingInfo = async () => {
        try {
            let res;
            if (memberInfo.memberRoles == 'ACADEMIC') {
                res = await getMeetingInfo();
            } else {
                const memberEmail = memberInfo.email;
                res = await getMeetingInfoByEmail(memberEmail);
            }
            setMeetingInfoList(res.data);

        } catch (err) {
            console.log(err.message);
        }
    };



    useEffect(() => {

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
                            style={{width: '100%'}}
                            type="primary"
                            onClick={() => {
                                window.location.href = (`/apprenticeship/meeting/${record.meetingId}/minutes`);
                            }}
                        >
                            Edit Meeting Minutes
                        </Button>
                        <Button
                            style={{width: '100%'}}
                            type="primary"
                            onClick={() => {
                                window.location.href = (`/apprenticeship/meeting/${record.meetingId}/minutesInfo`);
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
                                style={{width: '100%'}}
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
                                style={{width: '100%'}}
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
            },
            {
                title: 'Edit Meeting',
                dataIndex: 'editMeeting',
                render: (_, record) => (
                    <Space direction={"vertical"}>
                        <Button
                            style={{width: '100%'}}
                            type="primary"
                            onClick={() => handleShowEditMeetingModal(record.meetingId)}
                        >
                            Edit Meeting
                        </Button>
                        <Button
                            style={{width: '100%'}}
                            type="primary"
                            danger
                            onClick={() => showConfirmDeleteDialog(record.meetingId)}
                        >
                            Delete Meeting
                        </Button>
                    </Space>
                )
            }
        )
    } else if (memberInfo.memberRoles == 'APPRENTICE' || memberInfo.memberRoles == 'MENTOR') {
        columns.push(
            {
                title: 'Meeting Minutes',
                dataIndex: 'meetingMinutes',
                render: (_, record) => (
                    <Button
                        style={{width: '100%'}}
                        type="primary"
                        onClick={() => {
                            window.location.href = (`/apprenticeship/meeting/${record.meetingId}/minutesInfo`);
                        }}
                    >
                        View Meeting Minutes
                    </Button>
                )
            },
            {
                title: 'Participants',
                dataIndex: 'participants',
                render: (_, record) => {
                    return (
                        <Button
                            style={{width: '100%'}}
                            type="primary"
                            onClick={() => {
                                setDrawerVisible(true)
                                setSelectedMeetingId(record.meetingId)
                                setSelectedDrawer('viewParticipants');
                            }}
                        >
                            View Participants
                        </Button>
                    );
                }
            }
        )
    }

    return (
    <>
        <Divider>Meeting List</Divider>
        <Table
            columns={columns}
            dataSource={modifiedDataWithKey}
            bordered={true}
        />
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
        {deleteMeetingId && (
            <Modal
                title="Delete Meeting"
                open={showConfirmDeleteModal}
                onCancel={handleCancelDeleteMeeting}
                footer={[
                    <Button key="cancel" onClick={handleCancelDeleteMeeting}>
                        Cancel
                    </Button>,
                    <Button key="delete" type="primary" danger onClick={handleDeleteMeeting}>
                        Delete
                    </Button>
                ]}
            >
                Are you sure you want to delete this meeting?
            </Modal>
        )}
        {editMeetingId && (
            <Modal
                title="Edit Meeting"
                centered
                open={showEditMeetingModal}
                onCancel={() => {
                    handleCloseEditMeetingModal();
                    setEditMeetingId(null);
                }}
                footer={null}
            >
                <EditMeetingInfoForm
                    meetingId={editMeetingId}
                    onClose={handleCloseEditMeetingModal}
                />
            </Modal>
        )}

    </>
    )
}

export default MeetingList;