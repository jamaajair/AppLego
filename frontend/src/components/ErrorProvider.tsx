import { createContext, useContext, useState } from "react";
import AppErrorPopup from "../components/ui/AppErrorPopup";

type ErrorContextType = {
    showError: (message: string) => void;
};

const ErrorContext = createContext<ErrorContextType | null>(null);

export function ErrorProvider({ children }: { children: React.ReactNode }) {
    const [error, setError] = useState<string | null>(null);

    return (
        <ErrorContext.Provider value={{ showError: setError }}>
            {children}
            <AppErrorPopup message={error} onClose={() => setError(null)} />
        </ErrorContext.Provider>
    );
}

export function useError() {
    const ctx = useContext(ErrorContext);
    if (!ctx) throw new Error("useError must be used within an ErrorProvider");
    return ctx;
}