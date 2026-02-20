<template>
  <div>
    <div class="flex items-center justify-between mb-6">
      <h1 class="text-xl font-light tracking-wider">Banner 管理</h1>
      <BaseButton @click="showModal = true">新增 Banner</BaseButton>
    </div>

    <LoadingSpinner v-if="loading" />
    <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div v-for="banner in banners" :key="banner.id" class="border border-brand-light p-4">
        <div class="aspect-[16/5] bg-brand-cream mb-3 flex items-center justify-center text-brand-gray text-sm">
          {{ banner.title || 'Banner Image' }}
        </div>
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm font-medium">{{ banner.title }}</p>
            <p class="text-xs text-brand-gray">{{ banner.position }} · 排序: {{ banner.sortOrder }}</p>
          </div>
          <div class="flex gap-2">
            <button @click="editBanner(banner)" class="text-xs underline">編輯</button>
            <button @click="deleteBanner(banner)" class="text-xs underline text-red-500">刪除</button>
          </div>
        </div>
      </div>
    </div>

    <BaseModal :show="showModal" @close="closeModal" :title="editing ? '編輯 Banner' : '新增 Banner'">
      <form @submit.prevent="saveBanner" class="space-y-4">
        <BaseInput label="標題" v-model="form.title" required />
        <BaseInput label="連結" v-model="form.link" />
        <div>
          <label class="block text-xs uppercase tracking-wider text-brand-charcoal mb-1">位置</label>
          <select v-model="form.position" class="input-field w-full">
            <option value="HOME_HERO">首頁主圖</option>
            <option value="HOME_SUB">首頁副圖</option>
            <option value="CATEGORY">分類頁</option>
          </select>
        </div>
        <BaseInput label="排序" type="number" v-model.number="form.sortOrder" />
        <div class="flex items-center gap-2">
          <input id="bannerActive" type="checkbox" v-model="form.active" />
          <label for="bannerActive" class="text-sm">啟用</label>
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
import { getBanners, createBanner, updateBanner, deleteBanner as apiDeleteBanner } from '@/api/admin'
import DataTable from '@/components/admin/DataTable.vue'
import BaseButton from '@/components/ui/BaseButton.vue'
import BaseInput from '@/components/ui/BaseInput.vue'
import BaseModal from '@/components/ui/BaseModal.vue'
import LoadingSpinner from '@/components/ui/LoadingSpinner.vue'

const banners = ref([])
const loading = ref(true)
const showModal = ref(false)
const editing = ref(null)
const saving = ref(false)
const form = reactive({ title: '', link: '', position: 'HOME_HERO', sortOrder: 0, active: true })

async function loadBanners () {
  loading.value = true
  try { const res = await getBanners(); banners.value = res.data || [] } finally { loading.value = false }
}

function editBanner (b) { editing.value = b; Object.assign(form, b); showModal.value = true }
function closeModal () { showModal.value = false; editing.value = null; Object.assign(form, { title: '', link: '', position: 'HOME_HERO', sortOrder: 0, active: true }) }

async function saveBanner () {
  saving.value = true
  try {
    if (editing.value) await updateBanner(editing.value.id, form); else await createBanner(form)
    await loadBanners(); closeModal()
  } finally { saving.value = false }
}

async function deleteBanner (b) {
  if (!confirm('確定刪除？')) return
  await apiDeleteBanner(b.id); await loadBanners()
}

onMounted(() => loadBanners())
</script>
