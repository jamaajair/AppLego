import { useAuth } from "../hooks/useAuth.tsx";
import { Navigate } from "react-router-dom";

export function RootRedirect(){
    const { isAuthenticated } = useAuth();
    return <Navigate to={isAuthenticated ? "/user" : "/login"} replace />;
}