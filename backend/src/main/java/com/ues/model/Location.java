package com.ues.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToMany(mappedBy = "location")
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "location")
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "location")
    private Set<Manages> managementAssignments = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }
}
