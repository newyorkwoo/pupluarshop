import { ref } from 'vue'
import { useProductStore } from '@/stores/productStore'

export function useProduct () {
  const productStore = useProductStore()
  const loading = ref(false)
  const error = ref(null)

  async function fetchProducts (params = {}) {
    loading.value = true
    error.value = null
    try {
      await productStore.fetchProducts(params)
    } catch (e) {
      error.value = e.message || '載入商品失敗'
    } finally {
      loading.value = false
    }
  }

  async function fetchCategories () {
    try {
      await productStore.fetchCategories()
    } catch (e) {
      error.value = e.message || '載入分類失敗'
    }
  }

  async function search (keyword) {
    loading.value = true
    error.value = null
    try {
      await productStore.searchProducts(keyword)
    } catch (e) {
      error.value = e.message || '搜尋失敗'
    } finally {
      loading.value = false
    }
  }

  return { loading, error, fetchProducts, fetchCategories, search }
}
