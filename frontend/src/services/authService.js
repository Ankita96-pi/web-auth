import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth';

const authService = {
    register: async (userData) => {
        try {
            const response = await axios.post(`${API_URL}/register`, userData);
            return response.data;
        } catch (error) {
            throw error.response?.data || { error: 'Registration failed' };
        }
    },

    login: async (email, password) => {
        try {
            const response = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password }),
                credentials: 'include',
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.error || 'Failed to login');
            }

            const data = await response.json();
            localStorage.setItem('token', data.token);
            return data;
        } catch (error) {
            throw { error: error.message };
        }
    },

    verifyEmail: async (token) => {
        try {
            const response = await axios.get(`${API_URL}/verify-email`, {
                params: { token }
            });
            return response.data;
        } catch (error) {
            throw error.response?.data || { error: 'Email verification failed' };
        }
    },

    logout: () => {
        localStorage.removeItem('token');
    },

    getCurrentUser: () => {
        const token = localStorage.getItem('token');
        if (token) {
            return JSON.parse(atob(token.split('.')[1]));
        }
        return null;
    },

    forgotPassword: async (email) => {
        try {
            const response = await axios.post(`${API_URL}/forgot-password`, { email });
            return response.data;
        } catch (error) {
            throw error.response?.data || { error: 'Failed to send reset email' };
        }
    },

    resetPassword: async (token, newPassword) => {
        try {
            const response = await axios.post(`${API_URL}/reset-password`, {
                token,
                newPassword,
            });
            return response.data;
        } catch (error) {
            throw error.response?.data || { error: 'Failed to reset password' };
        }
    },
};

export default authService; 