import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  accessToken: '',
  isLoggedIn: false,
  nickname: '',
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    login: (state, action) => {
      state.accessToken = action.payload.accessToken.replace('Bearer ', '');
      state.isLoggedIn = true;
      state.nickname = action.payload.nickname || '';
    },
    logout: (state) => {
      state.accessToken = '';
      state.isLoggedIn = false;
      state.nickname = '';
    },
  },
});

export const { login, logout } = authSlice.actions;
export default authSlice.reducer;
