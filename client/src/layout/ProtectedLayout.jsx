import { useSelector } from 'react-redux';
import { Navigate, Outlet } from 'react-router-dom';
import { useEffect } from 'react';

export default function ProtectedLayout() {
  const isLoggedIn = useSelector((state) => state.auth.isLoggedIn);

  useEffect(() => {
    if (!isLoggedIn) {
      alert('로그인 후 이용 가능합니다.');
    }
  }, [isLoggedIn]);

  if (!isLoggedIn) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
}