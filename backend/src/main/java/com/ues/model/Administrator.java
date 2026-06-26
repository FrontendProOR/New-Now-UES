package com.ues.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("ADMIN")
@NoArgsConstructor
public class Administrator extends User {

    @Override
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (getRole() == null || getRole().equals("ROLE_USER")) {
            setRole("ROLE_ADMIN");
        }
    }
}
