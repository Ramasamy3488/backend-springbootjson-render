package com.example.jsondatacreate.models;

public class Student {
    private int id;
    private String name;
    private String email;
    private int age;
    private String batch;
    private String city;

    // Constructors
    public Student() {}
    public Student(int id, String name, String email, int age, String batch, String city) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.batch = batch;
        this.city = city;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public String getBatch() { return batch; }
    public void setBatch(String batch) { this.batch = batch; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}
