import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  accessToken: '',
  isLoggedIn: false,
  nickname: '',
  memberId: '',
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    login: (state, action) => {
      state.accessToken = action.payload.accessToken.replace('Bearer ', '');
      state.isLoggedIn = true;
      state.nickname = action.payload.nickname || '';
      state.memberId = action.payload.memberId || '';
    },
    logout: (state) => {
      state.accessToken = '';
      state.isLoggedIn = false;
      state.nickname = '';
      state.memberId = '';
    },
  },
});

export const { login, logout } = authSlice.actions;
export default authSlice.reducer;
