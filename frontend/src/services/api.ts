import type {AxiosRequestConfig} from "axios";
import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api";

const apiClient = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

apiClient.interceptors.response.use(
    (response) => response,
    (error) => {
        console.error("Error :", error.response?.status, error.response?.data);
        return Promise.reject(error);
    }
);

export const api = {
    get: async (endpoint: string, config?: AxiosRequestConfig) => {
        const response = await apiClient.get(
            endpoint.startsWith('/') ? endpoint : `/${endpoint}`, 
            config
        );
        return response.data;
    },
    post: async (endpoint: string, body: unknown, config?: AxiosRequestConfig) => {
        return await apiClient.post(endpoint, body, config);
    },
    patch: async (endpoint: string, body: unknown, config?: AxiosRequestConfig) => {
        return await apiClient.patch(endpoint, body, config);
    },
    delete: async (endpoint: string, config?: AxiosRequestConfig) => {
        return await apiClient.delete(endpoint, config);
    },
};