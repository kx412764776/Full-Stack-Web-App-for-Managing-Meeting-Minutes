import React from 'react'
import ReactDOM from 'react-dom/client'
import {ChakraProvider} from '@chakra-ui/react'
import {RouterProvider} from "react-router-dom";
import {BrowserRouter} from "./component/shared/BrowserRouter.jsx";
import {createStandaloneToast} from "@chakra-ui/toast";

const {ToastContainer} = createStandaloneToast();

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <ChakraProvider>
                <RouterProvider router={BrowserRouter}/>
            <ToastContainer/>
        </ChakraProvider>
    </React.StrictMode>,
)
