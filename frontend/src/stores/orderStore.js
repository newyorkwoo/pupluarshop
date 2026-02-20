import { defineStore } from 'pinia'
import { ref } from 'vue'
import { orderApi } from '@/api/order'

export const useOrderStore = defineStore('order', () => {
  const orders = ref([])
  const currentOrder = ref(null)
  const loading = ref(false)

  const totalPages = ref(1)

  async function fetchOrders(params = {}) {
    loading.value = true
    try {
      const { data } = await orderApi.getOrders(params)
      orders.value = data.content || data
      totalPages.value = data.totalPages || 1
      return data
    } finally {
      loading.value = false
    }
  }

  async function fetchOrderById(id) {
    loading.value = true
    try {
      const { data } = await orderApi.getOrderById(id)
      currentOrder.value = data
      return data
    } finally {
      loading.value = false
    }
  }

  async function createOrder(orderData) {
    const { data } = await orderApi.createOrder(orderData)
    currentOrder.value = data
    return data
  }

  async function cancelOrder(id) {
    await orderApi.cancelOrder(id)
    if (currentOrder.value?.id === id) {
      currentOrder.value.status = 'CANCELLED'
    }
  }

  return {
    orders,
    currentOrder,
    loading,
    totalPages,
    fetchOrders,
    fetchOrderById,
    createOrder,
    cancelOrder,
  }
})
