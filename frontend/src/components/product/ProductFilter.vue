<template>
  <div class="space-y-4">
    <select id="product-sort" name="product-sort" v-model="sort" @change="$emit('update:sortBy', sort)" class="input-field text-xs">
      <option value="">排序方式</option>
      <option value="price_asc">價格：低到高</option>
      <option value="price_desc">價格：高到低</option>
      <option value="newest">最新上架</option>
      <option value="name_asc">名稱 A-Z</option>
    </select>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({ sortBy: { type: String, default: '' } })
defineEmits(['update:sortBy'])
const sort = ref(props.sortBy)
watch(() => props.sortBy, (v) => { sort.value = v })
</script>
