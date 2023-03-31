package kr.codesqaud.cafe.repository;

import kr.codesqaud.cafe.domain.Reply;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTemplateReplyRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateReplyRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Optional<Reply> saveReply(Reply reply) {
        String sql = "INSERT INTO CAFE_REPLY(WRITER, CONTENTS, ARTICLEID) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, reply.getWriter());
            pstmt.setString(2, reply.getContents());
            pstmt.setLong(3, reply.getArticleId());
            return pstmt;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();
        return findById(id);
//        jdbcTemplate.update("INSERT INTO CAFE_REPLY(WRITER, CONTENTS, ARTICLEID) VALUES (?, ?, ?)"
//                , reply.getWriter(), reply.getContents(), reply.getArticleId());
    }

    public boolean updateReply(Reply reply) {
        if (findById(reply.getId()).isPresent()) {
            jdbcTemplate.update("UPDATE CAFE_REPLY SET CONTENTS=?, TIME=? WHERE ID=?"
                    , reply.getContents(), LocalDateTime.now(), reply.getId());
            return true;
        }
        return false;
    }

    public boolean deleteReply(long id) {
        if (findById(id).isPresent()) {
            jdbcTemplate.update("UPDATE CAFE_REPLY SET DELETED=TRUE WHERE ID=?", id);
//            jdbcTemplate.update("DELETE CAFE_REPLY where ID=?", id);
            return true;
        }
        return false;
    }

    public boolean deleteReplyFormArticle(long articleId) {
        if (!findAllReply(articleId).isEmpty()) {
            jdbcTemplate.update("UPDATE CAFE_REPLY SET DELETED=TRUE WHERE ARTICLEID=?", articleId);
            return true;
        }
        return false;
    }

    public Optional<Reply> findById(long id) {
        List<Reply> result = jdbcTemplate.query("SELECT * FROM CAFE_REPLY WHERE ID = ? AND DELETED = FALSE", replyRowMapper(), id);
        return result.stream().findAny();
    }

    public List<Reply> findAllReply(long articleId) {
        return jdbcTemplate.query("SELECT * FROM CAFE_REPLY WHERE ARTICLEID=? AND DELETED = FALSE", replyRowMapper(), articleId);
    }

    private RowMapper<Reply> replyRowMapper() {
        return (rs, rowNum) -> {
            Reply reply = new Reply();
            reply.setId(rs.getLong("id"));
            reply.setWriter(rs.getString("writer"));
            reply.setContents(rs.getString("contents"));
            reply.setTime(rs.getTimestamp("time"));
            reply.setArticleId(rs.getLong("articleId"));
            return reply;
        };
    }
}
