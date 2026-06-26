package com.ues.repository;

import com.ues.model.DescriptionDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptionDocumentRepository extends JpaRepository<DescriptionDocument, Long> {
}
