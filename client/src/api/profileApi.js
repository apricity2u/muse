import api from './axios';
const ENDPOINT = '/profiles';

const profileApi = {
  getProfile: async (memberId) => await api.get(`${ENDPOINT}/${memberId}`),
  updateProfile: async (memberId, formData) =>
    await api.patch(`${ENDPOINT}/${memberId}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    }),
};

export default profileApi;
