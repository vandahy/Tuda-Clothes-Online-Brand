<template>
  <div class="admin-container">
    <div class="form-section card">
      <h2>{{ isEdit ? 'Edit Order Information' : 'Create New Order' }}</h2>
      <div class='form-grid'>
        <input v-model='order.orderCode' placeholder='Order Code' :disabled='isEdit' />
        <input v-model='order.username' placeholder='Username' />
        <input type="datetime-local" v-model="order.orderDate" />
        <select v-model='order.status'>
          <option value='PENDING'>PENDING</option>
          <option value='CONFIRMED'>CONFIRMED</option>
          <option value='SHIPPING'>SHIPPING</option>
          <option value='COMPLETED'>COMPLETED</option>
          <option value='CANCELLED'>CANCELLED</option>
        </select>
        <input type="number" v-model.number='order.totalAmount' placeholder='Total Amount' />
        <input v-model='order.shippingAddress' placeholder='Shipping Address' />
      </div>
      <div class='btn-group'>
        <button class="btn btn-primary" @click='submitForm'>
          {{ isEdit ? "Update Order" : "Create Order" }}
        </button>
        <button class="btn btn-secondary" @click='resetForm'>Reset</button>
      </div>
    </div>

    <div class="table-section card">
      <div class="table-header">
        <h3>Order List</h3>
        <input type="text" v-model="searchQuery" class="search-input" placeholder="Search by Order Code, Username...">
      </div>
      <div class="table-wrapper">
        <table>
          <thead>
            <tr>
              <th class="col-no">No</th>
              <th class="col-code">Order Code</th>
              <th class="col-user">Username</th>
              <th class="col-date">Order Date</th>
              <th class="col-total">Total</th>
              <th class="col-status">Status</th>
              <th class="col-action">Action</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for='(o, i) in filteredOrders' :key='o.orderCode'>
              <td>{{ i + 1 }}</td>
              <td>{{ o.orderCode }}</td>
              <td>{{ o.username }}</td>
              <td>{{ formatDate(o.orderDate) }}</td>
              <td class="text-right">{{ formatPrice(o.totalAmount) }}</td>
              <td class="text-center">
                <span :class="['status', getStatusClass(o.status)]">
                  {{ o.status }}
                </span>
              </td>
              <td class="action-buttons">
                <button class="btn-icon" @click='editOrder(o)' title="Edit Order">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34a.9959.9959 0 00-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"/></svg>
                </button>
                <button class="btn-icon btn-danger-icon" @click='deleteOrder(o.orderCode)' title="Delete Order">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/></svg>
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
// Script không thay đổi, giữ nguyên như cũ
import { ref, onMounted, computed } from "vue";

// API URL
const API_URL = "http://localhost:8080/api/manager/orders";

// State
const orders = ref([]);
const order = ref({
  orderCode: "",
  username: "",
  orderDate: "",
  status: "PENDING",
  totalAmount: 0,
  shippingAddress: ""
});
const isEdit = ref(false);
const searchQuery = ref('');

// Computed: Logic to filter orders
const filteredOrders = computed(() => {
  if (!searchQuery.value) {
    return orders.value;
  }
  const lowerCaseQuery = searchQuery.value.toLowerCase();
  return orders.value.filter(o =>
    (o.orderCode ?? '').toLowerCase().includes(lowerCaseQuery) ||
    (o.username ?? '').toLowerCase().includes(lowerCaseQuery)
  );
});

// Fetch orders
const getOrders = async () => {
  try {
    const res = await fetch(API_URL);
    if (!res.ok) throw new Error("Failed to fetch orders");
    orders.value = await res.json();
  } catch (err) {
    console.error("Error fetching orders:", err);
    alert("Error fetching orders. Check console.");
  }
};

// Reset form
const resetForm = () => {
  order.value = {
    orderCode: "",
    username: "",
    orderDate: "",
    status: "PENDING",
    totalAmount: 0,
    shippingAddress: ""
  };
  isEdit.value = false;
};

// Create / Update
const submitForm = async () => {
  try {
    const method = isEdit.value ? "PUT" : "POST";
    const url = isEdit.value ? `${API_URL}/${order.value.orderCode}` : API_URL;

    const res = await fetch(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(order.value)
    });

    if (!res.ok) throw new Error(`${isEdit.value ? "Update" : "Create"} failed`);

    alert(`${isEdit.value ? "Order updated" : "Order created"} successfully!`);
    resetForm();
    getOrders();
  } catch (err) {
    console.error(err);
    alert("Error. Check console.");
  }
};

