package org.spftech.backend.service;

import org.spftech.backend.dto.CodeDto;
import org.spftech.backend.entity.Code;

public class CodeMapper {
    public static CodeDto toDto(Code code) {
        if (code == null) return null;

        return new CodeDto(
                code.getCodeValue(),
                code.getLabelNl(),
                code.getLabelEn(),
                code.getLabelFr(),
                code.getLabelDe()
        );
    }

}