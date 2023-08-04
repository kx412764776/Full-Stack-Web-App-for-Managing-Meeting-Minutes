import minutesLogo from '../../assets/minutes.svg';
import {
    Box,
    Flex,
    Text,
    IconButton,
    Stack,
    Popover,
    PopoverTrigger,
    PopoverContent,
    useColorModeValue,
    Menu,
    MenuButton,
    MenuList,
    MenuItem,
    Image,
    MenuDivider, Portal
} from '@chakra-ui/react';
import {HamburgerIcon} from '@chakra-ui/icons';
import {LoginAuth} from "../context/LoginContext.jsx";
import { useNavigate} from "react-router-dom";
import {useEffect} from "react";


export function HomeNav() {

    const {memberInfo, logOut} = LoginAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (!memberInfo) {
            navigate('/login')
            setTimeout(() => {
                logOut();
            }, 0)
        }
    }, [memberInfo, navigate]);


    return (
        <Box>
            <Flex
                bg={useColorModeValue('white', 'gray.800')}
                color={useColorModeValue('gray.600', 'white')}
                minH="60px"
                py={{base: 2}}
                px={{base: 4}}
                borderBottom={1}
                borderStyle="solid"
                borderColor={useColorModeValue('gray.200', 'gray.900')}
                align="center"
            >

                <Flex flex={{base: 1}} justify={{base: 'center', md: 'start'}}>
                    <Image src={minutesLogo}
                           alt="logo"
                           boxSize="50px"
                    />
                    <Flex display={{base: 'none', md: 'flex'}} ml={10} mt={3}>
                        <Nav/>
                    </Flex>
                </Flex>

                <Flex flex={{base: 1, md: 1}} justify="flex-end" align="center">
                    <Box mr={4}>{memberInfo.memberRoles}</Box>
                    <Menu>
                        <MenuButton
                            as={IconButton}
                            aria-label='Options'
                            icon={<HamburgerIcon/>}
                            variant='outline'
                        />
                        <Portal>
                            <MenuList>
                                <Text
                                    ml={2}
                                    justifyContent={'center'}
                                    style={{zIndex: 1000}}
                                >
                                    Email: <br/>
                                    {memberInfo.email}
                                </Text>
                                <MenuDivider/>
                                <MenuItem>
                                    Account Settings
                                </MenuItem>
                                <MenuItem
                                    onClick={() => {
                                        navigate(`/`)
                                        logOut();
                                    }}
                                >
                                    Logout
                                </MenuItem>
                            </MenuList>
                        </Portal>
                    </Menu>
                </Flex>
            </Flex>
        </Box>
    );
}

function SubNav({label, href, subLabel}) {
    return (
        <Box
            as="a"
            href={href}
            role="group"
            display="block"
            p={2}
            rounded="md"
            _hover={{bg: useColorModeValue('gray.200', 'gray.900')}}
        >
            <Stack direction="row" align="center">
                <Box>
                    <Text transition="all .3s ease" fontWeight={500}>
                        {label}
                    </Text>
                    <Text fontSize="sm">{subLabel}</Text>
                </Box>
                <Flex
                    transition="all .3s ease"
                    transform="translateX(-10px)"
                    opacity={0}
                    _groupHover={{opacity: '100%', transform: 'translateX(0)'}}
                    justify="flex-end"
                    align="center"
                    flex={1}
                >
                </Flex>
            </Stack>
        </Box>
    );
}

function Nav() {
    const linkColor = useColorModeValue('gray.600', 'gray.200');
    const linkHoverColor = useColorModeValue('gray.800', 'white');
    const popoverContentBgColor = useColorModeValue('white', 'gray.800');

    return (
        <Stack direction="row" spacing={4}>
            {NAV_ITEMS.map(navItem => (
                <Box key={navItem.label}>
                    <Popover trigger="hover" placement="bottom-start">
                        <PopoverTrigger>
                            <Box
                                as="a"
                                p={2}
                                href={navItem.href || '#'}
                                fontSize="medium"
                                fontWeight={500}
                                color={linkColor}
                                _hover={{
                                    textDecoration: 'none',
                                    color: linkHoverColor,
                                }}
                            >
                                {navItem.label}
                            </Box>
                        </PopoverTrigger>

                        {navItem.children &&
                            <PopoverContent border={0} boxShadow="xl" bg={popoverContentBgColor} p={4} rounded="xl"
                                            minW="sm">
                                <Stack>
                                    {navItem.children.map(child => (
                                        <SubNav key={child.label} {...child} />
                                    ))}
                                </Stack>
                            </PopoverContent>}
                    </Popover>
                </Box>
            ))}
        </Stack>
    );
}

const NAV_ITEMS = [
    {
        label: 'Home',
        href: '/apprenticeship/dashboard',
    },
    {
        label: 'Meeting',
        href: '/apprenticeship/meeting',
    },
    {
        label: 'To Sign',
        href: '/apprenticeship/signatures',
    },
    {
        label: 'About',
        children: [
            {
                label: 'About Us',
                subLabel: 'Find out more about this website',
                href: '/apprenticeship/about',
            },
            {
                label: 'Feedback',
                subLabel: 'Give us your feedback!',
                href: '/apprenticeship/feedback',
            },
        ],
    },
];

