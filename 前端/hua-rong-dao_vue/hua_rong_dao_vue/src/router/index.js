import { createRouter, createWebHistory } from 'vue-router';
// import HomeView from '@/views/components/HomeView.vue';

import Layout from '@/views/layout.vue'; // 引入 Layout 组件
import normalGameView from '@/views/normalGame.vue';
import timeModeView from '@/views/timeMode.vue';
import leaderboardView from '@/views/leaderboard.vue';
import spectateView from '@/views/spectate.vue';
import loginView from '@/views/login.vue';
import registerView from '@/views/register.vue';
import GameView from '@/views/Game.vue'; 
import guestView from '@/views/guest.vue';
import realSpectateView from '@/views/real_spectate.vue';

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            component: Layout, // Layout 作为父路由
            redirect: '/login', // 默认重定向到 normalGame
            children: [
                {
                    path: 'normalGame',
                    name: 'normalGame', // 给这个路由起的名字
                    component: normalGameView // 展示的组件就是上方 import 的那个组件
                },
                {
                    path: 'timeMode',
                    name: 'timeMode', // 时间模式路由
                    component: timeModeView // 对应的时间模式组件
                },
                {
                    path: 'leaderboard',
                    name: 'leaderboard', // 排行榜路由
                    component: leaderboardView // 对应的排行榜组件
                },
                {
                    path: 'spectate',
                    name: 'spectate', // 旁观模式路由
                    component: spectateView // 对应的旁观模式组件
                },
                {
                    path: '/game',
                    name: 'game', // 游戏路由
                    component: GameView
                },
                {
                    path: '/real_spectate/:username',
                    name: 'real_spectate', // 真实旁观路由
                    component: realSpectateView // 对应的真实旁观组件
                }
            ]
        },
        {
            path: '/login',
            name: 'login', // 登录路由
            component: loginView // 对应的登录组件
        },
        {
            path: '/register',
            name: 'register', // 注册路由
            component: registerView // 对应的注册组件
        },
        {
            path: '/guest',
            name: 'guest', // 游客路由
            component: guestView
        }
    ]
});

export default router;