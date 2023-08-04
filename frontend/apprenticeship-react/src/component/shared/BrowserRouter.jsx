import {createBrowserRouter} from "react-router-dom";
import WelcomePage from "./WelcomePage.jsx";
import Dashboard from "../../Dashboard.jsx";
import LoginPage from "../login/LoginPage.jsx";
import RegisterPage from "../login/RegisterPage.jsx";
import ErrorPage from "./ErrorPage.jsx";
import LoginAuthProvider from "../context/LoginContext.jsx";
import ProtectedRoute from "./ProtectedRoute.jsx";
import MeetingPage from "../../MeetingPage.jsx";

export const BrowserRouter = createBrowserRouter(
    [
        {
            path: '/',
            element: <WelcomePage/>,
            errorElement: <ErrorPage/>
        },
        {
            path: '/apprenticeship',
            element: <WelcomePage/>,
            errorElement: <ErrorPage/>
        },
        {
            path: '/apprenticeship/login',
            element: <LoginAuthProvider><LoginPage/></LoginAuthProvider>,
            errorElement: <ErrorPage/>
        },
        {
            path: "/apprenticeship/register",
            element: <RegisterPage/>,
            errorElement: <ErrorPage/>
        },
        {
            path: '/apprenticeship/dashboard',
            element:
                <LoginAuthProvider>
                    <ProtectedRoute>
                            <Dashboard/>
                    </ProtectedRoute>
                </LoginAuthProvider>,
            errorElement: <ErrorPage/>
        },
        {
            path: '/apprenticeship/meeting',
            element:
                <LoginAuthProvider>
                    <ProtectedRoute>
                        <MeetingPage/>
                    </ProtectedRoute>
                </LoginAuthProvider>,
            errorElement: <ErrorPage/>
        }
    ]
)