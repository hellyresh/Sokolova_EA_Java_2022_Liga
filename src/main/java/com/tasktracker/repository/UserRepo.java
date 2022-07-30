package com.tasktracker.repository;

import com.tasktracker.model.User;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;



import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "tasks"
            }
    )
    List<User> findAll();
}
