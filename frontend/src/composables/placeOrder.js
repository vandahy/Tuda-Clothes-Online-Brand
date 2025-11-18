// src/composables/usePlaceOrder.js
import { ref } from 'vue'
import { useRouter } from 'vue-router'

export function usePlaceOrder(customer, payment, cartCode, selectedProvinceCode, selectedWardCode, provinces, wards) {

    const router = useRouter();
    const isPlacingOrder = ref(false);

    const placeOrder = async () => {
        // 1. Validation
        if (!customer.value.firstName || !customer.value.phone || !customer.value.address || !selectedProvinceCode.value || !selectedWardCode.value) {
            alert('Vui lòng điền đầy đủ thông tin địa chỉ (Tên, SĐT, Số nhà, Tỉnh/Thành phố, Phường/Xã)');
            return;
        }

        // 2. Lookup tên tỉnh/phường từ code
        const selectedProvince = provinces.value.find(p => p.Code === selectedProvinceCode.value);
        const selectedWard = wards.value.find(w => w.Code === selectedWardCode.value);

        const provinceName = selectedProvince ? selectedProvince.FullName : selectedProvinceCode.value;
        const wardName = selectedWard ? selectedWard.FullName : selectedWardCode.value;

        // 3. Xây dựng JSON body
        const orderRequest = {
            cartCode: cartCode.value,
            paymentMethod: payment.value.method.toUpperCase(),
            shippingAddress: {
                street: customer.value.address,
                ward: wardName,        // Gửi tên
                city: provinceName      // Gửi tên
            }
        };

        const token = localStorage.getItem('token');
        if (!token) {
            alert('Phiên đăng nhập hết hạn.');
            return;
        }

        isPlacingOrder.value = true; // Bắt đầu loading

        // 3. Gọi API POST
        try {
            const response = await fetch('/api/orders/placeOrder', {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(orderRequest)
            });

            if (response.ok) {
                const data = await response.json();
                console.log('API Response:', data); // Debug log

                const newOrderCode = data?.orderCode || data?.order?.orderCode;

                if (!newOrderCode) {
                    alert('Lỗi: Không thể lấy mã đơn hàng từ server');
                    console.error('Order code not found in response:', data);
                    return;
                }

                router.push({
                    name: 'OrderSuccess',
                    params: { orderId: newOrderCode }
                });
            } else {
                const errorData = await response.json();
                alert(`Đặt hàng thất bại: ${errorData.message || 'Lỗi không xác định'}`);
            }
        } catch (error) {
            console.error('Fetch Error (PlaceOrder):', error);
            alert('Lỗi kết nối máy chủ khi đặt hàng.');
        } finally {
            isPlacingOrder.value = false; // Dừng loading
        }
    };

    // Trả về hàm và state loading
    return {
        placeOrder,
        isPlacingOrder
    };
}