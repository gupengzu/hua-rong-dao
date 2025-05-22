<template>
    <svg :width="width" :height="height" @touchstart="e => e.preventDefault()">
        <Grid
            v-for="(t, i) in layout"
            :type="t"
            :position="i"
            :key="`grid-${i}`"
            :success="t === '5' && success"
            :unitSize="unitSize"
            :startX="(i % 4) * unitSize"
            :startY="Math.floor(i / 4) * unitSize"
            :handleMove="handleMove"
            @mouse-up-select="handleMouseUpSelect"
        />
        <rect class="tip" :class="{ success, thinking }" 
            :x="unitSize * 1.2" :y="unitSize * 5.05"
            :width="unitSize * 1.6" :height="unitSize * 0.4"
            @click="help" @touchstart="help" />
        <use xlink:href="#draging" />
    </svg>
</template>

<script>

import Grid from './Grid.vue';
import core from '@/api/core.js'
import { ElMessageBox } from 'element-plus';
import { ElMessage } from 'element-plus';


export default {
    components: { Grid },
    props: [ 'unitSize', 'layout' ],
    data () { 
        return {
            state: this.layout,
            answer: [], 
            thinking: false
        };
    },
    computed: {
        width () { return this.unitSize * 4; },
        height () { return this.unitSize * 5.5; },
        success () { return this.state[13] === '5'; }
    },
    watch: {
        layout(newValue) {
            console.log("Board.vue 接收到新的 layout:", newValue);
            this.state = newValue; this.answer = [];
        }
    },
    methods: {
        handleMove (direction, position) {
            let nextState = false;
            switch (direction) {
                case 1: nextState = core.moveUp(this.state, position); break;
                case 2: nextState = core.moveRight(this.state, position); break;
                case 3: nextState = core.moveDown(this.state, position); break;
                case 4: nextState = core.moveLeft(this.state, position); break;
            }
            if (nextState) {
                this.state = nextState;
                this.answer = [];

                //移动成功，通知父组件更新layout
                this.$emit('update-layout', this.state);

                //移动成功，修改兄弟组件ShowCount的count,再通过ShowCount修改父组件Game
                this.$emit('move-success');

                if (this.state[13] === '5') {
                    this.$parent.layout = '22222222222222222222';
                    ElMessageBox.alert(
                        `恭喜你，成功过关！<br>共用步数：<b style="color:#409EFF">${this.$parent.count}</b> 步`,
                        '通关成功',
                        {
                            dangerouslyUseHTMLString: true,
                            confirmButtonText: '确定',
                            center: true,
                            customClass: 'success-message-box'
                        }
                    );
                    this.$parent.isGameActive = false; // 游戏结束
                    console.log("beforebefore")
                    this.$parent.checkVictory(); // 检查是否成功过关,追加获胜数
                    console.log("afterafter")
                }

            }
        },
        // 处理鼠标抬起事件,通知父组件改变选中位置
        handleMouseUpSelect(position) {
            // 通知 Game.vue 选中该滑块
            this.$emit('select-index', position);
        },
        help() {
            console.log("help调用");
            console.log(this.answer);
            this.thinking = true;
            setTimeout(() => {
                if (!this.answer.length)
                    this.answer = core.getSolve(this.state);
                if (this.answer.length)
                    this.state = this.answer.pop();
                this.thinking = false;
                this.$emit('update-layout', this.state);
                this.$emit('help-used');
            }, 10);
        }
    }
}

</script>

<style lang="less">

    svg {
        margin: 0 auto;

        .tip {
            fill: #0a0;

            &.thinking {
                fill: #f44;
            }

            &.success {
                fill: #fff;
            }
        }
    }
</style>