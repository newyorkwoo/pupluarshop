<template>
  <div>
    <label v-if="label" class="text-xs uppercase tracking-wider text-brand-gray mb-1 block">{{ label }}</label>
    <input v-model="model" :type="type" :placeholder="placeholder" :class="['input-field', error ? 'border-brand-error' : '']" v-bind="$attrs" />
    <p v-if="error" class="text-xs text-brand-error mt-1">{{ error }}</p>
  </div>
</template>

<script setup>
const model = defineModel()
defineProps({
  label: String,
  type: { type: String, default: 'text' },
  placeholder: String,
  error: String,
})
</script>
