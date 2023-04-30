package com.lucas.reddit.repository;

import com.lucas.reddit.model.Post;
import com.lucas.reddit.model.User;
import com.lucas.reddit.model.Vote;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {

//    Optional<Vote> findTopByPostAndOrderByVoteIdDesc(Post post, User currentUser);

}
