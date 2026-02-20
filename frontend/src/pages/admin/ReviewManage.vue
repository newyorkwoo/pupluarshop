<template>
  <div>
    <h1 class="text-xl font-light tracking-wider mb-6">評論管理</h1>

    <div class="flex gap-4 mb-6">
      <input v-model="search" type="text" placeholder="搜尋商品或評論內容..." class="input-field flex-1" />
      <select v-model="filterStatus" class="input-field w-40">
        <option value="">全部</option>
        <option value="PENDING">待審核</option>
        <option value="APPROVED">已核准</option>
        <option value="REJECTED">已拒絕</option>
      </select>
    </div>

    <LoadingSpinner v-if="loading" />
    <DataTable v-else :columns="columns" :rows="reviews">
      <template #cell-rating="{ value }">
        <div class="flex">
          <span v-for="i in 5" :key="i" class="text-sm" :class="i <= value ? 'text-yellow-500' : 'text-gray-300'">★</span>
        </div>
      </template>
      <template #cell-status="{ value }">
        <span class="text-xs px-2 py-0.5 rounded-full" :class="statusClass(value)">{{ statusLabel(value) }}</span>
      </template>
      <template #cell-actions="{ row }">
        <div class="flex gap-2">
          <button v-if="row.status !== 'APPROVED'" @click="approve(row)" class="text-xs underline text-green-600">核准</button>
          <button v-if="row.status !== 'REJECTED'" @click="reject(row)" class="text-xs underline text-red-500">拒絕</button>
          <button @click="deleteReview(row)" class="text-xs underline text-red-500">刪除</button>
        </div>
      </template>
    </DataTable>

    <Pagination v-if="totalPages > 1" :currentPage="page" :totalPages="totalPages" @update:page="page = $event" class="mt-6" />
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { getReviews, approveReview, rejectReview, deleteReview as apiDeleteReview } from '@/api/admin'
import DataTable from '@/components/admin/DataTable.vue'
import LoadingSpinner from '@/components/ui/LoadingSpinner.vue'
import Pagination from '@/components/ui/Pagination.vue'

const search = ref('')
const filterStatus = ref('')
const reviews = ref([])
const loading = ref(true)
const page = ref(1)
const totalPages = ref(1)

const columns = [
  { key: 'productName', label: '商品' },
  { key: 'userName', label: '用戶' },
  { key: 'rating', label: '評分' },
  { key: 'content', label: '內容' },
  { key: 'status', label: '狀態' },
  { key: 'createdAt', label: '日期' },
  { key: 'actions', label: '操作' },
]

function statusClass (s) {
  return { PENDING: 'bg-yellow-100 text-yellow-700', APPROVED: 'bg-green-100 text-green-700', REJECTED: 'bg-red-100 text-red-700' }[s] || ''
}
function statusLabel (s) {
  return { PENDING: '待審核', APPROVED: '已核准', REJECTED: '已拒絕' }[s] || s
}

async function loadReviews () {
  loading.value = true
  try {
    const res = await getReviews({ page: page.value - 1, search: search.value, status: filterStatus.value })
    reviews.value = res.data?.content || []
    totalPages.value = res.data?.totalPages || 1
  } finally { loading.value = false }
}

async function approve (row) { await approveReview(row.id); row.status = 'APPROVED' }
async function reject (row) { await rejectReview(row.id); row.status = 'REJECTED' }
async function deleteReview (row) {
  if (!confirm('確定刪除此評論？')) return
  await apiDeleteReview(row.id); await loadReviews()
}

watch([page, search, filterStatus], () => loadReviews())
onMounted(() => loadReviews())
</script>
