package com.socialplatformapi.repository;

import com.socialplatformapi.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
    List<Post> findAllBy(Pageable pageable);
    List<Post> findAllByPosterUsername(String username, Pageable pageable);
}
