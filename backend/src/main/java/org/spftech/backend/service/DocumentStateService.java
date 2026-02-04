package org.spftech.backend.service;

import java.util.List;

import org.spftech.backend.entity.Document.DocumentState;
import org.spftech.backend.repository.DocumentStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentStateService {

    @Autowired
    private DocumentStateRepository repository;

    public DocumentState createDocumentState(DocumentState documentState){
        return repository.save(documentState);
    }

    public List<DocumentState> getAllDocumentStates(){
        return repository.findAll();
    }
   
}
