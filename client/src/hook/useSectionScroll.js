import { useEffect, useRef } from 'react';

export default function useSectionScroll({
  currentSection,
  setCurrentSection,
  videoRef,
  reviewWrapperRef,
  debounceDelay = 100,
}) {
  const isScrollingRef = useRef(false);

  useEffect(() => {
    if (videoRef.current && currentSection === 0) {
      videoRef.current.play().catch((e) => {
        console.warn('자동 재생 실패:', e);
      });
    }
  }, [currentSection, videoRef]);

  useEffect(() => {
    const handleWheel = (e) => {
      if (isScrollingRef.current) return;

      isScrollingRef.current = true;

      if (e.deltaY > 0 && currentSection === 0) {
        reviewWrapperRef.current?.scrollIntoView({ behavior: 'smooth' });
        setCurrentSection(1);
      } else if (e.deltaY < 0 && currentSection === 1) {
        window.scrollTo({ top: 0, behavior: 'smooth' });
        setCurrentSection(0);
      }

      setTimeout(() => {
        isScrollingRef.current = false;
      }, debounceDelay);
    };

    window.addEventListener('wheel', handleWheel, { passive: true });

    return () => window.removeEventListener('wheel', handleWheel);
  }, [currentSection, reviewWrapperRef, setCurrentSection, debounceDelay]);
}
