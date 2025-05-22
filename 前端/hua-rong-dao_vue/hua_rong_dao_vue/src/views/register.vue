<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { sendVerificationCode,registerApi } from '@/api/register'
import registerMusic from '@/assets/register.mp3'

const router = useRouter();

const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  verificationCode: ''
})

const sendCode = () => {
  if (registerForm.value.email) {
    // 调用发送验证码的API
    sendVerificationCode(registerForm.value.email).then(response => {
      if (response.code) {
        ElMessage.success('验证码已发送到您的邮箱！');
      } else {
        ElMessage.error('发送验证码失败，请稍后再试！111');
      }
    }).catch(error => {
      console.error('发送验证码失败:', error);
      ElMessage.error('发送验证码失败，请稍后再试！222');
    });
  } else {
    ElMessage.error('请输入有效的邮箱地址！');
  }
  
}

const register =async () => {
  //注册逻辑
  if(registerForm.value.password !== registerForm.value.confirmPassword) {
    ElMessage.error('两次输入的密码不一致！')
    return
  }
  // 调用注册API
  const result =await registerApi(registerForm.value);
  if (result.code) {
    ElMessage.success(result.msg);
    // 注册成功，跳转到登录页面
    router.push('/login')
  } else {
    // 注册失败，提示错误信息
    ElMessage.error(result.msg);
  }
}

const toLogin = () => {
  // 跳转到登录页面
  router.push('/login')
}
</script>

<template>
  <div id="container">
     <!-- 背景音乐 -->
     <audio :src="registerMusic" autoplay loop></audio>    
     <!-- 背景视频 -->
    <video autoplay muted loop id="background-video">
      <source src="@/assets/16mins超清水墨视频素材，无水印.mp4" type="video/mp4" />
    </video>

    <!-- 注册表单 -->
    <div class="register-form">
      <el-form label-width="100px">
        <p class="title">华容道 - 注册</p>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="registerForm.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input type="password" v-model="registerForm.confirmPassword" placeholder="请再次输入密码"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="registerForm.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="verificationCode">
          <div class="verification-code">
            <el-input v-model="registerForm.verificationCode" placeholder="请输入验证码"></el-input>
            <el-button type="primary" @click="sendCode">发送验证码</el-button>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button class="button" type="primary" @click="register">注册</el-button>
          <el-button class="button" type="info" @click="toLogin">登录</el-button>
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
  position: relative; /* 确保子元素定位正确 */
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

.register-form {
  width: 400px; /* 设置表单宽度 */
  padding: 20px; /* 内边距 */
  background: rgba(255, 255, 255, 0.8); /* 半透明背景 */
  border-radius: 8px; /* 圆角 */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* 添加阴影 */
}

.title {
  text-align: center; /* 标题居中 */
  font-size: 20px; /* 标题字体大小 */
  font-weight: bold; /* 标题加粗 */
  margin-bottom: 20px; /* 标题与表单的间距 */
}

.verification-code {
  display: flex;
  gap: 10px; /* 输入框和按钮之间的间距 */
}

.verification-code .el-button {
  background-color: rgba(255, 255, 255, 0.3) !important; /* 半透明白色背景 */
  color: #000 !important; /* 黑色文字 */
  border: 1px solid rgba(255, 255, 255, 1); /* 半透明边框 */
  font-size: 16px; /* 调整文字大小 */
  padding: 10px 0; /* 调整按钮高度 */
  border-radius: 8px; /* 圆角 */
  transition: all 0.3s ease; /* 添加过渡效果 */
}

.verification-code .el-button:hover {
  background-color: rgba(255, 255, 255, 1) !important; /* 鼠标悬停时背景颜色加深 */
  border-color: rgba(255, 255, 255, 1); /* 鼠标悬停时边框颜色加深 */
}

.button {
  width: 100px; /* 按钮宽度 */
  margin-right: 10px; /* 按钮间距 */
  background-color: rgba(255, 255, 255, 0.3); /* 半透明白色背景 */
  color: #000; /* 黑色文字 */
  border: 1px solid rgba(255, 255, 255, 0.6); /* 半透明边框 */
}
.button:hover {
  background-color: rgba(255, 255, 255, 0.5); /* 鼠标悬停时背景颜色加深 */
  border-color: rgba(255, 255, 255, 0.8); /* 鼠标悬停时边框颜色加深 */
}
</style>