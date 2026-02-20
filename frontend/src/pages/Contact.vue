<template>
  <div class="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
    <h1 class="text-2xl font-light tracking-wider mb-8">聯繫我們</h1>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-12">
      <div>
        <h2 class="text-sm uppercase tracking-[0.15em] font-semibold mb-4">客服資訊</h2>
        <div class="space-y-3 text-sm text-brand-charcoal">
          <p><strong>電子郵件：</strong>support@popularshop.com</p>
          <p><strong>客服電話：</strong>02-2345-6789</p>
          <p><strong>營業時間：</strong>週一至週五 10:00 - 18:00</p>
          <p><strong>地址：</strong>台北市信義區松仁路 100 號 8 樓</p>
        </div>
      </div>

      <div>
        <h2 class="text-sm uppercase tracking-[0.15em] font-semibold mb-4">留言給我們</h2>
        <form @submit.prevent="handleSubmit" class="space-y-4">
          <BaseInput label="姓名" v-model="form.name" required />
          <BaseInput label="電子郵件" type="email" v-model="form.email" required />
          <div>
            <label class="block text-xs uppercase tracking-wider text-brand-charcoal mb-1">訊息內容</label>
            <textarea v-model="form.message" rows="5" required class="input-field w-full resize-none"></textarea>
          </div>
          <BaseButton type="submit" :loading="sending">送出</BaseButton>
        </form>
        <p v-if="sent" class="text-green-600 text-sm mt-2">訊息已送出，我們將盡快回覆您！</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import BaseInput from '@/components/ui/BaseInput.vue'
import BaseButton from '@/components/ui/BaseButton.vue'

const form = reactive({ name: '', email: '', message: '' })
const sending = ref(false)
const sent = ref(false)

async function handleSubmit () {
  sending.value = true
  try {
    // await api call
    await new Promise(r => setTimeout(r, 800))
    sent.value = true
    form.name = ''; form.email = ''; form.message = ''
  } finally { sending.value = false }
}
</script>
