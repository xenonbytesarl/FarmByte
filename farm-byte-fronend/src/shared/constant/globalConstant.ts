import i18n from "@/i18n.tsx";

export const API_BASE_URL = import.meta.env.VITE_API_BACKEND_URL;

export const API_JSON_HEADER = {
    'Content-Type': 'application/json',
    'Accept-Language': i18n.language
}

export const UNKNOWN_ERROR = 'Unknown error - Contact your administrator';

export type ToastType = "danger" | "info" | "success";
