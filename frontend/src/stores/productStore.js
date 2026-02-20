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
  const currentPage = ref(0)

  async function fetchProducts(params = {}) {
    loading.value = true
    try {
      const { data } = await productApi.getProducts(params)
      products.value = data.content || data
      totalPages.value = data.totalPages || 0
      currentPage.value = data.number || 0
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
    } finally {
      loading.value = false
    }
  }

  async function fetchCategories() {
    const { data } = await productApi.getCategories()
    categories.value = data
  }

  async function fetchBanners() {
    const { data } = await productApi.getBanners()
    banners.value = data
  }

  async function searchProducts(query, params = {}) {
    loading.value = true
    try {
      const { data } = await productApi.searchProducts(query, params)
      products.value = data.content || data
      totalPages.value = data.totalPages || 0
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
    currentPage,
    fetchProducts,
    fetchProductBySlug,
    fetchCategories,
    fetchBanners,
    searchProducts,
  }
})
