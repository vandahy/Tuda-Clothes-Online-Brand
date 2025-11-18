<template>
  <div class="register-container c-black">
    <h2 class="title">Login</h2>
    <form class="register-form" @submit.prevent="login">
      <!-- Họ và tên -->
      <div class="form-group">
        <label>Email Or Phone Number *</label>
        <input type="text" v-model="username" placeholder="Emai or Phone Number" required />
      </div>

      <!-- Mật khẩu -->
<div class="form-group">
        <label>Password *</label>
        <input type="password" v-model="password" placeholder="Password" required />
      </div>
      <label class="t-bold">Forgot Password</label>

      <!-- Nút đăng ký -->
      <button type="submit" class="btn-submit">Login</button>
      <router-link to="/signup" class="move-sign-up c-black">
          Don’t have an account? Sign up now
      </router-link>
    </form>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </div>
</template>
<script>
import api from "@/utils/api.js";

export default {
  data() {
    return {
      username: "",
      password: "",
      errorMessage: ""
    };
  },
  methods: {
    async login() {
      try {
        const res = await api.post("/api/login", {
          username: this.username,
          password: this.password
        });
        if (res.data.success) {
          alert("Login thành công!");
          localStorage.setItem("userLoggedIn", "true"); 
          localStorage.setItem("username", res.data.username || this.username);
          localStorage.setItem("token", res.data.token || ""); 
          // redirect tới trang home
          this.$router.push("/");
        } else {
          this.errorMessage = res.data.message;
        }
      } catch (err) {
        this.errorMessage = "Lỗi server, thử lại sau";
      }
    }
  }
};
</script>
