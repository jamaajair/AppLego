import React, { useEffect, useMemo, useState } from "react";
import { jwtDecode } from "jwt-decode";
import { api } from "../../services/api.ts";
import { clearToken, getToken, setToken, isTokenExpired } from "../../services/auth.ts";
import { AuthContext } from "../../lib/AuthContext.ts"
import type { AuthState, JwtPayload, LoginResponse } from "../../types/auth.types.ts";

export function AuthProvider({ children }: { children: React.ReactNode }) {
    const [token, setTokenState] = useState<string | null>(null);
    const [userId, setUserId] = useState<string | null>(null);
    const [groups, setGroups] = useState<string[]>([]);
    const [loading, setLoading] = useState(true);

    function applyToken(t: string | null) {
        setTokenState(t);

        if (!t) {
            setUserId(null);
            setGroups([]);
            return;
        }

        const payload = jwtDecode<JwtPayload>(t);
        setUserId(payload.sub ?? null);
        setGroups(payload.groups ?? []);
    }

    useEffect(() => {
        const t = getToken();
        if (t && !isTokenExpired(t)) {
            applyToken(t);
        } else {
            clearToken();
            applyToken(null);
        }
        setLoading(false);
    }, []);

    async function login(username: string) {
        const data = await api.post<LoginResponse>("/auth/login", { username });
        const t = data.access_token;

        setToken(t);
        applyToken(t);
    }

    function logout() {
        clearToken();
        applyToken(null);
    }

    const value = useMemo<AuthState>(
        () => ({
            token,
            userId,
            groups,
            loading,
            isAuthenticated: !!token,
            login,
            logout
        }),
        [token, userId, groups, loading]
    );

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
