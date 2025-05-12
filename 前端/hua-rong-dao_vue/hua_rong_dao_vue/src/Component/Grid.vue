<template>
    <svg>
        <!-- 如果 type 是 5，使用 foreignObject 显示图片 -->
        <foreignObject
            v-if="type === '5'"
            :x="x"
            :y="y"
            :width="width"
            :height="height"
        >
            <div
                :style="{
                    width: '50%',
                    height: '36%',
                    backgroundImage: `url(${imageSrcType5})`,
                    backgroundSize: '100% 100%',
                    backgroundPosition: 'center'
                }"
                @touchstart="handleTouchStart"
                @touchmove="handleTouchMove"
                @touchend="handleTouchEnd"
                @mousedown="handleMouseDown"
                @mouseup="handleMouseUp"
                ></div>
        </foreignObject>

        <!-- 如果 type 是 4，使用 foreignObject 显示图片 -->
        <foreignObject
        v-else-if="type === '4'"
        :x="x"
        :y="y"
        :width="width"
        :height="height"
        >
            <div
                :style="{
                    width: '50%',
                    height: '18%',
                    backgroundImage: `url(${imageSrcType4})`,
                    backgroundSize: '100% 100%',
                    backgroundPosition: 'center'
                }"
                @touchstart="handleTouchStart"
                @touchmove="handleTouchMove"
                @touchend="handleTouchEnd"
                @mousedown="handleMouseDown"
                @mouseup="handleMouseUp"
            ></div>
        </foreignObject>
        <!-- 如果 type 是 3，使用 foreignObject 显示图片 -->
        <foreignObject
        v-else-if="type === '3'"
        :x="x"
        :y="y"
        :width="width"
        :height="height"
        >
            <div
                :style="{
                    width: '25%',
                    height: '36%',
                    backgroundImage: `url(${imageSrcType3})`,
                    backgroundSize: '100% 100%',
                    backgroundPosition: 'center'
                }"
                @touchstart="handleTouchStart"
                @touchmove="handleTouchMove"
                @touchend="handleTouchEnd"
                @mousedown="handleMouseDown"
                @mouseup="handleMouseUp"
            ></div>
        </foreignObject>

        <!-- 如果 type 是 2，使用 foreignObject 显示图片 -->
        <foreignObject
        v-else-if="type === '2'"
        :x="x"
        :y="y"
        :width="width"
        :height="height"
        >
            <div
                :style="{
                    width: '25%',
                    height: '18%',
                    backgroundImage: `url(${imageSrcType2})`,
                    backgroundSize: '100% 100%',
                    backgroundPosition: 'center'
                }"
                @touchstart="handleTouchStart"
                @touchmove="handleTouchMove"
                @touchend="handleTouchEnd"
                @mousedown="handleMouseDown"
                @mouseup="handleMouseUp"
            ></div>
        </foreignObject>
        <!-- 如果不是 type 5432，显示普通矩形 -->
        <rect
            v-else
            class="grid"
            :x="x"
            :y="y"
            :width="width"
            :height="height"
            :fill="fillColor"
            :data-type="type"
            @touchstart="handleTouchStart"
            @touchmove="handleTouchMove"
            @touchend="handleTouchEnd"
            @mousedown="handleMouseDown"
            @mouseup="handleMouseUp"
        />
    </svg>
</template>

<script>

import caocaoImage from '@/assets/caocao.png';
import guanyuImage from '@/assets/guanyu.png';
import zhaoyunImage from '@/assets/zhaoyun.png';
import xiaobingImage from '@/assets/xiaobing.png';

