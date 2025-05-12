<template>
    <div id="main">
        <Ground :unitSize="unitSize" style="position: absolute; top: 0; left: 0;" />
        <Board :unitSize="unitSize" :layout="layout"
            :style="{ position: 'absolute', top: unitSize * 0.8, left: unitSize * 0.5 }" />
        <div :style="{ top: `${(unitSize * 0.8 - 34) / 2}px`, left: `${(unitSize * 5 - 124) / 2}px` }"
            class="select-btn" @click="showLevel = true;">{{ title }}</div>
        <Level :unitSize="unitSize" :show="showLevel" :handleSelect="handleSelect" />
    </div>
</template>

<script>

import Ground from '@/Component/Ground.vue';
import Board from '@/Component/BoardForGuest.vue';
import Level from '@/Component/Level.vue';

export default {
    components: { Ground, Board, Level },
    data () {
        return {
            unitSize: Math.min(window.innerWidth / 8, window.innerHeight / 8), // 基于宽度和高度的最小值
            layout: '22222222222222222222',
            title: '选择关卡',
            showLevel: false,
            isGameActive: true // 游戏是否处于活动状态
        }
    },
    methods: {
        handleSelect (level) {
            this.layout = level.layout;
            this.title = level.title;
            this.showLevel = false;
            this.isGameActive = true; // 游戏开始
            console.log("更新后的 layout:", this.layout);
       },
       checkVictory() {
         if (this.layout[13] === '5') {
          return true; // 成功过关
         } else {
          return false; // 继续游戏
         }
       } 
    },
    created () {
        location.hash && (this.layout = location.hash.slice(1)) && (this.title = '自定义');
        window.onresize = () => this.unitSize = Math.min(window.innerWidth / 8, window.innerHeight / 8); // 同步调整
        window.onhashchange = () => location.hash &&
            (this.layout = location.hash.slice(1)) && (this.title = '自定义');

    }
}

</script>

<style lang="less">
#main {
    position: absolute;
    top: 20%;
    left: 20%;
    transform: translate(-50%, -50%);
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background-color: #f0f0f0; /* 背景颜色 */
    border: 2px solid #ccc; /* 边框 */
    padding: 20px; /* 内边距 */
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* 阴影效果 */
    border-radius: 10px; /* 圆角 */
}

.select-btn {
    position: relative; /* 确保 z-index 生效 */
    z-index: 10; /* 设置更高的层级 */
    margin-top: 0px;
    background-color: #ccc;
    border: 2px solid #aaa;
    border-radius: 5px;
    min-width: 120px;
    line-height: 30px;
    text-align: center;
    cursor: pointer;

    &:hover {
        background-color: #eee;
    }

    &:active {
        color: #09c;
    }
}
</style>