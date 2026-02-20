<template>
  <nav class="flex items-center text-xs text-brand-gray mb-6">
    <template v-for="(item, i) in items" :key="i">
      <span v-if="i > 0" class="mx-2">/</span>
      <router-link v-if="item.to" :to="item.to" class="hover:text-brand-black transition-colors">{{ item.label }}</router-link>
      <span v-else class="text-brand-black">{{ item.label }}</span>
    </template>
  </nav>
</template>

<script setup>
defineProps({ items: { type: Array, default: () => [] } })
</script>
