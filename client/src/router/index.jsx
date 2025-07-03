import { createBrowserRouter } from 'react-router-dom';
import Home from '../pages/Home';
import RootLayout from '../layout/RootLayout';
import BookDetail from '../pages/BookDetail';
import UserReviews from '../pages/UserReviews';
import UserLikes from '../pages/UserLikes';
import ReviewCreate from '../pages/ReviewCreate';
import Profile from '../pages/Profile';
import Login from '../pages/Login';
import LoginCallback from '../pages/LoginCallback';
import ProtectedLayout from '../layout/ProtectedLayout';
import NotFound from '../components/NotFound';

const router = createBrowserRouter([
  {
    path: '/',
    element: <RootLayout />,
    errorElement: <NotFound />,
    children: [
      {
        index: true,
        element: <Home />,
      },
      {
        path: 'books/:bookId',
        element: <BookDetail />,
      },
      {
        path: 'users/:userId',
        element: <UserReviews />,
      },
    ],
  },
  {
    path: '/',
    element: (
      <ProtectedLayout>
        <RootLayout />
      </ProtectedLayout>
    ),
    errorElement: <NotFound />,
    children: [
      {
        path: 'likes',
        element: <UserLikes />,
      },
      {
        path: 'reviews/create',
        element: <ReviewCreate />,
      },
      {
        path: 'books/:bookId/reviews/:reviewId/edit',
        element: <ReviewCreate />,
      },
      {
        path: 'profile',
        element: <Profile />,
      },
    ],
  },
  {
    path: '/login',
    errorElement: <NotFound />,
    element: <Login />,
  },
  {
    path: '/login/success',
    errorElement: <NotFound />,
    element: <LoginCallback />,
  },
]);

export default router;
