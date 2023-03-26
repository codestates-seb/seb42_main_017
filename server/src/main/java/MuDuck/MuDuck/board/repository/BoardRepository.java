package MuDuck.MuDuck.board.repository;

import MuDuck.MuDuck.board.entity.Board;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("select b from Board b WHERE NOT b.boardStatus IN ('BOARD_DELETE')")
    Page<Board> findWithoutDeleteBoard(PageRequest pageRequest);

    @Query(value = "SELECT * FROM BOARD as b WHERE b.BOARD_ID IN (:ids) AND BOARD_STATUS != 'BOARD_DELETE'", nativeQuery = true)
    Page<Board> findByBoardId(List<Long> ids, PageRequest pageRequest);

    @Query(value = "SELECT * FROM BOARD WHERE CREATED_AT BETWEEN CURRENT_TIMESTAMP - INTERVAL '1' DAY AND NOW() ORDER BY LIKES DESC LIMIT 5", nativeQuery = true)
    List<Board> getDailyPopularPosts();

    @Query(value = "SELECT * FROM BOARD WHERE CREATED_AT BETWEEN CURRENT_TIMESTAMP - INTERVAL '7' DAY AND NOW() ORDER BY LIKES DESC LIMIT 5", nativeQuery = true)
    List<Board> getWeeklyPopularPosts();

    @Query(value = "SELECT * FROM BOARD WHERE MEMBER_ID = :memberId AND BOARD_STATUS != 'BOARD_DELETE'", nativeQuery = true)
    Page<Board> findByMemberId(long memberId, PageRequest pageRequest);

    @Query(value = "SELECT * FROM BOARD WHERE BOARD_ID IN (SELECT BOARD_ID FROM BOARD_LIKE WHERE MEMBER_ID = :memberId) AND BOARD_STATUS != 'BOARD_DELETE'", nativeQuery = true)
    Page<Board> findByBoardLikeId(long memberId, PageRequest pageRequest);
}
