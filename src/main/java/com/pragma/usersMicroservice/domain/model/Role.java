package com.pragma.usersMicroservice.domain.model;

import com.pragma.usersMicroservice.domain.util.RoleName;

/**
 * Represents a user role in the domain model.
 * Uses a manual Builder pattern to avoid external dependencies.
 */
public class Role {
    private String id;
    private RoleName name;
    private String description;

    /**
     * Initializes a new Role using the builder data.
     * @param roleBuilder Source builder.
     */
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

    /**
     * Creates a new builder instance.
     * @return A fresh RoleBuilder.
     */
    public static RoleBuilder builder(){
        return new RoleBuilder();
    }

    /**
     * Builder to construct {@link Role} instances.
     */
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

        /**
         * Builds and returns the Role instance.
         * @return The created Role.
         */
        public Role build() {
            return new Role(this);
        }
    }
}