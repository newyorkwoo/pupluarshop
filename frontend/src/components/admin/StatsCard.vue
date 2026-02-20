<template>
  <div class="bg-white rounded-lg border border-gray-200 p-6">
    <p class="text-xs uppercase tracking-wider text-brand-gray mb-1">{{ title }}</p>
    <p class="text-2xl font-bold text-brand-black">{{ value }}</p>
    <p v-if="change" :class="['text-xs mt-1', change > 0 ? 'text-brand-success' : 'text-brand-error']">
      {{ change > 0 ? '+' : '' }}{{ change }}%
    </p>
  </div>
</template>

<script setup>
defineProps({
  title: String,
  value: [String, Number],
  change: Number,
})
</script>
