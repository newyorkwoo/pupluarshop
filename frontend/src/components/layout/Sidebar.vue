<template>
  <aside class="w-full lg:w-64 shrink-0">
    <div class="sticky top-24">
      <h3 class="section-title mb-6">篩選</h3>

      <!-- Categories -->
      <div class="mb-6">
        <h4 class="text-xs font-semibold uppercase tracking-wider mb-3">分類</h4>
        <ul class="space-y-2">
          <li v-for="cat in categories" :key="cat.id">
            <router-link :to="`/category/${cat.slug}`"
              class="text-sm text-brand-charcoal hover:text-brand-black transition-colors">
              {{ cat.name }}
            </router-link>
          </li>
        </ul>
      </div>

      <!-- Price Range -->
      <div class="mb-6">
        <h4 class="text-xs font-semibold uppercase tracking-wider mb-3">價格範圍</h4>
        <div class="flex gap-2">
          <input id="filter-min-price" name="filter-min-price" v-model.number="filters.minPrice" type="number" placeholder="最低" class="input-field text-xs w-1/2" />
          <input id="filter-max-price" name="filter-max-price" v-model.number="filters.maxPrice" type="number" placeholder="最高" class="input-field text-xs w-1/2" />
        </div>
      </div>

      <!-- Sort -->
      <div class="mb-6">
        <h4 class="text-xs font-semibold uppercase tracking-wider mb-3">排序</h4>
        <select id="filter-sort" name="filter-sort" v-model="filters.sort" class="input-field text-xs">
          <option value="">預設</option>
          <option value="price_asc">價格：低到高</option>
          <option value="price_desc">價格：高到低</option>
          <option value="newest">最新上架</option>
        </select>
      </div>

      <button @click="$emit('apply', filters)" class="btn-primary w-full text-xs">套用篩選</button>
      <button @click="resetFilters" class="btn-secondary w-full text-xs mt-2">重設</button>
    </div>
  </aside>
</template>

<script setup>
import { ref } from 'vue'

defineProps({ categories: { type: Array, default: () => [] } })
defineEmits(['apply'])

const filters = ref({
  minPrice: null,
  maxPrice: null,
  sort: '',
})

function resetFilters() {
  filters.value = { minPrice: null, maxPrice: null, sort: '' }
}
</script>
