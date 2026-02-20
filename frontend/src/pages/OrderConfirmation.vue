<template>
  <div class="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-16 text-center">
    <div class="w-16 h-16 mx-auto mb-6 rounded-full bg-green-50 flex items-center justify-center">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
        <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7" />
      </svg>
    </div>
    <h1 class="text-2xl font-light tracking-wider mb-2">訂單已成立</h1>
    <p class="text-brand-gray mb-8">感謝您的購買！您的訂單編號為 <strong class="text-brand-black">{{ orderId }}</strong></p>

    <div v-if="order" class="text-left bg-brand-cream p-6 mb-8">
      <h2 class="text-sm uppercase tracking-[0.15em] font-semibold mb-4">訂單明細</h2>
      <div class="space-y-3 text-sm">
        <div class="flex justify-between"><span class="text-brand-gray">訂單日期</span><span>{{ formatDate(order.createdAt) }}</span></div>
        <div class="flex justify-between"><span class="text-brand-gray">付款方式</span><span>{{ order.paymentMethod }}</span></div>
        <div class="flex justify-between"><span class="text-brand-gray">配送地址</span><span>{{ order.shippingAddress }}</span></div>
        <div class="border-t border-brand-gray/20 pt-3 mt-3">
          <div v-for="item in order.items" :key="item.id" class="flex justify-between py-1">
            <span>{{ item.productName }} × {{ item.quantity }}</span>
            <span>{{ formatCurrency(item.subtotal) }}</span>
          </div>
        </div>
        <div class="border-t border-brand-gray/20 pt-3 flex justify-between font-semibold">
          <span>合計</span><span>{{ formatCurrency(order.totalAmount) }}</span>
        </div>
      </div>
    </div>

    <div class="flex justify-center gap-4">
      <router-link to="/orders" class="btn-secondary">查看訂單</router-link>
      <router-link to="/products" class="btn-primary">繼續購物</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useOrderStore } from '@/stores/orderStore'
import { formatCurrency, formatDate } from '@/utils/formatters'

const route = useRoute()
const orderStore = useOrderStore()
const orderId = route.params.id
const order = ref(null)

onMounted(async () => {
  try {
    order.value = await orderStore.fetchOrderById(orderId)
  } catch {}
})
</script>
