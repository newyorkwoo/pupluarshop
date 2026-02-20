<template>
  <nav class="flex items-center text-xs text-brand-gray mb-6">
    <router-link to="/" class="hover:text-brand-black transition-colors">首頁</router-link>
    <template v-for="(crumb, i) in crumbs" :key="i">
      <span class="mx-2">/</span>
      <router-link v-if="crumb.to" :to="crumb.to" class="hover:text-brand-black transition-colors">{{ crumb.text }}</router-link>
      <span v-else class="text-brand-black">{{ crumb.text }}</span>
    </template>
  </nav>
</template>

<script setup>
defineProps({ crumbs: { type: Array, default: () => [] } })
</script>
