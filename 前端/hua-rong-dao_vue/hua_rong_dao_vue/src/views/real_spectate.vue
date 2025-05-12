<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { useRoute } from 'vue-router';
import Board from '@/Component/Board.vue'; // 引入 Board 组件

const route = useRoute();
const username = route.params.username; // 获取路由参数中的用户名
const layout = ref('22222222222222222222'); // 存储布局信息
const unitSize = ref(Math.min(window.innerWidth / 8, window.innerHeight / 8)); // 单元格大小


let socket;

onMounted(() => {
  console.log("开始初始化WebSocket连接");
  // 初始化 WebSocket 连接
  if (!socket || socket.readyState === WebSocket.CLOSED) {
    socket = new WebSocket(`ws://localhost:8080/ws/${username}`);
    console.log("WebSocket 已重新初始化");
  }
  console.log("WebSocket连接已打开");

  

  socket.onmessage = (event) => {
    const data = JSON.parse(event.data);
    console.log('接收到的消息:', data);
    if(data.username === username) {
      layout.value = data.layout; // 更新布局信息
    }
  }
  console.log("WebSocket已完成onmessage事件绑定");
  socket.onopen = () => {
    console.log("WebSocket连接已打开");
  };
  socket.onclose = () => {
    console.log("WebSocket连接已关闭");
  };
})

onBeforeUnmount(() => {
  if (socket) {
    socket.close(); // 关闭 WebSocket 连接
    console.log("WebSocket连接已关闭");
  }
});

console.log('被观战的用户名:', username); // 打印被观战的用户名
</script>

<template>
  <div>
    <h1>实时观战</h1>
    <p>正在游玩的用户: {{ username }}</p>
    <Board 
      :layout="layout" 
      :unitSize="unitSize" 
      :style="{ position: 'relative', margin: '20px auto' }" 
    />
  </div>
</template>

<style lang="less" scoped>
h1 {
  font-size: 24px;
  color: #333;
  text-align: center;
}

p {
  font-size: 18px;
  color: #666;
  text-align: center;
}

div {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
</style>