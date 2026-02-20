import api from './axios'

export const productApi = {
  getProducts(params) {
    return api.get('/products', { params })
  },

  getProductBySlug(slug) {
    return api.get(`/products/slug/${slug}`)
  },

  searchProducts(query, params) {
    return api.get('/products/search', { params: { q: query, ...params } })
  },

  getCategories() {
    return api.get('/categories')
  },

  getCategoryProducts(slug, params) {
    return api.get(`/categories/${slug}/products`, { params })
  },

  getBanners() {
    return api.get('/banners')
  },
}

export const { getProducts, getProductBySlug, searchProducts, getCategories, getCategoryProducts, getBanners } = productApi
