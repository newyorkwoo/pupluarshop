<template>
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <BaseBreadcrumb :items="[{ label: '首頁', to: '/' }, { label: '購物車' }]" class="mb-8" />
    <h1 class="text-2xl font-light tracking-wider mb-8">購物車</h1>

    <div v-if="!cartStore.items.length" class="text-center py-20">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto text-brand-gray/40 mb-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1">
        <path stroke-linecap="round" stroke-linejoin="round" d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z" />
      </svg>
      <p class="text-brand-gray mb-6">您的購物車是空的</p>
      <router-link to="/products" class="btn-primary">繼續購物</router-link>
    </div>

    <div v-else class="grid grid-cols-1 lg:grid-cols-3 gap-10">
      <div class="lg:col-span-2 space-y-4">
        <CartItem v-for="item in cartStore.items" :key="item.id || item.productId" :item="item"
          @update="(productId, qty) => cartStore.updateQuantity(productId, qty)"
          @remove="(productId) => cartStore.removeItem(productId)" />
      </div>
      <div>
        <CartSummary />
        <router-link to="/checkout" class="btn-primary w-full text-center block mt-4">前往結帳</router-link>
        <router-link to="/products" class="block text-center text-sm text-brand-gray hover:text-brand-black mt-4 underline">繼續購物</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useCartStore } from '@/stores/cartStore'
import BaseBreadcrumb from '@/components/ui/BaseBreadcrumb.vue'
import CartItem from '@/components/cart/CartItem.vue'
import CartSummary from '@/components/cart/CartSummary.vue'

const cartStore = useCartStore()
</script>
