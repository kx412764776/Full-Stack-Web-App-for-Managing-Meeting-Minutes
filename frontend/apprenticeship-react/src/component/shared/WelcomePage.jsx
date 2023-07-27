import {
    Container,
    Heading,
    Stack,
    Text,
    Button
} from '@chakra-ui/react'


export default function WelcomePage() {

    // when user click on get started button, redirect to login page
    const handleGetStarted = () => {
        window.location.href = '/login'
    }

    // when user click on sign up button, redirect to register page
    const handleSignUp = () => {
        window.location.href = '/register'
    }


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
                        rounded={'full'}
                        px={6}
                        colorScheme={'blue'}
                        bg={'blue.400'}
                        _hover={{bg: 'blue.600'}}
                        onClick={handleGetStarted}
                    >
                        Get started
                    </Button>
                    <Button
                        rounded={'full'}
                        px={6}
                        onClick={handleSignUp}
                    >
                        Sign up
                    </Button>
                </Stack>
            </Stack>
        </Container>
    )
}