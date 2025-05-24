import api from './axios';

const ENDPOINT = '/reviews';

const reviewApi = {
  getMainReviewLists: async (pageNo) => {
    const response = await api.get(`${ENDPOINT}`, { params: { page: pageNo } });
    return response;
  },

  postReview: async (bookId, formData) => {
    const response = await api.post(`/books/${bookId}/reviews`, formData);
    return response;
  },

  deleteReview: async (reviewId) => {
    const response = await api.delete(`${ENDPOINT}/${reviewId}`);
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
