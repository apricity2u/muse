package com.example.muse.global.filter;

import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.stat.Statistics;
import org.springframework.context.annotation.Profile;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Profile("dev")
@Slf4j
@RequiredArgsConstructor
public class HibernateStatsFilter extends OncePerRequestFilter {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        SessionFactoryImplementor sessionFactoryImplementor = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
        Statistics stats = sessionFactoryImplementor.getStatistics();
        stats.clear();

        long start = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            long queryCount = stats.getPrepareStatementCount();
            log.info("실행시간: {} ms", duration);
            log.info("쿼리 수: {}", queryCount);
        }
    }
}
