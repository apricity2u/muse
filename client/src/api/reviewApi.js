import api from './axios';

const ENDPOINT = '/reviews';

const reviewApi = {
  getMainReviewLists: async (pageNo, size = 3) => {
    const response = await api.get(`${ENDPOINT}`, { params: { page: pageNo, size: size } });
    return response;
  },

  getReview: async (bookId, reviewId) => {
    const response = await api.get(`/books/${bookId}${ENDPOINT}/${reviewId}`);
    return response;
  },

  getUserReviewLists: async (memberId, pageNo, sort) => {
    const response = await api.get(`/users/${memberId}${ENDPOINT}`, {
      params: { page: pageNo, sort: sort },
    });
    return response;
  },

  getLikedReviewLists: async (pageNo, sort) => {
    const response = await api.get(`${ENDPOINT}/likes`, {
      params: { page: pageNo, sort: sort },
    });
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
    const { updatedImage, updatedContent } = updateReview;

    if (updatedImage) {
      formData.append('image', updatedImage);
    }

    if (updatedContent) {
      const contentBlob = new Blob([JSON.stringify({ content: updatedContent })], {
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
    const response = await api.post(`${ENDPOINT}/${reviewId}/like`);
    return response;
  },

  deleteReviewLikes: async (reviewId) => {
    const response = await api.delete(`${ENDPOINT}/${reviewId}/like`);
    return response;
  },

  getBookReviewLists: async (bookId, pageNo, sort) => {
    const response = await api.get(`/books/${bookId}${ENDPOINT}`, {
      params: { page: pageNo, sort: sort, size: 12 },
    });
    return response;
  },
};

export default reviewApi;
