<!-- 定义JS，控制模板部分的数据和行为 -->
<script setup>
import layout from '@/views/layout.vue';
import router from './router';
</script>

<!-- 模板部分，控制的是页面的结构 -->
<template>
<router-view></router-view>
</template>

<!-- 当前组件的CSS样式 -->
<style scoped>

</style>
