package com.tasktracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class User {

    private final int id;
    private String name;

    private final Set<Task> tasks = new HashSet<>();

    public String toCsvRow() {
        return id + ", " + name + "\n";
    }
}
