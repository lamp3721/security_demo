<template>
  <div class="redirect-container">
    <p>正在登录，请稍候...</p>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '../store/auth';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();

onMounted(() => {
  const token = route.query.token;
  if (token) {
    // 将从URL获取的token存入store和localStorage
    authStore.setToken(token);
    // 跳转到受保护的页面
    router.push('/dashboard');
  } else {
    // 如果没有token，跳转到登录页并提示错误
    alert('登录失败，未获取到有效的Token。');
    router.push('/login');
  }
});
</script>

<style scoped>
.redirect-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80vh;
  font-size: 1.5rem;
}
</style> 