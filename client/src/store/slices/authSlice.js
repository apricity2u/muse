import { createSlice } from '@reduxjs/toolkit';
import { jwtDecode } from 'jwt-decode';
import user from '../../assets/user.png';

const initialState = {
  accessToken: '',
  isLoggedIn: false,
  nickname: '',
  memberId: '',
  imageUrl: '' || user,
  isInitialized: false,
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
      state.memberId = decodedToken.sub || '';
      state.imageUrl = action.payload.imageUrl || user;
      state.isInitialized = true;
    },
    logout: (state) => {
      state.accessToken = '';
      state.isLoggedIn = false;
      state.nickname = '';
      state.memberId = '';
      state.imageUrl = '';
    },
    changeProfile: (state, action) => {
      state.nickname = action.payload.nickname;
      state.imageUrl = action.payload.profileImageUrl;
    },
  },
});

export const { login, logout, changeProfile } = authSlice.actions;
export default authSlice.reducer;
