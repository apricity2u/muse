import api from './axios';

const ENDPOINT = '/reviews';

const reviewApi = {
  deleteReview: async (reviewId) => {
    const response = await api.delete(`${ENDPOINT}/${reviewId} `);
    return response;
  },

  postReviewLikes: async (reviewId) => {
    const response = await api.post(`${ENDPOINT}/${reviewId}/likes`);
    return response;
  },

  deleteReviewLikes: async (reviewId) => {
    const response = await api.delete(`${ENDPOINT}/${reviewId}/likes`);
    return response;
  },
};

export default reviewApi;
