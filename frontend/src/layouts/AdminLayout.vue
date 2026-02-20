<template>
  <div class="min-h-screen flex bg-gray-50">
    <AdminSidebar />
    <div class="flex-1 flex flex-col ml-64">
      <!-- Admin Top Bar -->
      <header class="h-16 bg-white border-b border-gray-200 flex items-center justify-between px-8 sticky top-0 z-30">
        <h1 class="text-lg font-semibold text-brand-black">PopularShop 管理後台</h1>
        <div class="flex items-center gap-4">
          <span class="text-sm text-brand-gray">{{ authStore.fullName }}</span>
          <button @click="handleLogout" class="text-sm text-brand-error hover:underline">登出</button>
        </div>
      </header>
      <main class="flex-1 p-8">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import AdminSidebar from '@/components/admin/AdminSidebar.vue'

const authStore = useAuthStore()
const router = useRouter()

function handleLogout() {
  authStore.logout()
  router.push('/login')
}
</script>
