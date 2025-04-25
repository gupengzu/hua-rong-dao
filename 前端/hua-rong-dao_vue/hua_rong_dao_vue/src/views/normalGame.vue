
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

</style>
