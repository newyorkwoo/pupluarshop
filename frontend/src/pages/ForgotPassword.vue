<template>
  <div class="min-h-[70vh] flex items-center justify-center px-4">
    <div class="w-full max-w-md">
      <h1 class="text-2xl font-light tracking-wider text-center mb-4">忘記密碼</h1>
      <p class="text-sm text-brand-gray text-center mb-8">請輸入您的電子郵件，我們將寄送重設密碼的連結給您。</p>

      <template v-if="!sent">
        <form @submit.prevent="handleSubmit" class="space-y-4">
          <BaseInput label="電子郵件" type="email" v-model="email" :error="error" required />
          <BaseButton type="submit" :loading="loading" class="w-full">發送重設連結</BaseButton>
        </form>
      </template>
      <template v-else>
        <div class="text-center bg-green-50 p-6 rounded">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 mx-auto text-green-600 mb-3" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1.5">
            <path stroke-linecap="round" stroke-linejoin="round" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
          </svg>
          <p class="text-sm text-brand-charcoal">重設密碼連結已發送至 <strong>{{ email }}</strong></p>
          <p class="text-xs text-brand-gray mt-2">請檢查您的信箱（包括垃圾郵件資料夾）</p>
        </div>
      </template>

      <p class="mt-6 text-center text-sm">
        <router-link to="/login" class="text-brand-gray hover:text-brand-black underline">返回登入</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { forgotPassword } from '@/api/auth'
import BaseInput from '@/components/ui/BaseInput.vue'
import BaseButton from '@/components/ui/BaseButton.vue'

const email = ref('')
const error = ref('')
const loading = ref(false)
const sent = ref(false)

async function handleSubmit () {
  error.value = ''
  if (!email.value) { error.value = '請輸入電子郵件'; return }
  loading.value = true
  try {
    await forgotPassword(email.value)
    sent.value = true
  } catch (e) {
    error.value = e.response?.data?.message || '發送失敗，請稍後再試'
  } finally { loading.value = false }
}
</script>
