import { ref } from 'vue'
import api from '@/utils/api.js'

export function useUserProfile() {
    const profile = ref({
        fullName: '',
        email: '',
        phone: '',
        address: ''
    })

    const loading = ref(false)
    const error = ref(null)
    const message = ref(null)

    // Fetch profile từ backend
    async function fetchProfile() {
        loading.value = true
        error.value = null
        try {
            const token = localStorage.getItem('token')
            if (!token) {
                error.value = 'Not authenticated'
                return
            }

            const response = await fetch('/api/profile/me', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            })

            if (!response.ok) {
                throw new Error(`Failed to fetch profile: ${response.status}`)
            }

            const data = await response.json()
            profile.value = {
                fullName: data.fullName || '',
                email: data.email || '',
                phone: data.phone || '',
                address: data.address || ''
            }
        } catch (err) {
            error.value = err.message
            console.error('Error fetching profile:', err)
        } finally {
            loading.value = false
        }
    }

    // Update profile trên backend
    async function updateProfile() {
        loading.value = true
        error.value = null
        message.value = null
        try {
            const token = localStorage.getItem('token')
            if (!token) {
                error.value = 'Not authenticated'
                return
            }

            const response = await fetch('/api/profile/me', {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    fullName: profile.value.fullName,
                    email: profile.value.email,
                    phone: profile.value.phone,
                    address: profile.value.address
                })
            })

            if (!response.ok) {
                throw new Error(`Failed to update profile: ${response.status}`)
            }

            const data = await response.json()
            profile.value = {
                fullName: data.fullName || '',
                email: data.email || '',
                phone: data.phone || '',
                address: data.address || ''
            }
            message.value = 'Profile updated successfully!'

            // Clear success message sau 3 giây
            setTimeout(() => {
                message.value = null
            }, 3000)
        } catch (err) {
            error.value = err.message
            console.error('Error updating profile:', err)
        } finally {
            loading.value = false
        }
    }

    return {
        profile,
        loading,
        error,
        message,
        fetchProfile,
        updateProfile
    }
}
