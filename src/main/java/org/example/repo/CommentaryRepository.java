package org.example.repo;

import org.example.entity.Commentary;
import org.springframework.data.repository.CrudRepository;

public interface CommentaryRepository extends CrudRepository<Commentary, Integer> {
}
