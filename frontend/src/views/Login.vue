<template>
  <!-- Layout căn giữa form đăng nhập/đăng ký trên màn hình -->
  <v-row class="d-flex justify-center align-center fill-height" style="min-height: 100vh">
    <v-col cols="12" md="6">
      <v-card class="py-6">
        <!-- Tiêu đề động: Login hoặc Register -->
        <v-card-title class="d-flex justify-center">
          <div class="text-h4">{{ isRegister ? 'Register' : 'Login' }}</div>
        </v-card-title>
        <v-card-text>
          <!-- Nếu chưa đăng ký, hiển thị form đăng nhập -->
          <template v-if="!isRegister">
            <v-text-field label="Username" outlined v-model="username"></v-text-field>
            <v-text-field label="Password" outlined type="password" v-model="password"></v-text-field>
            <div class="text-right">
              <!-- Nút đăng nhập, gọi hàm handleLogin -->
              <v-btn color="primary" @click="handleLogin">Login</v-btn>
            </div>
            <div class="subtitle">
              Don't have an account?
              <!-- Chuyển sang form đăng ký khi bấm Create Account -->
              <span class="subtitle-action" @click="isRegister = true">Create Account</span>
            </div>
          </template>
          <!-- Nếu đang ở form đăng ký -->
          <template v-else>
            <v-text-field label="Username" outlined v-model="regUsername"></v-text-field>
            <v-text-field label="Email" outlined v-model="regEmail"></v-text-field>
            <v-text-field label="Password" outlined type="password" v-model="regPassword"></v-text-field>
            <v-text-field label="Confirm Password" outlined type="password" v-model="regConfirm"></v-text-field>
            <div class="text-right">
              <!-- Nút đăng ký, gọi hàm handleRegister -->
              <v-btn color="primary" @click="handleRegister">Register</v-btn>
              <!-- Nút quay lại form đăng nhập -->
              <v-btn text @click="isRegister = false">Back</v-btn>
            </div>
          </template>
          <!-- Hiển thị lỗi nếu có -->
          <div v-if="error" style="color: red; margin-top: 10px;">
            {{ error }}
          </div>
        </v-card-text>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
export default {
  data() {
    return {
      // Biến cho form đăng nhập
      username: '',    // Lưu username nhập vào
      password: '',    // Lưu password nhập vào
      // Biến cho form đăng ký
      regUsername: '', // Lưu username đăng ký
      regEmail: '',    // Lưu email đăng ký
      regPassword: '', // Lưu password đăng ký
      regConfirm: '',  // Lưu xác nhận password đăng ký
      // Trạng thái chuyển đổi giữa login/register
      isRegister: false, // true: hiện form đăng ký, false: hiện form đăng nhập
      error: ''          // Lưu thông báo lỗi
    }
  },
  methods: {
    // Hàm xử lý đăng nhập
    async handleLogin() {
      try {
        // Gửi yêu cầu GET tới API để kiểm tra tài khoản
        const response = await fetch(`http://localhost:3000/accounts?username=${this.username}&password=${this.password}`)
        const data = await response.json()
        // Nếu tìm thấy tài khoản (data.length > 0) thì đăng nhập thành công
        if (data.length > 0) {
          this.error = '';
          alert('Đăng nhập thành công!');
          localStorage.setItem('user', JSON.stringify(data[0])); // Lưu thông tin user vào localStorage
          this.$router.push('/'); // Chuyển về trang chủ
        } else {
          // Nếu không tìm thấy, báo lỗi
          this.error = 'Sai username hoặc mật khẩu!';
        }
      } catch (err) {
        // Nếu có lỗi kết nối server, báo lỗi
        this.error = 'Lỗi kết nối server!';
      }
    },
    // Hàm xử lý đăng ký
    async handleRegister() {
      // Kiểm tra dữ liệu nhập vào có đủ không
      if (!this.regUsername || !this.regEmail || !this.regPassword || !this.regConfirm) {
        this.error = 'Vui lòng điền đầy đủ thông tin!';
        return;
      }
      // Kiểm tra xác nhận mật khẩu
      if (this.regPassword !== this.regConfirm) {
        this.error = 'Mật khẩu xác nhận không khớp!';
        return;
      }
      try {
        // Lấy danh sách tài khoản hiện tại
        const res = await fetch('http://localhost:3000/accounts');
        const accounts = await res.json();
        // Kiểm tra trùng username
        if (accounts.some(acc => acc.username === this.regUsername)) {
          this.error = 'Username đã tồn tại!';
          return;
        }
        // Kiểm tra trùng email
        if (accounts.some(acc => acc.email === this.regEmail)) {
          this.error = 'Email đã tồn tại!';
          return;
        }
        // Tìm id lớn nhất để tự động tăng id cho tài khoản mới
        const maxId = accounts.length > 0 ? Math.max(...accounts.map(acc => Number(acc.id))) : 0;
        // Tạo tài khoản mới
        const newAccount = {
          id: maxId + 1,
          username: this.regUsername,
          password: this.regPassword,
          email: this.regEmail
        };
        // Gửi POST để thêm tài khoản mới vào db.json qua json-server
        const response = await fetch('http://localhost:3000/accounts', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(newAccount)
        });
        // Nếu đăng ký thành công
        if (response.ok) {
          alert('Đăng ký thành công!');
          this.isRegister = false; // Quay lại form đăng nhập
          this.error = '';
          // Reset form đăng ký
          this.regUsername = '';
          this.regEmail = '';
          this.regPassword = '';
          this.regConfirm = '';
        } else {
          // Nếu đăng ký thất bại
          this.error = 'Đăng ký thất bại!';
        }
      } catch (err) {
        // Nếu có lỗi kết nối server
        this.error = 'Lỗi kết nối server!';
      }
    }
  }
}
</script>