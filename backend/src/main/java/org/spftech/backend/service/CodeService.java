package org.spftech.backend.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Date;
import java.util.Arrays;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.spftech.backend.entity.Code;
import org.spftech.backend.repository.CodeRepository;

import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CodeService {
    private final CodeRepository codeRepository;

    public List<Code> getCodesByUsageContext(String usageContext) {
        return codeRepository.findByUsageContext(usageContext);
    }

    public void importCodes(List<Code> codes){
        try {
            codeRepository.saveAll(codes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Code> findDuplicated(List<Code> importedCodes){
        return importedCodes.stream()
                .flatMap(code -> codeRepository.findByCodeValueAndUsageContextAndDeactivated
                        (
                                code.getCodeValue(),
                                code.getUsageContext(),
                                false
                        ).stream()
                )
                .toList();
    }

    public void replaceCodes(List<Code> importedCodes){
        for(Code code: importedCodes){
            List<Code> existing = codeRepository.findByCodeValueAndUsageContextAndDeactivated(code.getCodeValue(),
                    code.getUsageContext(), false);
            for(Code old: existing){
                old.setDeactivated(true);
                old.setDeactivationDate(new Date());
            }
            code.setCreationDate(new Date());
            codeRepository.saveAll(existing);
            codeRepository.save(code);
        }
    }

    public void addNewCodes(List<Code> importedCodes){
        List<Code> newCodes = importedCodes.stream()
                .filter(code -> codeRepository
                        .findByCodeValueAndUsageContextAndDeactivated(
                                code.getCodeValue(),
                                code.getUsageContext(),
                                false
                        ).isEmpty()
                )
                .toList();

        for(Code code: newCodes){
            code.setCreationDate(new Date());
        }
        codeRepository.saveAll(newCodes);
    }
}