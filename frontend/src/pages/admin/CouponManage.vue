<template>
  <div>
    <div class="flex items-center justify-between mb-6">
      <h1 class="text-xl font-light tracking-wider">折扣碼管理</h1>
      <BaseButton @click="showModal = true">新增折扣碼</BaseButton>
    </div>

    <LoadingSpinner v-if="loading" />
    <DataTable v-else :columns="columns" :rows="coupons">
      <template #cell-active="{ value }">
        <span :class="value ? 'text-green-600' : 'text-red-500'" class="text-xs">{{ value ? '啟用' : '停用' }}</span>
      </template>
      <template #cell-discount="{ row }">
        {{ row.type === 'PERCENTAGE' ? row.value + '%' : formatCurrency(row.value) }}
      </template>
      <template #cell-actions="{ row }">
        <div class="flex gap-2">
          <button @click="editCoupon(row)" class="text-xs underline">編輯</button>
          <button @click="deleteCoupon(row)" class="text-xs underline text-red-500">刪除</button>
        </div>
      </template>
    </DataTable>

    <BaseModal :show="showModal" @close="closeModal" :title="editing ? '編輯折扣碼' : '新增折扣碼'">
      <form @submit.prevent="saveCoupon" class="space-y-4">
        <BaseInput label="折扣碼" v-model="form.code" required />
        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="block text-xs uppercase tracking-wider text-brand-charcoal mb-1">折扣類型</label>
            <select v-model="form.type" class="input-field w-full">
              <option value="PERCENTAGE">百分比</option>
              <option value="FIXED">固定金額</option>
            </select>
          </div>
          <BaseInput :label="form.type === 'PERCENTAGE' ? '折扣 (%)' : '折扣金額'" type="number" v-model.number="form.value" required />
        </div>
        <div class="grid grid-cols-2 gap-4">
          <BaseInput label="最低消費" type="number" v-model.number="form.minPurchase" />
          <BaseInput label="使用上限" type="number" v-model.number="form.maxUses" />
        </div>
        <div class="grid grid-cols-2 gap-4">
          <BaseInput label="開始日期" type="date" v-model="form.startDate" />
          <BaseInput label="結束日期" type="date" v-model="form.endDate" />
        </div>
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
import { getCoupons, createCoupon, updateCoupon, deleteCoupon as apiDeleteCoupon } from '@/api/admin'
import { formatCurrency } from '@/utils/formatters'
import DataTable from '@/components/admin/DataTable.vue'
import BaseButton from '@/components/ui/BaseButton.vue'
import BaseInput from '@/components/ui/BaseInput.vue'
import BaseModal from '@/components/ui/BaseModal.vue'
import LoadingSpinner from '@/components/ui/LoadingSpinner.vue'

const coupons = ref([])
const loading = ref(true)
const showModal = ref(false)
const editing = ref(null)
const saving = ref(false)
const form = reactive({ code: '', type: 'PERCENTAGE', value: 10, minPurchase: 0, maxUses: 100, startDate: '', endDate: '' })

const columns = [
  { key: 'code', label: '折扣碼' },
  { key: 'discount', label: '折扣' },
  { key: 'minPurchase', label: '最低消費' },
  { key: 'usedCount', label: '已使用' },
  { key: 'maxUses', label: '上限' },
  { key: 'active', label: '狀態' },
  { key: 'actions', label: '操作' },
]

async function loadCoupons () {
  loading.value = true
  try { const res = await getCoupons(); coupons.value = res.data || [] } finally { loading.value = false }
}

function editCoupon (row) { editing.value = row; Object.assign(form, row); showModal.value = true }
function closeModal () { showModal.value = false; editing.value = null; Object.assign(form, { code: '', type: 'PERCENTAGE', value: 10, minPurchase: 0, maxUses: 100, startDate: '', endDate: '' }) }

async function saveCoupon () {
  saving.value = true
  try {
    if (editing.value) await updateCoupon(editing.value.id, form); else await createCoupon(form)
    await loadCoupons(); closeModal()
  } finally { saving.value = false }
}

async function deleteCoupon (row) {
  if (!confirm(`確定刪除折扣碼「${row.code}」？`)) return
  await apiDeleteCoupon(row.id); await loadCoupons()
}

onMounted(() => loadCoupons())
</script>
