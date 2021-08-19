package com.example.demo.service.post;

import com.example.demo.model.post.Post;
import com.example.demo.model.user.User;
import com.example.demo.repository.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createNewPost(Post post, User user, Date releaseDate) {

        post.setPostUUID(UUID.randomUUID().toString());

        post.setUser(user);
        post.setReleaseDate(releaseDate);

        Post save = postRepository.save(post);
        return save;
    }

    public Page<Post> getAllPost(Pageable pageable) {
        return postRepository.findAll(pageable);
    }
    

}
