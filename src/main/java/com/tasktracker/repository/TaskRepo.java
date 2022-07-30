package com.tasktracker.repository;

import com.tasktracker.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo extends JpaRepository<Task, Long> {
}
