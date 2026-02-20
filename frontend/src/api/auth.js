import api from './axios'

export const authApi = {
  login(credentials) {
    return api.post('/auth/login', credentials)
  },

  register(userData) {
    return api.post('/auth/register', userData)
  },

  refreshToken(refreshToken) {
    return api.post('/auth/refresh', { refreshToken })
  },

  forgotPassword(email) {
    return api.post('/auth/forgot-password', { email })
  },

  resetPassword(data) {
    return api.post('/auth/reset-password', data)
  },

  logout() {
    return api.post('/auth/logout')
  },
}

export const { login, register, refreshToken, forgotPassword, resetPassword, logout } = authApi
