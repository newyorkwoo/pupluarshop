<template>
  <div class="min-h-[70vh] flex items-center justify-center px-4">
    <div class="w-full max-w-md">
      <h1 class="text-2xl font-light tracking-wider text-center mb-8">建立帳號</h1>
      <form @submit.prevent="handleRegister" class="space-y-4">
        <BaseInput label="姓名" v-model="form.name" :error="errors.name" required />
        <BaseInput label="電子郵件" type="email" v-model="form.email" :error="errors.email" required />
        <BaseInput label="密碼" type="password" v-model="form.password" :error="errors.password" required />
        <BaseInput label="確認密碼" type="password" v-model="form.confirmPassword" :error="errors.confirmPassword" required />
        <div class="flex items-start gap-2">
          <input id="agree" type="checkbox" v-model="form.agree" class="mt-1" />
          <label for="agree" class="text-xs text-brand-gray">我已閱讀並同意 <router-link to="/terms" class="underline">服務條款</router-link> 及 <router-link to="/privacy" class="underline">隱私權政策</router-link></label>
        </div>
        <p v-if="registerError" class="text-red-500 text-sm">{{ registerError }}</p>
        <BaseButton type="submit" :loading="loading" class="w-full">註冊</BaseButton>
      </form>
      <p class="mt-6 text-center text-sm text-brand-gray">已有帳號？ <router-link to="/login" class="text-brand-black underline">立即登入</router-link></p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import { isValidEmail, isValidPassword } from '@/utils/validators'
import BaseInput from '@/components/ui/BaseInput.vue'
import BaseButton from '@/components/ui/BaseButton.vue'

const router = useRouter()
const authStore = useAuthStore()

const form = reactive({ name: '', email: '', password: '', confirmPassword: '', agree: false })
const errors = reactive({ name: '', email: '', password: '', confirmPassword: '' })
const registerError = ref('')
const loading = ref(false)

async function handleRegister () {
  Object.keys(errors).forEach(k => errors[k] = '')
  registerError.value = ''
  let valid = true
  if (!form.name) { errors.name = '請輸入姓名'; valid = false }
  if (!isValidEmail(form.email)) { errors.email = '請輸入有效的電子郵件'; valid = false }
  if (!isValidPassword(form.password)) { errors.password = '密碼需至少8位，包含大小寫及數字'; valid = false }
  if (form.password !== form.confirmPassword) { errors.confirmPassword = '密碼不一致'; valid = false }
  if (!form.agree) { registerError.value = '請同意服務條款與隱私權政策'; valid = false }
  if (!valid) return

  loading.value = true
  try {
    await authStore.register({ name: form.name, email: form.email, password: form.password })
    router.push('/')
  } catch (e) {
    registerError.value = e.response?.data?.message || '註冊失敗，請稍後再試'
  } finally { loading.value = false }
}
</script>
