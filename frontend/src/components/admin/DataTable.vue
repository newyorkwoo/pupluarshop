<template>
  <div class="bg-white rounded-lg border border-gray-200 overflow-hidden">
    <div v-if="title" class="px-6 py-4 border-b border-gray-100 flex items-center justify-between">
      <h3 class="text-sm font-semibold">{{ title }}</h3>
      <slot name="actions" />
    </div>
    <div class="overflow-x-auto">
      <table class="w-full text-sm">
        <thead class="bg-gray-50 text-xs uppercase tracking-wider text-brand-gray">
          <tr>
            <th v-for="col in columns" :key="col.key" class="px-6 py-3 text-left font-medium">{{ col.label }}</th>
            <th v-if="$slots['row-actions']" class="px-6 py-3 text-right font-medium">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100">
          <tr v-for="row in rows" :key="row.id" class="hover:bg-gray-50 transition-colors">
            <td v-for="col in columns" :key="col.key" class="px-6 py-4">
              <slot :name="`cell-${col.key}`" :row="row" :value="row[col.key]">
                {{ row[col.key] }}
              </slot>
            </td>
            <td v-if="$slots['row-actions']" class="px-6 py-4 text-right">
              <slot name="row-actions" :row="row" />
            </td>
          </tr>
          <tr v-if="rows.length === 0">
            <td :colspan="columns.length + ($slots['row-actions'] ? 1 : 0)" class="px-6 py-12 text-center text-brand-gray">
              暫無資料
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
defineProps({
  title: String,
  columns: { type: Array, required: true },
  rows: { type: Array, default: () => [] },
})
</script>
