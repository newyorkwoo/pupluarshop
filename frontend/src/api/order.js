import api from './axios'

export const orderApi = {
  createOrder(orderData) {
    return api.post('/orders', orderData)
  },

  getOrders(params) {
    return api.get('/orders', { params })
  },

  getOrderById(id) {
    return api.get(`/orders/${id}`)
  },

  cancelOrder(id) {
    return api.post(`/orders/${id}/cancel`)
  },

  validateCoupon(code) {
    return api.post('/coupons/validate', { code })
  },
}

export const { createOrder, getOrders, getOrderById, cancelOrder, validateCoupon } = orderApi
