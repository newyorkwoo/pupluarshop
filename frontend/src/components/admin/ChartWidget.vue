<template>
  <div class="h-48 flex items-end gap-1">
    <div v-for="(val, i) in chartValues" :key="i"
      class="flex-1 bg-brand-gold/20 hover:bg-brand-gold/40 transition-colors rounded-t"
      :style="{ height: `${(val / max) * 100}%` }"
      :title="`${chartLabels?.[i] || i}: ${val}`" />
  </div>
  <div v-if="chartLabels" class="flex gap-1 mt-2">
    <span v-for="(l, i) in chartLabels" :key="i" class="flex-1 text-center text-[10px] text-brand-gray truncate">{{ l }}</span>
  </div>
</template>

<script setup>
import { computed } from 'vue'
const props = defineProps({
  title: String,
  data: { type: [Array, Object], default: () => [] },
  labels: Array,
})
const chartValues = computed(() => Array.isArray(props.data) ? props.data : (props.data?.values || []))
const chartLabels = computed(() => props.labels || (Array.isArray(props.data) ? null : props.data?.labels))
const max = computed(() => Math.max(...chartValues.value, 1))
</script>
