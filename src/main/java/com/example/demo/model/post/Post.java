package com.example.demo.model.post;

import com.example.demo.model.category.cat4post.PostCategory;
import com.example.demo.model.comment.Comment;
import com.example.demo.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String postUUID;

    @Column(name = "title", nullable = false)
    @Length(min = 5)
    @NotBlank
    private String title;

    @Column(columnDefinition = "TEXT")
    @NotBlank
    private String body;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "cat4post",
            joinColumns = @JoinColumn(name = "post_id"))
    @JsonIgnore
    private List<PostCategory> postCategory;


    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date updateDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date releaseDate;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Comment> comments;

    public Post() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<PostCategory> getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(List<PostCategory> postCategory) {
        this.postCategory = postCategory;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getPostUUID() {
        return postUUID;
    }

    public void setPostUUID(String postUUID) {
        this.postUUID = postUUID;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }
}
