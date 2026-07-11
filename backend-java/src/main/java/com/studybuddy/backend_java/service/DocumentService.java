package com.studybuddy.backend_java.service;

import com.studybuddy.backend_java.model.Document;
import com.studybuddy.backend_java.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementation Service CRUD for each Entity
@Service
public class DocumentService {

    // Definitive use of respective interface of repository
    private final DocumentRepository documentRepository;

    // Constructor with injection automatized via Spring
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    // Methods
    public Document save(Document document) {
        return documentRepository.save(document);
    }

    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    public Document findById(Long id) {
        return documentRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        documentRepository.deleteById(id);
    }
}