import api from './axios'

export const cartApi = {
  getCart() {
    return api.get('/cart')
  },

  addItem(productId, quantity = 1, size = null) {
    return api.post('/cart', { productId, quantity, size })
  },

  updateItem(itemId, quantity) {
    return api.put(`/cart/${itemId}?quantity=${quantity}`)
  },

  removeItem(itemId) {
    return api.delete(`/cart/${itemId}`)
  },

  clearCart() {
    return api.delete('/cart')
  },
}

export const { getCart, addItem, updateItem, removeItem, clearCart } = cartApi
