<template>
  <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 lg:gap-6">
    <ProductCard v-for="product in products" :key="product.id" :product="product" />
  </div>
  <div v-if="products.length === 0 && !loading" class="text-center py-20 text-brand-gray">
    <p class="text-sm">目前沒有符合條件的商品</p>
  </div>
</template>

<script setup>
import ProductCard from './ProductCard.vue'

defineProps({
  products: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false },
})
</script>
