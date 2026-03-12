import { createContext } from "react";
import type { AuthState } from "../types/auth.types"

export const AuthContext = createContext<AuthState | null>(null);