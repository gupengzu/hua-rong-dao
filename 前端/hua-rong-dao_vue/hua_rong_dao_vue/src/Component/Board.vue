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
        <!-- 左侧：提示走一步 -->
        <g @click="help" style="cursor:pointer;">
            <rect class="tip"
                :x="unitSize * 0.9"
                :y="unitSize * 5.05"
                :width="unitSize * 1.3"
                :height="unitSize * 0.4"
            />
            <text
                :x="unitSize * 1.55"
                :y="unitSize * 5.3"
                text-anchor="middle"
                alignment-baseline="middle"
                font-size="14"
                fill="#333"
                style="pointer-events: none;"
            >
                提示走一步
            </text>
        </g>
        <!-- 右侧：AI通关 -->
        <g @click="aiSolve" style="cursor:pointer;">
            <rect class="tip"
                :x="unitSize * 2.3"
                :y="unitSize * 5.05"
                :width="unitSize * 1.3"
                :height="unitSize * 0.4"
            />
            <text
                :x="unitSize * 2.95"
                :y="unitSize * 5.3"
                text-anchor="middle"
                alignment-baseline="middle"
                font-size="14"
                fill="#333"
                style="pointer-events: none;"
            >
                AI通关
            </text>
        </g>
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
            answer: [], 
            thinking: false,
            aiSteps:null,
        };
    },
    computed: {
        width () { return this.unitSize * 4; },
        height () { return this.unitSize * 5.5; },
        success () { return this.layout && this.layout[13] === '5'; }
    },
    methods: {
        handleMove (direction, position) {
            let nextState = false;
            switch (direction) {
                case 1: nextState = core.moveUp(this.layout, position); break;
                case 2: nextState = core.moveRight(this.layout, position); break;
                case 3: nextState = core.moveDown(this.layout, position); break;
                case 4: nextState = core.moveLeft(this.layout, position); break;
            }
            if (nextState) {
                this.answer = [];
                // 胜利判断应在 nextState 上
                if (nextState[13] === '5') {
                    this.$emit('update-layout', nextState);
                    this.$emit('move-success');
                    this.$emit('victory');
                    return;
                }
                //移动成功，通知父组件更新layout
                this.$emit('update-layout', nextState);
                this.$emit('move-success');
            }
        },
        // 处理鼠标抬起事件,通知父组件改变选中位置
        handleMouseUpSelect(position) {
            // 通知 Game.vue 选中该滑块
            this.$emit('select-index', position);
        },
        help() {
                return new Promise((resolve) => {
                    console.log("help调用");
                    this.thinking = true;
                    
                    if (!this.answer.length) {
                        console.log("this.answer.length为0,开始计算");
                        this.answer = core.getSolve(this.layout);
                    }

                    if (this.answer.length) {
                        console.log("this.answer.length不为0,开始更新");
                        const next = this.answer.pop();
                        // 胜利判断应在 next 上
                        if (next[13] === '5') {
                            this.$emit('update-layout', next);
                            this.$emit('help-used');
                            this.$emit('victory');
                            return;
                        }
                        this.$emit('update-layout', next);
                        this.$emit('help-used');
                    }

                    this.thinking = false;
                    setTimeout(resolve, 500);  // 延迟确保动画效果
                });
            },

            aiSolve() {
                if (this.thinking) return;
                if (!this.answer.length) {
                    this.answer = core.getSolve(this.layout);
                }
                const steps = [...this.answer].reverse();
                const total = steps.length;
                if (!total) {
                    ElMessage.warning('AI未找到解法');
                    return;
                }
                this.thinking = true;
                const playStep = (index) => {
                    if (index >= total) {
                        this.thinking = false;
                        return;
                    }
                    const next = steps[index];
                    const nextLayout = next.join ? next.join('') : next;
                    if (next[13] === '5') {
                        console.log("AI通关成功");
                        ElMessageBox.alert(
                            'AI已成功解答本局！',
                            'AI通关提示',
                            {
                                confirmButtonText: '确定',
                                type: 'success',
                                center: true,
                            }
                        );
                        this.$emit('update-layout', '22222222222222222222'); // 清空布局
                        this.thinking = false;
                        return;
                    }
                    console.log(`AI第${index + 1}步：`, nextLayout);
                    this.$emit('update-layout', nextLayout);
                    console.log(`AI第${index + 1}步update-layout完成`);
                    setTimeout(() => playStep(index + 1), 500);
                };
                playStep(0);
            }
        },
        watch: {
            layout(newVal, oldVal) {
                console.log("layout变化，重置AI答案");
                // 父组件layout变化时，重置AI答案
                this.answer = [];
                this.thinking = false;
            }
        }
    }
</script>

<style lang="less">

svg {
    margin: 0 auto;

    .tip {
        fill: #fff !important;
        stroke: #aaa;
        stroke-width: 1;
        cursor: pointer;

        &.thinking {
            fill: #f44 !important;
        }

        &.success {
            fill: #fff !important;
        }
    }
}
</style>