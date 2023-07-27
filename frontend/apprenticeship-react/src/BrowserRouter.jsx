import {createBrowserRouter} from "react-router-dom";
import WelcomePage from "./component/shared/WelcomePage.jsx";
import Dashboard from "./Dashboard.jsx";
import LoginPage from "./component/login/LoginPage.jsx";
import RegisterPage from "./component/login/RegisterPage.jsx";

export const BrowserRouter = createBrowserRouter(
    [{
        path: '/',
        element: <WelcomePage />
    },
        {
          path: '/login',
            element: <LoginPage />
        },
        {
          path: "/register",
            element: <RegisterPage />
        },
        {
            path: '/dashboard',
            element: <Dashboard />
        }]
)