export default {
    props: ['type', 'position', 'startX', 'startY', 'unitSize', 'handleMove', 'success'],
    data () {
        return {
            draging: false,
            x: this.startX,
            y: this.startY,
            previousX: 0,
            previousY: 0
        }
    },
    watch: {
        startX (newValue) { this.x = newValue; },
        startY (newValue) { this.y = newValue; }
    },
    computed: {
        imageSrcType5() {
            // 动态加载图片路径
            return caocaoImage;
        },
        imageSrcType4() {
            // 动态加载图片路径
            return guanyuImage;
        },
        imageSrcType3() {
            // 动态加载图片路径
            return zhaoyunImage;
        },
        imageSrcType2() {
            // 动态加载图片路径
            return xiaobingImage;
        },
        height () {
            switch (this.type) {
                case '2': case '4': return this.unitSize;
                case '3': case '5': return this.unitSize * 2;
                default: return 0;
            }
        },
        width () {
            switch (this.type) {
                case '2': case '3': return this.unitSize;
                case '4': case '5': return this.unitSize * 2;
                default: return 0;
            }
        }
    },
    methods: {
        handleTouchStart (e) {
            this.previousX = e.targetTouches[0].clientX;
            this.previousY = e.targetTouches[0].clientY;
            this.draging = true;
        },
        handleTouchMove (e) {
            let currentX = e.targetTouches[0].clientX;
            let currentY = e.targetTouches[0].clientY;
            

            this.x += currentX - this.previousX;
            this.y += currentY - this.previousY;

            this.previousX = currentX;
            this.previousY = currentY;

            let shiftX = this.x - this.startX;
            let shiftY = this.y - this.startY;
            let direction = 0;
            if (Math.abs(shiftY) > Math.abs(shiftX)) {
                if (-shiftY > this.unitSize / 1.5) direction = 1;
                else if (shiftY > this.unitSize / 1.5) direction = 3;
            } else {
                if (shiftX > this.unitSize / 1.5) direction = 2;
                else if (-shiftX > this.unitSize / 1.5) direction = 4;
            }
            this.handleMove(direction, this.position);
        },
        handleTouchEnd (e) {
            let _this = this, step = 5;
            let distanceX = _this.x - this.startX;
            let distanceY = _this.y - this.startY;
            let inv = setInterval(() => {
                if (!--step) { clearInterval(inv); _this.draging = false; }
                _this.x = this.startX + distanceX * step * 0.1;
                _this.y = this.startY + distanceY * step * 0.1;
            }, 10);
        },
        handleMouseDown(e) {
            // 记录鼠标初始位置
            this.previousX = e.clientX;
            this.previousY = e.clientY;
            this.draging = true;

            // 添加全局事件监听器，处理鼠标移动和释放
            document.addEventListener('mousemove', this.handleMouseMove);
            document.addEventListener('mouseup', this.handleMouseUp);
        },
        handleMouseMove(e) {
            // 计算鼠标移动的偏移量
            let deltaX = e.clientX - this.previousX;
            let deltaY = e.clientY - this.previousY;

            // 更新滑块的位置
            this.x += deltaX;
            this.y += deltaY;

            // 更新鼠标的上一次位置
            this.previousX = e.clientX;
            this.previousY = e.clientY;

            // 计算滑块的移动方向
            let shiftX = this.x - this.startX;
            let shiftY = this.y - this.startY;
            let direction = 0;

            if (Math.abs(shiftY) > Math.abs(shiftX)) {
                if (-shiftY > this.unitSize / 1.5) direction = 1; // 向上
                else if (shiftY > this.unitSize / 1.5) direction = 3; // 向下
            } else {
                if (shiftX > this.unitSize / 1.5) direction = 2; // 向右
                else if (-shiftX > this.unitSize / 1.5) direction = 4; // 向左
            }

            // 调用父组件的移动逻辑
            if (direction !== 0) {
                this.handleMove(direction, this.position);
            }
        },
        handleMouseUp() {
            // 停止拖动
            this.draging = false;

            // 移除全局事件监听器
            document.removeEventListener('mousemove', this.handleMouseMove);
            document.removeEventListener('mouseup', this.handleMouseUp);

            // 重置滑块的位置
            this.x = this.startX;
            this.y = this.startY;
        }
    }
}

</script>

<style lang="less">
    .grid {
        fill: #09c;
        stroke: #fff;

        &#draging {
            stroke: #0ff;
        }

        &[data-type="5"] {
            fill: #f44;

            &.success {
                fill: #0a0;
            }
        }
    }
    foreignObject {
    display: block;
    width: 100%;
    height: 100%;
}
</style>