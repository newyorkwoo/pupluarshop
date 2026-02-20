<template>
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <BaseBreadcrumb :items="breadcrumb" class="mb-6" />
    <LoadingSpinner v-if="loading" />
    <template v-else-if="product">
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-10">
        <ProductGallery :images="productImages" />
        <div>
          <p class="text-xs uppercase tracking-[0.2em] text-brand-gray mb-1">{{ product.brand }}</p>
          <h1 class="text-2xl font-light mb-4">{{ product.name }}</h1>
          <div class="flex items-center gap-3 mb-6">
            <span v-if="product.salePrice" class="text-lg text-red-600 font-medium">{{ formatCurrency(product.salePrice) }}</span>
            <span :class="product.salePrice ? 'line-through text-brand-gray text-sm' : 'text-lg font-medium'">{{ formatCurrency(product.price) }}</span>
            <span v-if="product.salePrice" class="text-xs text-red-600 ml-1">-{{ Math.round((1 - product.salePrice / product.price) * 100) }}%</span>
          </div>

          <SizeSelector v-if="product.sizes?.length" :sizes="product.sizes" v-model="selectedSize" class="mb-6" />

          <div class="flex gap-3 mb-8">
            <BaseButton @click="addToCart" :loading="adding" class="flex-1">加入購物車</BaseButton>
            <button @click="toggleWish" class="w-12 h-12 border border-brand-black flex items-center justify-center hover:bg-brand-light transition-colors">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" :fill="isWished ? 'currentColor' : 'none'" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5">
                <path stroke-linecap="round" stroke-linejoin="round" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
              </svg>
            </button>
          </div>

          <!-- Details Accordion -->
          <div class="border-t border-brand-light">
            <details v-for="section in detailSections" :key="section.title" class="border-b border-brand-light">
              <summary class="py-4 text-sm uppercase tracking-wider cursor-pointer flex justify-between items-center">
                {{ section.title }}
                <svg class="w-4 h-4 transition-transform" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M19 9l-7 7-7-7" /></svg>
              </summary>
              <div class="pb-4 text-sm text-brand-gray leading-relaxed" v-html="section.content" />
            </details>
          </div>
        </div>
      </div>

      <!-- Related Products -->
      <section v-if="relatedProducts.length" class="mt-16">
        <h2 class="section-title text-center mb-8">你可能也喜歡</h2>
        <ProductGrid :products="relatedProducts" />
      </section>
    </template>
    <div v-else class="text-center py-20">
      <p class="text-brand-gray">商品不存在</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useCartStore } from '@/stores/cartStore'
import { useWishlistStore } from '@/stores/wishlistStore'
import { getProductBySlug } from '@/api/product'
import { formatCurrency } from '@/utils/formatters'
import BaseBreadcrumb from '@/components/ui/BaseBreadcrumb.vue'
import ProductGallery from '@/components/product/ProductGallery.vue'
import SizeSelector from '@/components/product/SizeSelector.vue'
import ProductGrid from '@/components/product/ProductGrid.vue'
import BaseButton from '@/components/ui/BaseButton.vue'
import LoadingSpinner from '@/components/ui/LoadingSpinner.vue'

const route = useRoute()
const cartStore = useCartStore()
const wishlistStore = useWishlistStore()

const product = ref(null)
const loading = ref(true)
const adding = ref(false)
const selectedSize = ref('')
const relatedProducts = ref([])

const isWished = computed(() => product.value && wishlistStore.isInWishlist(product.value.id))

const productImages = computed(() => {
  if (!product.value?.images?.length) return ['/placeholder.jpg']
  return product.value.images
    .sort((a, b) => (a.sortOrder || 0) - (b.sortOrder || 0))
    .map(img => typeof img === 'string' ? img : img.imageUrl)
})

const breadcrumb = computed(() => {
  if (!product.value) return [{ label: '首頁', to: '/' }]
  return [
    { label: '首頁', to: '/' },
    { label: product.value.categoryName || '全部商品', to: '/products' },
    { label: product.value.name },
  ]
})

const detailSections = computed(() => {
  if (!product.value) return []
  return [
    { title: '商品描述', content: product.value.description || '暫無描述' },
    { title: '材質與成分', content: product.value.material || '請參閱吊牌' },
    { title: '尺寸指南', content: '請參閱品牌官方尺寸表，如有疑問歡迎聯繫客服' },
    { title: '配送與退換', content: '免費標準配送 (5-7 個工作天)<br/>30 天免費退貨<br/>請保持商品吊牌完整' },
  ]
})

async function fetchProduct () {
  loading.value = true
  try {
    const res = await getProductBySlug(route.params.slug)
    product.value = res.data
  } catch { product.value = null }
  finally { loading.value = false }
}

async function addToCart () {
  if (product.value.sizes?.length && !selectedSize.value) return
  adding.value = true
  try {
    await cartStore.addItem(product.value, 1)
  } finally { adding.value = false }
}

function toggleWish () {
  if (product.value) wishlistStore.toggle(product.value)
}

watch(() => route.params.slug, () => fetchProduct())
onMounted(() => fetchProduct())
</script>
