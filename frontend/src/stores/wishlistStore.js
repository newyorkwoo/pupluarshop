import { defineStore } from 'pinia'
import { ref } from 'vue'
import { userApi } from '@/api/user'
import { useAuthStore } from './authStore'

export const useWishlistStore = defineStore('wishlist', () => {
  const items = ref(JSON.parse(localStorage.getItem('wishlist')) || [])
  const loading = ref(false)

  function saveToLocal() {
    localStorage.setItem('wishlist', JSON.stringify(items.value))
  }

  async function fetchWishlist() {
    const authStore = useAuthStore()
    if (!authStore.isAuthenticated) return
    loading.value = true
    try {
      const { data } = await userApi.getWishlist()
      items.value = data
      saveToLocal()
    } finally {
      loading.value = false
    }
  }

  function isInWishlist(productId) {
    return items.value.some((i) => i.productId === productId || i.id === productId)
  }

  async function toggle(product) {
    const authStore = useAuthStore()
    if (isInWishlist(product.id)) {
      items.value = items.value.filter(
        (i) => i.productId !== product.id && i.id !== product.id
      )
      if (authStore.isAuthenticated) {
        await userApi.removeFromWishlist(product.id)
      }
    } else {
      items.value.push({
        productId: product.id,
        name: product.name,
        price: product.price,
        imageUrl: product.imageUrl,
        slug: product.slug,
      })
      if (authStore.isAuthenticated) {
        await userApi.addToWishlist(product.id)
      }
    }
    saveToLocal()
  }

  return { items, loading, fetchWishlist, isInWishlist, toggle }
})
