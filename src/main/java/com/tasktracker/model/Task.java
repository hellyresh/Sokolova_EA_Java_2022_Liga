package com.tasktracker.model;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "tasks")
public class Task {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "header")
    private String header;
    @Column(name = "description")
    private String description;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private User user;
    @JsonGetter("userId")
    public Long getUserId(){
        return user.getId();
    }
    @Column(name = "deadline")
    private LocalDate deadLine;
    @Column(name = "status")
    private Status status;



    public String toCsvRow() {
        return id + ", " + header + ", " + description + ", " + user.getId() + ", " +
                deadLine.format(formatter) + ", " + status + "\n";
    }
}
