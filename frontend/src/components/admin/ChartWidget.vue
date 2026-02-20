<template>
  <div class="bg-white rounded-lg border border-gray-200 p-6">
    <h3 class="text-sm font-semibold mb-4">{{ title }}</h3>
    <div class="h-48 flex items-end gap-1">
      <div v-for="(val, i) in data" :key="i"
        class="flex-1 bg-brand-gold/20 hover:bg-brand-gold/40 transition-colors rounded-t"
        :style="{ height: `${(val / max) * 100}%` }"
        :title="`${labels?.[i] || i}: ${val}`" />
    </div>
    <div v-if="labels" class="flex gap-1 mt-2">
      <span v-for="(l, i) in labels" :key="i" class="flex-1 text-center text-[10px] text-brand-gray truncate">{{ l }}</span>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
const props = defineProps({
  title: String,
  data: { type: Array, default: () => [] },
  labels: Array,
})
const max = computed(() => Math.max(...props.data, 1))
</script>
