import { createSlice } from '@reduxjs/toolkit';
import { jwtDecode } from 'jwt-decode';
import user from '../../assets/user.png';

const initialState = {
  accessToken: '',
  isLoggedIn: false,
  nickname: '',
  memberId: '',
  imageUrl: user,
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    login: (state, action) => {
      console.log("로그인 슬라이스")
      const accessToken = action.payload.accessToken.replace('Bearer ', '');
      const decodedToken = jwtDecode(accessToken);
      state.accessToken = accessToken;
      state.isLoggedIn = true;
      state.nickname = action.payload.nickname || '';
      state.memberId = decodedToken.id || '';
      state.imageUrl = action.payload.imageUrl || '';
    },
    logout: (state) => {
      state.accessToken = '';
      state.isLoggedIn = false;
      state.nickname = '';
      state.memberId = '';
      state.imageUrl = '';
    },
  },
});

export const { login, logout } = authSlice.actions;
export default authSlice.reducer;