// Delete order
const deleteOrder = async (orderCode) => {
  if (!confirm(`Delete order ${orderCode}?`)) return;
  try {
    const res = await fetch(`${API_URL}/${orderCode}`, { method: "DELETE" });
    if (!res.ok) throw new Error("Delete failed");
    alert("Order deleted successfully!");
    getOrders();
  } catch (err) {
    console.error(err);
    alert("Error deleting order. Check console.");
  }
};

// Edit order
const editOrder = (o) => {
  const localDate = o.orderDate ? new Date(o.orderDate).toISOString().slice(0, 16) : "";
  order.value = { ...o, orderDate: localDate };
  isEdit.value = true;

  const formElement = document.querySelector('.form-section');
  if (formElement) {
    formElement.scrollIntoView({ behavior: 'smooth' });
  }
};

// Format date
const formatDate = (datetime) => {
  if (!datetime) return "";
  return new Date(datetime).toLocaleString("vi-VN", {
      day: '2-digit', month: '2-digit', year: 'numeric'
  });
};

// Format price
const formatPrice = (price) => {
    if (price === undefined || price === null) return "";
    return Number(price).toLocaleString("vi-VN", { style: "currency", currency: "VND" });
};

// Get status class for styling
const getStatusClass = (status) => {
    const statusMap = {
        PENDING: 'status-pending',
        CONFIRMED: 'status-confirmed',
        SHIPPING: 'status-shipping',
        COMPLETED: 'status-completed',
        CANCELLED: 'status-cancelled'
    };
    return statusMap[status] || 'status-default';
}

// Load orders on mount
onMounted(() => {
  getOrders();
});
</script>

<style scoped>
/* THAY ĐỔI: Cập nhật CSS đồng bộ */
/* GENERAL LAYOUT */
.admin-container {
  padding: 24px;
  background-color: #f0f2f5;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}
.card {
  background: #fff;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
}
.form-section {
  margin-bottom: 24px;
}

/* TYPOGRAPHY & HEADER */
h2 {
  color: #333;
  margin: 0 0 24px 0;
  font-weight: 600;
}
.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.table-header h3 {
  margin: 0;
  color: #333;
  font-weight: 600;
}
.search-input {
  width: 300px;
  padding: 10px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  font-size: 14px;
  transition: all 0.3s;
}
.search-input:focus {
  outline: none;
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

/* FORM STYLES */
.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}
input, select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  font-size: 14px;
  transition: all 0.3s;
}
input:focus, select:focus {
  outline: none;
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

/* BUTTON STYLES */
.btn-group {
  display: flex;
  gap: 12px;
}
.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}
.btn-primary {
  background-color: #1890ff;
  color: white;
}
.btn-primary:hover {
  background-color: #40a9ff;
}
.btn-secondary {
  background-color: #fff;
  color: #333;
  border: 1px solid #d9d9d9;
}
.btn-secondary:hover {
  border-color: #1890ff;
  color: #1890ff;
}

/* TABLE STYLES */
.table-wrapper {
  overflow-x: auto;
}
table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}
th, td {
  padding: 16px;
  text-align: left;
  border-bottom: 1px solid #f0f0f0;
  vertical-align: middle;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
thead th {
  background-color: #fafafa;
  font-weight: 600;
  color: #000;
  text-transform: uppercase;
  font-size: 12px;
}
tbody tr:hover {
  background-color: #f5f5f5;
}

/* Column Widths for Order Manager */
.col-no { width: 5%; }
.col-code { width: 20%; }
.col-user { width: 20%; }
.col-date { width: 15%; }
.col-total { width: 15%; }
.col-status { width: 15%; text-align: center; }
.col-action { width: 10%; text-align: center; }

.text-right { text-align: right; }
.text-center { text-align: center; }

/* Status Styles */
.status {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 16px;
  font-weight: 600;
  font-size: 12px;
  color: white;
  text-transform: uppercase;
}
.status-pending { background-color: #faad14; }
.status-confirmed { background-color: #1890ff; }
.status-shipping { background-color: #13c2c2; }
.status-completed { background-color: #52c41a; }
.status-cancelled { background-color: #f5222d; }
.status-default { background-color: #bfbfbf; }

/* Icon Button Styles */
.action-buttons {
  display: flex;
  gap: 12px;
  justify-content: center;
}
.btn-icon {
    background: transparent;
    border: none;
    cursor: pointer;
    padding: 6px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: background-color 0.2s;
}
.btn-icon:hover {
    background-color: #e9ecef;
}
.btn-icon svg {
    width: 20px;
    height: 20px;
    fill: #6c757d;
    transition: fill 0.2s;
}
.btn-icon:hover svg {
    fill: #1890ff;
}
.btn-icon.btn-danger-icon:hover svg {
    fill: #f5222d;
}
</style>