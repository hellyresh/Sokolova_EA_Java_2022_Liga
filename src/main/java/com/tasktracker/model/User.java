package com.tasktracker.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static org.hibernate.FetchMode.JOIN;

@Getter
@Setter
@AllArgsConstructor
@Entity
//@RequiredArgsConstructor
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "user")
    @Fetch(FetchMode.JOIN)
    private Set<Task> tasks = new HashSet<>();



    public String toCsvRow() {
        return id + ", " + name + "\n";
    }
}
