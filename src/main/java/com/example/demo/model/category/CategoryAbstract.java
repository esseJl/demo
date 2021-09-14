package com.example.demo.model.category;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Categories")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "category_type")
public abstract class CategoryAbstract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column
    private String categoryUUID;

    @ManyToOne
    @JoinColumn(name = "FK_PARENT_ID")
    private CategoryAbstract parent;

    @OneToMany(mappedBy = "parent")
    public List<CategoryAbstract> children = new ArrayList<>();

    private String name;

    public CategoryAbstract() {
    }

    public CategoryAbstract(String name) {
        this.name = name;
    }

    public CategoryAbstract(String catName, String uuid) {
        this.name = catName;
        this.categoryUUID = uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryUUID() {
        return categoryUUID;
    }

    public void setCategoryUUID(String categoryUUID) {
        this.categoryUUID = categoryUUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryAbstract getParent() {
        return parent;
    }

    public void setParent(CategoryAbstract parentCategory) {
        this.parent = parentCategory;
    }

    public List<CategoryAbstract> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryAbstract> children) {
        this.children = children;
    }

    public void addChildren(CategoryAbstract cat) {
        this.children.add(cat);
    }

    public void removeChildren(CategoryAbstract cat) {
        this.children.remove(cat);
    }


}
