<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { loginApi } from '@/api/login'
import{ElMessage} from 'element-plus'

const loginForm = ref({ username: '', password: '' })
const router = useRouter()

// 登录
const login =async () => {
  const result = await loginApi(loginForm.value);
  if (result.code) {
    ElMessage.success(result.msg);

    // 登录成功，保存用户信息到本地存储
    localStorage.setItem('loginUser', JSON.stringify(result.data));
    // 登录成功，跳转到首页
    router.push('/')
  } else {
    // 登录失败，提示错误信息
    ElMessage.error(result.msg);
  }
}

// 跳转到注册界面
const goToRegister = () => {
  router.push('/register')
}

// 游客模式登录
const guestLogin = () => {
  console.log('以游客模式登录')
  // 在这里添加游客模式登录的逻辑
  router.push('/guest')  
}
</script>

<template>
  <div id="container">

    <!-- 背景视频 -->
    <video autoplay muted loop id="background-video">
      <source src="@/assets/login_backVideo.mp4" type="video/mp4" />
    </video>
    <!-- 登录表单 -->
    <div class="login-form">
      <el-form label-width="80px">
        <p class="title">华容道</p>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="loginForm.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <div class="button-group">
            <el-button class="button" type="primary" @click="login">登录</el-button>
            <el-button class="button" type="success" @click="goToRegister">注册</el-button>
          </div>
          <div class="guest-button">
            <el-button class="button" type="warning" @click="guestLogin">游客模式</el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
#container {
  position: relative; /* 确保子元素定位正确 */
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  height: 100vh; /* 占满整个视口高度 */
  overflow: hidden; /* 防止内容溢出 */
}

#background-video {
  position: absolute; /* 绝对定位 */
  top: 0; /* 顶部对齐 */
  left: 0; /* 左侧对齐 */
  width: 100%; /* 宽度占满 */
  height: 100%; /* 高度占满 */
  object-fit: cover; /* 保持视频比例填充容器 */
  z-index: -1; /* 背景视频在最底层 */
}

.login-form {
  width: 400px; /* 设置表单宽度 */
  padding: 20px; /* 内边距 */
  background: rgba(255, 255, 255, 0.4); /* 半透明背景 */
  border-radius: 8px; /* 圆角 */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* 添加阴影 */
}

.title {
  text-align: center; /* 标题居中 */
  font-size: 20px; /* 标题字体大小 */
  font-weight: bold; /* 标题加粗 */
  margin-bottom: 20px; /* 标题与表单的间距 */
}

.button-group {
  display: flex;
  justify-content: space-between; /* 按钮两端对齐 */
  gap: 20px; /* 按钮之间的间距 */
  margin-bottom: 10px; /* 与游客模式按钮的间距 */
}

.button-group .button {
  flex: none; /* 取消自动平分空间 */
  width: 140px; /* 设置固定宽度 */

}

.guest-button {
  display: flex;
  justify-content: center; /* 居中游客模式按钮 */
  width: 100%; /* 占满父容器宽度 */
}

.guest-button .button {
  width: 100%; /* 设置宽度为父容器宽度 */
  max-width: 400px; /* 限制最大宽度 */
}
.button-group .button,
.guest-button .button {
  background-color: rgba(255, 255, 255, 0.3); /* 半透明白色背景 */
  color: #000; /* 白色文字 */
  border: 1px solid rgba(255, 255, 255, 0.6); /* 半透明边框 */
  font-size: 16px; /* 调整文字大小 */
  padding: 10px 0; /* 调整按钮高度 */
  border-radius: 8px; /* 圆角 */
  transition: all 0.3s ease; /* 添加过渡效果 */
}

.button-group .button:hover,
.guest-button .button:hover {
  background-color: rgba(255, 255, 255, 0.5); /* 鼠标悬停时背景颜色加深 */
  border-color: rgba(255, 255, 255, 0.8); /* 鼠标悬停时边框颜色加深 */
}
</style>