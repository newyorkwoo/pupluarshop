<template>
  <div v-if="totalPages > 1" class="flex items-center justify-center gap-2 mt-8">
    <button @click="$emit('update:page', currentPage - 1)" :disabled="currentPage <= 1"
      class="px-3 py-2 border border-gray-300 text-xs hover:border-brand-black disabled:opacity-30 disabled:cursor-not-allowed">&lt;</button>
    <button v-for="page in visiblePages" :key="page" @click="$emit('update:page', page)"
      :class="['px-3 py-2 border text-xs', page === currentPage ? 'border-brand-black bg-brand-black text-white' : 'border-gray-300 hover:border-brand-black']">
      {{ page }}
    </button>
    <button @click="$emit('update:page', currentPage + 1)" :disabled="currentPage >= totalPages"
      class="px-3 py-2 border border-gray-300 text-xs hover:border-brand-black disabled:opacity-30 disabled:cursor-not-allowed">&gt;</button>
  </div>
</template>

<script setup>
import { computed } from 'vue'
const props = defineProps({ currentPage: Number, totalPages: Number })
defineEmits(['update:page'])

const visiblePages = computed(() => {
  const pages = []
  const start = Math.max(1, props.currentPage - 2)
  const end = Math.min(props.totalPages + 1, start + 5)
  for (let i = start; i < end; i++) pages.push(i)
  return pages
})
</script>
