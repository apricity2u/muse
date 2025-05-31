import axios from 'axios';
import store from '../store/store';
import { login, logout } from '../store/slices/authSlice';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  withCredentials: true,
});

api.interceptors.request.use((config) => {
  const accessToken = store.getState().auth.accessToken;

  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }

  return config;
});

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const refreshResponse = await api.post('/auth/reissue', {}, { withCredentials: true });

        const newAccessToken = refreshResponse.headers['authorization'];
        const { nickname, imageUrl } = refreshResponse.data.data;

        if (!newAccessToken) {
          throw new Error('token not found');
        }

        store.dispatch(
          login({
            accessToken: newAccessToken,
            nickname,
            imageUrl,
          }),
        );

        originalRequest.headers.Authorization = newAccessToken;
        return api(originalRequest);
      } catch (refreshError) {
        console.error('리이슈 실패', refreshError);
        store.dispatch(logout());
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  },
);

export default api;
