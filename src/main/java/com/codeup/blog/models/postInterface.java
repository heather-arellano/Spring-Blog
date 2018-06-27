package com.codeup.blog.models;

import org.springframework.data.repository.CrudRepository;


public interface postInterface extends CrudRepository<Post, Long> {
    Post findByTitle(String title);
}
