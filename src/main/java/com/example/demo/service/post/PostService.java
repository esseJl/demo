package com.example.demo.service.post;

import com.example.demo.model.post.Post;
import com.example.demo.model.user.User;
import com.example.demo.repository.post.PostRepository;
import com.github.eloyzone.jalalicalendar.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class PostService {

    private PostRepository postRepository;
    private static Pattern DATE_PATTERN = Pattern.compile(
            "^[1-4]\\d{3}\\/((0[1-6]\\/((3[0-1])|([1-2][0-9])|(0[1-9])))|((1[0-2]|(0[7-9]))\\/(30|([1-2][0-9])|(0[1-9]))))$"
    );

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public Post createNewPost(Post post, User user) {

        post.setUser(user);
        post.setPostUUID(UUID.randomUUID().toString());

        Post save = postRepository.save(post);
        return save;
    }

    public Page<Post> getAllPost(Pageable pageable) {
        return postRepository.findAll(pageable);
    }


    public Post findPostByPostUUID(String postUUID) {
        return postRepository.findByPostUUID(postUUID);
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public boolean isUniqueLinkExist(String uniqueLink) {
        Post post = postRepository.findByUniqueLink(uniqueLink);
        if (post == null)
            return false;

        return true;
    }


    //this method does not need any more because valid with hibernate pattern validator
    public boolean matchesDate(String date) {
        if (date != null && !date.equals("")) {
            if (DATE_PATTERN.matcher(date).matches())
                return true;
        }
        return false;
    }

    public LocalDate parseDate(String date) {
        String[] split = date.split("/");
        DateConverter dateConverter = new DateConverter();
        int year = Integer.valueOf(split[0]);
        int month = Integer.valueOf(split[1]);
        int day = Integer.valueOf(split[2]);
        LocalDate localDate = dateConverter.jalaliToGregorian(year, month, day);

        return localDate;

    }

}
