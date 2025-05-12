<template>
<div>
    <div id="main">
        <Ground :unitSize="unitSize" style="position: absolute; top: 0; left: 0;" />
        <Board 
        :key="layout"
        :unitSize="unitSize" 
        :layout="layout"
        @move-success="incrementCount"
        @update-layout="updateLayout"
        :style="{ position: 'absolute', top: unitSize * 0.8, left: unitSize * 0.5 }" 
        />
        <div :style="{ top: `${(unitSize * 0.8 - 34) / 2}px`, left: `${(unitSize * 5 - 124) / 2}px` }"
            class="select-btn" @click="showLevel = true;">{{ title }}</div>
        <Level :unitSize="unitSize" :show="showLevel" :handleSelect="handleSelect" />
    </div>
    <div class="show-count-container">
        <ShowCount 
        ref="showCount"
        :count="count" 
        :layout="layout" 
        @update:count="count=$event"
        @update:layout="layout=$event"    
        />
    </div>
</div>
</template>

<script>

import Ground from '@/Component/Ground.vue';
import Board from '@/Component/Board.vue';
import Level from '@/Component/Level.vue';
import ShowCount from '@/Component/ShowCount.vue'; 
import { addAward,addPlayingUser,deletePlayingUser,changePlayingUser,getPlayingUsers,queryById } from '@/api/users';
import { onBeforeUnmount } from 'vue';
import { useRouter } from 'vue-router';

export default {
    components: { Ground, Board, Level, ShowCount },
    
    data () {
        return {
            unitSize: Math.min(window.innerWidth / 8, window.innerHeight / 8), // 基于宽度和高度的最小值
            layout: '22222222222222222222',
            title: '选择关卡',
            showLevel: false,
            isGameActive: true, // 游戏是否处于活动状态
            count: 0
        }
    },
    methods: {
        handleSelect (level) {
            this.layout = level.layout;
            this.title = level.title;
            this.showLevel = false;
            this.isGameActive = true; // 游戏开始
            this.count = 0; // 重置步数
       },
        checkVictory() {
                console.log("before")
                let userInfo = JSON.parse(localStorage.getItem('loginUser'));
                console.log(userInfo)
            addAward(userInfo.username);// 继续游戏
                console.log("after")
        },
        incrementCount() {
            // console.log(this.count);
             // 调用 ShowCount 的 addCount 方法
            this.$refs.showCount.addCount();
        },
        updateLayout(newLayout) {
            this.layout = newLayout;
            console.log("父组件的 layout 更新为:", newLayout);
        },
        async notifyStartGame() {
            console.log("notifyStartGame函数被调用");
            const userInfo = JSON.parse(localStorage.getItem('loginUser')) || { username: 'guest' };
            const result = await queryById(userInfo.id);
            console.log('获取用户信息:', result);
            
            const data = { "username": userInfo.username,"image":result.data.image, "layout": this.layout };

            addPlayingUser(data).catch(error => {
                console.error('通知服务器用户进入游戏失败:', error);
            });

            //动态添加路由
            
            this.router.addRoute({
                path: `/real_spectate/${userInfo.username}`,
                name: `real_spectate_${userInfo.username}`,
                component: () => import('@/views/real_spectate.vue')//动态加载组件
            });
            console.log(`动态路由已添加: /real_spectate/${userInfo.username}`);
        }, 
        notifyExitGame() {
            console.log("notifyExitGame函数被调用");
            const userInfo = JSON.parse(localStorage.getItem('loginUser')) || { username: 'guest' };
            const data = { "username": userInfo.username };

            deletePlayingUser(data).catch(error => {
                console.error('通知服务器用户退出游戏失败:', error);
            });
        }
    },
    created () {
        location.hash && (this.layout = location.hash.slice(1)) && (this.title = '自定义');
        window.onresize = () => this.unitSize = Math.min(window.innerWidth / 8, window.innerHeight / 8); // 同步调整
        window.onhashchange = () => location.hash &&
            (this.layout = location.hash.slice(1)) && (this.title = '自定义');

        const userInfo = JSON.parse(localStorage.getItem('loginUser'));
        if (!userInfo || !userInfo.username) {
            console.error("localStorage 中的 loginUser 或 username 不存在");
        } else {
            console.log("WebSocket 使用的用户名:", userInfo.username);
        }

        //通知服务器用户进入游戏
        this.notifyStartGame();

        //初始化WebSocket连接
        this.socket = new WebSocket(`ws://localhost:8080/ws/${userInfo.username}`);
        this.socket.onopen = () => {
            console.log("WebSocket连接已打开");
        };
        this.socket.onclose=() => {
            console.log("WebSocket连接已关闭");
        };
    },
    watch: {
        layout(newLayout,oldLayout) {
            console.log("父组件的 layout 更新为:", newLayout);
            console.log("旧的 layout:", oldLayout);

            // 获取用户信息
            const userInfo = JSON.parse(localStorage.getItem('loginUser')) || { username: 'guest' };

            // 构造请求数据
            const data = {
                username: userInfo.username,
                layout: newLayout
            };

            // 调用 changePlayingUser 函数
            changePlayingUser(data).catch(error => {
                console.error('通知服务器用户布局变化失败:', error);
            });

            //通过WebSocket通知观战界面
            if (this.socket) {
                this.socket.send(JSON.stringify(data));
                console.log("通过WebSocket发送数据:", data);
                console.log(`WebSocket URL: ws://localhost:8080/ws/${userInfo.username}`);
            }
        }
    },
    setup() {
        const router = useRouter();
        
        // 使用 onBeforeUnmount 替代 beforeDestroy
        onBeforeUnmount(() => {
            console.log("onBeforeUnmount 函数被调用");
            const userInfo = JSON.parse(localStorage.getItem('loginUser')) || { username: 'guest' };
            const data = { "username": userInfo.username };

            deletePlayingUser(data).catch(error => {
                console.error('通知服务器用户退出游戏失败:', error);
            });
            console.log("notifyExitGame 函数调用完毕");
        });

        return { router };
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
    background-color: rgba(255, 255, 255, 0.6); /* 半透明白色背景 */
    background-image: none; /* 移除背景图片 */
    border: 2px solid #ccc; /* 边框 */
    padding: 20px; /* 内边距 */
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* 阴影效果 */
    border-radius: 10px; /* 圆角 */
}

.select-btn {
    position: relative; /* 确保 z-index 生效 */
    z-index: 10; /* 设置更高的层级 */
    margin-top: 0px;
    background-color: rgba(255, 255, 255, 0.1); /* 半透明白色背景 */
    color: #000; /* 黑色文字 */
    border: 1px solid rgba(255, 255, 255, 0.1); /* 半透明边框 */
    border-radius: 5px; /* 圆角 */
    min-width: 120px;
    line-height: 30px;
    text-align: center;
    cursor: pointer;
    transition: all 0.3s ease; /* 添加过渡效果 */

    &:hover {
        background-color: rgba(255, 255, 255, 0.8); /* 鼠标悬停时背景颜色加深 */
        border-color: rgba(255, 255, 255, 1); /* 鼠标悬停时边框颜色加深 */
    }

    &:active {
        color: #09c; /* 点击时文字颜色变化 */
    }
}

.show-count-container{
    position: absolute;
    top:10%;
    right:5%;
    width: 200px;
    background-color: #fff;
    border:1px solid #ccc;
    border-radius: 8px;
    padding: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}    
</style>