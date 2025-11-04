import api from './axios';

const ENDPOINT = '/auth';

const authApi = {
  logout: async () => {
    const response = await api.post(`${ENDPOINT}`);
    return response;
  },
};

export default authApi;
