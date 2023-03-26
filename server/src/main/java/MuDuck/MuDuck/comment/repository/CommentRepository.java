package MuDuck.MuDuck.comment.repository;

import MuDuck.MuDuck.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT * FROM COMMENT WHERE MEMBER_ID = :memberId AND COMMENT_STATUS != 'COMMENT_DELETE'", nativeQuery = true)
    List<Comment> findByMemberId(long memberId);
}
