import api from './axios'

export const adminApi = {
  // Dashboard
  getDashboard() {
    return api.get('/admin/dashboard')
  },

  // Products
  getProducts(params) {
    return api.get('/admin/products', { params })
  },
  createProduct(data) {
    return api.post('/admin/products', data)
  },
  updateProduct(id, data) {
    return api.put(`/admin/products/${id}`, data)
  },
  deleteProduct(id) {
    return api.delete(`/admin/products/${id}`)
  },
  toggleProduct(id) {
    return api.patch(`/admin/products/${id}/toggle`)
  },
  uploadProductImages(id, formData) {
    return api.post(`/admin/products/${id}/images`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
  },

  // Categories
  getCategories() {
    return api.get('/admin/categories')
  },
  createCategory(data) {
    return api.post('/admin/categories', data)
  },
  updateCategory(id, data) {
    return api.put(`/admin/categories/${id}`, data)
  },
  deleteCategory(id) {
    return api.delete(`/admin/categories/${id}`)
  },

  // Orders
  getOrders(params) {
    return api.get('/admin/orders', { params })
  },
  updateOrderStatus(id, status) {
    return api.put(`/admin/orders/${id}/status`, { status })
  },

  // Users
  getUsers(params) {
    return api.get('/admin/users', { params })
  },
  updateUserRole(id, role) {
    return api.put(`/admin/users/${id}/role`, { role })
  },
  toggleUser(id) {
    return api.put(`/admin/users/${id}/toggle`)
  },

  // Coupons
  getCoupons(params) {
    return api.get('/admin/coupons', { params })
  },
  createCoupon(data) {
    return api.post('/admin/coupons', data)
  },
  updateCoupon(id, data) {
    return api.put(`/admin/coupons/${id}`, data)
  },
  deleteCoupon(id) {
    return api.delete(`/admin/coupons/${id}`)
  },

  // Banners
  getBanners() {
    return api.get('/admin/banners')
  },
  createBanner(formData) {
    return api.post('/admin/banners', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
  },
  updateBanner(id, data) {
    return api.put(`/admin/banners/${id}`, data)
  },
  deleteBanner(id) {
    return api.delete(`/admin/banners/${id}`)
  },

  // Reviews
  getReviews(params) {
    return api.get('/admin/reviews', { params })
  },
  approveReview(id) {
    return api.put(`/admin/reviews/${id}/approve`)
  },
  deleteReview(id) {
    return api.delete(`/admin/reviews/${id}`)
  },

  // Settings
  getSettings() {
    return api.get('/admin/settings')
  },
  updateSettings(data) {
    return api.put('/admin/settings', data)
  },

  // Audit Logs
  getAuditLogs(params) {
    return api.get('/admin/audit-logs', { params })
  },
}

export const {
  getDashboard, getProducts, createProduct, updateProduct, deleteProduct, toggleProduct, uploadProductImages,
  getCategories, createCategory, updateCategory, deleteCategory,
  getOrders, updateOrderStatus,
  getUsers, updateUserRole, toggleUser,
  getCoupons, createCoupon, updateCoupon, deleteCoupon,
  getBanners, createBanner, updateBanner, deleteBanner,
  getReviews, approveReview, rejectReview, deleteReview,
  getSettings, updateSettings, getAuditLogs,
} = adminApi
