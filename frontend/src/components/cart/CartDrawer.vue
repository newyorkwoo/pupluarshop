<template>
  <Teleport to="body">
    <!-- Overlay -->
    <transition name="fade">
      <div v-if="isOpen" class="fixed inset-0 bg-black/40 z-50" @click="isOpen = false" />
    </transition>
    <!-- Drawer -->
    <transition name="slide">
      <div v-if="isOpen" class="fixed top-0 right-0 h-full w-full max-w-md bg-white z-50 shadow-2xl flex flex-col">
        <div class="flex items-center justify-between px-6 py-4 border-b border-gray-100">
          <h2 class="text-sm uppercase tracking-widest font-semibold">購物車 ({{ cartStore.itemCount }})</h2>
          <button @click="isOpen = false" class="text-brand-gray hover:text-brand-black">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" />
            </svg>
          </button>
        </div>

        <div v-if="cartStore.items.length === 0" class="flex-1 flex items-center justify-center text-brand-gray text-sm">
          您的購物車是空的
        </div>

        <div v-else class="flex-1 overflow-y-auto px-6">
          <CartItem v-for="item in cartStore.items" :key="item.productId" :item="item"
            @update="cartStore.updateQuantity" @remove="cartStore.removeItem" />
        </div>

        <div v-if="cartStore.items.length > 0" class="border-t border-gray-100 p-6">
          <div class="flex justify-between mb-4 text-sm font-semibold">
            <span>小計</span>
            <span>{{ formatCurrency(cartStore.totalAmount) }}</span>
          </div>
          <router-link to="/cart" @click="isOpen = false" class="btn-secondary block text-center mb-2">查看購物車</router-link>
          <router-link to="/checkout" @click="isOpen = false" class="btn-primary block text-center">前往結帳</router-link>
        </div>
      </div>
    </transition>
  </Teleport>
</template>

<script setup>
import { inject } from 'vue'
import { useCartStore } from '@/stores/cartStore'
import { formatCurrency } from '@/utils/formatters'
import CartItem from './CartItem.vue'

const cartStore = useCartStore()
const isOpen = inject('cartDrawerOpen')
</script>

<style scoped>
.fade-enter-active, .fade-leave-active { transition: opacity 0.3s; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
.slide-enter-active, .slide-leave-active { transition: transform 0.3s ease; }
.slide-enter-from, .slide-leave-to { transform: translateX(100%); }
</style>
