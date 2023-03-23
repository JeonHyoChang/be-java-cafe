package kr.codesqaud.cafe.repository;

import kr.codesqaud.cafe.domain.Article;
import kr.codesqaud.cafe.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcTemplateArticleRepository implements ArticleRepository{
    private final JdbcTemplate jdbcTemplate;
    public JdbcTemplateArticleRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean saveArticle(Article article) {
        // 인덱스 중복 여부 확인
        if (findById(article.getId()).isEmpty()) {
            // jdbcTemplate으로 변경 / PK값을 가져오는 방법이 이것뿐인가?;;;;
            jdbcTemplate.update("INSERT INTO CAFE_ARTICLE(WRITER, TITLE, CONTENTS, TIME) VALUES (?, ?, ?, ?)"
                    ,article.getWriter(), article.getTitle(), article.getContents(), LocalDateTime.now());
            List<Article> lastValue = jdbcTemplate.query("SELECT MAX(ID) FROM CAFE_ARTICLE", articlePKMapper());
            article.setId(lastValue.get(0).getId());
            return true;
        }
        return false;
    }

    @Override
    public Optional<Article> findById(long id) {
        List<Article> result = jdbcTemplate.query("select * from cafe_article where id = ?", articleRowMapper(), id);
        return result.stream().findAny();
    }

    @Override
    public List<Article> findAllArticle() {
        List<Article> result = jdbcTemplate.query("select * from cafe_article ORDER BY id desc ", articleRowMapper());
        return result;
    }

    private RowMapper<Article> articleRowMapper() {
        return (rs, rowNum) -> {
            Article article = new Article();
            article.setId(rs.getLong("id"));
            article.setWriter(rs.getString("writer"));
            article.setTitle(rs.getString("title"));
            article.setContents(rs.getString("contents"));
            article.setTime(rs.getTimestamp("time"));
            return article;
        };
    }

    private RowMapper<Article> articlePKMapper() {
        return (rs, rowNum) -> {
            Article article = new Article();
            rs.getLong("MAX(ID)");
            return article;
        };
    }
}
