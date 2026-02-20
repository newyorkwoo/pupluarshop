<template>
  <div v-if="totalPages > 1" class="flex items-center justify-center gap-2 mt-8">
    <button @click="$emit('change', current - 1)" :disabled="current <= 0"
      class="px-3 py-2 border border-gray-300 text-xs hover:border-brand-black disabled:opacity-30 disabled:cursor-not-allowed">&lt;</button>
    <button v-for="page in visiblePages" :key="page" @click="$emit('change', page)"
      :class="['px-3 py-2 border text-xs', page === current ? 'border-brand-black bg-brand-black text-white' : 'border-gray-300 hover:border-brand-black']">
      {{ page + 1 }}
    </button>
    <button @click="$emit('change', current + 1)" :disabled="current >= totalPages - 1"
      class="px-3 py-2 border border-gray-300 text-xs hover:border-brand-black disabled:opacity-30 disabled:cursor-not-allowed">&gt;</button>
  </div>
</template>

<script setup>
import { computed } from 'vue'
const props = defineProps({ current: Number, totalPages: Number })
defineEmits(['change'])

const visiblePages = computed(() => {
  const pages = []
  const start = Math.max(0, props.current - 2)
  const end = Math.min(props.totalPages, start + 5)
  for (let i = start; i < end; i++) pages.push(i)
  return pages
})
</script>
