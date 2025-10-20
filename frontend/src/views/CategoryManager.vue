<template>
  <div class="form-container">
    <h2>Category Management - CRUD</h2>

    <!-- Form Inputs -->
    <div class="form-grid">
      <input
        v-model="category.categoryCode"
        placeholder="Category Code"
        :disabled="isEdit"
      />
      <input v-model="category.name" placeholder="Category Name" />
      <input v-model="category.description" placeholder="Description" />
      <select v-model="category.status">
        <option value="ACTIVE">ACTIVE</option>
        <option value="INACTIVE">INACTIVE</option>
      </select>
    </div>

    <!-- Action Buttons -->
    <div class="btn-group">
      <button @click="submitForm">{{ isEdit ? "Update" : "Create" }}</button>
      <button @click="resetForm">Reset</button>
    </div>

    <!-- Category List Table -->
    <h3>Category List</h3>
    <table>
      <thead>
        <tr>
          <th>No</th>
          <th>Category Code</th>
          <th>Name</th>
          <th>Description</th>
          <th>Status</th>
          <th>Created At</th>
          <th>Updated At</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(c, i) in categories" :key="c.categoryCode">
          <td>{{ i + 1 }}</td>
          <td>{{ c.categoryCode }}</td>
          <td>{{ c.name }}</td>
          <td>{{ c.description }}</td>
          <td>{{ c.status }}</td>
          <td>{{ formatDate(c.createdAt) }}</td>
          <td>{{ formatDate(c.updatedAt) }}</td>
          <td>
            <button @click="editCategory(c)">Edit</button>
            <button @click="deleteCategory(c.categoryCode)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

// --- API URL ---
const API_URL = "http://localhost:8080/api/categories";

// --- State ---
const categories = ref([]);
const category = ref({
  categoryCode: "",
  name: "",
  description: "",
  status: "ACTIVE"
});
const isEdit = ref(false);

// --- Fetch danh sách categories ---
const getCategories = async () => {
  try {
    const res = await fetch(API_URL);
    if (!res.ok) throw new Error("Failed to fetch categories");
    categories.value = await res.json();
  } catch (err) {
    console.error("Error fetching categories:", err);
    alert("Error fetching categories. Check console.");
  }
};

// --- Reset form ---
const resetForm = () => {
  category.value = {
    categoryCode: "",
    name: "",
    description: "",
    status: "ACTIVE"
  };
  isEdit.value = false;
};

// --- Create / Update ---
const submitForm = async () => {
  try {
    const method = isEdit.value ? "PUT" : "POST";
    const url = isEdit.value
      ? `${API_URL}/${category.value.categoryCode}`
      : API_URL;

    const res = await fetch(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(category.value)
    });

    if (!res.ok)
      throw new Error(`${isEdit.value ? "Update" : "Create"} failed`);

    alert(
      `${isEdit.value ? "Category updated" : "Category created"} successfully!`
    );
    resetForm();
    getCategories();
  } catch (err) {
    console.error(err);
    alert("Error. Check console.");
  }
};

// --- Delete category ---
const deleteCategory = async (categoryCode) => {
  if (!confirm(`Delete category ${categoryCode}?`)) return;
  try {
    const res = await fetch(`${API_URL}/${categoryCode}`, { method: "DELETE" });
    if (!res.ok) throw new Error("Delete failed");
    alert("Category deleted successfully!");
    getCategories();
  } catch (err) {
    console.error(err);
    alert("Error deleting category. Check console.");
  }
};

// --- Edit category ---
const editCategory = (c) => {
  category.value = { ...c };
  isEdit.value = true;
};

// --- Định dạng ngày ---
const formatDate = (datetime) => {
  if (!datetime) return "";
  return new Date(datetime).toLocaleString("vi-VN");
};

// --- Load categories khi component mounted ---
onMounted(() => {
  getCategories();
});
</script>
