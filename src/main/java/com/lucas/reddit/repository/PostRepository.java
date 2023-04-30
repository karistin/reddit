package com.lucas.reddit.repository;

import com.lucas.reddit.model.Post;
import com.lucas.reddit.model.Subreddit;
import com.lucas.reddit.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
