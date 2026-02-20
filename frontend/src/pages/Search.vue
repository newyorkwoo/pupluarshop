<template>
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <BaseBreadcrumb :items="[{ label: '首頁', to: '/' }, { label: '搜尋結果' }]" class="mb-8" />
    <h1 class="text-2xl font-light tracking-wider mb-2">搜尋「{{ query }}」</h1>
    <p class="text-sm text-brand-gray mb-8">找到 {{ total }} 件商品</p>

    <LoadingSpinner v-if="loading" />
    <template v-else>
      <ProductGrid v-if="products.length" :products="products" />
      <div v-else class="text-center py-20">
        <p class="text-brand-gray mb-4">找不到符合「{{ query }}」的商品</p>
        <router-link to="/products" class="btn-primary">瀏覽全部商品</router-link>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useProductStore } from '@/stores/productStore'
import BaseBreadcrumb from '@/components/ui/BaseBreadcrumb.vue'
import ProductGrid from '@/components/product/ProductGrid.vue'
import LoadingSpinner from '@/components/ui/LoadingSpinner.vue'

const route = useRoute()
const productStore = useProductStore()
const loading = ref(false)
const products = ref([])
const total = ref(0)
const query = ref(route.query.q || '')

async function doSearch () {
  loading.value = true
  try {
    await productStore.searchProducts(query.value)
    products.value = productStore.products
    total.value = productStore.totalElements
  } finally { loading.value = false }
}

watch(() => route.query.q, v => { query.value = v || ''; doSearch() })
onMounted(() => doSearch())
</script>
