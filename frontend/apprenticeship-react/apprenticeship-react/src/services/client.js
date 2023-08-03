import axios from 'axios';

const getAuthConfig = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem("access_token")}`
    }
})

export const getMemberInfo= async (memberEmail) => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/memberInfo/${memberEmail}`,
            memberEmail,
            getAuthConfig()
        )
    } catch (error) {
        console.log(error);
    }
}

export const login = async (usernameAndPassword) => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/login`,
            usernameAndPassword
        );
    }catch (error) {
        console.log(error);
    }
}

export const register = async (member) => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/register`,
            member
        );
    }catch (error) {
        console.log(error);
    }
}

export const getMeetingInfo = async (memberEmail) => {
    try {
        return await axios.post(
            `http://localhost:8090/apprenticeship/meeting/${memberEmail}`,
            memberEmail,
            getAuthConfig()
        )
    } catch (error) {
        console.log(error);
    }
}