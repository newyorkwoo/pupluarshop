<template>
  <div class="flex items-center gap-4 py-4 border-b border-gray-100">
    <router-link :to="`/products/${item.slug}`" class="w-20 h-24 bg-brand-cream shrink-0 overflow-hidden">
      <img :src="item.image || '/placeholder.jpg'" :alt="item.name" class="w-full h-full object-cover" />
    </router-link>
    <div class="flex-1 min-w-0">
      <h4 class="text-sm font-medium truncate">{{ item.name }}</h4>
      <p class="text-xs text-brand-gray mt-1">{{ formatCurrency(item.price) }}</p>
      <div class="flex items-center gap-3 mt-2">
        <button @click="$emit('update', item.productId, item.quantity - 1)" class="w-6 h-6 border border-gray-300 flex items-center justify-center text-xs hover:border-brand-black">−</button>
        <span class="text-sm w-6 text-center">{{ item.quantity }}</span>
        <button @click="$emit('update', item.productId, item.quantity + 1)" class="w-6 h-6 border border-gray-300 flex items-center justify-center text-xs hover:border-brand-black">+</button>
      </div>
    </div>
    <div class="text-right">
      <p class="text-sm font-medium">{{ formatCurrency(item.price * item.quantity) }}</p>
      <button @click="$emit('remove', item.productId)" class="text-xs text-brand-gray hover:text-brand-error mt-2">移除</button>
    </div>
  </div>
</template>

<script setup>
import { formatCurrency } from '@/utils/formatters'
defineProps({ item: { type: Object, required: true } })
defineEmits(['update', 'remove'])
</script>
