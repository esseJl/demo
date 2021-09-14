package com.example.demo.service.tag;

import com.example.demo.model.category.cat4post.Category4Post;
import com.example.demo.model.tag.Tag;
import com.example.demo.repository.tag.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagService {

    private TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> parseTags(List<String> tagNames) {
        Set<Tag> tags = new LinkedHashSet<>();
        if (tagNames != null) {
            for (String tagName : tagNames) {
                if (tagName != null && !tagName.equals("")) {
                    Tag byName = tagRepository.findByName(tagName);
                    if (byName == null)
                        byName = new Tag(tagName, UUID.randomUUID().toString());

                    tags.add(byName);
                }
            }
        }
        return new ArrayList<Tag>(tags);
    }

    public List<String> findByNameContains(String nameLike) {
        List<String> catList = new ArrayList<>();
        List<Tag> byNameLike = tagRepository.findByNameContains(nameLike);

        for (Tag tag : byNameLike) {
            catList.add(tag.getName());
        }
        return catList;
    }
}
