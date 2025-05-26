<template>
  <div>
      <button class="undo-btn" @click="handleUndo">悔棋</button>
      <Time_limit
        ref="timeLimit"
        :startOnSelect="startOnSelect"
        @timeout="handleTimeout"
        @timer-started="onTimerStarted"
      />
      <div id="main">
          <Ground :unitSize="unitSize" style="position: absolute; top: 0; left: 0;" />
          <Board 
          :unitSize="unitSize" 
          :layout="layout"
          @move-success="incrementCount"
          @update-layout="updateLayout"
          @select-index="handleSelectIndex"
          @help-used="count++"
          @victory="handleVictory" 
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
      <div class="move-bottom-right">
          <!-- 移动控制组件 -->
          <Move ref="move" :layout="layout" @update-layout="updateLayout" @move-success="incrementCount" />
      </div>
  </div>
</template>
  
<script>

import Ground from '@/Component/Ground.vue';
import Board from '@/Component/Board.vue';
import Level from '@/Component/Level.vue';
import ShowCount from '@/Component/ShowCount.vue'; 
import Move from '@/Component/Move.vue'; 
import Time_limit from '@/Component/Time_limit.vue';
import { addAward,addPlayingUser,deletePlayingUser,changePlayingUser,getPlayingUsers,queryById } from '@/api/users';
import { onBeforeUnmount } from 'vue';
import { useRouter } from 'vue-router';

export default {
    components: { Ground, Board, Level, ShowCount,Move,Time_limit },
    
    data () {
        return {
          unitSize: Math.min(window.innerWidth / 8, window.innerHeight / 8), // 基于宽度和高度的最小值
          layout: '22222222222222222222',
          title: '选择关卡',
          showLevel: false,
          isGameActive: true, // 游戏是否处于活动状态
          count: 0,
          history: [], // 保存 { layout, selectedIndex }
          startOnSelect: false, // 是否在选择关卡后自动开始计时  
        }
    },
    methods: {
      handleSelect (level) {
        this.layout = level.layout;
        this.title = level.title;
        this.showLevel = false;
        this.isGameActive = true; // 游戏开始
        this.count = 0; // 重置步数
        this.startOnSelect = false; // 重置计时器状态
        this.$nextTick(() => {
          this.startOnSelect = true; // 触发Time_limit自动开始
        });
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
      handleTimeout() {
        if(!this.isGameActive) return;// 如果游戏已经结束，则不处理超时
        this.isGameActive = false;
        this.$alert(
          `很遗憾，时间已到，挑战失败！`,
          '失败',
          {
            confirmButtonText: '确定',
            center: true,
            customClass: 'fail-message-box'
          }
        );
        // 可选：重置关卡
        this.layout = '22222222222222222222';
      },
      onTimerStarted() {
      // 计时开始时的回调（可选）
          console.log("计时器已开始");
      },
      handleVictory() {
          if (!this.isGameActive) return;
          this.isGameActive = false;
        this.layout = '22222222222222222222'; // 终局
        //获取用时
        const usedTime = this.$refs.timeLimit ? this.$refs.timeLimit.getUsedTime() : 0;
          this.$nextTick(() => {
              this.$alert(
                  `恭喜你，成功过关！<br>共用步数：<b style="color:#409EFF">${this.count}</b> 步<br>用时：<b style="color:#409EFF">${usedTime}</b> 秒`,
                  '通关成功',
                  {
                      dangerouslyUseHTMLString: true,
                      confirmButtonText: '确定',
                      center: true,
                      customClass: 'success-message-box'
                  }
              );
              this.checkVictory();
          });
      },
      handleUndo() {
          if (this.history.length > 0) {
              const prev = this.history.pop();
              console.log("layout更改为：", prev.layout);
              this.layout = prev.layout;
              if (this.count > 0) this.count--;
              this.$nextTick(() => {
                  // 恢复 Move 的选中位置
                  console.log("恢复 Move 的选中位置:", prev.selectedIndex);
                  this.$refs.move.setSelectedIndex(prev.selectedIndex);
              });
          } else {
              alert('没有可以悔棋的步骤了！');
          }
      },
      updateLayout(newLayout) {
          console.log("updateLayout函数被调用，新的布局为:", newLayout);
          const moveRef = this.$refs.move;
          let selectedIndex = -1;
          if (moveRef && moveRef.selectedIndex !== undefined) {
              selectedIndex = typeof moveRef.selectedIndex === 'object' && 'value' in moveRef.selectedIndex
                  ? moveRef.selectedIndex.value
                  : moveRef.selectedIndex;
          }
          console.log('保存到history的selectedIndex:', selectedIndex);
          console.log('保存到history的layout:', this.layout);
          this.history.push({ layout: this.layout, selectedIndex });
          this.layout = newLayout;
      },
      //用于在鼠标点击后重新确定选中位置
      handleSelectIndex(newIndex) {
          // 保存当前 layout 和当前选中位置到 history
          const moveRef = this.$refs.move;
          let selectedIndex = -1;
          if (moveRef && moveRef.selectedIndex !== undefined) {
              selectedIndex = typeof moveRef.selectedIndex === 'object' && 'value' in moveRef.selectedIndex
                  ? moveRef.selectedIndex.value
                  : moveRef.selectedIndex;
          }

          // 设置 Move 的新选中位置
          if (this.$refs.move && typeof newIndex === 'number') {
              this.$refs.move.setSelectedIndex(newIndex);
          }
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

            // 检查是否为初始关卡，若是则停止计时
            if (newLayout === '22222222222222222222') {
              this.$refs.timeLimit && this.$refs.timeLimit.stopTimer();
            }
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
.undo-btn {
    position: absolute;
    top: 18%;
    left: 290px;
    z-index: 100;
    padding: 6px 16px;
    background: rgba(255,255,255,0.7);  // 半透明白色背景
    color: #000;                        // 黑色文字
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 16px;
}  
.move-bottom-right {
    position: fixed;
    right: 40px;
    bottom: 40px;
    z-index: 200;
}
</style>

