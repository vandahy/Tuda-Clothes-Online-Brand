<template>
  <div class="order-success">
    <!-- Icon check -->
    <div class="icon">
      <svg xmlns="http://www.w3.org/2000/svg" height="48px" viewBox="0 -960 960 960" width="48px" fill="#000000"><path d="M492-80 357-706l-97 461H80v-59h131l112-538h66l135 630 87-383h68l71 291h130v59H704l-58-231-92 396h-62Z"/></svg>
    </div>

    <!-- Title -->
    <h2 class="title">Order Successful!</h2>

    <!-- Message -->
    <p class="message">
      Thank you for your purchase. We will contact you soon to confirm your order.
    </p>

    <!-- Actions -->
    <div class="actions">
      <button
        class="btn btn-outline"
        @click="toggleSidebarCart"
      >
        View Order Details
      </button>

      <router-link to="/" class="btn btn-primary">
        Continue Shopping
      </router-link>
    </div>
  </div>

  <div v-if="orderDetailForm" class="modal fade show d-block" tabindex="-1">
    <div class="modal-dialog modal-xl">
      <div class="modal-content shadow-lg">
        <div class="modal-header ">
          <button type="button" class="btn-close" @click="closeSidebar"></button>
          <h2 class="modal-title fw-bold">Order Details</h2>
        </div>
        <div class="modal-body row">
          <!-- Left: Info + Products -->
          <div class="col-md-8">
            <p>
              Order ID: <a href="#" class="text-decoration-none">{{ orderId }}</a><br>
              {{ orderDate }} | Sales Staff: {{ staffName }} - {{ staffEmail }}
            </p>

            <div class="row g-3">
              <!-- Customer -->
              <div class="col-md-6">
                <div class="border p-3 rounded">
                  <h6 class="fw-bold">CUSTOMER</h6>
                  <p class="mb-1">{{ customerName }}</p>
                  <small>{{ customerPhone }}</small>
                </div>
              </div>
              <!-- Recipient -->
              <div class="col-md-6">
                <div class="border p-3 rounded">
                  <h6 class="fw-bold">RECIPIENT</h6>
                  <p class="mb-1">{{ recipientName }}</p>
                  <small>{{ recipientPhone }}</small><br>
                  <small>{{ recipientAddress }}</small>
                </div>
              </div>
            </div>

            <!-- Product list -->
            <div class="table-responsive mt-4">
              <table class="table table-bordered table-hover">
                <thead class="table-light">
                  <tr>
                    <th>Product Name</th>
                    <th>Quantity</th>
                    <th>Unit Price</th>
                    <th>Total</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(item, i) in products" :key="i">
                    <td>{{ item.name }}</td>
                    <td>{{ item.qty }}</td>
                    <td>{{ item.price }} </td>
                    <td>{{ item.qty * item.price }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <!-- Right: Payment -->
          <div class="col-md-4">
            <div class="border p-3 rounded bg-light">
              <h6 class="fw-bold">PAYMENT METHOD</h6>
              <p>{{ paymentMethod }}</p>

              <hr>
              <p class="text-success">Promotion: {{ promotion }}</p>
              <p class="text-success">Shipping Fee: {{ shippingFee }}</p>
              <p class="text-success">Discount Code: {{ discountCode }}</p>
              <p class="fw-bold">Subtotal: {{ subtotal }}</p>
              <p class="text-danger">Total Amount Due: <strong>{{ total }}</strong></p>
            </div>
          </div>
        </div>

        <!-- Footer buttons -->
        <div class="modal-footer">
          <router-link to="/notes" class="btn btn-secondary">Notes</router-link>
          <router-link to="/edit-order/123" class="btn btn-warning">Edit Order</router-link>
          <router-link to="/cancel-order/123" class="btn btn-danger">Cancel Order</router-link>
          <router-link to="/" class="btn btn-primary">Close</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import '../assets/css/checkout.css'
import { ref } from 'vue'
export default {
  name: "OrderSuccess",
  props: {
    orderId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      orderId: "",
      orderDate: "",
      staffName: "",
      staffEmail: "",
      customerName: "",
      customerPhone: "",
      recipientName: "",
      recipientPhone: "",
      recipientAddress: "",
      products: [], // empty list
      paymentMethod: "",
      promotion: "",
      shippingFee: "",
      discountCode: "",
      subtotal: "",
      total: ""
    }
  },
    methods: {
      closeModal() {
        // Close modal logic here
      }
    },
    setup() {
  
    const orderDetailForm = ref(false)

    const toggleSidebarCart = () => {
      orderDetailForm.value = !orderDetailForm.value
    }
    
    const closeSidebar = () => {
      orderDetailForm.value = false
    }

    return { orderDetailForm, closeSidebar, toggleSidebarCart }

    }
  }
</script>

<style scoped>
.order-success {
  text-align: center;
  margin: 150px auto;
  padding: 40px 20px;
  max-width: 600px;
  border: 1px solid #eee;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.icon {
  font-size: 70px;
  margin-bottom: 15px;
}

.title {
  color: black;
  font-weight: bold;
  margin: 10px 0;
  font-size: 28px;
}

.message {
  margin: 10px 0 30px;
  font-size: 16px;
  color: #555;
}

.actions {
  display: flex;
  justify-content: center;
  gap: 15px;
  flex-wrap: wrap;
}

.btn {
  padding: 12px 22px;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  text-decoration: none;
  transition: all 0.3s ease;
}

.btn-outline {
  border: 1px solid #555;
  background: #fff;
  color: #555;
}

.btn-primary {
  background: black;
  color: #fff;
  border: 1px solid black;
}

</style>
