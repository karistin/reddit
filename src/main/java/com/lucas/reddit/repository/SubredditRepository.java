package com.lucas.reddit.repository;

import com.lucas.reddit.model.Subreddit;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubredditRepository extends JpaRepository<Subreddit, Long> {

    Optional<Subreddit> findByName(String  subredditName);

}
