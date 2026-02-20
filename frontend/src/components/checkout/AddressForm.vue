<template>
  <form @submit.prevent="$emit('submit', form)" class="space-y-4">
    <div class="grid grid-cols-2 gap-4">
      <div>
        <label class="text-xs uppercase tracking-wider text-brand-gray mb-1 block">收件人姓名</label>
        <input v-model="form.recipient" class="input-field" required />
      </div>
      <div>
        <label class="text-xs uppercase tracking-wider text-brand-gray mb-1 block">電話</label>
        <input v-model="form.phone" class="input-field" required />
      </div>
    </div>
    <div>
      <label class="text-xs uppercase tracking-wider text-brand-gray mb-1 block">地址</label>
      <input v-model="form.addressLine1" class="input-field" placeholder="街道地址" required />
    </div>
    <div>
      <input v-model="form.addressLine2" class="input-field" placeholder="樓層 / 門牌 (選填)" />
    </div>
    <div class="grid grid-cols-3 gap-4">
      <div>
        <label class="text-xs uppercase tracking-wider text-brand-gray mb-1 block">城市</label>
        <input v-model="form.city" class="input-field" required />
      </div>
      <div>
        <label class="text-xs uppercase tracking-wider text-brand-gray mb-1 block">區域</label>
        <input v-model="form.state" class="input-field" />
      </div>
      <div>
        <label class="text-xs uppercase tracking-wider text-brand-gray mb-1 block">郵遞區號</label>
        <input v-model="form.postalCode" class="input-field" required />
      </div>
    </div>
    <button type="submit" class="btn-primary w-full">儲存地址</button>
  </form>
</template>

<script setup>
import { reactive } from 'vue'

const props = defineProps({ initial: { type: Object, default: () => ({}) } })
defineEmits(['submit'])

const form = reactive({
  recipient: props.initial.recipient || '',
  phone: props.initial.phone || '',
  addressLine1: props.initial.addressLine1 || '',
  addressLine2: props.initial.addressLine2 || '',
  city: props.initial.city || '',
  state: props.initial.state || '',
  postalCode: props.initial.postalCode || '',
  country: '台灣',
})
</script>
