import api from './axios';

const ENDPOINT = '/reviews';

const reviewApi = {
  getMainReviewLists: async (pageNo) => {
    const response = await api.get(`${ENDPOINT}`, { params: { page: pageNo } });
    return response;
  },

  getReview: async (bookId, reviewId) => {
    const response = await api.get(`/books/${bookId}${ENDPOINT}/${reviewId}`);
    return response;
  },

  postReview: async (bookId, review) => {
    const formData = new FormData();
    const { image, content } = review;

    if (image) {
      formData.append('image', image);
    }

    const contentBlob = new Blob([JSON.stringify({ content: content })], {
      type: 'application/json',
    });

    formData.append('content', contentBlob);

    const response = await api.post(`/books/${bookId}${ENDPOINT}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    return response;
  },

  patchReview: async (reviewId, updateReview) => {
    const formData = new FormData();
    const { image, content } = updateReview;

    if (image) {
      formData.append('image', image);
    }

    if (content) {
      const contentBlob = new Blob([JSON.stringify({ content: content })], {
        type: 'application/json',
      });
      formData.append('content', contentBlob);
    }

    const response = await api.patch(`${ENDPOINT}/${reviewId}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

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
