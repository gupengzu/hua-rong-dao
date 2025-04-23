<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const loginForm = ref({ username: '', password: '' })
const router = useRouter()

// 跳转到注册界面
const goToRegister = () => {
  router.push('/register')
}

// 游客模式登录
const guestLogin = () => {
  console.log('以游客模式登录')
  // 在这里添加游客模式登录的逻辑
}
</script>

<template>
  <div id="container">
    <div class="login-form">
      <el-form label-width="80px">
        <p class="title">Tlias智能学习辅助系统</p>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="loginForm.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button class="button" type="primary" @click="">登录</el-button>
        </el-form-item>
        <el-form-item>
          <el-button class="button" type="success" @click="goToRegister">注册</el-button>
          <el-button class="button" type="warning" @click="guestLogin">游客模式</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
#container {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  height: 100vh; /* 占满整个视口高度 */
  background-color: #f5f5f5; /* 背景颜色 */
}

.login-form {
  width: 400px; /* 设置表单宽度 */
  padding: 20px; /* 内边距 */
  background: #ffffff; /* 背景颜色 */
  border-radius: 8px; /* 圆角 */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* 添加阴影 */
}

.title {
  text-align: center; /* 标题居中 */
  font-size: 20px; /* 标题字体大小 */
  font-weight: bold; /* 标题加粗 */
  margin-bottom: 20px; /* 标题与表单的间距 */
}

.button {
  width: 100px; /* 按钮宽度 */
  margin-right: 10px; /* 按钮间距 */
}
</style>