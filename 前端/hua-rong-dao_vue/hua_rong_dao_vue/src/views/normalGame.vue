
<script setup>
import { ref, onMounted } from 'vue'; // 导入 ref 和 onMounted
import { useRouter } from 'vue-router'; // 导入 useRouter
import { ElMessage } from 'element-plus'; // 导入 ElMessage

const router= useRouter(); // 创建路由实例

const startGame = () => {
    // 跳转到游戏界面
    router.push('/Game')
}

</script>

<template>
    <!-- 按钮，点击后跳转到游戏界面 -->
    <div class="start-container">
        <button class="start-button" @click="startGame">开始游戏</button>
    </div>
</template>


<style scoped>
.start-container {
    display: flex;
    justify-content: center; /* 水平居中 */
    align-items: center; /* 垂直居中 */
    height: 100vh; /* 占满整个视口高度 */
    /* 移除背景颜色，使背景透明 */
}

.start-button {
    background-color: rgba(255, 255, 255, 0.6); /* 半透明白色背景 */
    color: #000; /* 黑色文字 */
    border: 1px solid rgba(255, 255, 255, 0.8); /* 半透明边框 */
    font-size: 18px; /* 调整文字大小 */
    padding: 10px 20px; /* 按钮内边距 */
    border-radius: 8px; /* 圆角 */
    cursor: pointer; /* 鼠标悬停时显示手型 */
    transition: all 0.3s ease; /* 添加过渡效果 */
}

.start-button:hover {
    background-color: rgba(255, 255, 255, 0.8); /* 鼠标悬停时背景颜色加深 */
    border-color: rgba(255, 255, 255, 1); /* 鼠标悬停时边框颜色加深 */
}
</style>
