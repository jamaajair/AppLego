import type { AxiosRequestConfig } from "axios";
import axios from "axios";
import { clearToken, getToken, isTokenExpired } from "./auth.ts";

export const API_BASE_URL =
    import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080/api";

const apiClient = axios.create({
    baseURL: API_BASE_URL,
    headers: { "Content-Type": "application/json" },
});

apiClient.interceptors.request.use((config) => {
    const token = getToken();
    if (token && !isTokenExpired(token) && !config.url?.includes("/auth/login")) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

apiClient.interceptors.response.use(
    (response) => response,
    (error) => {
        const status = error.response?.status;
        console.error("Error :", status, error.response?.data);
        if (status === 401) {
            clearToken();
            window.location.href = "/login";
        }

        const message = error.response?.data?.message;
        return Promise.reject(new Error(typeof message === "string" ? message : "Request failed"));
    }
);

function normalizeEndpoint(endpoint: string) {
    return endpoint.startsWith("/") ? endpoint : `/${endpoint}`;
}

/** GET */
function get(endpoint: string, config?: AxiosRequestConfig): Promise<unknown>;
function get<T>(endpoint: string, config?: AxiosRequestConfig): Promise<T>;
async function get<T>(endpoint: string, config?: AxiosRequestConfig): Promise<T> {
    const response = await apiClient.get<T>(normalizeEndpoint(endpoint), config);
    return response.data;
}

/** POST */
function post(endpoint: string, body?: unknown, config?: AxiosRequestConfig): Promise<unknown>;
function post<TResponse, TBody = unknown>(
    endpoint: string,
    body?: TBody,
    config?: AxiosRequestConfig
): Promise<TResponse>;
async function post<TResponse, TBody = unknown>(
    endpoint: string,
    body?: TBody,
    config?: AxiosRequestConfig
): Promise<TResponse> {
    const response = await apiClient.post<TResponse>(normalizeEndpoint(endpoint), body, config);
    return response.data;
}

/** PATCH */
function patch(endpoint: string, body?: unknown, config?: AxiosRequestConfig): Promise<unknown>;
function patch<TResponse, TBody = unknown>(
    endpoint: string,
    body?: TBody,
    config?: AxiosRequestConfig
): Promise<TResponse>;
async function patch<TResponse, TBody = unknown>(
    endpoint: string,
    body?: TBody,
    config?: AxiosRequestConfig
): Promise<TResponse> {
    const response = await apiClient.patch<TResponse>(normalizeEndpoint(endpoint), body, config);
    return response.data;
}

/** DELETE */
function del(endpoint: string, body?: unknown, config?: AxiosRequestConfig): Promise<unknown>;
function del<TResponse, TBody = unknown>(
    endpoint: string,
    body?: TBody,
    config?: AxiosRequestConfig
): Promise<TResponse>;
async function del<TResponse, TBody = unknown>(
    endpoint: string,
    body?: TBody,
    config?: AxiosRequestConfig
): Promise<TResponse> {
    const response = await apiClient.delete<TResponse>(normalizeEndpoint(endpoint), {
        ...config,
        data: body,
    });
    return response.data;
}

export const api = {
    get,
    post,
    patch,
    delete: del,
    
    upload: async (endpoint: string, file: File, config?: AxiosRequestConfig) => {
        const formData = new FormData();
        formData.append('file', file);
        
        const response = await apiClient.post(
            endpoint.startsWith('/') ? endpoint : `/${endpoint}`, 
            formData,
            {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
                ...config,
            }
        );
        return response.data;
    },
};
