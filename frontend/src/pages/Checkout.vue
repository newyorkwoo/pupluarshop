<template>
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
    <BaseBreadcrumb :items="[{ label: '首頁', to: '/' }, { label: '結帳' }]" class="mb-8" />
    <h1 class="text-2xl font-light tracking-wider mb-8">結帳</h1>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-10">
      <div class="lg:col-span-2 space-y-8">
        <!-- Step 1: Address -->
        <section>
          <h2 class="text-sm uppercase tracking-[0.15em] font-semibold mb-4">1. 配送資訊</h2>
          <AddressForm v-model="address" />
        </section>

        <!-- Step 2: Payment -->
        <section>
          <h2 class="text-sm uppercase tracking-[0.15em] font-semibold mb-4">2. 付款方式</h2>
          <PaymentForm v-model="paymentMethod" />
        </section>

        <!-- Coupon -->
        <section>
          <h2 class="text-sm uppercase tracking-[0.15em] font-semibold mb-4">折扣碼</h2>
          <div class="flex gap-2">
            <input v-model="couponCode" type="text" placeholder="輸入折扣碼" class="input-field flex-1" />
            <BaseButton variant="secondary" @click="applyCoupon" :loading="couponLoading">套用</BaseButton>
          </div>
          <p v-if="couponError" class="text-red-500 text-xs mt-1">{{ couponError }}</p>
          <p v-if="discount > 0" class="text-green-600 text-xs mt-1">已折抵 {{ formatCurrency(discount) }}</p>
        </section>
      </div>

      <!-- Order Summary -->
      <div>
        <div class="bg-brand-cream p-6 sticky top-24">
          <h2 class="text-sm uppercase tracking-[0.15em] font-semibold mb-4">訂單摘要</h2>
          <OrderReview :items="cartStore.items" />
          <div class="border-t border-brand-gray/20 mt-4 pt-4 space-y-2 text-sm">
            <div class="flex justify-between"><span>小計</span><span>{{ formatCurrency(cartStore.totalAmount) }}</span></div>
            <div class="flex justify-between"><span>運費</span><span>{{ shipping === 0 ? '免運費' : formatCurrency(shipping) }}</span></div>
            <div v-if="discount > 0" class="flex justify-between text-green-600"><span>折扣</span><span>-{{ formatCurrency(discount) }}</span></div>
            <div class="flex justify-between font-semibold text-base border-t border-brand-gray/20 pt-2 mt-2">
              <span>合計</span><span>{{ formatCurrency(total) }}</span>
            </div>
          </div>
          <BaseButton @click="placeOrder" :loading="placing" class="w-full mt-6">確認下單</BaseButton>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cartStore'
import { useOrderStore } from '@/stores/orderStore'
import { validateCoupon } from '@/api/order'
import { formatCurrency } from '@/utils/formatters'
import BaseBreadcrumb from '@/components/ui/BaseBreadcrumb.vue'
import AddressForm from '@/components/checkout/AddressForm.vue'
import PaymentForm from '@/components/checkout/PaymentForm.vue'
import OrderReview from '@/components/checkout/OrderReview.vue'
import BaseButton from '@/components/ui/BaseButton.vue'

const router = useRouter()
const cartStore = useCartStore()
const orderStore = useOrderStore()

const address = ref({ name: '', phone: '', email: '', city: '', district: '', zipCode: '', addressLine: '' })
const paymentMethod = ref('credit_card')
const couponCode = ref('')
const couponLoading = ref(false)
const couponError = ref('')
const discount = ref(0)
const placing = ref(false)

const shipping = computed(() => cartStore.totalAmount >= 5000 ? 0 : 150)
const total = computed(() => Math.max(0, cartStore.totalAmount + shipping.value - discount.value))

async function applyCoupon () {
  couponLoading.value = true
  couponError.value = ''
  try {
    const res = await validateCoupon(couponCode.value)
    discount.value = res.data.discount || 0
  } catch (e) {
    couponError.value = '折扣碼無效或已過期'
    discount.value = 0
  } finally { couponLoading.value = false }
}

async function placeOrder () {
  placing.value = true
  try {
    const order = await orderStore.createOrder({
      address: address.value,
      paymentMethod: paymentMethod.value,
      couponCode: couponCode.value || undefined,
      items: cartStore.items.map(i => ({ productId: i.productId, size: i.size, quantity: i.quantity })),
    })
    cartStore.clearCart()
    router.push({ name: 'OrderConfirmation', params: { id: order.id } })
  } catch { /* handled by store */ }
  finally { placing.value = false }
}
</script>
