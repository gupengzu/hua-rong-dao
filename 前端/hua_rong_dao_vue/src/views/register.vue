<script setup>
import { ref } from 'vue'

const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  verificationCode: ''
})

const sendVerificationCode = () => {
  console.log('发送验证码到邮箱:', registerForm.value.email)
  // 在这里添加发送验证码的逻辑
}
</script>

<template>
  <div id="container">
    <div class="register-form">
      <el-form label-width="100px">
        <p class="title">Tlias智能学习辅助系统 - 注册</p>
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
            <el-button type="primary" @click="sendVerificationCode">发送验证码</el-button>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button class="button" type="primary" @click="">注册</el-button>
          <el-button class="button" type="info" @click="">重置</el-button>
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

.register-form {
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

.verification-code {
  display: flex;
  gap: 10px; /* 输入框和按钮之间的间距 */
}

.button {
  width: 100px; /* 按钮宽度 */
  margin-right: 10px; /* 按钮间距 */
}
</style>