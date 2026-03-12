import { createBrowserRouter } from "react-router-dom";

import Layout from "../components/layout/Layout.tsx";
import DeploymentsPage from "../pages/deployments";
import NewDeploymentPage from "../pages/deployments/new";
import Dossier from "../pages/dossiers/dossier";
import MyDossiers from "../pages/dossiers/my_dossiers";
import NewDossierPage from "../pages/dossiers/new";
import Home from "../pages/index.tsx";

import LoginPage from "../components/auth/Login.tsx";
import { ProtectedRoute } from "./protected";
import {RootRedirect} from "./rootredirect";

export const router = createBrowserRouter([
    // ---- Public ----
    {
        path: "/",
        element: <RootRedirect />,
    },
    {
        path: "/login",
        element: <LoginPage />,
    },
    {
        path: "/forbidden",
        element: <div>403 Forbidden</div>,
    },

    // ---- Authenticated ----
    {
        element: <ProtectedRoute />,
        children: [
            {
                path: "/user",
                element: <Layout />,
                children: [
                    { index: true, element: <Home /> },
                    { path: "home", element: <Home /> },
                    {
                        path: "dossiers",
                        children: [
                            { index: true, element: <MyDossiers /> },
                            { path: "new", element: <NewDossierPage /> },
                            { path: ":id", element: <Dossier /> },
                        ],
                    },
                ],
            },
        ],
    },

    // ---- Admin ----
    {
        element: <ProtectedRoute requiredGroups={["ADMIN"]} />,
        children: [
            {
                path: "/admin",
                element: <Layout />,
                children: [
                    { index: true, element: <Home /> },
                    { path: "deployments", element: <DeploymentsPage /> },
                    { path: "deployments/new", element: <NewDeploymentPage /> },
                ],
            },
        ],
    },

    // ---- Fallback ----
    { path: "*", element: <div>404</div> },
]);

export default router;