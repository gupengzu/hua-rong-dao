<template>
    <div class="show-count">
        <div class="count">
            <span>当前步数：</span>
            <span @click="addCount">{{ count }}</span>
        </div>
        <div class="actions">
          <!-- 存档按钮 -->
          <button class="btn" @click="saveGame">保存</button>
          <!-- 读档按钮 -->
          <button class="btn" @click="getGame">继续</button>
          <!-- 立即存档按钮 -->
          <button
            class="btn"
            :class="{ active: autoSaveActive }"
            @click="toggleAutoSave"
          >
            {{ autoSaveActive ? '停止自动存档' : '自动存档' }}
          </button>
        </div>
    </div>
</template>

<script setup lang="ts">
import { defineEmits, defineProps, defineExpose } from 'vue';
import { storeLayout, getLayout } from '../api/users.js';
import { ref } from 'vue';
import {ElMessage} from 'element-plus';

const data = ref({
  username: '',
  count: 0,
  layout: ''
});

const props = defineProps({
  count: Number,
  layout:String
});

const emit = defineEmits(['update:count', 'update:layout']);

function addCount() {
  emit('update:count', props.count + 1);
}

function saveGame() {
  let userInfo = JSON.parse(localStorage.getItem('loginUser'));
  console.log(userInfo);//方便调试
  data.value.username = userInfo.username;
  console.log("当前props的count:", props.count);
  data.value.count = props.count;
  console.log("当前props的layout:", props.layout); 
  data.value.layout = props.layout;
  console.log(data.value);//方便调试
  storeLayout(data.value);
  ElMessage.success('存档成功！');
}

async function getGame() {
  console.log("进入getGame函数");
  let userInfo = JSON.parse(localStorage.getItem('loginUser'));
  console.log("用户名:",userInfo.username);//方便调试

  console.log("开始调用getLayout")
  const result =await getLayout(userInfo.username);
  //检测到的返回的是否是空，如果是空，则改为默认布局
  if (!result.data) {
    console.log("没有存档或者存档损坏,已为您将当前局面存档");
    saveGame();
    alert("没有存档或者存档损坏,已为您将当前局面存档 😊");
    return;
  }
  console.log("getLayout 返回结果:", result);//方便调试
  console.log("提取到的 layout:",result.data.layout)
  console.log("提取到的 count:",result.data.count)
  console.log("调用完getLayout函数")

  
  emit('update:layout', result.data.layout+' ');
  emit('update:count', result.data.count);
}

//自动存档相关
const autoSaveActive = ref(false);
const toggleAutoSave = () => {
  autoSaveActive.value = !autoSaveActive.value;
  if (autoSaveActive.value) {
    ElMessage.info('已开启自动存档');
    saveGame(); // 立即存档一次
    // 开始自动存档
    autoSaveInterval = setInterval(() => {
      saveGame();
    }, 300000 ); // 每5分钟自动存档一次
  } else {
    ElMessage.info('已停止自动存档');
    // 停止自动存档
    clearInterval(autoSaveInterval);
  }
};




defineExpose({
  addCount
})

</script>

<style scoped lang="less">
.show-count {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 10px; /* 元素之间的间距 */
    background-color: rgba(255, 255, 255, 0.6); /* 半透明白色背景 */
    padding: 10px; /* 内边距 */
    border-radius: 8px; /* 圆角 */
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
}

.count {
    font-size: 16px;
    margin-bottom: 10px;
}

.actions {
    display: flex;
    gap: 10px; /* 按钮之间的间距 */
}

.btn {
    padding: 8px 16px;
    font-size: 14px;
    color: #000; /* 黑色文字 */
    background-color: rgba(255, 255, 255, 0.6); /* 半透明白色背景 */
    border: 1px solid rgba(255, 255, 255, 0.8); /* 半透明边框 */
    border-radius: 4px; /* 圆角 */
    cursor: pointer;
    transition: all 0.3s ease; /* 添加过渡效果 */

    &:hover {
        background-color: rgba(255, 255, 255, 0.8); /* 鼠标悬停时背景颜色加深 */
        border-color: rgba(255, 255, 255, 1); /* 鼠标悬停时边框颜色加深 */
    }

    &:active {
        background-color: rgba(255, 255, 255, 1); /* 点击时背景颜色加深 */
        color: #09c; /* 点击时文字颜色变化 */
    }
}
</style>