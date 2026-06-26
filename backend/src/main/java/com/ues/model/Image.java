package com.ues.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String serverFilename;

    @JsonIgnore
    @OneToOne(mappedBy = "image")
    private Location location;

    @JsonIgnore
    @OneToOne(mappedBy = "image")
    private Event event;

    @JsonIgnore
    @OneToOne(mappedBy = "profileImage")
    private User user;

    public Image() {
    }

    public Image(Long id, String serverFilename, Location location, Event event, User user) {
        this.id = id;
        this.serverFilename = serverFilename;
        this.location = location;
        this.event = event;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServerFilename() {
        return serverFilename;
    }

    public void setServerFilename(String serverFilename) {
        this.serverFilename = serverFilename;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
