package com.ues.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "description_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DescriptionDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String serverFilename;

    @OneToOne(mappedBy = "descriptionDocument")
    private Location location;
}
