package com.ues.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    @NotBlank
    @Column(nullable = false)
    private String address;

    private Double totalRating;

    private String type;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToOne
    @JoinColumn(name = "description_document_id")
    private DescriptionDocument descriptionDocument;

    @JsonIgnore
    @OneToMany(mappedBy = "location")
    private Set<Event> events = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "location")
    private Set<Review> reviews = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "location")
    private Set<Manages> managementAssignments = new HashSet<>();

    public Location() {
    }

    public Location(Long id, String name, String description, LocalDate createdAt, String address, Double totalRating, String type, Image image, DescriptionDocument descriptionDocument, Set<Event> events, Set<Review> reviews, Set<Manages> managementAssignments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.address = address;
        this.totalRating = totalRating;
        this.type = type;
        this.image = image;
        this.descriptionDocument = descriptionDocument;
        this.events = events;
        this.reviews = reviews;
        this.managementAssignments = managementAssignments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(Double totalRating) {
        this.totalRating = totalRating;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public DescriptionDocument getDescriptionDocument() {
        return descriptionDocument;
    }

    public void setDescriptionDocument(DescriptionDocument descriptionDocument) {
        this.descriptionDocument = descriptionDocument;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public Set<Manages> getManagementAssignments() {
        return managementAssignments;
    }

    public void setManagementAssignments(Set<Manages> managementAssignments) {
        this.managementAssignments = managementAssignments;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }
}
