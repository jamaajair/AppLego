package org.spftech.backend.controller;

import org.spftech.backend.dto.CodeDto;
import org.spftech.backend.dto.DossierDto;
import org.spftech.backend.dto.LinkKindDTO;
import org.spftech.backend.dto.LinkedDossierDTO;
import org.spftech.backend.entity.Code;
import org.spftech.backend.entity.LinkedDossier;

public class Utils {
    public static LinkedDossierDTO toDTO(LinkedDossier linkedDossier){
        return new LinkedDossierDTO(
            new DossierDto(
                linkedDossier.getParentDossier().getRef(),
                linkedDossier.getParentDossier().getLabel(),
                linkedDossier.getParentDossier().getComments(),
                linkedDossier.getParentDossier().getCreatedAt(),
                linkedDossier.getParentDossier().getStartNoLaterThan(),
                linkedDossier.getParentDossier().getExpectCompletion(),
                new CodeDto(
                    linkedDossier.getParentDossier().getState().getCode().getCodeValue(),
                    linkedDossier.getParentDossier().getState().getCode().getLabelNl(),
                    linkedDossier.getParentDossier().getState().getCode().getLabelEn(),
                    linkedDossier.getParentDossier().getState().getCode().getLabelFr(),
                    linkedDossier.getParentDossier().getState().getCode().getLabelDe()
                ),
                new CodeDto(
                    linkedDossier.getParentDossier().getType().getCode().getCodeValue(),
                    linkedDossier.getParentDossier().getType().getCode().getLabelNl(),
                    linkedDossier.getParentDossier().getType().getCode().getLabelEn(),
                    linkedDossier.getParentDossier().getType().getCode().getLabelFr(),
                    linkedDossier.getParentDossier().getType().getCode().getLabelDe()
                )
            ),
            new DossierDto(
                linkedDossier.getChildDossier().getRef(),
                linkedDossier.getChildDossier().getLabel(),
                linkedDossier.getChildDossier().getComments(),
                linkedDossier.getChildDossier().getCreatedAt(),
                linkedDossier.getChildDossier().getStartNoLaterThan(),
                linkedDossier.getChildDossier().getExpectCompletion(),
                new CodeDto(
                    linkedDossier.getChildDossier().getState().getCode().getCodeValue(),
                    linkedDossier.getChildDossier().getState().getCode().getLabelNl(),
                    linkedDossier.getChildDossier().getState().getCode().getLabelEn(),
                    linkedDossier.getChildDossier().getState().getCode().getLabelFr(),
                    linkedDossier.getChildDossier().getState().getCode().getLabelDe()
                ),
                new CodeDto(
                    linkedDossier.getChildDossier().getType().getCode().getCodeValue(),
                    linkedDossier.getChildDossier().getType().getCode().getLabelNl(),
                    linkedDossier.getChildDossier().getType().getCode().getLabelEn(),
                    linkedDossier.getChildDossier().getType().getCode().getLabelFr(),
                    linkedDossier.getChildDossier().getType().getCode().getLabelDe()
                )
            ),
            linkedDossier.getSequenceNumber(),
            new LinkKindDTO(
                linkedDossier.getLinkKind().getId(),
                new CodeDto(
                    linkedDossier.getLinkKind().getCode().getCodeValue(),
                    linkedDossier.getLinkKind().getCode().getLabelNl(),
                    linkedDossier.getLinkKind().getCode().getLabelEn(),
                    linkedDossier.getLinkKind().getCode().getLabelFr(),
                    linkedDossier.getLinkKind().getCode().getLabelDe()
                )
            )
        );
    }

    public static CodeDto toDTO(Code code){
        return new CodeDto(
                code.getCodeValue(),
                code.getLabelNl(),
                code.getLabelEn(),
                code.getLabelFr(),
                code.getLabelDe()
        );
    }
}
