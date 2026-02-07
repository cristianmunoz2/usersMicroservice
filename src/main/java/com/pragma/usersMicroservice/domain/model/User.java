package com.pragma.usersMicroservice.domain.model;

import java.time.LocalDate;
/**
 * Represents a User within the application's domain model
 * */
public class User {
    private String id;
    private String name;
    private String lastName;
    private String idDocument;
    private String phone;
    private LocalDate birthDate;
    private String email;
    private String password;
    private Role role;

    public User(UserBuilder userBuilder) {
        this.id = userBuilder.id;
        this.name = userBuilder.name;
        this.idDocument = userBuilder.name;
        this.lastName = userBuilder.lastName;
        this.birthDate = userBuilder.birthDate;
        this.phone = userBuilder.phone;
        this.email = userBuilder.email;
        this.password = userBuilder.password;
        this.role = userBuilder.role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static UserBuilder builder(){
        return new UserBuilder();
    }

    public static class UserBuilder {
        private String id;
        private String name;
        private String lastName;
        private String idDocument;
        private String phone;
        private LocalDate birthDate;
        private String email;
        private String password;
        private Role role;

        public UserBuilder id(String id){
            this.id = id;
            return this;
        }

        public UserBuilder name(String name){
            this.name = name;
            return this;
        }

        public UserBuilder idDocument(String idDocument){
            this.idDocument = idDocument;
            return this;
        }

        public UserBuilder birthdate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public UserBuilder lastname(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }
}


