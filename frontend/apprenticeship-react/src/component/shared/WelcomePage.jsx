import {
    Container,
    Heading,
    Stack,
    Text,
    Button
} from '@chakra-ui/react'
import { Link } from 'react-router-dom';

export default function WelcomePage() {


    return (
        <Container maxW={'5xl'}>
            <Stack
                textAlign={'center'}
                align={'center'}
                spacing={{base: 8, md: 10}}
                py={{base: 20, md: 28}}
                justify={'center'}
            >
                <Heading
                    fontWeight={600}
                    fontSize={{base: '3xl', sm: '4xl', md: '6xl'}}
                    lineHeight={'110%'}>
                    Welcome to Manage Your{' '}
                    <Text as={'span'} color={'blue.400'}>
                        Apprenticeship Meetings
                    </Text>
                </Heading>
                <Text color={'gray.500'} maxW={'3xl'}>
                    This application is aim to help academics,
                    student apprentices and mentors effectively manage and track their apprenticeship meetings.
                    You can record and sign meeting minutes, track action points, upload relevant documents,
                    and stay up-to-date on the status of everything discussed during the meeting.
                    Start using the app now to make your apprenticeship meetings more organised and productive!
                </Text>
                <Stack spacing={6} direction={'row'}>
                    <Button
                        as={Link}
                        to={'/apprenticeship/login'}
                        rounded={'full'}
                        px={6}
                        colorScheme={'blue'}
                        bg={'blue.400'}
                        _hover={{bg: 'blue.600'}}

                    >
                        Get started
                    </Button>
                    <Button
                        as={Link}
                        to={'/apprenticeship/register'}
                        rounded={'full'}
                        px={6}
                    >
                        Sign up
                    </Button>
                </Stack>
            </Stack>
        </Container>
    )
}