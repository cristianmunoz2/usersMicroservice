package com.pragma.usersMicroservice.domain.model;

import com.pragma.usersMicroservice.domain.util.RoleName;

public class Role {
    private String id;
    private RoleName name;
    private String description;

    public Role(RoleBuilder roleBuilder) {
        this.id = roleBuilder.id;
        this.name = roleBuilder.name;
        this.description = roleBuilder.description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static RoleBuilder roleBuilder(){
        return new RoleBuilder();
    }

    public static class RoleBuilder{
        private String id;
        private RoleName name;
        private String description;

        public RoleBuilder id(String id){
            this.id = id;
            return this;
        }

        public RoleBuilder name(RoleName name){
            this.name = name;
            return this;
        }

        public RoleBuilder description(String description){
            this.description = description;
            return this;
        }
    }
}

