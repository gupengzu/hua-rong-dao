<script setup>
import { ref, onMounted,onBeforeUnmount } from 'vue';
import { getPlayingUsers } from '@/api/users';
import { useRouter,useRoute } from 'vue-router';


const playingUsers = ref([]); // 存储正在游玩的用户列表
const router = useRouter(); // 用于路由跳转

// 获取正在游玩的用户列表
const fetchPlayingUsers = async () => {
  try {
    const response = await getPlayingUsers();
    console.log('获取正在游玩的用户信息列表:', response.data);
    playingUsers.value=response.data.map(user => ({
      username: user.username,
      image: user.image,
      layout: user.layout
    }));
  } catch (error) {
    console.error('获取正在游玩的用户列表失败:', error);
  }
};

// 观战按钮点击事件
const spectateUser = (username) => {
  console.log(`被观战用户: ${username},调用spectateUser函数`);
  router.push(`/real_spectate/${username}`); // 跳转到观战页面
};

// 在组件挂载时获取用户列表
onMounted(() => {
  fetchPlayingUsers();
});
</script>

<template>
  <div>
    <h1>当前正在游玩的用户</h1>
    <ul>
      <li v-for="user in playingUsers" :key="user.username">
        <span>{{ user.username }}</span>
        <img :src="user.image" alt="用户头像" style="width: 50px; height: 50px; border-radius: 50%; margin-right: 10px;" />
        <button @click="spectateUser(user.username)">观战</button>
      </li>
    </ul>
  </div>
</template>

<style scoped>
h1 {
  font-size: 24px;
  margin-bottom: 16px;
}

ul {
  list-style: none;
  padding: 0;
}

li {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
</style>