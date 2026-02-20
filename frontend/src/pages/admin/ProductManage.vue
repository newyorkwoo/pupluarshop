<template>
  <div>
    <div class="flex items-center justify-between mb-6">
      <h1 class="text-xl font-light tracking-wider">商品管理</h1>
      <BaseButton @click="showModal = true">新增商品</BaseButton>
    </div>

    <div class="flex gap-4 mb-6">
      <input v-model="search" type="text" placeholder="搜尋商品名稱或品牌..." class="input-field flex-1" />
      <select v-model="filterCategory" class="input-field w-48">
        <option value="">全部分類</option>
        <option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</option>
      </select>
    </div>

    <LoadingSpinner v-if="loading" />
    <DataTable v-else :columns="columns" :rows="products">
      <template #cell-image="{ row }">
        <div class="w-12 h-12 bg-brand-cream"></div>
      </template>
      <template #cell-price="{ value }">{{ formatCurrency(value) }}</template>
      <template #cell-active="{ value }">
        <span :class="value ? 'text-green-600' : 'text-red-500'" class="text-xs">{{ value ? '上架中' : '已下架' }}</span>
      </template>
      <template #cell-actions="{ row }">
        <div class="flex gap-2">
          <button @click="editProduct(row)" class="text-xs underline">編輯</button>
          <button @click="toggleActive(row)" class="text-xs underline">{{ row.active ? '下架' : '上架' }}</button>
          <button @click="deleteProduct(row)" class="text-xs underline text-red-500">刪除</button>
        </div>
      </template>
    </DataTable>

    <Pagination v-if="totalPages > 1" :currentPage="page" :totalPages="totalPages" @update:page="page = $event" class="mt-6" />

    <!-- Add/Edit Modal -->
    <BaseModal :show="showModal" @close="showModal = false" :title="editingProduct ? '編輯商品' : '新增商品'">
      <form @submit.prevent="saveProduct" class="space-y-4">
        <BaseInput label="商品名稱" v-model="form.name" required />
        <BaseInput label="品牌" v-model="form.brand" required />
        <BaseInput label="Slug" v-model="form.slug" required />
        <div class="grid grid-cols-2 gap-4">
          <BaseInput label="價格" type="number" v-model.number="form.price" required />
          <BaseInput label="特價" type="number" v-model.number="form.salePrice" />
        </div>
        <div>
          <label class="block text-xs uppercase tracking-wider text-brand-charcoal mb-1">描述</label>
          <textarea v-model="form.description" rows="4" class="input-field w-full resize-none"></textarea>
        </div>
        <BaseInput label="庫存" type="number" v-model.number="form.stock" />
        <div class="flex justify-end gap-3">
          <BaseButton variant="secondary" @click="showModal = false">取消</BaseButton>
          <BaseButton type="submit" :loading="saving">儲存</BaseButton>
        </div>
      </form>
    </BaseModal>
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from 'vue'
import { getProducts as fetchAdminProducts } from '@/api/admin'
import { formatCurrency } from '@/utils/formatters'
import DataTable from '@/components/admin/DataTable.vue'
import BaseButton from '@/components/ui/BaseButton.vue'
import BaseInput from '@/components/ui/BaseInput.vue'
import BaseModal from '@/components/ui/BaseModal.vue'
import LoadingSpinner from '@/components/ui/LoadingSpinner.vue'
import Pagination from '@/components/ui/Pagination.vue'

const search = ref('')
const filterCategory = ref('')
const products = ref([])
const categories = ref([])
const loading = ref(true)
const page = ref(1)
const totalPages = ref(1)

const showModal = ref(false)
const editingProduct = ref(null)
const saving = ref(false)
const form = reactive({ name: '', brand: '', slug: '', price: 0, salePrice: null, description: '', stock: 0 })

const columns = [
  { key: 'image', label: '' },
  { key: 'name', label: '商品名稱' },
  { key: 'brand', label: '品牌' },
  { key: 'price', label: '價格' },
  { key: 'stock', label: '庫存' },
  { key: 'active', label: '狀態' },
  { key: 'actions', label: '操作' },
]

async function loadProducts () {
  loading.value = true
  try {
    const res = await fetchAdminProducts({ page: page.value - 1, search: search.value, category: filterCategory.value })
    products.value = res.data?.content || []
    totalPages.value = res.data?.totalPages || 1
  } finally { loading.value = false }
}

function editProduct (row) {
  editingProduct.value = row
  Object.assign(form, row)
  showModal.value = true
}

function toggleActive (row) { row.active = !row.active }
function deleteProduct (row) { if (confirm('確定刪除？')) products.value = products.value.filter(p => p.id !== row.id) }
async function saveProduct () { saving.value = true; setTimeout(() => { saving.value = false; showModal.value = false }, 500) }

watch([page, search, filterCategory], () => loadProducts())
onMounted(() => loadProducts())
</script>
