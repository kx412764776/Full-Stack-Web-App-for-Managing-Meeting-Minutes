import {createBrowserRouter} from "react-router-dom";
import WelcomePage from "./WelcomePage.jsx";
import LoginPage from "../login/LoginPage.jsx";
import RegisterPage from "../login/RegisterPage.jsx";
import ErrorPage from "./ErrorPage.jsx";
import LoginAuthProvider from "../context/LoginContext.jsx";
import ProtectedRoute from "./ProtectedRoute.jsx";
import MeetingPage from "../../MeetingPage.jsx";
import AddMinutesPage from "../minutes/AddMinutesPage.jsx";
import ViewMinutes from "../minutes/ViewMinutes.jsx";
import SignedMeetingList from "../minutes/SignedMeetingList.jsx";
import SignedAttendeeListPage from "../minutes/SignedAttendeeListPage.jsx";
import UnFullSignedMeetingList from "../minutes/UnFullSignedMeetingList.jsx";

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
            path: '/apprenticeship/meeting',
            element:
                <LoginAuthProvider>
                    <ProtectedRoute>
                        <MeetingPage/>
                    </ProtectedRoute>
                </LoginAuthProvider>,
            errorElement: <ErrorPage/>,
        },
        {
            path: '/apprenticeship/meeting/:meetingId/minutes',
            element:
                <LoginAuthProvider>
                    <ProtectedRoute>
                        <AddMinutesPage/>
                    </ProtectedRoute>
                </LoginAuthProvider>,
            errorElement: <ErrorPage/>
        },
        {
            path: '/apprenticeship/meeting/:meetingId/minutesInfo',
            element:
                <LoginAuthProvider>
                    <ProtectedRoute>
                        <ViewMinutes/>
                    </ProtectedRoute>
                </LoginAuthProvider>,
            errorElement: <ErrorPage/>
        },
        {
            path: '/apprenticeship/minutes/signed',
            element:
                <LoginAuthProvider>
                    <ProtectedRoute>
                        <SignedMeetingList/>
                    </ProtectedRoute>
                </LoginAuthProvider>,
            errorElement: <ErrorPage/>
        },
        {
            path: '/apprenticeship/minutes/unFullSigned',
            element:
                <LoginAuthProvider>
                    <ProtectedRoute>
                        <UnFullSignedMeetingList/>
                    </ProtectedRoute>
                </LoginAuthProvider>,
            errorElement: <ErrorPage/>
        },
        {
            path: '/apprenticeship/minutes/signature/:meetingId',
            element:
                <LoginAuthProvider>
                    <ProtectedRoute>
                        <SignedAttendeeListPage/>
                    </ProtectedRoute>
                </LoginAuthProvider>,
            errorElement: <ErrorPage/>
        }
    ]
)