import { jwtDecode } from "jwt-decode";

const STORAGE_KEY = "access_token";

type JwtPayload = {
    exp?: number;
    sub?: string;
    groups?: string[];
};

export function setToken(token: string) {
    sessionStorage.setItem(STORAGE_KEY, token);
}

export function getToken(): string | null {
    return sessionStorage.getItem(STORAGE_KEY);
}

export function clearToken() {
    sessionStorage.removeItem(STORAGE_KEY);
}

export function decodeToken(token: string): JwtPayload | null {
    try {
        return jwtDecode<JwtPayload>(token);
    } catch {
        return null;
    }
}

export function isTokenExpired(token: string): boolean {
    const decoded = decodeToken(token);
    if (!decoded?.exp) return true;

    const now = Math.floor(Date.now() / 1000);
    return now >= decoded.exp;
}
