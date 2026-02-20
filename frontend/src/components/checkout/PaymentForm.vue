<template>
  <div class="space-y-4">
    <h3 class="section-title">付款方式</h3>
    <div class="space-y-3">
      <label v-for="method in methods" :key="method.value"
        :class="['flex items-center gap-3 p-4 border cursor-pointer transition-colors',
          modelValue === method.value ? 'border-brand-black' : 'border-gray-200 hover:border-gray-400']">
        <input type="radio" :value="method.value" :checked="modelValue === method.value"
          @change="$emit('update:modelValue', method.value)" class="accent-brand-black" />
        <div>
          <p class="text-sm font-medium">{{ method.label }}</p>
          <p class="text-xs text-brand-gray">{{ method.desc }}</p>
        </div>
      </label>
    </div>
  </div>
</template>

<script setup>
defineProps({ modelValue: String })
defineEmits(['update:modelValue'])

const methods = [
  { value: 'credit_card', label: '信用卡', desc: '支援 VISA / Mastercard / JCB' },
  { value: 'atm', label: 'ATM 轉帳', desc: '銀行虛擬帳號轉帳' },
  { value: 'cvs', label: '超商代碼', desc: '7-11 / 全家 / 萊爾富' },
  { value: 'line_pay', label: 'LINE Pay', desc: '使用 LINE Pay 付款' },
]
</script>
