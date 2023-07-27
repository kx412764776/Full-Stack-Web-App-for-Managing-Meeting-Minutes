import React from 'react'
import ReactDOM from 'react-dom/client'
import {ChakraProvider} from '@chakra-ui/react'
import {RouterProvider} from "react-router-dom";
import {BrowserRouter} from "./BrowserRouter.jsx";

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <ChakraProvider>
            <RouterProvider router={BrowserRouter} />
        </ChakraProvider>
    </React.StrictMode>,
)
