import {useEffect} from "react";
import {useNavigate} from "react-router-dom";
import {LoginAuth} from "../context/LoginContext.jsx";
import {errorNotification} from "../../services/notification.js";

const ProtectedRoute = ({ children }) => {

    const { isMemberAuthenticated } = LoginAuth()
    const navigate = useNavigate();

    useEffect(() => {
        if (!isMemberAuthenticated()) {
            navigate("/")
            errorNotification("Unauthorized", "You need to login first!");
        }
    })

    return isMemberAuthenticated() ? children : "";
}

export default ProtectedRoute;