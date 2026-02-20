<template>
  <div>
    <h1 class="text-xl font-light tracking-wider mb-8">儀表板</h1>

    <!-- Stats -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
      <StatsCard title="本月營收" :value="formatCurrency(stats.monthlyRevenue)" :change="stats.revenueChange" />
      <StatsCard title="本月訂單" :value="stats.monthlyOrders" :change="stats.ordersChange" />
      <StatsCard title="新會員" :value="stats.newUsers" :change="stats.usersChange" />
      <StatsCard title="平均客單價" :value="formatCurrency(stats.avgOrderValue)" :change="stats.avgChange" />
    </div>

    <!-- Charts -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-8">
      <div class="bg-white border border-brand-light p-6">
        <h2 class="text-sm uppercase tracking-wider font-semibold mb-4">最近 7 天營收</h2>
        <ChartWidget :data="revenueChart" />
      </div>
      <div class="bg-white border border-brand-light p-6">
        <h2 class="text-sm uppercase tracking-wider font-semibold mb-4">訂單趨勢</h2>
        <ChartWidget :data="ordersChart" />
      </div>
    </div>

    <!-- Recent Orders -->
    <div class="bg-white border border-brand-light p-6">
      <h2 class="text-sm uppercase tracking-wider font-semibold mb-4">最新訂單</h2>
      <DataTable :columns="orderColumns" :rows="recentOrders">
        <template #cell-status="{ value }">
          <span class="text-xs px-2 py-0.5 rounded-full" :class="statusClass(value)">{{ value }}</span>
        </template>
      </DataTable>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAdminStore } from '@/stores/adminStore'
import { formatCurrency } from '@/utils/formatters'
import StatsCard from '@/components/admin/StatsCard.vue'
import ChartWidget from '@/components/admin/ChartWidget.vue'
import DataTable from '@/components/admin/DataTable.vue'

const adminStore = useAdminStore()

const stats = ref({ monthlyRevenue: 2450000, revenueChange: 12.5, monthlyOrders: 186, ordersChange: 8.3, newUsers: 42, usersChange: -2.1, avgOrderValue: 13172, avgChange: 3.8 })
const revenueChart = ref({ labels: ['一', '二', '三', '四', '五', '六', '日'], values: [380000, 420000, 350000, 390000, 410000, 280000, 220000] })
const ordersChart = ref({ labels: ['一', '二', '三', '四', '五', '六', '日'], values: [28, 32, 26, 30, 31, 21, 18] })
const recentOrders = ref([
  { id: 'ORD-20241201-001', customer: '王小明', amount: 'NT$52,000', status: 'PENDING', date: '2024-12-01' },
  { id: 'ORD-20241201-002', customer: '李美玲', amount: 'NT$18,500', status: 'SHIPPED', date: '2024-12-01' },
  { id: 'ORD-20241130-003', customer: '張大華', amount: 'NT$89,000', status: 'DELIVERED', date: '2024-11-30' },
])
const orderColumns = [
  { key: 'id', label: '訂單編號' },
  { key: 'customer', label: '顧客' },
  { key: 'amount', label: '金額' },
  { key: 'status', label: '狀態' },
  { key: 'date', label: '日期' },
]

function statusClass (s) {
  const map = { PENDING: 'bg-yellow-100 text-yellow-700', SHIPPED: 'bg-blue-100 text-blue-700', DELIVERED: 'bg-green-100 text-green-700', CANCELLED: 'bg-red-100 text-red-700' }
  return map[s] || 'bg-gray-100 text-gray-600'
}

onMounted(async () => {
  try { await adminStore.fetchDashboard() } catch {}
})
</script>
