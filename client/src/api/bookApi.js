import api from './axios';

const ENDPOINT = '/books';

const bookApi = {
  searchTitle: async (title) => {
    const response = await api.get(`${ENDPOINT}`, { params: { title } });
    return response;
  },

  postBookLikes: async (bookId) => {
    const response = await api.post(`${ENDPOINT}/${bookId}/likes`);
    return response;
  },

  deleteBookLikes: async (bookId) => {
    const response = await api.delete(`${ENDPOINT}/${bookId}/likes`);
    return response;
  },
};

export default bookApi;
