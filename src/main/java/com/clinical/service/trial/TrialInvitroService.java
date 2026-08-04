package com.clinical.service.trial;

import com.clinical.dto.trialInvitro.TrialInvitroRequestDTO;
import com.clinical.dto.trialInvitro.TrialInvitroResponseDTO;

import java.util.List;

public interface TrialInvitroService {
    List<TrialInvitroResponseDTO> getAllItems();
    TrialInvitroResponseDTO getItemById(Long id);
    TrialInvitroResponseDTO saveItem(TrialInvitroRequestDTO dto, Long userId);
    TrialInvitroResponseDTO updateItem(TrialInvitroRequestDTO dto, Long userId);
    void deleteItem(Long id);
}