package com.example.muse.domain.book;

import com.example.muse.domain.book.dto.SearchBookResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
class BookServiceTrendingTest {
    private static final String REDIS_IMAGE = "redis:7.0.12-alpine";
    private static final String MYSQL_IMAGE = "mysql:8.0";
    private static final int REDIS_PORT = 6379;
    private static final int TOTAL_BOOKS = 15;
    private static final int TOP_N = 10;
    private static final String KEY_PREFIX = "trending:";
    private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(REDIS_IMAGE)
            .withExposedPorts(REDIS_PORT);

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>(MYSQL_IMAGE)
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");

        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", () -> redis.getMappedPort(REDIS_PORT));
    }

    @Autowired
    private BookService bookService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private BookRepository bookRepository;

    private List<Book> books;

    @BeforeEach
    void setUp() {
        books = IntStream.rangeClosed(1, TOTAL_BOOKS)
                .mapToObj(i -> Book.builder()
                        .title("title" + i)
                        .author("author")
                        .description("description")
                        .isbn("isbn" + i)
                        .imageUrl("url")
                        .build())
                .toList();

        bookRepository.saveAll(books);
    }

    @AfterEach
    void afterEach() {
        bookRepository.deleteAll();

        Set<String> keys = redisTemplate.keys(KEY_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    @Test
    void trending_books_integration_test() {
        // Given
        for (int id = 1; id <= TOTAL_BOOKS; id++) {
            for (int count = 0; count < id; count++) {
                bookService.getBook((long) id, null);
            }
        }

        String todayKey = getTodayKey();
        ZSetOperations<String, String> zOps = redisTemplate.opsForZSet();

        // Then
        Long size = zOps.size(todayKey);
        assertThat(size).isEqualTo(15L);


        Double score15 = zOps.score(todayKey, "15");
        assertThat(score15).isEqualTo(15.0);


        Double score3 = zOps.score(todayKey, "3");
        assertThat(score3).isEqualTo(3.0);


        Long ttl = redisTemplate.getExpire(todayKey);
        assertThat(ttl).isGreaterThan(0L);
        assertThat(ttl).isLessThanOrEqualTo(8 * 24 * 60 * 60L);

        // When
        SearchBookResponseDto trending = bookService.getTrendingBooks();

        // Then
        List<Long> top10BookIds = books.subList(5, 15).stream()
                .map(Book::getId)
                .sorted(Comparator.reverseOrder())
                .toList();

        List<Long> actualIds = trending.getData().stream()
                .map(SearchBookResponseDto.SimpleBookDto::getId)
                .toList();

        assertThat(actualIds).hasSize(TOP_N);
        assertThat(actualIds).containsExactlyElementsOf(top10BookIds);
    }

    private String getTodayKey() {
        return KEY_PREFIX + LocalDate.now(ZONE).format(FORMATTER);
    }
}