import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../hooks/useAuth.tsx";

export function ProtectedRoute({ requiredGroups }: { requiredGroups?: string[] }) {
    const { isAuthenticated, groups, loading } = useAuth();
    if(loading) return null;

    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;
    }

    if (requiredGroups && requiredGroups.length > 0) {
        const ok = requiredGroups.some((g) => groups.includes(g));
        if (!ok) return <Navigate to="/forbidden" replace />;
    }

    return <Outlet />;
}