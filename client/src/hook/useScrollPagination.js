import { useEffect } from 'react';

export default function useScrollPagination(
  userId,
  isReview,
  selected,
  fetchUserReviewLists,
  fetchUserBookLists,
  isFetchingRef,
  paginationRef,
  page,
  setPage,
  setReviewCardLists,
  setBookCardLists,
) {
  const { pageNo, hasNext } = page;

  const onIntersection = (entries) => {
    const [entry] = entries;

    if (!entry.isIntersecting) return;

    isReview ? fetchUserReviewLists() : fetchUserBookLists();
  };

  useEffect(() => {
    if (!hasNext || !paginationRef.current || pageNo == 1) return;

    const observer = new IntersectionObserver(onIntersection, { threshold: 1 });
    const target = paginationRef.current;
    observer.observe(target);

    return () => observer.disconnect();
  }, [hasNext, pageNo]);

  useEffect(() => {
    isFetchingRef.current = false;

    queueMicrotask(() => {
      setReviewCardLists([]);
      setBookCardLists([]);
      setPage({
        pageNo: 1,
        totalPages: 1,
        totalElements: 0,
        hasPrevious: false,
        hasNext: false,
      });
    });
  }, [isReview, selected]);

  useEffect(() => {
    if (pageNo !== 1) return;
    if (!userId) return;

    isReview ? fetchUserReviewLists() : fetchUserBookLists();
  }, [pageNo, userId, isReview]);

  return;
}
