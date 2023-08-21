import {HomeNav} from "../shared/HomeNav.jsx";
import {Button, Divider, Input, Space, Table} from "antd";
import {SearchOutlined} from "@ant-design/icons";
import Highlighter from "react-highlight-words";
import {useEffect, useState} from "react";
import {
    getNotFullSignedMeetingList, getNotSignedMeetingListByMemberEmail,
} from "../../services/client.js";
import {LoginAuth} from "../context/LoginContext.jsx";

const UnFullSignedMeetingList = () => {

    const {memberInfo} = LoginAuth();
    const [searchText, setSearchText] = useState('');
    const [searchedColumn, setSearchedColumn] = useState('');
    const [unFullSignedMeetingList, setUnFullSignedMeetingList] = useState([]);

    const fetchNotFullSignedMeetingList = async () => {
        try {
            if (memberInfo.memberRoles == "ACADEMIC") {
                const res = await getNotFullSignedMeetingList();
                setUnFullSignedMeetingList(res.data);
            } else {
                const res = await getNotSignedMeetingListByMemberEmail(memberInfo.email);
                setUnFullSignedMeetingList(res.data);
            }
        } catch (error) {
            console.log(error.message);
        }
    }

    useEffect(() => {
        fetchNotFullSignedMeetingList();
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

    const columns = unFullSignedMeetingList.length > 0 ? Object.keys(unFullSignedMeetingList[0]).slice(1).map((key) => ({
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
    const modifiedData = unFullSignedMeetingList.map(item => {
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

    if (memberInfo.memberRoles == 'ACADEMIC') {
        columns.push(
            {
                title: 'View Signature Status',
                dataIndex: 'viewSignatureStatus',
                render: (_, record) => (
                    <Button
                        type="primary"
                        onClick={() => {
                            window.location.href = `/apprenticeship/minutes/signature/${record.meetingId}`;
                        }}
                    >
                        View Signature Status
                    </Button>
                ),
            }
        )
    } else if (memberInfo.memberRoles == 'APPRENTICE' || memberInfo.memberRoles == 'MENTOR') {
        columns.push(
            {
                title: 'To Sign',
                dataIndex: 'toSign',
                render: (_, record) => (
                    <Button
                        type="primary"
                        onClick={() => {
                            window.location.href = `/apprenticeship/meeting/${record.meetingId}/minutesInfo`;
                        }}
                    >
                        To Sign
                    </Button>
                ),
            }
        )
    }


    return (
        <>
            <HomeNav/>
            <Divider>UnSigned Meeting List</Divider>
            <Table
                columns={columns}
                dataSource={modifiedDataWithKey}
            />
        </>
    )

}

export default UnFullSignedMeetingList;