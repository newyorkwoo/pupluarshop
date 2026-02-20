import { defineStore } from 'pinia'
import { ref } from 'vue'
import { productApi } from '@/api/product'

export const useProductStore = defineStore('product', () => {
  const products = ref([])
  const product = ref(null)
  const categories = ref([])
  const banners = ref([])
  const loading = ref(false)
  const totalPages = ref(0)
  const totalElements = ref(0)
  const currentPage = ref(0)

  async function fetchProducts(params = {}) {
    loading.value = true
    try {
      const { data } = await productApi.getProducts(params)
      const list = data.content || (Array.isArray(data) ? data : [])
      products.value = list.map(p => ({ ...p, imageUrl: p.imageUrl || p.images?.[0]?.imageUrl || '' }))
      totalPages.value = data.totalPages || 0
      totalElements.value = data.totalElements || list.length
      currentPage.value = data.page ?? data.number ?? 0
    } catch {
      products.value = []
      totalPages.value = 0
    } finally {
      loading.value = false
    }
  }

  async function fetchProductBySlug(slug) {
    loading.value = true
    try {
      const { data } = await productApi.getProductBySlug(slug)
      product.value = data
      return data
    } catch {
      product.value = null
      return null
    } finally {
      loading.value = false
    }
  }

  async function fetchCategories() {
    try {
      const { data } = await productApi.getCategories()
      categories.value = data
    } catch {
      categories.value = []
    }
  }

  async function fetchBanners() {
    try {
      const { data } = await productApi.getBanners()
      banners.value = data
    } catch {
      banners.value = []
    }
  }

  async function searchProducts(query, params = {}) {
    loading.value = true
    try {
      const { data } = await productApi.searchProducts(query, params)
      const list = data.content || (Array.isArray(data) ? data : [])
      products.value = list.map(p => ({ ...p, imageUrl: p.imageUrl || p.images?.[0]?.imageUrl || '' }))
      totalPages.value = data.totalPages || 0
      totalElements.value = data.totalElements || list.length
    } catch {
      products.value = []
      totalPages.value = 0
    } finally {
      loading.value = false
    }
  }

  return {
    products,
    product,
    categories,
    banners,
    loading,
    totalPages,
    totalElements,
    currentPage,
    fetchProducts,
    fetchProductBySlug,
    fetchCategories,
    fetchBanners,
    searchProducts,
  }
})
