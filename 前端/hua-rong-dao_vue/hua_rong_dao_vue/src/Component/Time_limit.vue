<template>
  <div class="time-limit">
    <div v-if="!timing">
      <label>设置倒计时（秒）：</label>
      <input type="number" v-model.number="inputSeconds" min="1" />
      <button @click="startTimer" :disabled="inputSeconds < 1">关卡开启后自动开始计时</button>
    </div>
    <div v-else>
      <span>剩余时间：{{ seconds }} 秒</span>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    // 关卡选择后自动开始计时
    startOnSelect: { type: Boolean, default: false }
  },
  data() {
    return {
      inputSeconds: 60,
      seconds: 60,
      timer: null,
      timing: false
    };
  },
  methods: {
    startTimer() {
      this.stopTimer();
      this.seconds = this.inputSeconds;
      this.timing = true;
      this.$emit('timer-started');
      this.timer = setInterval(() => {
        if (this.seconds > 0) {
          this.seconds--;
        }
        if (this.seconds === 0) {
          this.stopTimer();
          this.$emit('timeout');
        }
      }, 1000);
    },
    getUsedTime() {
      return this.inputSeconds - this.seconds;
    },
    stopTimer() {
    if (this.timer) {
      clearInterval(this.timer);
      this.timer = null;
    }
    this.timing = false;
  },
    resetTimer() {
      this.stopTimer();
      this.seconds = this.inputSeconds;
    }
  },
  watch: {
    startOnSelect(val) {
      if (val) {
        this.startTimer();
      }
    }
  },
  beforeUnmount() {
    this.stopTimer();
  }
};
</script>

<style scoped>
.time-limit button {
  background: #fff;
  color: #000;
  border: 1px solid #ccc;
  padding: 4px 12px;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.2s;
}
.time-limit button:disabled {
  background: #eee;
  color: #aaa;
  cursor: not-allowed;
}
</style>