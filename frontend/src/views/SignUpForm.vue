<template>
  <div class="register-container c-black">
    <h2 class="title">Đăng ký</h2>
    <form class="register-form" @submit.prevent="logup">
      <!-- Họ và tên -->
      <div class="form-group">
        <label>Họ và tên *</label>
        <input type="text" v-model="fullName" placeholder="Nhập họ tên tại đây" required />
      </div>

      <!-- Số điện thoại -->
      <div class="form-group">
        <label>Số điện thoại *</label>
        <input type="text" v-model="phone" placeholder="Nhập số điện thoại tại đây" required />
      </div>

      <!-- Email -->
      <div class="form-group">
        <label>Email *</label>
        <input type="email" v-model="email" placeholder="Thông tin này cần bắt buộc" required />
      </div>

      <!-- Mật khẩu -->
      <div class="form-group">
        <label>Mật khẩu *</label>
        <input type="password" v-model="password" placeholder="Mật khẩu" required />
      </div>

      <!-- Nút đăng ký -->
      <button type="submit" class="btn-submit">Đăng ký</button>
      <router-link to="/login" class="move-login c-black">
          Bạn đã có tài khoản? Đăng nhập ngay
      </router-link>
    </form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      fullName: "",
      phone: "",
      email: "",
      password: "",
      errorMessage: ""
    };
  },
  methods: {
    async logup() {
      try {
        const res = await axios.post("/api/signup", {
          fullName: this.fullName,
          phone: this.phone,
          email: this.email,
          password: this.password
        });
        if (res.data.success) {
          alert("Sign up thành công!");
          // redirect tới trang home
          this.$router.push("/login");
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

