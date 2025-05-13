import api from './axios';

const ENDPOINT = '/books';

const bookApi = {
  searchTitle: async (title) => {
    const response = await api.get(`${ENDPOINT}`, { params: { title } });
    return response;
  },
};

export default bookApi;
