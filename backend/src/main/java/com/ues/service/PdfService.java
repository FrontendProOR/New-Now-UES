package com.ues.service;

import com.ues.model.DescriptionDocument;
import com.ues.model.Location;
import com.ues.repository.DescriptionDocumentRepository;
import com.ues.repository.LocationRepository;
import com.ues.util.MinioUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfService {

    private static final Logger logger = LogManager.getLogger(PdfService.class);

    private final LocationRepository locationRepository;
    private final DescriptionDocumentRepository descriptionDocumentRepository;
    private final MinioUtil minioUtil;
    private final LocationIndexService locationIndexService;

    public PdfService(LocationRepository locationRepository,
                      DescriptionDocumentRepository descriptionDocumentRepository,
                      MinioUtil minioUtil,
                      LocationIndexService locationIndexService) {
        this.locationRepository = locationRepository;
        this.descriptionDocumentRepository = descriptionDocumentRepository;
        this.minioUtil = minioUtil;
        this.locationIndexService = locationIndexService;
    }

    @Transactional
    public void uploadPdf(Long locationId, MultipartFile pdfFile) throws Exception {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));

        String extractedText = extractTextFromPdf(pdfFile);

        // Ako već postoji PDF, obriši stari iz MinIO
        if (location.getDescriptionDocument() != null) {
            try {
                minioUtil.deleteFile(location.getDescriptionDocument().getServerFilename());
            } catch (Exception e) {
                logger.warn("Failed to delete old PDF from MinIO: {}", e.getMessage());
            }
        }

        String objectName = minioUtil.uploadFile("documents", pdfFile);

        if (location.getDescriptionDocument() != null) {
            location.getDescriptionDocument().setServerFilename(objectName);
            descriptionDocumentRepository.save(location.getDescriptionDocument());
        } else {
            DescriptionDocument doc = new DescriptionDocument();
            doc.setServerFilename(objectName);
            descriptionDocumentRepository.save(doc);
            location.setDescriptionDocument(doc);
            locationRepository.save(location);
        }

        try {
            locationIndexService.updateFileDescription(locationId, extractedText);
        } catch (Exception e) {
            logger.error("Failed to index PDF text in ES for location id={}: {}", locationId, e.getMessage());
        }

        logger.info("PDF uploaded for location id={}, extracted {} characters", locationId, extractedText.length());
    }

    private String extractTextFromPdf(MultipartFile file) throws Exception {
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
}
