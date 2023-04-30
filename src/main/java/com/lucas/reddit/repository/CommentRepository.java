package com.lucas.reddit.repository;

import com.lucas.reddit.model.Comment;
import com.lucas.reddit.model.Post;
import com.lucas.reddit.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);
    List<Comment> findAllByUser(User user);

}
