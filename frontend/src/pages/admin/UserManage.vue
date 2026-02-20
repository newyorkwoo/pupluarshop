<template>
  <div>
    <h1 class="text-xl font-light tracking-wider mb-6">會員管理</h1>

    <div class="flex gap-4 mb-6">
      <input v-model="search" type="text" placeholder="搜尋姓名或信箱..." class="input-field flex-1" />
      <select v-model="filterRole" class="input-field w-40">
        <option value="">全部角色</option>
        <option value="USER">一般會員</option>
        <option value="ADMIN">管理員</option>
      </select>
    </div>

    <LoadingSpinner v-if="loading" />
    <DataTable v-else :columns="columns" :rows="users">
      <template #cell-role="{ value }">
        <span class="text-xs px-2 py-0.5 rounded-full" :class="value === 'ADMIN' ? 'bg-purple-100 text-purple-700' : 'bg-gray-100 text-gray-600'">{{ value === 'ADMIN' ? '管理員' : '會員' }}</span>
      </template>
      <template #cell-active="{ value }">
        <span :class="value ? 'text-green-600' : 'text-red-500'" class="text-xs">{{ value ? '啟用' : '停用' }}</span>
      </template>
      <template #cell-actions="{ row }">
        <div class="flex gap-2">
          <button @click="toggleUserActive(row)" class="text-xs underline">{{ row.active ? '停用' : '啟用' }}</button>
          <button v-if="row.role !== 'ADMIN'" @click="promoteUser(row)" class="text-xs underline">設為管理員</button>
        </div>
      </template>
    </DataTable>

    <Pagination v-if="totalPages > 1" :currentPage="page" :totalPages="totalPages" @update:page="page = $event" class="mt-6" />
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { getUsers } from '@/api/admin'
import DataTable from '@/components/admin/DataTable.vue'
import LoadingSpinner from '@/components/ui/LoadingSpinner.vue'
import Pagination from '@/components/ui/Pagination.vue'

const search = ref('')
const filterRole = ref('')
const users = ref([])
const loading = ref(true)
const page = ref(1)
const totalPages = ref(1)

const columns = [
  { key: 'name', label: '姓名' },
  { key: 'email', label: '電子郵件' },
  { key: 'role', label: '角色' },
  { key: 'active', label: '狀態' },
  { key: 'createdAt', label: '註冊日期' },
  { key: 'actions', label: '操作' },
]

async function loadUsers () {
  loading.value = true
  try {
    const res = await getUsers({ page: page.value - 1, search: search.value, role: filterRole.value })
    users.value = res.data?.content || []
    totalPages.value = res.data?.totalPages || 1
  } finally { loading.value = false }
}

function toggleUserActive (row) { row.active = !row.active }
function promoteUser (row) { row.role = 'ADMIN' }

watch([page, search, filterRole], () => loadUsers())
onMounted(() => loadUsers())
</script>
