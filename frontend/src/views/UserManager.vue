<template>
  <div class="form-container">
    <h2>User Management - CRUD</h2>

    <!-- Form Inputs -->
    <div class="form-grid">
      <input v-model="user.username" placeholder="Username" :disabled="isEdit" />
      <input v-model="user.password" type="password" placeholder="Password" />
      <input v-model="user.fullName" placeholder="Full Name" />
      <input v-model="user.email" type="email" placeholder="Email" />
      <input v-model="user.phone" placeholder="Phone" />
      <input v-model="user.address" placeholder="Address" />
      <select v-model="user.role">
        <option value="EMPLOYEE">EMPLOYEE</option>
        <option value="ADMIN">ADMIN</option>
        <option value="FOUNDER">FOUNDER</option>
      </select>
      <select v-model="user.status">
        <option value="ACTIVE">ACTIVE</option>
        <option value="INACTIVE">INACTIVE</option>
      </select>
    </div>

    <!-- Action Buttons -->
    <div class="btn-group">
      <button @click="submitForm">{{ isEdit ? "Update" : "Create" }}</button>
      <button @click="resetForm">Reset</button>
    </div>

    <!-- User List Table -->
    <h3>User List</h3>
    <table>
      <thead>
        <tr>
          <th>No</th>
          <th>Username</th>
          <th>Password</th>
          <th>Full Name</th>
          <th>Email</th>
          <th>Phone</th>
          <th>Address</th>
          <th>Role</th>
          <th>Status</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(u, i) in users" :key="u.username">
          <td>{{ i + 1 }}</td>
          <td>{{ u.username }}</td>
          <td>{{ u.password }}</td>
          <td>{{ u.fullName }}</td>
          <td>{{ u.email }}</td>
          <td>{{ u.phone }}</td>
          <td>{{ u.address }}</td>
          <td>{{ u.role }}</td>
          <td>{{ u.status }}</td>
          <td>
            <button @click="editUser(u)">Edit</button>
            <button @click="deleteUser(u.username)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

// --- API URL ---
const API_URL = "http://localhost:8080/api/users";

// --- State ---
const users = ref([]);
const user = ref({
  username: "",
  password: "",
  fullName: "",
  email: "",
  phone: "",
  address: "",
  role: "EMPLOYEE",
  status: "ACTIVE"
});
const isEdit = ref(false);

// --- Lấy danh sách users ---
const getUsers = async () => {
  try {
    const res = await fetch(API_URL);
    if (!res.ok) throw new Error("Failed to fetch users");
    users.value = await res.json();
  } catch (err) {
    console.error("Error fetching users:", err);
    alert("Error fetching users. Check console.");
  }
};

// --- Reset form ---
const resetForm = () => {
  user.value = {
    username: "",
    password: "",
    fullName: "",
    email: "",
    phone: "",
    address: "",
    role: "EMPLOYEE",
    status: "ACTIVE"
  };
  isEdit.value = false;
};

// --- Create / Update ---
const submitForm = async () => {
  try {
    const method = isEdit.value ? "PUT" : "POST";
    const url = isEdit.value ? `${API_URL}/${user.value.username}` : API_URL;

    const res = await fetch(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(user.value)
    });

    if (!res.ok) throw new Error(`${isEdit.value ? "Update" : "Create"} failed`);

    alert(`${isEdit.value ? "User updated" : "User created"} successfully!`);
    resetForm();
    getUsers();
  } catch (err) {
    console.error(err);
    alert("Error. Check console.");
  }
};

// --- Delete user ---
const deleteUser = async (username) => {
  if (!confirm(`Delete user ${username}?`)) return;
  try {
    const res = await fetch(`${API_URL}/${username}`, { method: "DELETE" });
    if (!res.ok) throw new Error("Delete failed");
    alert("User deleted successfully!");
    getUsers();
  } catch (err) {
    console.error(err);
    alert("Error deleting user. Check console.");
  }
};

// --- Edit user ---
const editUser = (u) => {
  user.value = { ...u };
  isEdit.value = true;
};

// --- Load users khi component mounted ---
onMounted(() => {
  getUsers();
});
</script>


