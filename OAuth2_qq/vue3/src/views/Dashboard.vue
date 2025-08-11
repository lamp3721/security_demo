<template>
  <div class="dashboard-container">
    <h1>仪表盘</h1>
    <p v-if="authStore.isAuthenticated">
      您已登录！Token: <span class="token-display">{{ authStore.token }}</span>
    </p>
    <p v-else>
      您尚未登录。
    </p>

    <div class="api-test-section">
      <h2>API 权限测试</h2>
      <div class="button-group">
        <button @click="callApi(api.getUserListData)" class="btn">测试 'sys:user:list' 权限</button>
        <button @click="callApi(api.getAdminRoleData)" class="btn">测试 'ADMIN' 角色</button>
        <button @click="callApi(api.getUserRoleData)" class="btn">测试 'USER' 角色</button>
      </div>
      <div v-if="apiResult" class="api-result">
        <h3>API 响应:</h3>
        <pre :class="{ 'error': isError }">{{ apiResult }}</pre>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '../store/auth';
import * as api from '../api/auth';

const authStore = useAuthStore();
const apiResult = ref(null);
const isError = ref(false);

const callApi = async (apiFunc) => {
  try {
    const response = await apiFunc();
    apiResult.value = response.data;
    isError.value = false;
  } catch (error) {
    apiResult.value = error.response?.data || { message: '请求失败，请查看控制台' };
    isError.value = true;
  }
};
</script>

<style scoped>
.dashboard-container {
  max-width: 900px;
  margin: 2rem auto;
  padding: 2rem;
  border: 1px solid #ddd;
  border-radius: 8px;
}
.token-display {
  word-break: break-all;
  font-family: monospace;
  background-color: #f5f5f5;
  padding: 2px 4px;
  border-radius: 4px;
}
.api-test-section {
  margin-top: 2rem;
  padding-top: 1.5rem;
  border-top: 1px solid #eee;
}
.button-group {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
}
.btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  background-color: #28a745;
  color: white;
  font-weight: bold;
}
.api-result {
  margin-top: 1rem;
  padding: 1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: #f9f9f9;
}
.api-result pre {
  white-space: pre-wrap;
  word-wrap: break-word;
}
.api-result .error {
  color: #dc3545;
}
</style> 