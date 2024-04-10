package com.example.demo.model;


import jakarta.persistence.*;

@Entity

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public Role() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ERole getName() {return name;}

    public void setName(ERole name) {
        this.name = name;
    }
}