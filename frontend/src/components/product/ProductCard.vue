<template>
  <router-link :to="`/products/${product.slug}`" class="product-card block">
    <div class="relative overflow-hidden bg-brand-cream aspect-[3/4]">
      <img :src="product.imageUrl || '/placeholder.jpg'" :alt="product.name"
        class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105" loading="lazy" />
      <button @click.prevent="wishlistStore.toggle(product)"
        class="absolute top-3 right-3 p-2 bg-white/80 rounded-full hover:bg-white transition-colors">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" :fill="wishlistStore.isInWishlist(product.id) ? 'currentColor' : 'none'"
          viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5">
          <path stroke-linecap="round" stroke-linejoin="round"
            d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
        </svg>
      </button>
      <span v-if="product.salePrice" class="absolute top-3 left-3 bg-brand-error text-white text-[10px] px-2 py-1 uppercase tracking-wider">
        Sale
      </span>
    </div>
    <div class="pt-3 pb-1">
      <p class="text-[10px] uppercase tracking-widest text-brand-gray mb-1">{{ product.brand }}</p>
      <h3 class="text-sm text-brand-charcoal leading-snug mb-2 line-clamp-2">{{ product.name }}</h3>
      <div class="flex items-center gap-2">
        <span v-if="product.salePrice" class="text-sm font-medium text-brand-error">
          {{ formatCurrency(product.salePrice) }}
        </span>
        <span :class="product.salePrice ? 'line-through text-brand-gray text-xs' : 'text-sm font-medium'">
          {{ formatCurrency(product.price) }}
        </span>
      </div>
    </div>
  </router-link>
</template>

<script setup>
import { useWishlistStore } from '@/stores/wishlistStore'
import { formatCurrency } from '@/utils/formatters'

defineProps({
  product: { type: Object, required: true },
})

const wishlistStore = useWishlistStore()
</script>
