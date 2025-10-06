<template>
  <div class="bg-light min-vh-100 py-5">
    <div class="container">
      

      <div class="row gx-4 gy-4">
        
        <!-- LEFT: products + forms (2/3) -->
        <div class="col-lg-8">
          <h1 class="mb-4 font-bold text-black">
            TudaBrand — Checkout
          </h1>
          <!-- Products card -->
          <div class="card mb-4 shadow-sm">
            <div class="card-body">
              <h3 class="card-title">Selected Products</h3>
              <div v-if="products.length === 0" class="text-muted">No products selected.</div>

              <div v-for="(item, index) in products" :key="index" class="d-flex align-items-center py-3 border-top" :class="{ 'border-top-0': index === 0 }">
                <img :src="item.imageUrl" alt="img" class="product-img me-3" />
                <div class="flex-grow-1">
                  <div class="d-flex justify-content-between align-items-start">
                    <div>
                      <div class="fw-semibold">{{ item.name }}</div>
                      <div class="text-muted small">{{ (item.price).toLocaleString() }}₫</div>
                    </div>
                    <div class="text-end">
                      <div class="d-flex align-items-center">
                        <button class="btn btn-sm btn-outline-secondary me-2" @click="decreaseQty(index)">−</button>
                        <div class="px-2">{{ item.quantity }}</div>
                        <button class="btn btn-sm btn-outline-secondary ms-2" @click="increaseQty(index)">+</button>
                      </div>
                      <div class="small text-muted mt-1">Subtotal: <span class="fw-bold">{{ (item.price * item.quantity).toLocaleString() }}₫</span></div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Shipping info -->
          <div class="card mb-4 shadow-sm">
            <div class="card-body">
              <h3 class="card-title">Shipping Information</h3>
              <div class="row g-2">
                <div class="col-md-6">
                  <input v-model="customer.firstName" type="text" class="form-control" placeholder="First Name *" />
                </div>
                <div class="col-md-6">
                  <input v-model="customer.lastName" type="text" class="form-control" placeholder="Last Name *" />
                </div>
                <div class="col-12 mt-2">
                  <input v-model="customer.email" type="email" class="form-control" placeholder="Email *" />
                </div>
                <div class="col-12 mt-2">
                  <input v-model="customer.phone" type="tel" class="form-control" placeholder="Phone Number *" />
                </div>
                <div class="col-12 mt-2">
                  <input v-model="customer.company" type="text" class="form-control" placeholder="Company (optional)" />
                </div>
              </div>
            </div>
          </div>

          <!-- Address -->
          <div class="card mb-4 shadow-sm">
            <div class="card-body">
              <h3 class="card-title">Shipping Address</h3>
              <div class="mb-2">
                <input v-model="customer.address" class="form-control" placeholder="Address *" />
              </div>
              <div class="mb-2">
                <input v-model="customer.apartment" class="form-control" placeholder="Apartment / Room (optional)" />
              </div>
              <div class="row g-2">
                <div class="col-md-4">
                  <input v-model="customer.city" class="form-control" placeholder="City *" />
                </div>
                <div class="col-md-4">
                  <input v-model="customer.state" class="form-control" placeholder="State *" />
                </div>
                <div class="col-md-4">
                  <input v-model="customer.zip" class="form-control" placeholder="Postal Code *" />
                </div>
              </div>
              <div class="mt-2">
                <input v-model="customer.country" class="form-control" placeholder="Country *" />
              </div>
            </div>
          </div>

          <!-- Payment -->
          <div class="card mb-4 shadow-sm">
            <div class="card-body">
              <h3 class="card-title">Payment Method</h3>

              <div class="mb-3">
                <div class="form-check">
                  <input class="form-check-input" type="radio" id="payCard" value="card" v-model="payment.method">
                  <label class="form-check-label" for="payCard">Credit/Debit Card</label>
                </div>
                <div class="form-check">
                  <input class="form-check-input" type="radio" id="payPaypal" value="paypal" v-model="payment.method">
                  <label class="form-check-label" for="payPaypal">PayPal</label>
                </div>
                <div class="form-check">
                  <input class="form-check-input" type="radio" id="payCod" value="cod" v-model="payment.method">
                  <label class="form-check-label" for="payCod">Cash on Delivery (COD)</label>
                </div>
              </div>

              <div v-if="payment.method === 'card'" class="mt-3">
                <input v-model="payment.cardName" class="form-control mb-2" placeholder="Name on Card *" />
                <input v-model="payment.cardNumber" class="form-control mb-2" placeholder="Card Number *" />
                <div class="row g-2">
                  <div class="col-6"><input v-model="payment.expiry" class="form-control" placeholder="MM/YY *" /></div>
                  <div class="col-6"><input v-model="payment.cvv" class="form-control" placeholder="CVV *" /></div>
                </div>
              </div>

            </div>
          </div>
        </div>

        <!-- RIGHT: order summary -->
        <div class="col-lg-4">
          <div class="card sticky-top" style="top: 90px;">
            <div class="card-body">
              <h3 class="card-title">Order Summary</h3>
              <ul class="list-unstyled mb-3">
                <li v-for="(it, i) in products" :key="i" class="d-flex justify-content-between small mb-2">
                  <span>{{ it.name }} x{{ it.quantity }}</span>
                  <span>{{ (it.price * it.quantity).toLocaleString() }}₫</span>
                </li>
              </ul>

              <div class="d-flex justify-content-between mb-2">
                <span>Subtotal</span>
                <p>{{ subtotal.toLocaleString() }}₫</p>
              </div>

              <!-- Voucher -->
              <div class="input-group mb-3 mt-3">
                <input v-model="voucherCode" type="text" class="form-control form-control-sm" placeholder="Enter voucher code" />
                <button class="btn btn-outline-primary btn-sm " @click="applyVoucher">Apply</button>
              </div>
              <div v-if="discount > 0" class="d-flex justify-content-between mb-3 mt-3 text-success">
                <span>Discount (Voucher)</span>
                <strong>-{{ discount.toLocaleString() }}₫</strong>
              </div>
              <div v-if="voucherMessage" class="small text-muted mb-3 mt-3">{{ voucherMessage }}</div>
              <!-- End Voucher -->

              <div class="d-flex justify-content-between mb-3 mt-3">
                <span>Shipping</span>
                <span class="text-success">Free</span>
              </div>
              <div class="d-flex justify-content-between fs-5 fw-bold border-top pt-2 mb-3 mt-3">
                <span>Total</span>
                <span class="text-black">{{ total.toLocaleString() }}₫</span>
              </div>

              <router-link to="my-OrderSuccess" class="btn btn-primary">Place Order</router-link>
            </div>
          </div>
        </div>

      </div> <!-- row -->
    </div> <!-- container -->
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import '../assets/css/checkout.css'

