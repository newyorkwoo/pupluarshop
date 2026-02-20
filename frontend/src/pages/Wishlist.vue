<template>
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <BaseBreadcrumb :items="[{ label: '首頁', to: '/' }, { label: '收藏清單' }]" class="mb-8" />
    <h1 class="text-2xl font-light tracking-wider mb-8">收藏清單</h1>

    <div v-if="!wishlistStore.items.length" class="text-center py-20">
      <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto text-brand-gray/40 mb-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1">
        <path stroke-linecap="round" stroke-linejoin="round" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
      </svg>
      <p class="text-brand-gray mb-6">收藏清單是空的</p>
      <router-link to="/products" class="btn-primary">探索商品</router-link>
    </div>

    <ProductGrid v-else :products="wishlistStore.items" />
  </div>
</template>

<script setup>
import { useWishlistStore } from '@/stores/wishlistStore'
import BaseBreadcrumb from '@/components/ui/BaseBreadcrumb.vue'
import ProductGrid from '@/components/product/ProductGrid.vue'

const wishlistStore = useWishlistStore()
</script>
