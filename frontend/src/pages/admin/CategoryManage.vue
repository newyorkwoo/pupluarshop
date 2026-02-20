<template>
  <div>
    <div class="flex items-center justify-between mb-6">
      <h1 class="text-xl font-light tracking-wider">分類管理</h1>
      <BaseButton @click="showModal = true">新增分類</BaseButton>
    </div>

    <LoadingSpinner v-if="loading" />
    <DataTable v-else :columns="columns" :rows="categories">
      <template #cell-actions="{ row }">
        <div class="flex gap-2">
          <button @click="editCategory(row)" class="text-xs underline">編輯</button>
          <button @click="deleteCategory(row)" class="text-xs underline text-red-500">刪除</button>
        </div>
      </template>
    </DataTable>

    <BaseModal :show="showModal" @close="closeModal" :title="editing ? '編輯分類' : '新增分類'">
      <form @submit.prevent="saveCategory" class="space-y-4">
        <BaseInput label="分類名稱" v-model="form.name" required />
        <BaseInput label="Slug" v-model="form.slug" required />
        <div>
          <label class="block text-xs uppercase tracking-wider text-brand-charcoal mb-1">描述</label>
          <textarea v-model="form.description" rows="3" class="input-field w-full resize-none"></textarea>
        </div>
        <select v-model="form.parentId" class="input-field w-full">
          <option :value="null">無上層分類</option>
          <option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</option>
        </select>
        <div class="flex justify-end gap-3">
          <BaseButton variant="secondary" @click="closeModal">取消</BaseButton>
          <BaseButton type="submit" :loading="saving">儲存</BaseButton>
        </div>
      </form>
    </BaseModal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getCategories, createCategory, updateCategory, deleteCategory as apiDeleteCat } from '@/api/admin'
import DataTable from '@/components/admin/DataTable.vue'
import BaseButton from '@/components/ui/BaseButton.vue'
import BaseInput from '@/components/ui/BaseInput.vue'
import BaseModal from '@/components/ui/BaseModal.vue'
import LoadingSpinner from '@/components/ui/LoadingSpinner.vue'

const categories = ref([])
const loading = ref(true)
const showModal = ref(false)
const editing = ref(null)
const saving = ref(false)
const form = reactive({ name: '', slug: '', description: '', parentId: null })

const columns = [
  { key: 'name', label: '分類名稱' },
  { key: 'slug', label: 'Slug' },
  { key: 'productCount', label: '商品數' },
  { key: 'actions', label: '操作' },
]

async function loadCategories () {
  loading.value = true
  try {
    const res = await getCategories()
    categories.value = res.data || []
  } finally { loading.value = false }
}

function editCategory (row) {
  editing.value = row
  Object.assign(form, row)
  showModal.value = true
}

function closeModal () {
  showModal.value = false
  editing.value = null
  Object.assign(form, { name: '', slug: '', description: '', parentId: null })
}

async function saveCategory () {
  saving.value = true
  try {
    if (editing.value) await updateCategory(editing.value.id, form)
    else await createCategory(form)
    await loadCategories()
    closeModal()
  } finally { saving.value = false }
}

async function deleteCategory (row) {
  if (!confirm(`確定刪除分類「${row.name}」？`)) return
  await apiDeleteCat(row.id)
  await loadCategories()
}

onMounted(() => loadCategories())
</script>
