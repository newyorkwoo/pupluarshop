<template>
  <div>
    <h1 class="text-xl font-light tracking-wider mb-6">系統設定</h1>

    <div class="space-y-8 max-w-2xl">
      <!-- General -->
      <section>
        <h2 class="text-sm uppercase tracking-[0.15em] font-semibold mb-4">網站基本設定</h2>
        <div class="space-y-4">
          <BaseInput label="網站名稱" v-model="settings.siteName" />
          <BaseInput label="網站描述" v-model="settings.siteDescription" />
          <BaseInput label="客服 Email" v-model="settings.supportEmail" />
          <BaseInput label="客服電話" v-model="settings.supportPhone" />
        </div>
      </section>

      <!-- Shipping -->
      <section>
        <h2 class="text-sm uppercase tracking-[0.15em] font-semibold mb-4">配送設定</h2>
        <div class="space-y-4">
          <BaseInput label="免運門檻 (NT$)" type="number" v-model.number="settings.freeShippingThreshold" />
          <BaseInput label="運費 (NT$)" type="number" v-model.number="settings.shippingFee" />
        </div>
      </section>

      <!-- Notification -->
      <section>
        <h2 class="text-sm uppercase tracking-[0.15em] font-semibold mb-4">通知設定</h2>
        <div class="space-y-3">
          <div class="flex items-center gap-3">
            <input id="orderNotify" type="checkbox" v-model="settings.orderNotification" />
            <label for="orderNotify" class="text-sm">新訂單通知</label>
          </div>
          <div class="flex items-center gap-3">
            <input id="reviewNotify" type="checkbox" v-model="settings.reviewNotification" />
            <label for="reviewNotify" class="text-sm">新評論通知</label>
          </div>
          <div class="flex items-center gap-3">
            <input id="stockNotify" type="checkbox" v-model="settings.lowStockNotification" />
            <label for="stockNotify" class="text-sm">庫存不足通知</label>
          </div>
          <BaseInput label="庫存不足警戒值" type="number" v-model.number="settings.lowStockThreshold" />
        </div>
      </section>

      <!-- Announcement -->
      <section>
        <h2 class="text-sm uppercase tracking-[0.15em] font-semibold mb-4">公告列</h2>
        <div class="space-y-4">
          <BaseInput label="公告文字" v-model="settings.announcementText" />
          <div class="flex items-center gap-3">
            <input id="annActive" type="checkbox" v-model="settings.announcementActive" />
            <label for="annActive" class="text-sm">顯示公告</label>
          </div>
        </div>
      </section>

      <BaseButton @click="saveSettings" :loading="saving">儲存設定</BaseButton>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getSettings, updateSettings } from '@/api/admin'
import BaseInput from '@/components/ui/BaseInput.vue'
import BaseButton from '@/components/ui/BaseButton.vue'

const saving = ref(false)
const settings = reactive({
  siteName: 'PopularShop',
  siteDescription: '精品設計師品牌線上購物',
  supportEmail: 'support@popularshop.com',
  supportPhone: '02-2345-6789',
  freeShippingThreshold: 5000,
  shippingFee: 150,
  orderNotification: true,
  reviewNotification: true,
  lowStockNotification: true,
  lowStockThreshold: 5,
  announcementText: '全館消費滿 NT$5,000 享免運',
  announcementActive: true,
})

async function loadSettings () {
  try {
    const res = await getSettings()
    if (res.data) Object.assign(settings, res.data)
  } catch {}
}

async function saveSettings () {
  saving.value = true
  try { await updateSettings(settings) } finally { saving.value = false }
}

onMounted(() => loadSettings())
</script>
