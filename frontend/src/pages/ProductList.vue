<template>
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <BaseBreadcrumb :items="breadcrumb" />

    <div class="flex flex-col lg:flex-row gap-8 mt-6">
      <!-- Sidebar Filters -->
      <aside class="w-full lg:w-64 flex-shrink-0">
        <Sidebar :categories="categories"
          @apply="applyFilters" />
      </aside>

      <!-- Product List -->
      <div class="flex-1">
        <div class="flex items-center justify-between mb-6">
          <p class="text-sm text-brand-gray">{{ totalProducts }} 件商品</p>
          <ProductFilter :sortBy="sortBy" @update:sortBy="sortBy = $event" />
        </div>

        <LoadingSpinner v-if="loading" />

        <template v-else>
          <ProductGrid v-if="products.length" :products="products" />
          <div v-else class="text-center py-20">
            <p class="text-brand-gray">找不到符合條件的商品</p>
          </div>
        </template>

        <Pagination v-if="totalPages > 1" :currentPage="currentPage" :totalPages="totalPages"
          @update:page="currentPage = $event" class="mt-10" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useProductStore } from '@/stores/productStore'
import BaseBreadcrumb from '@/components/ui/BaseBreadcrumb.vue'
import Sidebar from '@/components/layout/Sidebar.vue'
import ProductGrid from '@/components/product/ProductGrid.vue'
import ProductFilter from '@/components/product/ProductFilter.vue'
import LoadingSpinner from '@/components/ui/LoadingSpinner.vue'
import Pagination from '@/components/ui/Pagination.vue'

const route = useRoute()
const router = useRouter()
const productStore = useProductStore()

const loading = ref(false)
const selectedCategory = ref(route.params.slug || '')
const priceRange = ref([0, 200000])
const sortBy = ref('newest')
const currentPage = ref(1)

const products = computed(() => productStore.products)
const totalProducts = computed(() => productStore.totalElements)
const totalPages = computed(() => productStore.totalPages)
const categories = computed(() => productStore.categories)

const breadcrumb = computed(() => {
  const items = [{ label: '首頁', to: '/' }]
  if (selectedCategory.value) {
    items.push({ label: selectedCategory.value, to: `/category/${selectedCategory.value}` })
  } else {
    items.push({ label: '全部商品' })
  }
  return items
})

function applyFilters (filters) {
  if (filters.minPrice != null) priceRange.value[0] = filters.minPrice
  if (filters.maxPrice != null) priceRange.value[1] = filters.maxPrice
  if (filters.sort) sortBy.value = filters.sort
  currentPage.value = 1
  fetchProducts()
}

async function fetchProducts () {
  loading.value = true
  try {
    await productStore.fetchProducts({
      category: selectedCategory.value,
      minPrice: priceRange.value[0],
      maxPrice: priceRange.value[1],
      sort: sortBy.value,
      page: currentPage.value - 1,
      size: 24,
    })
  } finally {
    loading.value = false
  }
}

watch([selectedCategory, priceRange, sortBy], () => { currentPage.value = 1; fetchProducts() })
watch(currentPage, () => fetchProducts())
watch(() => route.params.slug, v => { selectedCategory.value = v || '' })

onMounted(async () => {
  await productStore.fetchCategories()
  await fetchProducts()
})
</script>
