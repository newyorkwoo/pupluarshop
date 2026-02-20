<template>
  <div>
    <h1 class="text-xl font-light tracking-wider mb-6">訂單管理</h1>

    <div class="flex gap-4 mb-6">
      <input v-model="search" type="text" placeholder="搜尋訂單編號..." class="input-field flex-1" />
      <select v-model="filterStatus" class="input-field w-40">
        <option value="">全部狀態</option>
        <option value="PENDING">待處理</option>
        <option value="PROCESSING">處理中</option>
        <option value="SHIPPED">已出貨</option>
        <option value="DELIVERED">已送達</option>
        <option value="CANCELLED">已取消</option>
      </select>
    </div>

    <LoadingSpinner v-if="loading" />
    <DataTable v-else :columns="columns" :rows="orders">
      <template #cell-status="{ value, row }">
        <select :value="value" @change="updateStatus(row, $event.target.value)" class="text-xs border rounded px-2 py-1">
          <option value="PENDING">待處理</option>
          <option value="PROCESSING">處理中</option>
          <option value="SHIPPED">已出貨</option>
          <option value="DELIVERED">已送達</option>
          <option value="CANCELLED">已取消</option>
        </select>
      </template>
      <template #cell-amount="{ value }">{{ formatCurrency(value) }}</template>
      <template #cell-actions="{ row }">
        <button @click="viewOrder(row)" class="text-xs underline">查看詳情</button>
      </template>
    </DataTable>

    <Pagination v-if="totalPages > 1" :currentPage="page" :totalPages="totalPages" @update:page="page = $event" class="mt-6" />

    <!-- Order Detail Modal -->
    <BaseModal :show="!!selectedOrder" @close="selectedOrder = null" title="訂單詳情">
      <template v-if="selectedOrder">
        <div class="space-y-3 text-sm">
          <div class="flex justify-between"><span class="text-brand-gray">訂單編號</span><span>{{ selectedOrder.orderNumber }}</span></div>
          <div class="flex justify-between"><span class="text-brand-gray">顧客</span><span>{{ selectedOrder.customerName }}</span></div>
          <div class="flex justify-between"><span class="text-brand-gray">電子郵件</span><span>{{ selectedOrder.email }}</span></div>
          <div class="flex justify-between"><span class="text-brand-gray">配送地址</span><span>{{ selectedOrder.shippingAddress }}</span></div>
          <div class="border-t pt-3 mt-3">
            <div v-for="item in selectedOrder.items" :key="item.id" class="flex justify-between py-1">
              <span>{{ item.productName }} × {{ item.quantity }}</span>
              <span>{{ formatCurrency(item.subtotal) }}</span>
            </div>
          </div>
          <div class="border-t pt-3 flex justify-between font-semibold">
            <span>合計</span><span>{{ formatCurrency(selectedOrder.totalAmount) }}</span>
          </div>
        </div>
      </template>
    </BaseModal>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { getOrders } from '@/api/admin'
import { formatCurrency } from '@/utils/formatters'
import DataTable from '@/components/admin/DataTable.vue'
import LoadingSpinner from '@/components/ui/LoadingSpinner.vue'
import Pagination from '@/components/ui/Pagination.vue'
import BaseModal from '@/components/ui/BaseModal.vue'

const search = ref('')
const filterStatus = ref('')
const orders = ref([])
const loading = ref(true)
const page = ref(1)
const totalPages = ref(1)
const selectedOrder = ref(null)

const columns = [
  { key: 'orderNumber', label: '訂單編號' },
  { key: 'customerName', label: '顧客' },
  { key: 'amount', label: '金額' },
  { key: 'status', label: '狀態' },
  { key: 'createdAt', label: '日期' },
  { key: 'actions', label: '' },
]

async function loadOrders () {
  loading.value = true
  try {
    const res = await getOrders({ page: page.value - 1, search: search.value, status: filterStatus.value })
    orders.value = res.data?.content || []
    totalPages.value = res.data?.totalPages || 1
  } finally { loading.value = false }
}

function viewOrder (row) { selectedOrder.value = row }
function updateStatus (row, status) { row.status = status }

watch([page, search, filterStatus], () => loadOrders())
onMounted(() => loadOrders())
</script>
