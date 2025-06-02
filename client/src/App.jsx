import { RouterProvider } from 'react-router-dom';
import router from './router';

import { Provider } from 'react-redux';
import store from './store/store';
import { useEffect } from 'react';
import authApi from './api/authApi';
import { login, logout } from './store/slices/authSlice';

export default function App() {
  useEffect(() => {
    const initializeAuth = async () => {
      try {
        const response = await authApi.reissue();

        const newAccessToken = response.headers['authorization'];
        const { nickname, imageUrl } = response.data.data;

        store.dispatch(
          login({
            accessToken: newAccessToken,
            nickname,
            imageUrl,
          }),
        );
      } catch (refreshError) {
        store.dispatch(logout());
      }
    };
    initializeAuth();
  }, []);

  return (
    <>
      <Provider store={store}>
        <RouterProvider router={router}></RouterProvider>
      </Provider>
    </>
  );
}
