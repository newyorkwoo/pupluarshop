<template>
  <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <BaseBreadcrumb :items="[{ label: '首頁', to: '/' }, { label: '我的帳號', to: '/account' }, { label: '訂單紀錄' }]" class="mb-6" />
    <h1 class="text-2xl font-light tracking-wider mb-8">訂單紀錄</h1>

    <LoadingSpinner v-if="loading" />

    <template v-else>
      <div v-if="!orders.length" class="text-center py-16">
        <p class="text-brand-gray mb-4">尚無訂單紀錄</p>
        <router-link to="/products" class="btn-primary">開始購物</router-link>
      </div>

      <div v-else class="space-y-4">
        <div v-for="order in orders" :key="order.id" class="border border-brand-light p-6">
          <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2 mb-4">
            <div>
              <p class="text-sm font-medium">訂單 #{{ order.orderNumber }}</p>
              <p class="text-xs text-brand-gray">{{ formatDate(order.createdAt) }}</p>
            </div>
            <span :class="statusClass(order.status)" class="text-xs px-3 py-1 rounded-full inline-block">{{ statusLabel(order.status) }}</span>
          </div>
          <div class="space-y-2">
            <div v-for="item in order.items" :key="item.id" class="flex items-center gap-4 text-sm">
              <div class="w-16 h-16 bg-brand-cream flex-shrink-0"></div>
              <div class="flex-1">
                <p>{{ item.productName }}</p>
                <p class="text-brand-gray text-xs">{{ item.size }} × {{ item.quantity }}</p>
              </div>
              <p>{{ formatCurrency(item.subtotal) }}</p>
            </div>
          </div>
          <div class="mt-4 pt-4 border-t border-brand-light flex items-center justify-between">
            <span class="font-medium">合計 {{ formatCurrency(order.totalAmount) }}</span>
            <button v-if="order.status === 'PENDING'" @click="cancelOrder(order.id)" class="text-xs text-red-500 underline">取消訂單</button>
          </div>
        </div>
      </div>

      <Pagination v-if="totalPages > 1" :currentPage="page" :totalPages="totalPages" @update:page="page = $event" class="mt-8" />
    </template>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useOrderStore } from '@/stores/orderStore'
import { formatCurrency, formatDate } from '@/utils/formatters'
import { ORDER_STATUS } from '@/utils/constants'
import BaseBreadcrumb from '@/components/ui/BaseBreadcrumb.vue'
import LoadingSpinner from '@/components/ui/LoadingSpinner.vue'
import Pagination from '@/components/ui/Pagination.vue'

const orderStore = useOrderStore()
const orders = ref([])
const loading = ref(true)
const page = ref(1)
const totalPages = ref(1)

function statusLabel (s) { return ORDER_STATUS[s]?.label || s }
function statusClass (s) { return ORDER_STATUS[s]?.color || 'bg-gray-100 text-gray-600' }

async function fetchOrders () {
  loading.value = true
  try {
    await orderStore.fetchOrders({ page: page.value - 1 })
    orders.value = orderStore.orders
    totalPages.value = orderStore.totalPages
  } finally { loading.value = false }
}

async function cancelOrder (id) {
  if (!confirm('確定要取消此訂單嗎？')) return
  await orderStore.cancelOrder(id)
  await fetchOrders()
}

watch(page, () => fetchOrders())
onMounted(() => fetchOrders())
</script>
