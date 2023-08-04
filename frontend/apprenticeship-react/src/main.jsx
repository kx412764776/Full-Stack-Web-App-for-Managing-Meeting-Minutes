import ReactDOM from 'react-dom/client'
import {ChakraProvider} from '@chakra-ui/react'
import {RouterProvider} from "react-router-dom";
import {BrowserRouter} from "./component/shared/BrowserRouter.jsx";


ReactDOM.createRoot(document.getElementById('root')).render(
        <ChakraProvider>
            <RouterProvider router={BrowserRouter}>
            </RouterProvider>
        </ChakraProvider>
)
