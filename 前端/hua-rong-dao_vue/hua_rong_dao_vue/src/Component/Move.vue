<template>
  <div class="move-panel">
    <div class="select-btns">
      <button @click="selectPrev">选中上一个滑块</button>
      <button @click="selectNext">选中下一个滑块</button>
      <span
        v-if="selectedIndex !== -1"
        class="selected-info"
      >
        当前选中位置: 第{{ Math.floor(selectedIndex / 4) + 1 }}行，第{{ (selectedIndex % 4) + 1 }}列
      </span>
    </div>
    <div class="jump-btn-row">
    <input
      v-model.number="inputRow"
      type="number"
      min="1"
      max="5"
      placeholder="行"
      style="width: 50px; margin-right: 6px;"
    />
    <span style="margin-right: 8px;">行</span>
    <input
      v-model.number="inputCol"
      type="number"
      min="1"
      max="4"
      placeholder="列"
      style="width: 50px; margin-right: 6px;"
    />
    <span style="margin-right: 8px;">列</span>
    <button @click="selectByInput">跳转选择</button>
  </div>
    <p class="input-tip">
      （请选择块的左上角位置，比如曹操块为34行、12列，请选3行1列）
    </p>
    <div class="move-btns">
      <button @click="move('up')">向上移动</button>
      <button @click="move('down')">向下移动</button>
      <button @click="move('left')">向左移动</button>
      <button @click="move('right')">向右移动</button>
    </div>
  </div>
</template>

<script setup>
import { ref, watch,defineExpose } from 'vue';
import core from '@/api/core.js'; //core.js 中有 moveUp, moveDown, moveLeft, moveRight 方法

// 假设父组件通过 provide 传递 layout 字符串
const props = defineProps({
  layout: String
});
// emit 事件用于更新布局
const emit = defineEmits(['update-layout']);

const selectedIndex = ref(-1);
const inputRow = ref(1); // 行（1~5）
const inputCol = ref(1); // 列（1~4）


// 只允许选中2/3/4/5
function isSelectable(val) {
  return ['2', '3', '4', '5'].includes(val);
}

// 只在初始时自动选中第一个可选块
let initialized = false;
watch(
  () => props.layout,
  (val, oldVal) => {
    if (!val) return;
    // 只有第一次或layout长度变化时才自动选
    if (!initialized || (oldVal && val.length !== oldVal.length)) {
      for (let i = 0; i < val.length; i++) {
        if (isSelectable(val[i])) {
          selectedIndex.value = i;
          break;
        }
      }
      initialized = true;
    }
    // 否则什么都不做，保持selectedIndex
  },
  { immediate: true }
);

const selectPrev = () => {
  if (!props.layout || selectedIndex.value <= 0) return;
  for (let i = selectedIndex.value - 1; i >= 0; i--) {
    if (isSelectable(props.layout[i])) {
      selectedIndex.value = i;
      console.log('当前选中位置:', selectedIndex.value);
      return;
    }
  }
  console.log('没有找到上一个可选块');
};

const selectNext = () => {
  if (!props.layout || selectedIndex.value === -1 || selectedIndex.value >= props.layout.length - 1) return;
  for (let i = selectedIndex.value + 1; i < props.layout.length; i++) {
    if (isSelectable(props.layout[i])) {
      selectedIndex.value = i;
      console.log('当前选中位置:', selectedIndex.value);
      return;
    }
  }
  console.log('没有找到下一个可选块');
};

//通过输入行列选择块
const selectByInput = () => {
  console.log('输入行:', inputRow.value, '列:', inputCol.value);
  if (!props.layout) return;
  const row = inputRow.value;
  const col = inputCol.value;
  if (
    row < 1 || row > 5 ||
    col < 1 || col > 4
  ) return;
  let idx = (row - 1) * 4 + (col - 1);
  for (let i = idx; i < props.layout.length; i++) {
    if (isSelectable(props.layout[i])) {
      selectedIndex.value = i;
      console.log('当前选中位置:', selectedIndex.value);
      return;
    }
  }
};

const move = (direction) => {
  if (selectedIndex.value === -1) return;
  let nextState = false;
  let newIndex = selectedIndex.value;
  switch (direction) {
    case 'up':
      nextState = core.moveUp(props.layout, selectedIndex.value);
      if (nextState) newIndex -= 4;
      break;
    case 'down':
      nextState = core.moveDown(props.layout, selectedIndex.value);
      if (nextState) newIndex += 4;
      break;
    case 'left':
      nextState = core.moveLeft(props.layout, selectedIndex.value);
      if (nextState) newIndex -= 1;
      break;
    case 'right':
      nextState = core.moveRight(props.layout, selectedIndex.value);
      if (nextState) newIndex += 1;
      break;
  }
  if (nextState) {
    emit('update-layout', nextState);
    emit('move-success'); 
    // 检查新位置是否还是可选块，否则重新找
    if (
      newIndex >= 0 &&
      newIndex < nextState.length &&
      isSelectable(nextState[newIndex])
    ) {
      selectedIndex.value = newIndex;
    } else {
      // 如果新位置不是可选块，自动选第一个可选块
      for (let i = 0; i < nextState.length; i++) {
        if (isSelectable(nextState[i])) {
          selectedIndex.value = i;
          break;
        }
      }
    }
  }
};

//这个是在悔棋的时候也要把选中的位置悔棋一步。
function setSelectedIndex(idx) {
  selectedIndex.value = idx;
}
defineExpose({ setSelectedIndex,selectedIndex });

</script>

<style scoped>
.move-panel {
  display: flex;
  flex-direction: column;
  gap: 12px;
  align-items: flex-start;
}
.select-btns, .move-btns {
  display: flex;
  gap: 10px;
}
button {
  padding: 6px 14px;
  border-radius: 4px;
  border: 1px solid #409eff;
  background: #fff;
  color: #000;
  cursor: pointer;
  font-size: 14px;
  transition: background 0.2s, color 0.2s;
}
button:hover {
  background: #409eff;
  color: #fff;
}
.selected-info {
  margin-left: 10px;
  color: #333;
  background: #fff;
  border-radius: 4px;
  padding: 6px 14px;
  border: 1px solid #409eff;
  font-size: 14px;
  display: inline-block;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
}
</style>