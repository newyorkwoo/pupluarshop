import { computed } from 'vue'
import { useAuthStore } from '@/stores/authStore'
import { useRouter } from 'vue-router'

export function useAuth () {
  const authStore = useAuthStore()
  const router = useRouter()

  const user = computed(() => authStore.user)
  const isLoggedIn = computed(() => authStore.isLoggedIn)
  const isAdmin = computed(() => authStore.isAdmin)

  async function login (email, password) {
    await authStore.login(email, password)
  }

  async function register (data) {
    await authStore.register(data)
  }

  function logout () {
    authStore.logout()
    router.push('/login')
  }

  function requireAuth () {
    if (!isLoggedIn.value) {
      router.push({ path: '/login', query: { redirect: router.currentRoute.value.fullPath } })
      return false
    }
    return true
  }

  return { user, isLoggedIn, isAdmin, login, register, logout, requireAuth }
}
