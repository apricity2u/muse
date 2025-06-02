import api from './axios';

const ENDPOINT = '/auth/logout';

const authApi = {
  logout: async () => {
    const response = await api.post(`${ENDPOINT}`);
    return response;
  },
  reissue: async () => {
    const response = await api.post('/auth/reissue');
    return response;
  },
};

export default authApi;
