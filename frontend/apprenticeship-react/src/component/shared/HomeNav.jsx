import { useState } from 'react';
import minutesLogo from '../../assets/minutes.svg';
import {
    Box,
    Flex,
    Text,
    IconButton,
    Stack,
    Icon,
    Popover,
    PopoverTrigger,
    PopoverContent,
    useColorModeValue,
    Menu,
    MenuButton,
    MenuList,
    MenuItem,
    MenuDivider,
    Image
} from '@chakra-ui/react';
import { HamburgerIcon, CloseIcon, ChevronRightIcon } from '@chakra-ui/icons';


export default function HomeNav() {
    const [isOpen, setIsOpen] = useState(false);

    return (
        <Box>
            <Flex
                bg={useColorModeValue('white', 'gray.800')}
                color={useColorModeValue('gray.600', 'white')}
                minH="60px"
                py={{ base: 2 }}
                px={{ base: 4 }}
                borderBottom={1}
                borderStyle="solid"
                borderColor={useColorModeValue('gray.200', 'gray.900')}
                align="center"
            >
                <Flex flex={{ base: 1, md: 'auto' }} ml={{ base: -2 }} display={{ base: 'flex', md: 'none' }}>
                    <IconButton
                        onClick={() => setIsOpen(!isOpen)}
                        icon={isOpen ? <CloseIcon w={3} h={3} /> : <HamburgerIcon w={5} h={5} />}
                        variant="ghost"
                        aria-label="Toggle Navigation"
                    />
                </Flex>
                <Flex flex={{ base: 1 }} justify={{ base: 'center', md: 'start' }}>
                    <Image src={minutesLogo}
                           alt="logo"
                           boxSize="50px"
                    />
                    <Flex display={{ base: 'none', md: 'flex' }} ml={10} mt={3}>
                        <Nav />
                    </Flex>
                </Flex>

                <Flex flex={{ base: 1, md: 0 }} justify="flex-end" align="center">
                    {/*TODO: replace with user name*/}
                    <Box mr={4}>userName</Box>
                    <Menu>
                        <MenuButton
                            as={IconButton}
                            aria-label='Options'
                            icon={<HamburgerIcon />}
                            variant='outline'
                        />
                        <MenuList>
                            <MenuDivider />
                            <MenuItem>
                                Account Settings
                            </MenuItem>
                            <MenuItem>
                                Logout
                            </MenuItem>
                        </MenuList>
                    </Menu>
                </Flex>
            </Flex>
        </Box>
    );
}

function SubNav({ label, href, subLabel }) {
    return (
        <Box
            as="a"
            href={href}
            role="group"
            display="block"
            p={2}
            rounded="md"
            _hover={{ bg: useColorModeValue('gray.200', 'gray.900') }}
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
                    _groupHover={{ opacity: '100%', transform: 'translateX(0)' }}
                    justify="flex-end"
                    align="center"
                    flex={1}
                >
                    <Icon color="pink.400" w={5} h={5} as={ChevronRightIcon} />
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

                        {navItem.children && (
                            <PopoverContent border={0} boxShadow="xl" bg={popoverContentBgColor} p={4} rounded="xl" minW="sm">
                                <Stack>
                                    {navItem.children.map(child => (
                                        <SubNav key={child.label} {...child} />
                                    ))}
                                </Stack>
                            </PopoverContent>
                        )}
                    </Popover>
                </Box>
            ))}
        </Stack>
    );
}

const NAV_ITEMS = [
    {
        label: 'Home',
        href: '/dashboard',
    },
    {
        label: 'Meeting',
        href: '/meeting',
    },
    {
        label: 'To Sign',
        href: '/signatures',
    },
    {
        label: 'About',
        children: [
            {
                label: 'About Us',
                subLabel: 'Find out more about this website',
                href: '/about',
            },
            {
                label: 'Feedback',
                subLabel: 'Give us your feedback!',
                href: '/feedback',
            },
        ],
    },
];

