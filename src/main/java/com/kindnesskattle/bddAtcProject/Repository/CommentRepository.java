package com.kindnesskattle.bddAtcProject.Repository;

import com.kindnesskattle.bddAtcProject.Entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {


//    List<Comment> findByPostId(long post_id);
@Query(value = "SELECT u.user_id AS UserID,c.comment_id AS CommentID, c.post_id AS PostId, u.username AS UserName,u.image_url AS UserImage,c.comment_content, c.comment_date_time " +
        "FROM comments c " +
        "INNER JOIN user_accounts u ON c.user_id = u.user_id " +
        "WHERE c.post_id = :postId"
        , nativeQuery = true)
List<Map<String,Object>> findCommentsByPostId(Long postId);

}
