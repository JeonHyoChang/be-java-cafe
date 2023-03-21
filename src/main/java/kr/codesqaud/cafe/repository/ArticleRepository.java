package kr.codesqaud.cafe.repository;

import kr.codesqaud.cafe.domain.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    boolean saveArticle(Article article);

    Optional<Article> findByIndex(long index);

    List<Article> findAllArticle();
}
