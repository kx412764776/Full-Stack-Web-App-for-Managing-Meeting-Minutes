import {
    createContext,
    useContext,
    useEffect,
    useState
} from "react";
import {login as performLogin} from "../services/client.js";
import jwtDecode from "jwt-decode";

const LoginAuthContext = createContext({});

const LoginAuthProvider = ({children}) => {

    const [member, setMember] = useState(null);


    const setMemberFromToken = () => {
        let token = localStorage.getItem("access_token");
        if (token) {
            token = jwtDecode(token);
            setMember({
                username: token.sub,
                roles: token.scopes
            })
        }
    }
    useEffect(() => {
        setMemberFromToken()
    }, [])

    const memberEmail = () => {
        let token = localStorage.getItem("access_token");
        if (token) {
            token = jwtDecode(token);
            return token.sub;
        }
    }

    const login = async (usernameAndPassword) => {
        return new Promise((resolve, reject) => {
            performLogin(usernameAndPassword).then(res => {
                const jwtToken = res.headers["authorization"];
                localStorage.setItem("access_token", jwtToken);

                const decodedToken = jwtDecode(jwtToken);

                setMember({
                    username: decodedToken.sub,
                    roles: decodedToken.scopes
                })
                resolve(res);
            }).catch(err => {
                reject(err);
            })
        })
    }

    const logOut = () => {
        localStorage.removeItem("access_token")
        setMember(null)
    }

    const isMemberAuthenticated = () => {
        const token = localStorage.getItem("access_token");
        if (!token) {
            return false;
        }
        const {exp: expiration} = jwtDecode(token);
        if (Date.now() > expiration * 1000) {
            logOut()
            return false;
        }
        return true;
    }

    return (
        <LoginAuthContext.Provider value={{
            member,
            login,
            logOut,
            isMemberAuthenticated,
            setMemberFromToken,
            memberEmail
        }}>
            {children}
        </LoginAuthContext.Provider>
    )
}

export const LoginAuth = () => useContext(LoginAuthContext);

export default LoginAuthProvider;