package com.example.demo.repository.post;

import com.example.demo.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {


    Post findByPostUUID(String postUUID);

    Post save(Post post);

    Post findByUniqueLink(String uniqueLink);

}