const products = ref([])
const customer = ref({
  firstName: '', lastName: '', email: '', phone: '',
  company: '', address: '', apartment: '',
  city: '', state: '', zip: '', country: 'Vietnam'
})

const payment = ref({
  method: 'cod', cardName: '', cardNumber: '', expiry: '', cvv: ''
})

const voucherCode = ref('')
const voucherMessage = ref('')
const discount = ref(0)

onMounted(() => {
  products.value = (JSON.parse(localStorage.getItem('orderProducts')) || []).map(p => ({
    ...p,
    price: Number(p.price) || 0,
    quantity: Number(p.quantity) || 1,
    imageUrl: p.imageUrl || p.image || '/images/placeholder.png'
  }))
})

const subtotal = computed(() =>
    products.value.reduce((s, it) => s + it.price * (it.quantity || 1), 0)
)

const total = computed(() => subtotal.value - discount.value)

function applyVoucher() {
  const code = voucherCode.value.trim().toUpperCase()
  if (code === 'SALE10') {
    discount.value = Math.round(subtotal.value * 0.1)
    voucherMessage.value = '🎉 SALE10 applied: 10% off!'
  } else if (code === 'FREESHIP') {
    discount.value = 20000
    voucherMessage.value = '🚚 FREESHIP applied: 20,000₫ off!'
  } else {
    discount.value = 0
    voucherMessage.value = '❌ Invalid voucher code'
  }
}

function increaseQty(i) { products.value[i].quantity = (products.value[i].quantity || 1) + 1 }

function decreaseQty(i) {
  if ((products.value[i].quantity || 1) > 1) products.value[i].quantity -= 1
}

function placeOrder() {
  if (!customer.value.firstName || !customer.value.lastName || !customer.value.phone || !customer.value.address) {
    alert('❌ Please fill in all required shipping information')
    return
  }
  alert('✅ Order placed successfully!\n' +
      'Customer: ' + customer.value.firstName + ' ' + customer.value.lastName + '\n' +
      'Products: ' + products.value.map(p => `${p.name} x${p.quantity}`).join(', ') + '\n' +
      'Total: ' + total.value.toLocaleString() + '₫' + '\n' +
      'Payment method: ' + payment.value.method)
}
</script>
