<template>
  <header class="sticky top-0 z-40 bg-white border-b border-gray-100">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex items-center justify-between h-16 lg:h-20">
        <!-- Logo -->
        <router-link to="/" class="text-xl lg:text-2xl font-bold tracking-[0.15em] uppercase text-brand-black">
          PopularShop
        </router-link>

        <!-- Desktop Nav -->
        <nav class="hidden lg:flex items-center gap-8">
          <router-link to="/category/women" class="nav-link">WOMEN</router-link>
          <router-link to="/category/men" class="nav-link">MEN</router-link>
          <router-link to="/category/kids" class="nav-link">KIDS</router-link>
          <router-link to="/category/life" class="nav-link">LIFE</router-link>
          <router-link to="/products" class="nav-link">NEW ARRIVALS</router-link>
        </nav>

        <!-- Right actions -->
        <div class="flex items-center gap-5">
          <!-- Search -->
          <button @click="toggleSearch" class="hover:text-brand-gold transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </button>

          <!-- Wishlist -->
          <router-link to="/wishlist" class="hover:text-brand-gold transition-colors hidden sm:block">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
            </svg>
          </router-link>

          <!-- Account -->
          <router-link :to="authStore.isAuthenticated ? '/account' : '/login'" class="hover:text-brand-gold transition-colors hidden sm:block">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
          </router-link>

          <!-- Cart -->
          <button @click="cartDrawerOpen = true" class="relative hover:text-brand-gold transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z" />
            </svg>
            <span v-if="cartStore.itemCount > 0"
              class="absolute -top-2 -right-2 bg-brand-black text-white text-[10px] w-4 h-4 rounded-full flex items-center justify-center">
              {{ cartStore.itemCount }}
            </span>
          </button>

          <!-- Mobile menu toggle -->
          <button @click="mobileMenuOpen = !mobileMenuOpen" class="lg:hidden">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
          </button>
        </div>
      </div>

      <!-- Search bar -->
      <div v-if="searchOpen" class="pb-4">
        <form @submit.prevent="handleSearch" class="relative">
          <input v-model="searchQuery" ref="searchInput" type="text" placeholder="搜尋品牌、商品..."
            class="input-field pr-10" />
          <button type="submit" class="absolute right-3 top-1/2 -translate-y-1/2 text-brand-gray">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </button>
        </form>
      </div>
    </div>

    <!-- Mobile Menu -->
    <div v-if="mobileMenuOpen" class="lg:hidden border-t border-gray-100 bg-white">
      <nav class="max-w-7xl mx-auto px-4 py-4 space-y-3">
        <router-link to="/category/women" class="block nav-link" @click="mobileMenuOpen = false">WOMEN</router-link>
        <router-link to="/category/men" class="block nav-link" @click="mobileMenuOpen = false">MEN</router-link>
        <router-link to="/category/kids" class="block nav-link" @click="mobileMenuOpen = false">KIDS</router-link>
        <router-link to="/category/life" class="block nav-link" @click="mobileMenuOpen = false">LIFE</router-link>
        <router-link to="/products" class="block nav-link" @click="mobileMenuOpen = false">NEW ARRIVALS</router-link>
        <hr class="border-gray-100" />
        <router-link to="/wishlist" class="block nav-link" @click="mobileMenuOpen = false">WISHLIST</router-link>
        <router-link :to="authStore.isAuthenticated ? '/account' : '/login'" class="block nav-link" @click="mobileMenuOpen = false">
          {{ authStore.isAuthenticated ? 'MY ACCOUNT' : 'SIGN IN' }}
        </router-link>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { ref, nextTick, inject } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import { useCartStore } from '@/stores/cartStore'

const authStore = useAuthStore()
const cartStore = useCartStore()
const router = useRouter()

const searchOpen = ref(false)
const searchQuery = ref('')
const searchInput = ref(null)
const mobileMenuOpen = ref(false)
const cartDrawerOpen = inject('cartDrawerOpen')

async function toggleSearch() {
  searchOpen.value = !searchOpen.value
  if (searchOpen.value) {
    await nextTick()
    searchInput.value?.focus()
  }
}

function handleSearch() {
  if (searchQuery.value.trim()) {
    router.push({ name: 'Search', query: { q: searchQuery.value.trim() } })
    searchOpen.value = false
    searchQuery.value = ''
  }
}
</script>

<style scoped>
.nav-link {
  @apply text-xs uppercase tracking-[0.15em] text-brand-charcoal hover:text-brand-gold transition-colors font-medium;
}
</style>
