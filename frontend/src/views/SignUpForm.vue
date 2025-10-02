<template>
  <div class="register-container c-black">
    <h2 class="title">Đăng ký</h2>
    <form class="register-form">
      <!-- Họ và tên -->
      <div class="form-group">
        <label>Họ và tên *</label>
        <input type="text" placeholder="Nhập họ tên tại đây" required />
      </div>

      <!-- Số điện thoại -->
      <div class="form-group">
        <label>Số điện thoại *</label>
        <input type="text" placeholder="Nhập số điện thoại tại đây" required />
      </div>

      <!-- Email -->
      <div class="form-group">
        <label>Email *</label>
        <input type="email" placeholder="Thông tin này cần bắt buộc" required />
      </div>

      <!-- Ngày sinh -->
      <div class="form-group">
        <label>Ngày sinh *</label>
        <input type="date" required />
      </div>

      <!-- Giới tính -->
      <div class="form-group">
        <select>
          <option>Nam</option>
          <option>Nữ</option>
          <option>Khác</option>
        </select>
      </div>

      <!-- Mật khẩu -->
      <div class="form-group">
        <label>Mật khẩu *</label>
        <input type="password" placeholder="Mật khẩu" required />
      </div>

      <!-- Nút đăng ký -->
      <button type="submit" class="btn-submit">Đăng ký</button>
      <router-link to="/login" class="move-login c-black">
          Bạn đã có tài khoản? Đăng ký ngay
      </router-link>
    </form>
  </div>
</template>

