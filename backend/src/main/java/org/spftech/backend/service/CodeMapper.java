package org.spftech.backend.service;

import java.util.List;

import org.spftech.backend.dto.CodeDto;
import org.spftech.backend.entity.Code;
import org.spftech.backend.entity.common.CodeEntity;

public class CodeMapper {
    public static <T extends CodeEntity> List<CodeDto> toCodeDtoList(List<T> entities) {
        return entities.stream()
            .map(e -> new CodeDto(
                e.getCode(),
                e.getLabelNl(),
                e.getLabelEn(),
                e.getLabelFr(),
                e.getLabelDe()
            ))
            .toList();
    }

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