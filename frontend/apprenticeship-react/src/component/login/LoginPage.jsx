import {
    Flex,
    Box,
    Stack,
    Button,
    Heading,
    useColorModeValue,
    Link,
    Text,
} from '@chakra-ui/react'
import {ArrowBackIcon, ViewIcon, ViewOffIcon} from "@chakra-ui/icons";
import {Form, Formik} from "formik";
import * as Yup from 'yup';
import {LoginAuth} from "../context/LoginContext.jsx";
import {MyTextInput} from "../FormComponent.jsx";
import {useState} from "react";

const LoginForm = () => {
    const {login} = LoginAuth();

    const [showPassword, setShowPassword] = useState(false)

    return (
        <Formik
            validateOnMount={true}
            validationSchema={
                Yup.object({
                    username: Yup.string()
                        .email("Must be valid email")
                        .required("Email is required"),
                    password: Yup.string()
                        .max(20, "Password cannot be more than 20 characters")
                        .required("Password is required")
                })
            }
            initialValues={{username: '', password: ''}}
            onSubmit={(values, {setSubmitting}) => {
                setSubmitting(true);
                login(values).then(() => {
                    window.location.href = "/apprenticeship/meeting"
                }).catch(err => {
                    console.log(err)
                }).finally(() => {
                    setSubmitting(false);
                })
            }}>

            {({isValid, isSubmitting}) => (
                <Form>
                    <Stack mt={15} spacing={15}>
                        <MyTextInput
                            label={"Please enter your email"}
                            name={"username"}
                            type={"email"}
                            placeholder={"joe@gmail.com"}
                        />
                        <Box position={"relative"}>
                            <MyTextInput
                                label={"Please enter your password"}
                                name={"password"}
                                type={showPassword ? "text" : "password"}
                                placeholder={"Type your password"}
                            />
                            <Button
                                onClick={() => setShowPassword(!showPassword)}
                                size="sm"
                                position="absolute"
                                right="0"
                                _hover={{bg: 'transparent'}}
                            >
                                {showPassword ? <ViewIcon/> : <ViewOffIcon/>}
                            </Button>

                        </Box>
                        <Button
                            type={"submit"}
                            bg={'blue.400'}
                            mt={8}
                            isDisabled={!isValid || isSubmitting}>
                            Sign in
                        </Button>
                    </Stack>
                </Form>
            )}

        </Formik>
    )
}

export default function LoginPage() {
    const { member } = LoginAuth();
    if (member) {
        window.location.href = "/apprenticeship/meeting"
    }

    return (
        <Flex
            minH={'100vh'}
            align={'center'}
            justify={'center'}
            bg={useColorModeValue('gray.50', 'gray.800')}>
            <Box pos="absolute" top="6" left="6">
                <Button
                    size="lg"
                    colorScheme="gray"
                    leftIcon={<ArrowBackIcon/>}
                    onClick={
                        () => {
                            window.location.href = "/"
                        }
                    }
                >
                    Back
                </Button>
            </Box>
            <Stack spacing={8} mx={'auto'} maxW={'lg'} py={12} px={6}>
                <Stack align={'center'}>
                    <Heading fontSize={'4xl'}>Sign in to your account</Heading>
                </Stack>
                <Box
                    rounded={'lg'}
                    bg={useColorModeValue('white', 'gray.700')}
                    boxShadow={'lg'}
                    p={8}>
                    <Stack spacing={4}>
                        <LoginForm/>
                    </Stack>
                    <Text fontSize={'sm'} color={'gray.600'} textAlign={'center'} mt={5}>
                        Don't have an account? &nbsp;
                        <Link color={"blue.500"}
                              onClick={() => window.location.href='/apprenticeship/register'}
                        >
                            Sign up here!
                        </Link>
                    </Text>

                </Box>
            </Stack>
        </Flex>
    )
}