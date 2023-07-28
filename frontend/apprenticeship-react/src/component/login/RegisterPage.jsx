import {
    Flex,
    Box,
    Stack,
    Button,
    Heading,
    Text,
    useColorModeValue,
    Link
} from '@chakra-ui/react'
import {ArrowBackIcon, ViewIcon, ViewOffIcon} from '@chakra-ui/icons'
import {Form, Formik} from "formik";
import {useNavigate} from "react-router-dom";
import * as Yup from "yup";
import {errorNotification, successNotification} from "../../services/notification.js";
import {MySelect, MyTextInput} from "../FromComponent.jsx";
import {useState} from "react";
import {register} from "../../services/client.js";


const RegisterForm = () => {
    const navigate = useNavigate();

    const [showPassword, setShowPassword] = useState(false)

    return (
        <Formik
            validateOnMount={true}
            validationSchema={
                Yup.object({
                    firstname: Yup.string()
                        .required("First name is required"),
                    lastname: Yup.string()
                        .required("Last name is required"),
                    email: Yup.string()
                        .email("Must be valid email")
                        .required("Email is required"),
                    password: Yup.string()
                        .max(20, "Password cannot be more than 20 characters")
                        .required("Password is required"),
                    memberRole: Yup.string()
                        .oneOf(
                            ['APPRENTICESHIP', 'MENTOR', 'ACADEMIC'],
                            'Invalid Role'
                        )
                        .required("Role is required")
                })
            }
            initialValues={{
                firstName: '',
                lastName: '',
                email: '',
                password: '',
                memberRole: ''}}
            onSubmit={(member, {setSubmitting}) => {
                setSubmitting(true);
                register(member).then(() => {
                    navigate(`/apprenticeship/login`)
                    successNotification("Congratulations!",
                        "You have successfully registered!")
                }).catch(err => {
                    errorNotification(
                        err.code,
                        err.response.data.message
                    )
                }).finally(() => {
                    setSubmitting(false);
                })
            }}>

            {({isValid, isSubmitting}) => (
                <Form>
                    <Stack mt={15} spacing={10} w={"400px"} >
                        <MyTextInput
                            label={"first name"}
                            name={"firstname"}
                            type={"text"}
                            placeholder={"Type your first name"}
                        />
                        <MyTextInput
                            label={"last name"}
                            name={"lastname"}
                            type={"text"}
                            placeholder={"Type your last name"}
                        />
                        <MyTextInput
                            label={"email"}
                            name={"email"}
                            type={"email"}
                            placeholder={"Type your email"}
                        />


                        <Box position="relative">
                            <MyTextInput
                                label={"Password"}
                                name={"password"}
                                type={showPassword ? "text" : "password"}
                                placeholder={"Type your password"}
                            />
                            <Button
                                onClick={() => setShowPassword(!showPassword)}
                                size="sm"
                                position="absolute"
                                right="0"
                                _hover={{ bg: 'transparent' }}
                            >
                                {showPassword ? <ViewIcon /> : <ViewOffIcon />}
                            </Button>
                            <Text fontSize="sm"
                                  color="gray.500"
                                  mt={2}
                                  position='absolute'
                                  right='12%'
                            >
                                Show your password
                                </Text>
                        </Box>

                        <MySelect
                            label={"Role"}
                            name={"memberRole"}
                        >
                            <option value="">Select your role</option>
                            <option value="APPRENTICESHIP">Apprenticeship</option>
                            <option value="MENTOR">Mentor</option>
                            <option value="ACADEMIC">Academic</option>
                        </MySelect>
                        <Button
                            type={"submit"}
                            bg={'blue.400'}
                            isDisabled={!isValid || isSubmitting}>
                            Sign in

                        </Button>
                    </Stack>
                </Form>
            )}

        </Formik>
    )
}

export default function RegisterPage() {
    const handleLogin = () => {
        window.location.href = '/login'
    }

    const handleBack = () => {
        window.location.href = '/'
    }

    return (
        <Flex
            minH={'100vh'}
            align={'center'}
            justify={'center'}
            bg={useColorModeValue('gray.50', 'gray.800')}>
            <Stack spacing={8} mx={'auto'} maxW={'lg'} py={12} px={6}>
                <Stack align={'center'}>
                    <Heading fontSize={'4xl'} textAlign={'center'}>
                        Sign up
                    </Heading>
                    <Text fontSize={'lg'} color={'gray.600'}>
                        To manage your apprenticeship meetings
                    </Text>
                </Stack>
                <Box
                    rounded={'lg'}
                    bg={useColorModeValue('white', 'gray.700')}
                    boxShadow={'lg'}
                    p={8}>
                    <Stack spacing={4}>
                        <Box pos="absolute" top="6" left="6">
                            <Button
                                size="lg"
                                colorScheme="gray"
                                leftIcon={<ArrowBackIcon/>}
                                onClick={handleBack}
                            >
                                Back
                            </Button>
                        </Box>

                        <RegisterForm/>
                        <Stack pt={6}>
                            <Text align={'center'}>
                                Already a user?
                                <Link onClick={handleLogin} color={'blue.400'}> Login</Link>
                            </Text>
                        </Stack>
                    </Stack>
                </Box>
            </Stack>
        </Flex>
    )
}