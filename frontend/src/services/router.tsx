import { createBrowserRouter } from "react-router-dom";

import Layout from "../components/Layout";
import MyDossiers from "../pages/dossiers/my_dossiers";
import NewDossierPage from "../pages/dossiers/new";
import Dossier from "../pages/dossiers/dossier";
// import Stakeholders from "../pages/stakeholders";
import Home from "../pages/home";

export const router = createBrowserRouter([
    {
        path: "/",
        element: <Layout />,
        children: [
            { index: true, element: <Home /> },
        ],
    },

    {
        path: "/user",
        element: <Layout />,
        children: [
            {
                index: true,
                element: <MyDossiers />,
            },
            {
                path: "dossiers",
                children: [
                    {
                        index: true,
                        element: <MyDossiers />,
                    },
                    {
                        path: "new",
                        element: <NewDossierPage />,
                    },
                    {
                        path: ":id",
                        element: <Dossier />,
                    },
                ],
            },
        ],
    },
]);

export default router;
