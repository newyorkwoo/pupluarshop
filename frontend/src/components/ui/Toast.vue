<template>
  <Teleport to="body">
    <transition name="toast">
      <div v-if="visible" :class="['fixed top-20 right-4 z-[100] px-6 py-3 text-sm text-white shadow-lg', typeClass]">
        {{ message }}
      </div>
    </transition>
  </Teleport>
</template>

<script setup>
import { ref, watch, computed } from 'vue'
const props = defineProps({ message: String, type: { type: String, default: 'info' }, duration: { type: Number, default: 3000 } })
const visible = ref(false)

const typeClass = computed(() => ({
  success: 'bg-brand-success',
  error: 'bg-brand-error',
  info: 'bg-brand-black',
}[props.type] || 'bg-brand-black'))

watch(() => props.message, (val) => {
  if (val) {
    visible.value = true
    setTimeout(() => { visible.value = false }, props.duration)
  }
})
</script>

<style scoped>
.toast-enter-active, .toast-leave-active { transition: all 0.3s; }
.toast-enter-from, .toast-leave-to { opacity: 0; transform: translateX(100%); }
</style>
