package com.pragma.usersMicroservice.domain.model;

import com.pragma.usersMicroservice.domain.util.RoleName;

/**
 * Represents a Role in the application's domain model.
 * <p>
 * Implements the Builder pattern manually to maintain a pure domain
 * free of external dependencies.
 * </p>
 */
public class Role {
    private String id;
    private RoleName name;
    private String description;

    /**
     * Constructs a Role instance using the provided builder.
     * @param roleBuilder The builder containing the role data.
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
     * Initiates the Builder pattern for the Role entity.
     * @return A new instance of RoleBuilder.
     */
    public static RoleBuilder builder(){
        return new RoleBuilder();
    }

    /**
     * Builder class for constructing Role instances.
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
         * Finalizes the construction of the Role object.
         * @return A new Role instance with the configured attributes.
         */
        public Role build() {
            return new Role(this);
        }
    }
}