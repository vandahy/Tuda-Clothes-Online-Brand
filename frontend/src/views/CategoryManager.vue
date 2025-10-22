<template>
  <div class="admin-container">
    <div class="form-section card">
      <h2>{{ isEdit ? 'Edit Category Information' : 'Create New Category' }}</h2>
      <div class='form-grid'>
        <input v-model='category.categoryCode' placeholder='Category Code' :disabled='isEdit' />
        <input v-model='category.name' placeholder='Category Name' />
        <input v-model='category.description' placeholder='Description' />
        <select v-model='category.status'>
          <option value='ACTIVE'>ACTIVE</option>
          <option value='INACTIVE'>INACTIVE</option>
        </select>
      </div>
      <div class='btn-group'>
        <button class="btn btn-primary" @click='submitForm'>
          {{ isEdit ? "Update Category" : "Create Category" }}
        </button>
        <button class="btn btn-secondary" @click='resetForm'>Reset</button>
      </div>
    </div>

    <div class="table-section card">
      <div class="table-header">
        <h3>Category List</h3>
        <input type="text" v-model="searchQuery" class="search-input" placeholder="Search by code, name, description...">
      </div>
      <div class="table-wrapper">
        <table>
          <thead>
            <tr>
              <th class="col-no">No</th>
              <th class="col-code">Code</th>
              <th class="col-name">Name</th>
              <th class="col-desc">Description</th>
              <th class="col-status">Status</th>
              <th class="col-date">Created At</th>
              <th class="col-date">Updated At</th>
              <th class="col-action">Action</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for='(c, i) in filteredCategories' :key='c.categoryCode'>
              <td>{{ i + 1 }}</td>
              <td>{{ c.categoryCode }}</td>
              <td>{{ c.name }}</td>
              <td>{{ c.description }}</td>
              <td class="text-center">
                <span :class="['status', c.status === 'ACTIVE' ? 'status-active' : 'status-inactive']">
                  {{ c.status }}
                </span>
              </td>
              <td>{{ formatDate(c.createdAt) }}</td>
              <td>{{ formatDate(c.updatedAt) }}</td>
              <td class="action-buttons">
                <button class="btn-icon" @click='editCategory(c)' title="Edit Category">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34a.9959.9959 0 00-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"/></svg>
                </button>
                <button class="btn-icon btn-danger-icon" @click='deleteCategory(c.categoryCode)' title="Delete Category">
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
const searchQuery = ref('');

// --- Computed: Logic lọc dữ liệu ---
const filteredCategories = computed(() => {
  if (!searchQuery.value) {
    return categories.value;
  }
  const lowerCaseQuery = searchQuery.value.toLowerCase();
  return categories.value.filter(c =>
    (c.categoryCode ?? '').toLowerCase().includes(lowerCaseQuery) ||
    (c.name ?? '').toLowerCase().includes(lowerCaseQuery) ||
    (c.description ?? '').toLowerCase().includes(lowerCaseQuery)
  );
});


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
  const formElement = document.querySelector('.form-section');
  if (formElement) {
    formElement.scrollIntoView({ behavior: 'smooth' });
  }
};

// --- Định dạng ngày ---
const formatDate = (datetime) => {
  if (!datetime) return "";
  return new Date(datetime).toLocaleString("vi-VN", {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
  });
};

// --- Load categories khi component mounted ---
onMounted(() => {
  getCategories();
});
</script>

<style scoped>
/* SỬA LỖI: Cập nhật lại toàn bộ CSS */
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
/* Column Widths for Category Manager */
.col-no { width: 5%; }
.col-code { width: 15%; }
.col-name { width: 20%; }
.col-desc { width: 20%; }
.col-status { width: 10%; text-align: center; }
.col-date { width: 10%; }
.col-action { width: 10%; text-align: center; }

.text-center {
  text-align: center;
}
.status {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 16px;
  font-weight: 600;
  font-size: 12px;
  color: white;
  text-transform: uppercase;
}
.status-active {
  background-color: #52c41a;
}
.status-inactive {
  background-color: #bfbfbf;
}

/* === SỬA LỖI: CSS CHO NÚT ICON === */
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