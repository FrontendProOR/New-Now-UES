package com.ues.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;

@Entity
@DiscriminatorValue("ADMIN")
public class Administrator extends User {

    public Administrator() {
    }

    @Override
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (getRole() == null || getRole().equals("ROLE_USER")) {
            setRole("ROLE_ADMIN");
        }
    }
}
