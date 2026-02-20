import { defineStore } from 'pinia'
import { ref } from 'vue'
import { adminApi } from '@/api/admin'

export const useAdminStore = defineStore('admin', () => {
  const dashboard = ref(null)
  const loading = ref(false)

  async function fetchDashboard() {
    loading.value = true
    try {
      const { data } = await adminApi.getDashboard()
      dashboard.value = data
    } finally {
      loading.value = false
    }
  }

  return { dashboard, loading, fetchDashboard }
})
