<template>
  <div class="space-y-4">
    <h3 class="section-title">訂單確認</h3>
    <div class="space-y-3">
      <div v-for="item in cartStore.items" :key="item.productId" class="flex items-center gap-3 text-sm">
        <div class="w-12 h-16 bg-brand-cream shrink-0 overflow-hidden">
          <img :src="item.image || '/placeholder.jpg'" :alt="item.name" class="w-full h-full object-cover" />
        </div>
        <div class="flex-1">
          <p class="font-medium truncate">{{ item.name }}</p>
          <p class="text-brand-gray text-xs">x{{ item.quantity }}</p>
        </div>
        <p>{{ formatCurrency(item.price * item.quantity) }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useCartStore } from '@/stores/cartStore'
import { formatCurrency } from '@/utils/formatters'
const cartStore = useCartStore()
</script>
