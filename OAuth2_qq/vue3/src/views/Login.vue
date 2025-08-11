<template>
  <div class="login-container">
    <div class="login-box">
      <h1>{{ isRegistering ? '注册' : '登录' }}</h1>
      <form @submit.prevent="handleSubmit">
        <div class="input-group">
          <label for="username">用户名</label>
          <input type="text" id="username" v-model="credentials.username" required>
        </div>
        <div class="input-group">
          <label for="password">密码</label>
          <input type="password" id="password" v-model="credentials.password" required>
        </div>
        <button type="submit" class="btn btn-primary">{{ isRegistering ? '注册' : '登录' }}</button>
      </form>
      <div class="separator">或者</div>
      <button @click="handleQqLogin" class="btn btn-qq">
        <img src="/qq-logo.png" alt="QQ Logo" class="qq-logo">
        使用QQ登录
      </button>
      <p class="toggle-form">
        {{ isRegistering ? '已有账户?' : '还没有账户?' }}
        <a href="#" @click.prevent="isRegistering = !isRegistering">
          {{ isRegistering ? '立即登录' : '立即注册' }}
        </a>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '../store/auth';
import { register as apiRegister } from '../api/auth';

const authStore = useAuthStore();
const isRegistering = ref(false);
const credentials = ref({
  username: '',
  password: ''
});

const handleSubmit = async () => {
  if (isRegistering.value) {
    // 注册逻辑
    try {
      const response = await apiRegister(credentials.value);
      if (response.data.success) {
        alert('注册成功！请登录。');
        isRegistering.value = false; // 切换回登录模式
        credentials.value = { username: '', password: '' }; // 清空表单
      } else {
        alert(response.data.message || '注册失败');
      }
    } catch (error) {
      alert(error.response?.data?.message || '注册请求失败');
    }
  } else {
    // 登录逻辑
    await authStore.login(credentials.value);
  }
};

const handleQqLogin = () => {
  // Spring Boot后端提供的OAuth2登录入口
  window.location.href = 'http://localhost:8080/oauth2/authorization/qq';
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
}
.login-box {
  width: 100%;
  max-width: 400px;
  padding: 2.5rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}
h1 {
  text-align: center;
  margin-bottom: 1.5rem;
}
.input-group {
  margin-bottom: 1rem;
}
.input-group label {
  display: block;
  margin-bottom: 0.5rem;
}
.input-group input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.btn {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  padding: 0.75rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: bold;
}
.btn-primary {
  background-color: #007bff;
  color: white;
  margin-bottom: 1rem;
}
.separator {
  text-align: center;
  margin: 1rem 0;
  color: #888;
}
.btn-qq {
  background-color: #00a1d6;
  color: white;
}
.qq-logo {
  width: 20px;
  height: 20px;
  margin-right: 0.5rem;
}
.toggle-form {
  text-align: center;
  margin-top: 1.5rem;
}
</style> 