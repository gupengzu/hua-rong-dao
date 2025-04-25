<script setup>
import { ref, onMounted } from 'vue'; // 导入 ref 和 onMounted

//定义网格大小
const gridRows = 4;
const gridCols = 5;

//定义网格状态（0表示空白块）
const grid = ref([
  [1, 1, 0, 0],
  [1, 1, 2, 2],
  [3, 3, 2, 2],
  [4, 4, 5, 5],
  [6, 7, 8, 9],
])

//定义当前空白块位置
const emptyBlock = ref({ row: 0, col: 2 });

//初始化键盘事件监听
onMounted(() => {
  window.addEventListener('keydown', handleKeyDown);
});

//键盘事件处理函数
const handleKeyDown = (event) => {
    const { row, col } = emptyBlock.value;
    let newRow = row;
    let newCol = col;
    //根据按键方向更新空白块位置
    if (event.key === 'ArrowUp' && row < gridRows - 1) newRow++;
    if (event.key === 'ArrowDown' && row > 0) newRow--;
    if (event.key === 'ArrowLeft' && col < gridCols - 1) newCol++;
    if (event.key === 'ArrowRight' && col > 0) newCol--;

    //移动块
    moveBlock(newRow, newCol);
}
//鼠标点击事件处理函数
const handleTileClick = (row, col) => {
    const { row: emptyRow, col: emptyCol } = emptyBlock.value;
    //判断点击的块是否可以移动
    if ((row === emptyRow && Math.abs(col - emptyCol) === 1) || (col === emptyCol && Math.abs(row - emptyRow) === 1)) {
        moveBlock(row, col);
    }
}

//块移动函数
const moveBlock = (newRow, newCol) => {
    const { row, col } = emptyBlock.value;
    //交换空白块和目标块
    [grid.value[row][col], grid.value[newRow][newCol]] = [grid.value[newRow][newCol], grid.value[row][col]];
    //更新空白块位置
    emptyBlock.value = { row: newRow, col: newCol };
    //检查游戏是否完成
    if (isGameCompleted()) {
        alert('游戏完成！');
    }
}

// 获取块的样式
const getTileStyle = (tile, rowIndex, colIndex) => {
  if (tile === 1) {
    return {
      gridRow: `${rowIndex + 1} / span 2`, // 从当前行开始，占 2 行
      gridColumn: `${colIndex + 1} / span 2`, // 从当前列开始，占 2 列
      backgroundColor: '#f28b82', // 曹操块颜色
    };
  }
  if (tile === 2) {
    return {
      gridRow: `${rowIndex + 1} / span 1`, // 从当前行开始，占 1 行
      gridColumn: `${colIndex + 1} / span 2`, // 从当前列开始，占 2 列
      backgroundColor: '#fbbc04', // 关羽块颜色
    };
  }
  if (tile === 3) {
    return {
      gridRow: `${rowIndex + 1} / span 2`, // 从当前行开始，占 2 行
      gridColumn: `${colIndex + 1} / span 1`, // 从当前列开始，占 1 列
      backgroundColor: '#34a853', // 普通块颜色
    };
  }
  return {
    gridRow: `${rowIndex + 1} / span 1`, // 默认占 1 行
    gridColumn: `${colIndex + 1} / span 1`, // 默认占 1 列
    backgroundColor: '#4285f4', // 士兵块颜色
  };
};

</script>

<template>
  <div class="game-container">
    <div class="grid">
      <div
        v-for="(row, rowIndex) in grid"
        :key="rowIndex"
        class="row"
      >
        <div
          v-for="(tile, colIndex) in row"
          :key="colIndex"
          class="tile"
          :class="{ empty: tile === 0 }"
          :style="getTileStyle(tile,rowIndex,colIndex)"
          @click="handleTileClick(rowIndex, colIndex)"
        >
          {{ tile !== 0 ? tile : '' }}
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 游戏容器样式 */
.game-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* 全屏高度 */
  background-color: #f0f0f0; /* 背景颜色 */
}

/* 网格样式 */
.grid {
  display: grid;
  grid-template-rows: repeat(5, 80px); /* 每行高度 */
  grid-template-columns: repeat(4, 80px); /* 每列宽度 */
  gap: 0; /* 去掉块之间的间距 */
  border: 2px solid #ccc; /* 网格外边框 */
  box-sizing: border-box; /* 确保边框不会影响块大小 */
}

.tile {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #4285f4;
  color: white;
  font-size: 20px;
  font-weight: bold;
  border: 1px solid #ccc; /* 每个块的边框 */
  box-sizing: border-box; /* 确保边框不会影响块大小 */
}

.tile.empty {
  background-color: #fff; /* 空白块颜色 */
  border: 1px solid #ccc; /* 空白块边框 */
  box-sizing: border-box; /* 确保边框不会影响块大小 */
}
</style>