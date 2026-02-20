import api from './axios'

export const userApi = {
  getProfile() {
    return api.get('/user/profile')
  },

  updateProfile(data) {
    return api.put('/user/profile', data)
  },

  changePassword(data) {
    return api.put('/user/password', data)
  },

  getAddresses() {
    return api.get('/user/addresses')
  },

  addAddress(data) {
    return api.post('/user/addresses', data)
  },

  updateAddress(id, data) {
    return api.put(`/user/addresses/${id}`, data)
  },

  deleteAddress(id) {
    return api.delete(`/user/addresses/${id}`)
  },

  getWishlist() {
    return api.get('/wishlist')
  },

  addToWishlist(productId) {
    return api.post(`/wishlist/${productId}`)
  },

  removeFromWishlist(productId) {
    return api.delete(`/wishlist/${productId}`)
  },

  submitReview(data) {
    return api.post('/reviews', data)
  },
}

export const { getProfile, updateProfile, changePassword, getAddresses, addAddress, updateAddress, deleteAddress, getWishlist, addToWishlist, removeFromWishlist, submitReview } = userApi
