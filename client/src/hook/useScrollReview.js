import { useEffect } from 'react';

export default function useScrollReview(
  sort,
  fetchBookReviewLists,
  isFetchingRef,
  paginationRef,
  pageNo,
  hasNext,
  setPage,
  setReviews,
) {
  const onIntersection = (entries) => {
    const [entry] = entries;

    if (!entry.isIntersecting) return;

    fetchBookReviewLists();
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
    setReviews([]);
    setPage({
      pageNo: 1,
      totalPages: 1,
      totalElements: 0,
      hasPrevious: false,
      hasNext: false,
    });
  }, [sort]);

  useEffect(() => {
    if (pageNo !== 1) return;

    fetchBookReviewLists();
  }, [pageNo]);

  return;
}
