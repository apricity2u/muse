import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  bookId: '',
};

const bookSlice = createSlice({
  name: 'book',
  initialState,
  reducers: {
    select: (state, action) => {
      state.bookId = action.payload || '';
    },
    unselect: (state) => {
      state.bookId = '';
    },
  },
});

export const { select, unselect } = bookSlice.actions;
export default bookSlice.reducer;
