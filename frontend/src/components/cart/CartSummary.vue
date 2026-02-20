<template>
  <div class="bg-brand-cream p-6">
    <h3 class="section-title mb-4">訂單摘要</h3>
    <div class="space-y-2 text-sm">
      <div class="flex justify-between"><span>小計</span><span>{{ formatCurrency(cartStore.totalAmount) }}</span></div>
      <div class="flex justify-between text-brand-gray"><span>運費</span><span>{{ shipping === 0 ? '免運' : formatCurrency(shipping) }}</span></div>
      <div v-if="discount > 0" class="flex justify-between text-brand-success"><span>折扣</span><span>-{{ formatCurrency(discount) }}</span></div>
    </div>
    <hr class="my-4 border-gray-300" />
    <div class="flex justify-between text-base font-semibold">
      <span>總計</span>
      <span>{{ formatCurrency(cartStore.totalAmount + shipping - discount) }}</span>
    </div>
    <slot />
  </div>
</template>

<script setup>
import { useCartStore } from '@/stores/cartStore'
import { formatCurrency } from '@/utils/formatters'

defineProps({
  shipping: { type: Number, default: 0 },
  discount: { type: Number, default: 0 },
})

const cartStore = useCartStore()
</script>
