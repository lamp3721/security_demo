<template>
  <div class="container">
    <h1>欢迎来到我们的应用</h1>
    <p>这是一个公开的主页，任何人都可以访问。</p>
    <p>请<router-link to="/login">登录</router-link>或访问受保护的<router-link to="/dashboard">仪表盘</router-link>。</p>
  </div>
</template>

<style scoped>
.container {
  max-width: 800px;
  margin: 2rem auto;
  padding: 2rem;
  text-align: center;
  border: 1px solid #ddd;
  border-radius: 8px;
}
</style> 