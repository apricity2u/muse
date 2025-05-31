import api from './axios';

const ENDPOINT = '/auth/logout';

const authApi = {
  logout: async () => {
    const response = await api.post(`${ENDPOINT}`);
    return response;
  },
};

export default authApi;
