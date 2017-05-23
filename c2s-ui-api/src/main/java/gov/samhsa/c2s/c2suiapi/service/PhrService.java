package gov.samhsa.c2s.c2suiapi.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhrService {

    List<Object> getAllDocumentTypeCodesList();

    List<Object> getPatientDocumentInfoList(String patientMrn);

    Object getPatientDocumentByDocId(String patientMrn, Long id);

    Object saveNewPatientDocument(String patientMrn, MultipartFile file, String documentName, String description, Long documentTypeCodeId);

    void deletePatientDocument(String patientMrn, Long id);
}
