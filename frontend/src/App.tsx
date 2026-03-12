import './App.css';
import { RouterProvider } from "react-router-dom";
import router from "./services/router";
import {AuthProvider} from "./components/auth/AuthProvider.tsx";
import { ErrorProvider } from './components/ErrorProvider.tsx';

const App = () => {
    return (
        <ErrorProvider>
            <AuthProvider>
                <RouterProvider router={router}/>
            </AuthProvider>
        </ErrorProvider>
    );
}

export default App;
