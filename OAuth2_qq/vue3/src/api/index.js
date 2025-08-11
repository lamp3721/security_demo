import axios from 'axios';
import { useAuthStore } from '../store/auth';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080', // 您的Spring Boot后端地址
    headers: {
        'Content-Type': 'application/json'
    }
});

// 请求拦截器
apiClient.interceptors.request.use(
    config => {
        const authStore = useAuthStore();
        const token = authStore.token;
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

export default apiClient; 