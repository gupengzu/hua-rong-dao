<script setup>

</script>

<template>
  <div class="common-layout">
    <el-container>
      <!-- Header 区域 -->
      <el-header class="header">华容道</el-header>
      <el-container>
        <!-- 左侧菜单 -->
        <el-aside width="200px" class="aside">
          <!-- 左侧菜单栏 -->
            <el-menu router="true">
              <!-- 正常游戏菜单 -->
              <el-menu-item index="normalGame">
                <el-icon><GameIcon /></el-icon> 正常游戏
              </el-menu-item>
              <!-- 限时模式菜单 -->
              <el-menu-item index="timeMode">
                <el-icon><TimerIcon /></el-icon> 限时模式
              </el-menu-item>
              <!-- 排行榜菜单 -->
              <el-menu-item index="leaderboard">
                <el-icon><RankingIcon /></el-icon> 排行榜
              </el-menu-item>
              <!-- 观战菜单 -->
              <el-menu-item index="spectate">
                <el-icon><SpectateIcon /></el-icon> 观战
              </el-menu-item>
            </el-menu>
        </el-aside>
        <el-main>
          <<router-view></router-view>>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>



<style scoped>

</style>
