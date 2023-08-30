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
import {message} from "antd";
import {Form, Formik} from "formik";
import * as Yup from "yup";
import {MySelect, MyTextInput} from "../FormComponent.jsx";
import {useState} from "react";
import {register} from "../../services/client.js";
import {LoginAuth} from "../context/LoginContext.jsx";


const RegisterForm = ({ onSuccess }) => {

    const [showPassword, setShowPassword] = useState(false)

    return (
        <Formik
            validateOnMount={true}
            validationSchema={
                Yup.object({
                    firstName: Yup.string()
                        .matches(/^[a-zA-Z]*$/, 'Only letters are allowed')
                        .required("First name is required"),
                    lastName: Yup.string()
                        .matches(/^[a-zA-Z]*$/, 'Only letters are allowed')
                        .required("Last name is required"),
                    email: Yup.string()
                        .email("Must be valid email")
                        .required("Email is required"),
                    password: Yup.string()
                        .max(20, "Password cannot be more than 20 characters")
                        .required("Password is required"),
                    memberRole: Yup.string()
                        .oneOf(
                            ['APPRENTICE', 'MENTOR', 'ACADEMIC'],
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
                register(member)
                    .then(res => {
                        if (res.status == 200) {
                            window.location.href = "/apprenticeship/login";
                            onSuccess(res.headers["authorization"]);
                        }
                    }).catch(err => {
                    console.log(err);
                    message.error("Email already registered");
                }).finally(() => {
                    setSubmitting(false);
                })
            }}
        >

            {({isValid, isSubmitting}) => (
                <Form>
                    <Stack mt={15} spacing={10} w={"400px"} >
                        <MyTextInput
                            label={"First name"}
                            name={"firstName"}
                            type={"text"}
                            placeholder={"Type your first name"}
                        />
                        <MyTextInput
                            label={"Last name"}
                            name={"lastName"}
                            type={"text"}
                            placeholder={"Type your last name"}
                        />
                        <MyTextInput
                            label={"Email"}
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
                            <option value="APPRENTICE">Apprentice</option>
                            <option value="MENTOR">Mentor</option>
                            <option value="ACADEMIC">Academic</option>
                        </MySelect>
                        <Button
                            type={"submit"}
                            bg={'blue.400'}
                            isDisabled={!isValid || isSubmitting}>
                            Register

                        </Button>
                    </Stack>
                </Form>
            )}

        </Formik>
    )
}

export default function RegisterPage() {

    const {setMemberFromToken} = LoginAuth();

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
                                onClick={() => window.location.href = "/apprenticeship"}
                                size="lg"
                                colorScheme="gray"
                                leftIcon={<ArrowBackIcon/>}

                            >
                                Back
                            </Button>
                        </Box>

                        <RegisterForm
                            onSuccess={(token) => {
                                localStorage.setItem("token", token)
                                setMemberFromToken()
                                window.location.href = "/apprenticeship"
                            }}
                        />
                        <Stack pt={6}>
                            <Text align={'center'}>
                                Already a user? &nbsp;
                                <Link
                                    color={"blue.500"}
                                    onClick={() => window.location.href = "/apprenticeship/login"}
                                >
                                    Login
                                </Link>
                            </Text>
                        </Stack>
                    </Stack>
                </Box>
            </Stack>
        </Flex>
    )
}