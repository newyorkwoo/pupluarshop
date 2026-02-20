<template>
  <div>
    <div class="flex items-center justify-between mb-6">
      <h1 class="text-xl font-light tracking-wider">商品管理</h1>
      <BaseButton @click="openNewModal">新增商品</BaseButton>
    </div>

    <div class="flex gap-4 mb-6">
      <input id="product-search" name="product-search" v-model="search" type="text" placeholder="搜尋商品名稱或品牌..." class="input-field flex-1" />
      <select id="product-filter-category" name="product-filter-category" v-model="filterCategory" class="input-field w-48">
        <option value="">全部分類</option>
        <option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</option>
      </select>
    </div>

    <LoadingSpinner v-if="loading" />
    <DataTable v-else :columns="columns" :rows="products">
      <template #cell-image>
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
          <button @click="deleteProductItem(row)" class="text-xs underline text-red-500">刪除</button>
        </div>
      </template>
    </DataTable>

    <Pagination v-if="totalPages > 1" :currentPage="page" :totalPages="totalPages" @update:page="page = $event" class="mt-6" />

    <!-- Add/Edit Modal -->
    <BaseModal v-model="showModal">
      <h2 class="text-lg font-light tracking-wider mb-4">{{ editingProduct ? '編輯商品' : '新增商品' }}</h2>
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
        <div>
          <label class="block text-xs uppercase tracking-wider text-brand-charcoal mb-1">分類</label>
          <select id="product-category" name="product-category" v-model="form.categoryId" class="input-field w-full">
            <option :value="null">未分類</option>
            <option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</option>
          </select>
        </div>
        <div class="flex justify-end gap-3">
          <BaseButton variant="secondary" type="button" @click="showModal = false">取消</BaseButton>
          <BaseButton type="submit" :loading="saving">儲存</BaseButton>
        </div>
      </form>
    </BaseModal>
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from 'vue'
import { getProducts as fetchAdminProducts, createProduct, updateProduct, deleteProduct as removeProduct, toggleProduct, getCategories as fetchCategories } from '@/api/admin'
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
const form = reactive({ name: '', brand: '', slug: '', price: 0, salePrice: null, description: '', stock: 0, categoryId: null })

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
    const res = await fetchAdminProducts({ page: page.value - 1, keyword: search.value, category: filterCategory.value })
    products.value = res.data?.content || []
    totalPages.value = res.data?.totalPages || 1
  } finally { loading.value = false }
}

async function loadCategories () {
  try {
    const res = await fetchCategories()
    categories.value = res.data || []
  } catch (e) { /* ignore */ }
}

function openNewModal () {
  editingProduct.value = null
  Object.assign(form, { name: '', brand: '', slug: '', price: 0, salePrice: null, description: '', stock: 0, categoryId: null })
  showModal.value = true
}

function editProduct (row) {
  editingProduct.value = row
  Object.assign(form, { name: row.name, brand: row.brand, slug: row.slug || '', price: row.price, salePrice: row.salePrice, description: row.description || '', stock: row.stock, categoryId: row.categoryId || null })
  showModal.value = true
}

async function toggleActive (row) {
  try {
    await toggleProduct(row.id)
    row.active = !row.active
  } catch (e) { alert('操作失敗') }
}

async function deleteProductItem (row) {
  if (!confirm('確定刪除？')) return
  try {
    await removeProduct(row.id)
    await loadProducts()
  } catch (e) { alert('刪除失敗') }
}

async function saveProduct () {
  saving.value = true
  try {
    const payload = {
      name: form.name,
      brand: form.brand,
      price: form.price,
      salePrice: form.salePrice || null,
      description: form.description,
      stock: form.stock,
      categoryId: form.categoryId,
      active: true
    }
    if (editingProduct.value) {
      await updateProduct(editingProduct.value.id, payload)
    } else {
      await createProduct(payload)
    }
    showModal.value = false
    await loadProducts()
  } catch (e) {
    alert('儲存失敗：' + (e.message || '未知錯誤'))
  } finally { saving.value = false }
}

watch([page, search, filterCategory], () => loadProducts())
onMounted(() => { loadProducts(); loadCategories() })
</script>
