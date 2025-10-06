<template>
  <div class="modal fade show d-block" tabindex="-1">
    <div class="modal-dialog modal-xl">
      <div class="modal-content shadow-lg">
        <div class="modal-header ">
          <button type="button" class="btn-close"></button>
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
export default {
  name: "OrderDetail",
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
  }
}
</script>
