import { configureStore } from '@reduxjs/toolkit';
import authReduce from './slices/authSlice';
import bookReduce from './slices/bookSlice';

const store = configureStore({
  reducer: {
    auth: authReduce,
    book: bookReduce,
  },
});

export default store;
