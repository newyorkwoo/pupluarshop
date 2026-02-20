<template>
  <div class="space-y-4">
    <select v-model="sort" @change="$emit('change', { sort })" class="input-field text-xs">
      <option value="">排序方式</option>
      <option value="price_asc">價格：低到高</option>
      <option value="price_desc">價格：高到低</option>
      <option value="newest">最新上架</option>
      <option value="name_asc">名稱 A-Z</option>
    </select>
  </div>
</template>

<script setup>
import { ref } from 'vue'

defineEmits(['change'])
const sort = ref('')
</script>
