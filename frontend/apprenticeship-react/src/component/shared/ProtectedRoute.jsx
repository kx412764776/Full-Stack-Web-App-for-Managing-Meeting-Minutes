import {useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {LoginAuth} from "../LoginContext.jsx";

const ProtectedRoute = ({ children }) => {

    const { isMemberAuthenticated } = LoginAuth()
    const navigate = useNavigate();

    useEffect(() => {
        if (!isMemberAuthenticated()) {
            navigate("/")
        }
    })

    return isMemberAuthenticated() ? children : "";
}

export default ProtectedRoute;