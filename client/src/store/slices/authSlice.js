import { createSlice } from '@reduxjs/toolkit';
import { jwtDecode } from 'jwt-decode';

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
      const accessToken = action.payload.accessToken.replace('Bearer ', '');
      const decodedToken = jwtDecode(accessToken);
      state.accessToken = accessToken;
      state.isLoggedIn = true;
      state.nickname = action.payload.nickname || '';
      state.memberId = decodedToken.id || '';
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
