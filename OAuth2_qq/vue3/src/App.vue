<script setup>
import { useAuthStore } from './store/auth';
const authStore = useAuthStore();
</script>

<template>
  <div id="app">
    <nav>
      <router-link to="/">主页</router-link> |
      <router-link to="/dashboard">仪表盘</router-link> |
      <template v-if="!authStore.isAuthenticated">
        <router-link to="/login">登录</router-link>
      </template>
      <template v-else>
        <a href="#" @click.prevent="authStore.logout()">登出</a>
      </template>
    </nav>
    <main>
      <router-view />
    </main>
  </div>
</template>

<style>
/* 全局样式 */
body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  margin: 0;
  background-color: #f4f4f9;
  color: #333;
}

#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

nav {
  background-color: #fff;
  padding: 1rem 2rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: center;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
  text-decoration: none;
  margin: 0 1rem;
}

nav a.router-link-exact-active {
  color: #42b983;
  border-bottom: 2px solid #42b983;
}

main {
  flex: 1;
  padding: 1rem;
}
</style>
