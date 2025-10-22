<template>
  <div v-if="isVisible" class="modal-backdrop">
    <div class="modal-dialog modal-xl modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h2 class="modal-title">Order Details: {{ order?.orderCode || 'N/A' }}</h2>
          <button type="button" class="btn-close" @click="$emit('close')"></button>
        </div>
        <div class="modal-body">
          <div class="order-meta">
            Order Date: {{ order?.orderDate ? new Date(order.orderDate).toLocaleString('en-US') : 'N/A' }}
          </div>

          <div class="info-row">

            <div class="info-box payment-summary"> 
              <h6>Payment</h6>
              <p class="payment-method">{{ paymentMethod || 'N/A' }}</p>
              <hr>
              <div class="summary-row">
                <span>Subtotal (products):</span>
                <span class="amount">{{ subtotal.toLocaleString() }}₫</span>
              </div>
              <div class="summary-row shipping">
                <span>Shipping Fee:</span>
                <span class="amount">{{ shippingFee.toLocaleString() }}₫</span>
              </div>
              <div class="summary-row discount">
                <span>Discount (voucher):</span>
                <span class="amount">-{{ discountAmount.toLocaleString() }}₫</span>
              </div>
              <hr class="total-hr">
              <div class="summary-row total">
                <span class="total-label">Total:</span>
                <span class="total-amount">{{ total.toLocaleString() }}₫</span>
              </div>
            </div>
            <div class="info-box recipient-info">
              <h6>Recipient</h6>
              <p class="name">{{ recipientName || 'N/A' }}</p>
              <p class="phone">{{ recipientPhone || 'N/A' }}</p>
              <p class="address">{{ recipientAddress || 'N/A' }}</p>
            </div>
          </div>

          <div class="table-container">
            <table class="product-table">
              <thead>
                <tr>
                  <th class="product-col">Product</th>
                  <th class="qty-col">Quantity</th>
                  <th class="price-col">Unit Price</th>
                  <th class="price-col">Discount</th>
                  <th class="price-col subtotal-col">Subtotal</th>
                </tr>
              </thead>
              <tbody class="table-scroll-body">
                  <tr v-if="!products || products.length === 0">
                     <td colspan="5" class="no-products">No products found for this order.</td>
                  </tr>
                  <tr v-else v-for="(item, i) in products" :key="item.productCode || i">
                    <td class="product-details">
                      <div class="product-name">{{ item.productName || 'N/A' }}</div>
                      <div class="product-code">Code: {{ item.productCode || 'N/A' }}</div>
                    </td>
                    <td class="qty">{{ item.quantity || 0 }}</td>
                    <td class="price">{{ (item.unitPrice || 0).toLocaleString() }}₫</td>
                    <td class="price discount">{{ (item.discount || 0).toLocaleString() }}₫</td>
                    <td class="price subtotal">{{ (item.subTotal || 0).toLocaleString() }}₫</td>
                  </tr>
              </tbody>
            </table>
          </div>

        </div>
        <div class="modal-footer">
          <button type="button" class="btn-close-footer" @click="$emit('close')">Close</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// Props và Emits không đổi
const props = defineProps({
  isVisible: { type: Boolean, required: true },
  orderId: { type: String, default: "" },
  orderDate: { type: String, default: "" },
  staffName: { type: String, default: "" },
  staffEmail: { type: String, default: "" },
  customerName: { type: String, default: "" }, // Prop này vẫn còn nhưng không dùng trong template
  customerPhone: { type: String, default: "" }, // Prop này vẫn còn nhưng không dùng trong template
  recipientName: { type: String, default: "" },
  recipientPhone: { type: String, default: "" },
  recipientAddress: { type: String, default: "" },
  products: { type: Array, default: () => [] },
  paymentMethod: { type: String, default: "" },
  shippingFee: { type: Number, default: 0 },
  discountAmount: { type: Number, default: 0 },
  subtotal: { type: Number, default: 0 },
  total: { type: Number, default: 0 }
})
const emit = defineEmits(['close'])
</script>

