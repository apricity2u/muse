import { createSlice } from '@reduxjs/toolkit';
import { jwtDecode } from 'jwt-decode';
import dog from '../../assets/dog.jpg';

const initialState = {
  accessToken: '',
  isLoggedIn: false,
  nickname: '',
  memberId: '',
  imageUrl: dog, // TODO: 기본이미지 설정 후 변경
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
