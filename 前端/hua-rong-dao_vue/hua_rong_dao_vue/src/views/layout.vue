<script setup>
import { ElMessage } from 'element-plus'; // 导入 ElMessage
import { useRouter } from 'vue-router'; // 导入 useRouter
import { ref, onMounted } from 'vue'; // 导入 ref 和 onMounted
import {uploadImage} from '@/api/upload'; // 导入上传图片的 API
import { change,queryAll,queryById } from '@/api/users'; // 导入查询用户信息的 API
import layoutMusic from '@/assets/layout.mp3'



const router = useRouter();
const userPhoto = ref('');


const username = ref(''); // 定义响应式变量

onMounted(() => {
  const userinfo = JSON.parse(localStorage.getItem('loginUser'));
  if (userinfo && userinfo.username) {
    username.value = userinfo.username; // 设置用户名
  }
});

//获取用户信息
const fetchUserInfo = async () => {
  const userinfo = JSON.parse(localStorage.getItem('loginUser'));
  if (userinfo && userinfo.id) {
    try {
      const result = await queryById(userinfo.id);
      console.log('获取用户信息:', result);
      if (result && result.data && result.data.image) {
        userPhoto.value = result.data.image; // 设置用户头像路径
      } else {
        userPhoto.value = 'https://java-ai-gu.oss-cn-beijing.aliyuncs.com/2025/03/1d22c7d7-f04c-47f4-be45-40f81623bb31.png'; // 设置零食图
      }
    } catch (error) {
      console.error('获取用户信息失败:', error);
      userPhoto.value = 'https://java-ai-gu.oss-cn-beijing.aliyuncs.com/2025/03/267c3c9f-8c2d-4cf1-aeae-59a924d138ed.png'; // 设置起跳图
    }
  } else {
    userPhoto.value = 'https://tse2-mm.cn.bing.net/th/id/OIP-C.C3YXKB9EucJBm-fayz_EJQHaHZ?rs=1&pid=ImgDetMain'; // 设置默认头像
  }
};
onMounted(() => {
  fetchUserInfo();

  const audio = document.querySelector('audio');
  if (audio) {
    // 设置音频自动播放
    console.log('自动播放音频');
    audio.play().catch(() => {
      // 自动播放失败，等待用户交互
      const resumeAudio = () => {
        audio.play();
        document.removeEventListener('click', resumeAudio);
      };
      document.addEventListener('click', resumeAudio);
    });
  }
})


const logout = () => {
  // 清除本地存储的用户信息
  localStorage.removeItem('loginUser')
  //提示用户已退出登录
  ElMessage.success('已退出登录')
  // 跳转到登录页面
  router.push('/login')
}

const data = ref({
  username: '', // 用户名
  url:''
})

const change_image = () => {
  const input = document.createElement('input');
  input.type = 'file';
  input.accept = 'image/*'; // 只允许选择图片文件
  input.onchange = async (event) => {
      const file = event.target.files[0]; // 获取用户选择的文件
      if (file) {
        const resultOfUpload = await uploadImage(file); // 调用上传图片的 API
        const url= resultOfUpload.data; // 获取返回的图片路径
        // 获取用户信息
        const userInfo = JSON.parse(localStorage.getItem('loginUser'));

        data.value.username = userInfo.username; // 设置用户名
        data.value.url = url; // 设置图片路径
        const result = await change(data.value); // 上传图片
        if (result.code) {
          ElMessage.success('头像更换成功！');
          // 重新获取用户信息
          await fetchUserInfo();
        } else {
          ElMessage.error(result.value.msg);
        }
    }
  }
input.click(); // 模拟点击文件输入框
};
</script>

<template>
  <div class="common-layout">
    <!-- 背景音乐 -->
    <audio :src="layoutMusic" autoplay loop></audio>
    <!-- 背景视频 -->
    <video autoplay muted loop id="background-video">
      <source src="@/assets/水墨长画卷大屏背景.mp4" type="video/mp4" />
    </video>

    <el-container>
      <!-- Header 区域 -->
      <el-header class="header">
          <div class="header-title">华容道</div>
          <div class="header-actions">
            <!-- 用户照片 -->
            <img :src="userPhoto" alt="用户照片" class="user-photo" />
            <!-- 用户名 -->
            <span>{{ username }}</span>
            <!-- <更换用户照片按钮 /> -->
            <el-button type="primary" @click="change_image">更换头像</el-button>
            <!-- 退出按钮 -->
            <el-button type="danger" @click="logout">退出</el-button>
          </div>
        </el-header>
      <el-container>
        <!-- 左侧菜单 -->
        <el-aside width="200px" class="aside">
          <!-- 左侧菜单栏 -->
            <el-menu :router="true">
              <!-- 正常游戏菜单 -->
              <el-menu-item index="/normalGame">
                正常游戏
              </el-menu-item>
              <!-- 限时模式菜单 -->
              <el-menu-item index="/timeMode">
                限制步数模式
              </el-menu-item>
              <!-- 排行榜菜单 -->
              <el-menu-item index="/leaderboard">
                排行榜
              </el-menu-item>
              <!-- 观战菜单 -->
              <el-menu-item index="/spectate">
                观战
              </el-menu-item>
            </el-menu>
        </el-aside>
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>



<style scoped>

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  background-color: rgba(245, 245, 245, 0.8); /* 半透明背景 */
  height: 60px;
}

.header-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.header-actions {
  display: flex;
  align-items: center;
}

.user-photo {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}

#background-video {
  position: fixed; /* 固定位置，确保视频覆盖整个页面 */
  top: 0;
  left: 0;
  width: 100%; /* 宽度占满 */
  height: 100%; /* 高度占满 */
  object-fit: cover; /* 保持视频比例填充容器 */
  z-index: -1; /* 确保视频在所有内容的后面 */
}

.common-layout {
  position: relative; /* 确保子元素定位正确 */
  min-height: 100vh; /* 确保内容区域占满屏幕高度 */
}

.header-actions .el-button {
  background-color: rgba(255, 255, 255, 0.6) !important; /* 半透明白色背景 */
  color: #000 !important; /* 黑色文字 */
  border: 1px solid rgba(255, 255, 255, 0.8); /* 半透明边框 */
  font-size: 14px; /* 调整文字大小 */
  padding: 5px 10px; /* 调整按钮内边距 */
  border-radius: 8px; /* 圆角 */
  transition: all 0.3s ease; /* 添加过渡效果 */
}

.header-actions .el-button:hover {
  background-color: rgba(255, 255, 255, 0.8) !important; /* 鼠标悬停时背景颜色加深 */
  border-color: rgba(255, 255, 255, 1); /* 鼠标悬停时边框颜色加深 */
}
</style>