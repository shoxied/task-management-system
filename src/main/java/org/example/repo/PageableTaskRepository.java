package org.example.repo;

import org.example.entity.Task;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageableTaskRepository extends PagingAndSortingRepository<Task, Integer> {
}
