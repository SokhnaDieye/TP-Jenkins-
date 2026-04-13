package com.tp3.students.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String filiere;

    // Constructeur vide
    public Student() {}

    // Constructeur avec paramètres
    public Student(String name, String email, String filiere) {
        this.name    = name;
        this.email   = email;
        this.filiere = filiere;
    }

    // Getters & Setters
    public Long getId()              { return id; }
    public void setId(Long id)       { this.id = id; }

    public String getName()          { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail()            { return email; }
    public void setEmail(String email)  { this.email = email; }

    public String getFiliere()              { return filiere; }
    public void setFiliere(String filiere)  { this.filiere = filiere; }
}
