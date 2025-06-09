import { useSelector } from 'react-redux';
import { Navigate } from 'react-router-dom';

export default function ProtectedLayout({ children }) {
  const isLoggedIn = useSelector((state) => state.auth.isLoggedIn);

  if (!isLoggedIn) {
    alert('로그인 후 이용 가능합니다.');
    return <Navigate to="/login" replace />;
  }

  return children;
}
