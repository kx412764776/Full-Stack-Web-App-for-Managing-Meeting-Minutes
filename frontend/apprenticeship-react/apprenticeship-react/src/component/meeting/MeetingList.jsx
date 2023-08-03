import {useEffect, useState} from 'react';
import {errorNotification} from '../../services/notification';
import {getMeetingInfo} from "../../services/client.js";
import {LoginAuth} from "../context/LoginContext.jsx";
import Highlighter from 'react-highlight-words';
import {Button, Input, Space, Table} from 'antd';
import {SearchOutlined} from '@ant-design/icons';

const MeetingList = () =>  {

    const { memberInfo } = LoginAuth();

    const [meetingInfoList, setMeetingInfoList] = useState([]);
    const [searchText, setSearchText] = useState('');
    const [searchedColumn, setSearchedColumn] = useState('');


    useEffect(() => {
        const fetchMeetingInfo = async () => {
            try {
                const memberEmail = memberInfo?.email;
                if (memberEmail) {
                    const res = await getMeetingInfo(memberEmail);
                    console.log(res.data);
                    setMeetingInfoList(res.data);
                }
            } catch (err) {
                errorNotification(err.code, err.response?.data?.message);
            }
        };
        fetchMeetingInfo();
    }, [memberInfo]);

    const getColumnSearchProps = (dataIndex) => ({
        filterDropdown: ({ setSelectedKeys, selectedKeys, confirm, clearFilters }) => (
            <div style={{ padding: 8 }}>
                <Input
                    id="searchInput"
                    placeholder={`Search ${dataIndex}`}
                    value={selectedKeys[0]}
                    onChange={(e) => setSelectedKeys(e.target.value ? [e.target.value] : [])}
                    onPressEnter={() => handleSearch(selectedKeys, confirm, dataIndex)}
                    style={{ marginBottom: 8, display: 'block' }}
                />
                <Space>
                    <Button
                        type="primary"
                        onClick={() => handleSearch(selectedKeys, confirm, dataIndex)}
                        icon={<SearchOutlined />}
                        size="small"
                        style={{ width: 90 }}
                    >
                        Search
                    </Button>
                    <Button onClick={() => handleReset(clearFilters)} size="small" style={{ width: 90 }}>
                        Reset
                    </Button>
                </Space>
            </div>
        ),
        filterIcon: (filtered) => <SearchOutlined style={{ color: filtered ? '#1890ff' : undefined }} />,
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
                    highlightStyle={{ backgroundColor: '#ffc069', padding: 0 }}
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
            }else if (key === 'meetingDate') {
                return new Date(valueA) - new Date(valueB);
            }else {
                return valueA - valueB;
            }
        },
        ...getColumnSearchProps(key), // Apply search functionality to each column
    })) : [];

    // change the format of memberDate
    const modifiedData = meetingInfoList.map(item => {
        const { meetingDate, ...rest } = item;
        const modifiedMemberDate = new Date(meetingDate).toLocaleString().split(',')[0];
        return { ...rest, meetingDate: modifiedMemberDate };
    });

    const modifiedDataWithKey =
        modifiedData.map((row, index) =>
            ({ ...row, key: row[0] || index }));

    return <Table columns={columns} dataSource={modifiedDataWithKey} />;
}

export default MeetingList;



