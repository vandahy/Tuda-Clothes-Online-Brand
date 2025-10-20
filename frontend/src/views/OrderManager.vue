<template>
  <div class="form-container">
    <h2>Order Management - CRUD</h2>

    <!-- Form Inputs -->
    <div class="form-grid">
      <input
        v-model="order.orderCode"
        placeholder="Order Code"
        :disabled="isEdit"
      />
      <input v-model="order.username" placeholder="Username" />
      <input type="datetime-local" v-model="order.orderDate" placeholder="Order Date" />
      <select v-model="order.status">
        <option value="PENDING">PENDING</option>
        <option value="CONFIRMED">CONFIRMED</option>
        <option value="SHIPPING">SHIPPING</option>
        <option value="COMPLETED">COMPLETED</option>
        <option value="CANCELLED">CANCELLED</option>
      </select>
      <input type="number" v-model.number="order.totalAmount" placeholder="Total Amount" />
      <input v-model="order.shippingAddress" placeholder="Shipping Address" />
    </div>

    <!-- Action Buttons -->
    <div class="btn-group">
      <button @click="submitForm">{{ isEdit ? "Update" : "Create" }}</button>
      <button @click="resetForm">Reset</button>
    </div>

    <!-- Order List Table -->
    <h3>Order List</h3>
    <table>
      <thead>
        <tr>
          <th>No</th>
          <th>Order Code</th>
          <th>Username</th>
          <th>Order Date</th>
          <th>Status</th>
          <th>Total Amount</th>
          <th>Shipping Address</th>
          <th>Created At</th>
          <th>Updated At</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(o, i) in orders" :key="o.orderCode">
          <td>{{ i + 1 }}</td>
          <td>{{ o.orderCode }}</td>
          <td>{{ o.username }}</td>
          <td>{{ formatDate(o.orderDate) }}</td>
          <td>{{ o.status }}</td>
          <td>{{ o.totalAmount }}</td>
          <td>{{ o.shippingAddress }}</td>
          <td>{{ formatDate(o.createdAt) }}</td>
          <td>{{ formatDate(o.updatedAt) }}</td>
          <td>
            <button @click="editOrder(o)">Edit</button>
            <button @click="deleteOrder(o.orderCode)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

// API URL
const API_URL = "http://localhost:8080/api/orders";

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
  order.value = { ...o };
  isEdit.value = true;
};

// Format date
const formatDate = (datetime) => {
  if (!datetime) return "";
  return new Date(datetime).toLocaleString("vi-VN");
};

// Load orders on mount
onMounted(() => {
  getOrders();
});
</script>