<style scoped>
/* --- 1. CORE MODAL STYLES (Giữ nguyên) --- */
.modal-backdrop {
  position: fixed; top: 0; left: 0; width: 100%; height: 100%;
  background: rgba(0, 0, 0, 0.6); display: flex; align-items: center;
  justify-content: center; z-index: 1050; padding: 1rem;
  overflow-y: auto;
}
.modal-dialog {
  background-color: #fff; border-radius: 6px; box-shadow: 0 5px 20px rgba(0, 0, 0, 0.25);
  margin: auto; position: relative; width: 100%;
  max-width: 800px; /* Reduced from 900px */
  display: flex; flex-direction: column;
}
.modal-dialog.modal-dialog-centered {
  display: flex; align-items: center; min-height: calc(100% - 3.5rem);
}
.modal-content {
  display: flex; flex-direction: column; width: 100%;
  background-color: #fff; border-radius: 6px; outline: 0;
}
.modal-header {
  display: flex; justify-content: space-between; align-items: center;
  padding: 0.875rem 1.25rem; border-bottom: 1px solid #e9ecef;
  background-color: #212529; color: #fff;
  border-top-left-radius: 5px; border-top-right-radius: 5px;
}
.modal-title { font-size: 1rem; font-weight: 600; margin: 0; }
.btn-close { /* Style nút X */
  padding: 0.5rem; margin: -0.5rem -0.5rem -0.5rem auto;
  background: transparent url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16' fill='%23fff'%3e%3cpath d='M.293.293a1 1 0 0 1 1.414 0L8 6.586 14.293.293a1 1 0 1 1 1.414 1.414L9.414 8l6.293 6.293a1 1 0 0 1-1.414 1.414L8 9.414l-6.293 6.293a1 1 0 0 1-1.414-1.414L6.586 8 .293 1.707a1 1 0 0 1 0-1.414z'/%3e%3c/svg%3e") center/1em auto no-repeat;
  border: 0; border-radius: .25rem; opacity: .7; cursor: pointer; appearance: none;
}
.btn-close:hover { opacity: 1; }
.modal-body {
  padding: 1.25rem; overflow-y: auto; font-family: Arial, sans-serif;
  font-size: 0.875rem; color: #333;
}
.order-meta {
  font-size: 0.8rem; color: #6c757d; margin-bottom: 1rem;
  padding-bottom: 0.5rem; border-bottom: 1px dashed #ccc;
}
.modal-footer {
  display: flex; justify-content: flex-end; padding: 0.875rem 1.25rem;
  background-color: #f8f9fa; border-top: 1px solid #e9ecef;
  border-bottom-left-radius: 5px; border-bottom-right-radius: 5px;
}
.btn-close-footer { /* Style nút Close */
  display: inline-block; font-weight: 400; line-height: 1.5; color: #fff;
  text-align: center; text-decoration: none; vertical-align: middle; cursor: pointer;
  user-select: none; background-color: #6c757d; border: 1px solid #6c757d;
  padding: .4rem 0.85rem; font-size: 0.85rem; border-radius: .25rem;
  transition: all .15s ease-in-out;
}
.btn-close-footer:hover { background-color: #5a6268; border-color: #545b62; }

/* --- 2. Info Boxes (Recipient & Payment side-by-side) --- */
.info-row { display: flex; gap: 1rem; margin-bottom: 1rem; }
.info-box {
  flex: 1; /* Each box takes half space */
  padding: 0.875rem; border: 1px solid #e0e0e0; border-radius: 4px;
  background-color: #f8f9fa; /* Light background for both */
}
.info-box h6 { font-weight: 600; font-size: 0.75rem; text-transform: uppercase; margin-bottom: 0.5rem; color: #555; letter-spacing: 0.5px; }
.info-box p { margin-bottom: 0.2rem; line-height: 1.4; font-size: 0.85rem;}
.info-box .name { font-weight: 600; color: #000; }
.info-box .phone, .info-box .address { color: #444; }
.d-block { display: block !important; }

/* Payment specific styles within info-box */
.payment-summary .payment-method { font-weight: 600; margin-bottom: 0.5rem; font-size: 0.9rem; }
.payment-summary hr { margin: 0.5rem 0; border-top: 1px solid #ccc; }
.payment-summary .total-hr { border-top: 1px dashed #999; }
.summary-row { display: flex; justify-content: space-between; margin-bottom: 0.3rem; font-size: 0.85rem; }
.summary-row span { color: #444; }
.summary-row .amount { font-weight: 600; color: #000; white-space: nowrap;}
.summary-row.shipping .amount { color: #198754; }
.summary-row.discount .amount { color: #dc3545; }
.summary-row.total { margin-top: 0.5rem; font-size: 1rem; align-items: baseline; }
.summary-row.total .total-label { font-weight: 700; text-transform: uppercase; font-size: 0.85rem; }
.summary-row.total .total-amount { font-weight: 700; color: #dc3545; font-size: 1rem; }


/* --- 3. Product Table (Full Width) --- */
.table-container { margin-bottom: 0; } /* Remove bottom margin if payment is above footer */
.product-table { width: 100%; border-collapse: collapse; font-size: 0.8rem; border: 1px solid #dee2e6; border-radius: 4px; overflow: hidden; }
.product-table thead { display: table; width: 100%; table-layout: fixed; }
.product-table thead tr { display: table; width: 100%; }
.product-table thead th { background-color: #f8f9fa; font-weight: 600; color: #333; border-bottom: 1px solid #dee2e6; padding: 0.6rem 0.75rem; text-align: left; }
.product-table tbody { display: block; max-height: 200px; overflow-y: auto; width: 100%; }
.product-table tbody tr { display: table; width: 100%; table-layout: fixed; }
.product-table tbody td { padding: 0.6rem 0.75rem; vertical-align: middle; border-bottom: 1px solid #dee2e6; text-align: left; }
.product-table tbody tr:last-child td { border-bottom: none; }

/* Column widths - Adjust these carefully */
.product-table .product-col { width: 20%; } /* Equal width */
.product-table .qty-col { width: 20%; text-align: center; }
.product-table .price-col { width: 20%; text-align: right; white-space: nowrap;} 
.product-table .subtotal-col { width: 20%;} 

.product-table .product-details { line-height: 1.3; word-break: break-word; width: 20%; }
.product-table .product-name { font-weight: 600; color: #000; font-size: 0.85rem; }
.product-table .product-code { font-size: 0.75rem; color: #6c757d; }
.product-table .qty { text-align: center; width: 20%; }
.product-table .price { text-align: right; width: 20%; }
.product-table .discount { color: #dc3545; width: 20%; }
.product-table .subtotal { text-align: right; font-weight: 600; width: 20%; }
.product-table .no-products { text-align: center; color: #6c757d; padding: 1.5rem; }

/* --- 4. SCROLLABLE TBODY --- */
.table-scroll-body { display: table-row-group; }

/* --- 5. Payment Summary Container (Removed - Integrated into info-box) --- */
/* .payment-summary-container { ... } /* REMOVE THIS */

/* --- 6. General Text Styles (Giữ nguyên) --- */
.fw-semibold { font-weight: 600 !important; }
.fw-bold { font-weight: 700 !important; }
.text-muted { color: #6c757d !important; }
.text-success { color: #198754 !important; }
.text-danger { color: #dc3545 !important; }
.text-uppercase { text-transform: uppercase !important; }
.fs-5 { font-size: 1.25rem !important; }

</style>