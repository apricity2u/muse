import api from './axios';

const ENDPOINT = '/books';

const bookApi = {
  searchTitle: async (title) => {
    const response = await api.get(`${ENDPOINT}`, { params: { title } });
    return response;
  },

  getBook: async (bookId) => {
    const response = await api.get(`${ENDPOINT}/${bookId}`);
    return response;
  },

  getUserBookLists: async (memberId, pageNo, sort) => {
    const response = await api.get(`/users/${memberId}${ENDPOINT}`, {
      params: { page: pageNo, sort: sort },
    });
    return response;
  },

  getLikedBookLists: async (pageNo, sort) => {
    const response = await api.get(`${ENDPOINT}/likes`, {
      params: { page: pageNo, sort: sort },
    });
    return response;
  },

  postBookLikes: async (bookId) => {
    const response = await api.post(`${ENDPOINT}/${bookId}/like`);
    return response;
  },

  deleteBookLikes: async (bookId) => {
    const response = await api.delete(`${ENDPOINT}/${bookId}/like`);
    return response;
  },
};

export default bookApi;
