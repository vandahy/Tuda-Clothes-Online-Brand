<template>
  <div class="form-container">
    <h2>Product Management - CRUD</h2>

    <!-- Form Inputs -->
    <div class="form-grid">
      <input
        v-model="product.productCode"
        placeholder="Product Code"
        :disabled="isEdit"
      />
      <input v-model="product.name" placeholder="Product Name" />
      <input v-model="product.description" placeholder="Description" />
      <input type="number" v-model.number="product.price" placeholder="Price" />
      <input type="number" v-model.number="product.discount" placeholder="Discount" />
      <input type="number" v-model.number="product.stockQuantity" placeholder="Stock Quantity" />
      <input v-model="product.categoryCode" placeholder="Category Code" />
    </div>

    <!-- Action Buttons -->
    <div class="btn-group">
      <button @click="submitForm">{{ isEdit ? "Update" : "Create" }}</button>
      <button @click="resetForm">Reset</button>
    </div>

    <!-- Product List Table -->
    <h3>Product List</h3>
    <table>
      <thead>
        <tr>
          <th>No</th>
          <th>Product Code</th>
          <th>Name</th>
          <th>Description</th>
          <th>Price</th>
          <th>Discount</th>
          <th>Stock Quantity</th>
          <th>Category Code</th>
          <th>Created At</th>
          <th>Updated At</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(p, i) in products" :key="p.productCode">
          <td>{{ i + 1 }}</td>
          <td>{{ p.productCode }}</td>
          <td>{{ p.name }}</td>
          <td>{{ p.description }}</td>
          <td>{{ p.price }}</td>
          <td>{{ p.discount }}</td>
          <td>{{ p.stockQuantity }}</td>
          <td>{{ p.categoryCode }}</td>
          <td>{{ formatDate(p.createdAt) }}</td>
          <td>{{ formatDate(p.updatedAt) }}</td>
          <td>
            <button @click="editProduct(p)">Edit</button>
            <button @click="deleteProduct(p.productCode)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

// API URL
const API_URL = "http://localhost:8080/api/products";

// State
const products = ref([]);
const product = ref({
  productCode: "",
  name: "",
  description: "",
  price: 0,
  discount: 0,
  stockQuantity: 0,
  categoryCode: ""
});
const isEdit = ref(false);

// Fetch products
const getProducts = async () => {
  try {
    const res = await fetch(API_URL);
    if (!res.ok) throw new Error("Failed to fetch products");
    products.value = await res.json();
  } catch (err) {
    console.error("Error fetching products:", err);
    alert("Error fetching products. Check console.");
  }
};

// Reset form
const resetForm = () => {
  product.value = {
    productCode: "",
    name: "",
    description: "",
    price: 0,
    discount: 0,
    stockQuantity: 0,
    categoryCode: ""
  };
  isEdit.value = false;
};

// Create / Update
const submitForm = async () => {
  try {
    const method = isEdit.value ? "PUT" : "POST";
    const url = isEdit.value
      ? `${API_URL}/${product.value.productCode}`
      : API_URL;

    const res = await fetch(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(product.value)
    });

    if (!res.ok)
      throw new Error(`${isEdit.value ? "Update" : "Create"} failed`);

    alert(
      `${isEdit.value ? "Product updated" : "Product created"} successfully!`
    );
    resetForm();
    getProducts();
  } catch (err) {
    console.error(err);
    alert("Error. Check console.");
  }
};

// Delete product
const deleteProduct = async (productCode) => {
  if (!confirm(`Delete product ${productCode}?`)) return;
  try {
    const res = await fetch(`${API_URL}/${productCode}`, { method: "DELETE" });
    if (!res.ok) throw new Error("Delete failed");
    alert("Product deleted successfully!");
    getProducts();
  } catch (err) {
    console.error(err);
    alert("Error deleting product. Check console.");
  }
};

// Edit product
const editProduct = (p) => {
  product.value = { ...p };
  isEdit.value = true;
};

// Format date
const formatDate = (datetime) => {
  if (!datetime) return "";
  return new Date(datetime).toLocaleString("vi-VN");
};

// Load products on mount
onMounted(() => {
  getProducts();
});
</script>

<style scoped>
.form-container {
  max-width: 1400px; /* tăng chiều rộng container */
  margin: auto;
  padding: 1rem;
  overflow-x: auto; /* cho phép cuộn ngang nếu bảng quá rộng */
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); /* tự co dãn theo màn hình */
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.btn-group {
  margin-bottom: 1rem;
}

table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1200px; /* đảm bảo bảng đủ rộng để không bị quá chật */
}

table, th, td {
  border: 1px solid #ccc;
}

th, td {
  padding: 0.5rem;
  text-align: center;
  word-break: break-word; /* tránh tràn chữ dài */
}

thead {
  background-color: #f5f5f5;
}

button {
  padding: 0.3rem 0.7rem;
  margin: 0 0.2rem;
  cursor: pointer;
}
</style>
