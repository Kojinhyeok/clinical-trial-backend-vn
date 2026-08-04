package com.clinical.dto.irb;

import com.clinical.dto.file.FileResponseDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class IrbSurveyResponseDTO {
    private IrbSurveyAnswerDTO answerDTO;
    private IrbSurveyQuestionDTO questionDTO;
    private IrbSurveyResultDTO resultDTO;
    private IrbTestResponseDTO testResponseDTO;
    private List<FileResponseDTO> attachedFiles;

}
