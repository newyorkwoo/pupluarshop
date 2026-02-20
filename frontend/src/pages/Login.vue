<template>
  <div class="min-h-[70vh] flex items-center justify-center px-4">
    <div class="w-full max-w-md">
      <h1 class="text-2xl font-light tracking-wider text-center mb-8">登入</h1>
      <form @submit.prevent="handleLogin" class="space-y-4">
        <BaseInput label="電子郵件" type="email" v-model="form.email" :error="errors.email" required />
        <BaseInput label="密碼" type="password" v-model="form.password" :error="errors.password" required />
        <p v-if="loginError" class="text-red-500 text-sm">{{ loginError }}</p>
        <BaseButton type="submit" :loading="loading" class="w-full">登入</BaseButton>
      </form>
      <div class="mt-6 text-center text-sm space-y-2">
        <router-link to="/forgot-password" class="text-brand-gray hover:text-brand-black underline">忘記密碼？</router-link>
        <p class="text-brand-gray">還沒有帳號？ <router-link to="/register" class="text-brand-black underline">立即註冊</router-link></p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import BaseInput from '@/components/ui/BaseInput.vue'
import BaseButton from '@/components/ui/BaseButton.vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const form = reactive({ email: '', password: '' })
const errors = reactive({ email: '', password: '' })
const loginError = ref('')
const loading = ref(false)

async function handleLogin () {
  errors.email = ''
  errors.password = ''
  loginError.value = ''
  if (!form.email) { errors.email = '請輸入電子郵件'; return }
  if (!form.password) { errors.password = '請輸入密碼'; return }

  loading.value = true
  try {
    await authStore.login(form.email, form.password)
    router.push(route.query.redirect || '/')
  } catch (e) {
    loginError.value = e.response?.data?.message || '登入失敗，請檢查帳號密碼'
  } finally { loading.value = false }
}
</script>
