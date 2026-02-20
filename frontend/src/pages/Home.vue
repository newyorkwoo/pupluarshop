<template>
  <div>
    <!-- Hero Banner -->
    <section class="relative h-[70vh] bg-brand-cream overflow-hidden">
      <div class="absolute inset-0 flex items-center justify-center">
        <div class="text-center px-4">
          <h1 class="text-4xl md:text-6xl font-light tracking-[0.2em] uppercase mb-4">New Season</h1>
          <p class="text-sm md:text-base text-brand-charcoal mb-8 tracking-wider">探索最新精品設計師系列</p>
          <router-link to="/products" class="btn-primary">立即選購</router-link>
        </div>
      </div>
    </section>

    <!-- Category Grid -->
    <section class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
      <h2 class="section-title text-center mb-10">精選分類</h2>
      <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
        <router-link v-for="cat in categories" :key="cat.slug" :to="`/category/${cat.slug}`"
          class="group relative aspect-[3/4] overflow-hidden bg-brand-cream">
          <div class="absolute inset-0 bg-black/10 group-hover:bg-black/20 transition-colors z-10" />
          <div class="absolute inset-0 flex items-end z-20 p-6">
            <span class="text-white text-sm uppercase tracking-[0.2em] font-medium">{{ cat.name }}</span>
          </div>
        </router-link>
      </div>
    </section>

    <!-- Featured Products -->
    <section class="bg-brand-cream py-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h2 class="section-title text-center mb-10">新品上架</h2>
        <ProductGrid :products="featuredProducts" />
        <div class="text-center mt-10">
          <router-link to="/products" class="btn-secondary">瀏覽更多</router-link>
        </div>
      </div>
    </section>

    <!-- Brand Promise -->
    <section class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-8 text-center">
        <div>
          <div class="w-12 h-12 mx-auto mb-4 flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-brand-gold" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
            </svg>
          </div>
          <h3 class="text-xs uppercase tracking-[0.2em] font-semibold mb-2">正品保證</h3>
          <p class="text-sm text-brand-gray">所有商品 100% 正品，品質保障</p>
        </div>
        <div>
          <div class="w-12 h-12 mx-auto mb-4 flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-brand-gold" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" />
            </svg>
          </div>
          <h3 class="text-xs uppercase tracking-[0.2em] font-semibold mb-2">安全付款</h3>
          <p class="text-sm text-brand-gray">SSL 加密，多元支付方式</p>
        </div>
        <div>
          <div class="w-12 h-12 mx-auto mb-4 flex items-center justify-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-brand-gold" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z" />
            </svg>
          </div>
          <h3 class="text-xs uppercase tracking-[0.2em] font-semibold mb-2">免費退換</h3>
          <p class="text-sm text-brand-gray">30 天鑑賞期，免費退換貨</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import ProductGrid from '@/components/product/ProductGrid.vue'
import { useProductStore } from '@/stores/productStore'

const productStore = useProductStore()

const categories = ref([])
const featuredProducts = ref([])

onMounted(async () => {
  await productStore.fetchCategories()
  // Use only parent categories for display
  categories.value = productStore.categories.filter(c => !c.parentId).slice(0, 4)

  await productStore.fetchProducts({ page: 0, size: 8, sort: 'newest' })
  featuredProducts.value = productStore.products
})
</script>
