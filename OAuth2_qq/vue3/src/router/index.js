import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '../store/auth';

// 导入视图组件
import Home from '../views/Home.vue';
import Login from '../views/Login.vue';
import Dashboard from '../views/Dashboard.vue';
import OAuth2Redirect from '../views/OAuth2Redirect.vue';

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
    },
    {
        path: '/login',
        name: 'Login',
        component: Login,
    },
    {
        path: '/oauth2/redirect', // 用于处理OAuth2回调的路由
        name: 'OAuth2Redirect',
        component: OAuth2Redirect,
    },
    {
        path: '/dashboard',
        name: 'Dashboard',
        component: Dashboard,
        meta: { requiresAuth: true } // 标记这个路由需要认证
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

// 全局前置守卫
router.beforeEach((to, from, next) => {
    const authStore = useAuthStore();
    const isAuthenticated = authStore.isAuthenticated;

    // 检查目标路由是否需要认证
    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!isAuthenticated) {
            // 如果用户未认证，重定向到登录页
            next({ name: 'Login' });
        } else {
            // 如果用户已认证，正常放行
            next();
        }
    } else {
        // 如果目标路由不需要认证，直接放行
        next();
    }
});

export default router; 