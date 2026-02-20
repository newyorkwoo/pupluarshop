<template>
  <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <h1 class="text-2xl font-light tracking-wider mb-8">我的帳號</h1>

    <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
      <!-- Sidebar -->
      <nav class="space-y-1">
        <button v-for="tab in tabs" :key="tab.key" @click="activeTab = tab.key"
          :class="['block w-full text-left px-4 py-2 text-sm transition-colors', activeTab === tab.key ? 'bg-brand-black text-white' : 'text-brand-charcoal hover:bg-brand-light']">
          {{ tab.label }}
        </button>
        <button @click="handleLogout" class="block w-full text-left px-4 py-2 text-sm text-red-500 hover:bg-red-50">登出</button>
      </nav>

      <!-- Content -->
      <div class="md:col-span-3">
        <!-- Profile -->
        <div v-if="activeTab === 'profile'">
          <h2 class="text-sm uppercase tracking-[0.15em] font-semibold mb-4">個人資料</h2>
          <form @submit.prevent="saveProfile" class="space-y-4 max-w-md">
            <BaseInput label="姓名" v-model="profile.name" />
            <BaseInput label="電子郵件" type="email" v-model="profile.email" disabled />
            <BaseInput label="電話" v-model="profile.phone" />
            <BaseButton type="submit" :loading="saving">儲存變更</BaseButton>
          </form>
        </div>

        <!-- Addresses -->
        <div v-if="activeTab === 'addresses'">
          <div class="flex justify-between items-center mb-4">
            <h2 class="text-sm uppercase tracking-[0.15em] font-semibold">配送地址</h2>
            <BaseButton variant="secondary" size="sm" @click="showAddressModal = true">新增地址</BaseButton>
          </div>
          <div v-if="addresses.length" class="grid gap-4">
            <div v-for="addr in addresses" :key="addr.id" class="border border-brand-light p-4">
              <p class="font-medium text-sm">{{ addr.name }}</p>
              <p class="text-sm text-brand-gray">{{ addr.phone }}</p>
              <p class="text-sm text-brand-gray">{{ addr.city }} {{ addr.district }} {{ addr.zipCode }}</p>
              <p class="text-sm text-brand-gray">{{ addr.addressLine }}</p>
              <div class="mt-2 flex gap-2">
                <button class="text-xs underline">編輯</button>
                <button class="text-xs underline text-red-500">刪除</button>
              </div>
            </div>
          </div>
          <p v-else class="text-sm text-brand-gray">尚未新增配送地址</p>
        </div>

        <!-- Change Password -->
        <div v-if="activeTab === 'password'">
          <h2 class="text-sm uppercase tracking-[0.15em] font-semibold mb-4">變更密碼</h2>
          <form @submit.prevent="changePassword" class="space-y-4 max-w-md">
            <BaseInput label="目前密碼" type="password" v-model="pw.current" />
            <BaseInput label="新密碼" type="password" v-model="pw.newPass" />
            <BaseInput label="確認新密碼" type="password" v-model="pw.confirm" />
            <BaseButton type="submit" :loading="changingPw">更新密碼</BaseButton>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import { getProfile, updateProfile, getAddresses } from '@/api/user'
import BaseInput from '@/components/ui/BaseInput.vue'
import BaseButton from '@/components/ui/BaseButton.vue'

const router = useRouter()
const authStore = useAuthStore()

const activeTab = ref('profile')
const tabs = [
  { key: 'profile', label: '個人資料' },
  { key: 'addresses', label: '配送地址' },
  { key: 'password', label: '變更密碼' },
]

const profile = reactive({ name: '', email: '', phone: '' })
const addresses = ref([])
const saving = ref(false)
const showAddressModal = ref(false)
const pw = reactive({ current: '', newPass: '', confirm: '' })
const changingPw = ref(false)

async function loadProfile () {
  try {
    const res = await getProfile()
    Object.assign(profile, res.data)
  } catch {}
}

async function loadAddresses () {
  try {
    const res = await getAddresses()
    addresses.value = res.data
  } catch {}
}

async function saveProfile () {
  saving.value = true
  try { await updateProfile(profile) } finally { saving.value = false }
}

async function changePassword () {
  changingPw.value = true
  try {
    // await api call
  } finally { changingPw.value = false }
}

function handleLogout () {
  authStore.logout()
  router.push('/login')
}

onMounted(() => { loadProfile(); loadAddresses() })
</script>
