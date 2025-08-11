import apiClient from './index';

export const login = (credentials) => {
    return apiClient.post('/api/auth/login', credentials);
};

export const register = (userInfo) => {
    return apiClient.post('/api/auth/register', userInfo);
};

// 测试接口
export const getAdminRoleData = () => {
    return apiClient.get('/api/test/role/admin');
};

export const getUserRoleData = () => {
    return apiClient.get('/api/test/role/user');
};

export const getUserListData = () => {
    return apiClient.get('/api/test/permission/user/list');
}; 