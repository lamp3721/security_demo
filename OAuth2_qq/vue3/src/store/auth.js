import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { login as apiLogin } from '../api/auth';
import router from '../router';

export const useAuthStore = defineStore('auth', () => {
    // State
    const token = ref(localStorage.getItem('token') || null);

    // Getters
    const isAuthenticated = computed(() => !!token.value);

    // Actions
    async function login(credentials) {
        try {
            const response = await apiLogin(credentials);
            if (response.data.success) {
                const newToken = response.data.data.token;
                setToken(newToken);
                router.push('/dashboard'); // 登录成功后跳转到受保护的页面
            } else {
                // 可以根据后端返回的错误信息进行提示
                alert(response.data.message || '登录失败');
            }
        } catch (error) {
            console.error('登录请求失败:', error);
            alert('登录失败，请检查网络或联系管理员。');
        }
    }

    function setToken(newToken) {
        token.value = newToken;
        localStorage.setItem('token', newToken);
    }

    function logout() {
        token.value = null;
        localStorage.removeItem('token');
        router.push('/login'); // 登出后跳转到登录页
    }

    return { token, isAuthenticated, login, setToken, logout };
}); 