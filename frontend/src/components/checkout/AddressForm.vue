<template>
  <div class="space-y-4">
    <div class="grid grid-cols-2 gap-4">
      <div>
        <label class="text-xs uppercase tracking-wider text-brand-gray mb-1 block">收件人姓名</label>
        <input v-model="form.name" @input="emitUpdate" class="input-field" required />
      </div>
      <div>
        <label class="text-xs uppercase tracking-wider text-brand-gray mb-1 block">電話</label>
        <input v-model="form.phone" @input="emitUpdate" class="input-field" required />
      </div>
    </div>
    <div>
      <label class="text-xs uppercase tracking-wider text-brand-gray mb-1 block">地址</label>
      <input v-model="form.addressLine" @input="emitUpdate" class="input-field" placeholder="街道地址" required />
    </div>
    <div class="grid grid-cols-3 gap-4">
      <div>
        <label class="text-xs uppercase tracking-wider text-brand-gray mb-1 block">城市</label>
        <input v-model="form.city" @input="emitUpdate" class="input-field" required />
      </div>
      <div>
        <label class="text-xs uppercase tracking-wider text-brand-gray mb-1 block">區域</label>
        <input v-model="form.district" @input="emitUpdate" class="input-field" required />
      </div>
      <div>
        <label class="text-xs uppercase tracking-wider text-brand-gray mb-1 block">郵遞區號</label>
        <input v-model="form.zipCode" @input="emitUpdate" class="input-field" required />
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'

const props = defineProps({ modelValue: { type: Object, default: () => ({}) } })
const emit = defineEmits(['update:modelValue'])

const form = reactive({
  name: props.modelValue.name || '',
  phone: props.modelValue.phone || '',
  addressLine: props.modelValue.addressLine || '',
  city: props.modelValue.city || '',
  district: props.modelValue.district || '',
  zipCode: props.modelValue.zipCode || '',
})

function emitUpdate() {
  emit('update:modelValue', { ...form })
}
</script>
