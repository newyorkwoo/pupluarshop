import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { cartApi } from '@/api/cart'
import { useAuthStore } from './authStore'

export const useCartStore = defineStore('cart', () => {
  const items = ref(JSON.parse(localStorage.getItem('cart_items')) || [])
  const loading = ref(false)

  const itemCount = computed(() =>
    items.value.reduce((sum, item) => sum + item.quantity, 0)
  )

  const totalAmount = computed(() =>
    items.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
  )

  function saveToLocal() {
    localStorage.setItem('cart_items', JSON.stringify(items.value))
  }

  async function fetchCart() {
    const authStore = useAuthStore()
    if (!authStore.isAuthenticated) return
    loading.value = true
    try {
      const { data } = await cartApi.getCart()
      items.value = data || []
      saveToLocal()
    } finally {
      loading.value = false
    }
  }

  async function addItem(product, quantity = 1, size = null) {
    const authStore = useAuthStore()
    const existing = items.value.find((i) => i.productId === product.id && i.size === size)

    if (existing) {
      existing.quantity += quantity
    } else {
      items.value.push({
        productId: product.id,
        name: product.name,
        price: product.salePrice || product.price,
        image: product.imageUrl,
        slug: product.slug,
        size,
        quantity,
      })
    }
    saveToLocal()

    if (authStore.isAuthenticated) {
      await cartApi.addItem(product.id, quantity, size)
    }
  }

  async function updateQuantity(productId, quantity) {
    const authStore = useAuthStore()
    const item = items.value.find((i) => i.productId === productId)
    if (!item) return

    if (quantity <= 0) {
      return removeItem(productId)
    }

    item.quantity = quantity
    saveToLocal()

    if (authStore.isAuthenticated) {
      await cartApi.updateItem(item.id, quantity)
    }
  }

  async function removeItem(productId) {
    const authStore = useAuthStore()
    const item = items.value.find((i) => i.productId === productId)
    items.value = items.value.filter((i) => i.productId !== productId)
    saveToLocal()

    if (authStore.isAuthenticated && item?.id) {
      await cartApi.removeItem(item.id)
    }
  }

  async function clearCart() {
    const authStore = useAuthStore()
    items.value = []
    saveToLocal()

    if (authStore.isAuthenticated) {
      await cartApi.clearCart()
    }
  }

  return {
    items,
    loading,
    itemCount,
    totalAmount,
    fetchCart,
    addItem,
    updateQuantity,
    removeItem,
    clearCart,
  }
})
