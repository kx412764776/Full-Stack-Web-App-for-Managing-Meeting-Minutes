import { useRouteError } from "react-router-dom";
import {Flex} from "@chakra-ui/react";

export default function ErrorPage() {
    const error = useRouteError();
    console.error(error);

    return (
        <Flex direction={"column"} align={"center"} justify={"center"} h={"100vh"} w={"100vw"} bg={"gray.100"} color={"gray.700"} >
            <h1>Oops!</h1>
            <p>Sorry, an unexpected error has occurred.</p>
            <p>
                <b>Error:</b> <i>{ error.status }</i> &nbsp;
                <i>{ error.statusText || error.message}</i>
            </p>
        </Flex>
    );
}