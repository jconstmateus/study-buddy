package com.studybuddy.backend_java.controller;

import com.studybuddy.backend_java.model.Document;
import com.studybuddy.backend_java.service.DocumentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents") // Base URL
public class DocumentController {

    // Use of respective service for each request
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping // POST (create object with information on Body)
    public Document create(@RequestBody Document document) {
        return documentService.save(document);
    }

    @GetMapping // GET (get a list of objects)
    public List<Document> findAll() {
        return documentService.findAll();
    }

    @GetMapping("/{id}") // GET (object by id extracted in the path)
    public Document findById(@PathVariable Long id) {
        return documentService.findById(id);
    }

    @DeleteMapping("/{id}") // DELETE (object by id extracted in the path)
    public void deleteById(@PathVariable Long id) {
        documentService.deleteById(id);
    }

    @PutMapping("/{id}") // PUT (update object by id, with new data on Body)
    public Document update(@PathVariable Long id, @RequestBody Document document) {
        document.setId(id);
        return documentService.save(document);
    }
}