import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/api/auth'
import { userApi } from '@/api/user'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(JSON.parse(localStorage.getItem('user')) || null)
  const token = ref(localStorage.getItem('token') || '')
  const refreshToken = ref(localStorage.getItem('refreshToken') || '')

  const isAuthenticated = computed(() => !!token.value)
  const isAdmin = computed(() => user.value?.role === 'ADMIN')
  const fullName = computed(() =>
    user.value ? `${user.value.firstName} ${user.value.lastName}` : ''
  )

  function setTokens(accessToken, newRefreshToken) {
    token.value = accessToken
    refreshToken.value = newRefreshToken
    localStorage.setItem('token', accessToken)
    localStorage.setItem('refreshToken', newRefreshToken)
  }

  function setUser(userData) {
    user.value = userData
    localStorage.setItem('user', JSON.stringify(userData))
  }

  async function login(credentials) {
    const { data } = await authApi.login(credentials)
    setTokens(data.accessToken, data.refreshToken)
    setUser(data.user)
    return data
  }

  async function register(userData) {
    const { data } = await authApi.register(userData)
    setTokens(data.accessToken, data.refreshToken)
    setUser(data.user)
    return data
  }

  async function fetchProfile() {
    const { data } = await userApi.getProfile()
    setUser(data)
    return data
  }

  function logout() {
    user.value = null
    token.value = ''
    refreshToken.value = ''
    localStorage.removeItem('token')
    localStorage.removeItem('refreshToken')
    localStorage.removeItem('user')
  }

  return {
    user,
    token,
    refreshToken,
    isAuthenticated,
    isAdmin,
    fullName,
    setTokens,
    setUser,
    login,
    register,
    fetchProfile,
    logout,
  }
})
