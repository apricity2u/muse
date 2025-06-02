import { RouterProvider } from 'react-router-dom';
import { Provider, useSelector } from 'react-redux';
import store from './store/store';
import { useEffect, useState } from 'react';
import authApi from './api/authApi';
import { login, logout } from './store/slices/authSlice';
import router from './router';

export default function App() {
  const [isInitialized, setIsInitialized] = useState(false);

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
      setIsInitialized(true);
    };
    initializeAuth();
  }, []);

  if (!isInitialized) return null;

  return (
    <>
      <Provider store={store}>
        <RouterProvider router={router}></RouterProvider>
      </Provider>
    </>
  );
}
