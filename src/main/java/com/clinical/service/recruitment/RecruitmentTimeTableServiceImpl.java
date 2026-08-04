package com.clinical.service.recruitment;

import com.clinical.dto.recruitment.RecruitmentTimeTableRequestDTO;
import com.clinical.dto.recruitment.RecruitmentTimeTableResponseDTO;
import com.clinical.entity.recruitment.RecruitmentTimeTable;
import com.clinical.exception.NotFoundException;
import com.clinical.repository.recruitment.RecruitmentTimeTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RecruitmentTimeTableServiceImpl implements RecruitmentTimeTableService {

    private final RecruitmentTimeTableRepository timeTableRepository;

    @Override
    @Transactional
    public RecruitmentTimeTableResponseDTO createTimeTable(RecruitmentTimeTableRequestDTO requestDTO) {
        RecruitmentTimeTable timeTable = RecruitmentTimeTable.builder()
                .recruitmentId(requestDTO.getRecruitmentId())
                .date(requestDTO.getDate())
                .timeSlot(requestDTO.getTimeSlot())
                .maxCnt(requestDTO.getMaxCnt())
                .count(0)
                .build();

        RecruitmentTimeTable saved = timeTableRepository.save(timeTable);
        return convertToResponseDTO(saved);
    }

    @Override
    @Transactional
    public RecruitmentTimeTableResponseDTO updateTimeTable(Long id, RecruitmentTimeTableRequestDTO requestDTO) {
        RecruitmentTimeTable timeTable = timeTableRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("시간대를 찾을 수 없습니다. ID: " + id));

        timeTable.setTimeSlot(requestDTO.getTimeSlot());
        timeTable.setMaxCnt(requestDTO.getMaxCnt());

        RecruitmentTimeTable updated = timeTableRepository.save(timeTable);
        return convertToResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deleteTimeTable(Long id) {
        if (!timeTableRepository.existsById(id)) {
            throw new NotFoundException("시간대를 찾을 수 없습니다. ID: " + id);
        }
        timeTableRepository.deleteById(id);
    }

    @Override
    public List<RecruitmentTimeTableResponseDTO> getTimeTablesByRecruitmentId(Long recruitmentId) {
        List<RecruitmentTimeTable> timeTables = timeTableRepository.findByRecruitmentIdOrderByTimeSlotAsc(recruitmentId);
        return timeTables.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecruitmentTimeTableResponseDTO> getTimeTablesByRecruitmentIdAndDate(Long recruitmentId, LocalDate date) {
        List<RecruitmentTimeTable> timeTables = timeTableRepository.findByRecruitmentIdAndDateOrderByTimeSlotAsc(recruitmentId, date);
        return timeTables.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void incrementCount(Long recruitmentId, String timeSlot) {
        RecruitmentTimeTable timeTable = timeTableRepository
                .findByRecruitmentIdAndTimeSlot(recruitmentId, timeSlot)
                .orElse(null);

        if (timeTable == null) {
            timeTable = RecruitmentTimeTable.builder()
                    .recruitmentId(recruitmentId)
                    .timeSlot(timeSlot)
                    .maxCnt(0)
                    .count(1)
                    .build();
            timeTableRepository.save(timeTable);
            return;
        }

        if (timeTable.getMaxCnt() > 0 && timeTable.getCount() >= timeTable.getMaxCnt()) {
            throw new IllegalStateException("해당 시간대는 정원이 마감되었습니다.");
        }

        timeTable.setCount(timeTable.getCount() + 1);
        timeTableRepository.save(timeTable);
    }

    @Override
    @Transactional
    public void decrementCount(Long recruitmentId, String timeSlot) {
        RecruitmentTimeTable timeTable = timeTableRepository
                .findByRecruitmentIdAndTimeSlot(recruitmentId, timeSlot)
                .orElse(null);

        if (timeTable != null && timeTable.getCount() > 0) {
            timeTable.setCount(timeTable.getCount() - 1);
            timeTableRepository.save(timeTable);
        }
    }

    @Override
    @Transactional
    public void incrementCount(Long recruitmentId, LocalDate date, String timeSlot) {
        RecruitmentTimeTable timeTable = timeTableRepository
                .findByRecruitmentIdAndDateAndTimeSlot(recruitmentId, date, timeSlot)
                .orElse(null);

        if (timeTable == null) {
            timeTable = RecruitmentTimeTable.builder()
                    .recruitmentId(recruitmentId)
                    .date(date)
                    .timeSlot(timeSlot)
                    .maxCnt(0)
                    .count(1)
                    .build();
            timeTableRepository.save(timeTable);
            return;
        }

        if (timeTable.getMaxCnt() > 0 && timeTable.getCount() >= timeTable.getMaxCnt()) {
            throw new IllegalStateException("해당 시간대는 정원이 마감되었습니다.");
        }

        timeTable.setCount(timeTable.getCount() + 1);
        timeTableRepository.save(timeTable);
    }

    @Override
    @Transactional
    public void decrementCount(Long recruitmentId, LocalDate date, String timeSlot) {
        RecruitmentTimeTable timeTable = timeTableRepository
                .findByRecruitmentIdAndDateAndTimeSlot(recruitmentId, date, timeSlot)
                .orElse(null);

        if (timeTable != null && timeTable.getCount() > 0) {
            timeTable.setCount(timeTable.getCount() - 1);
            timeTableRepository.save(timeTable);
        }
    }

    private RecruitmentTimeTableResponseDTO convertToResponseDTO(RecruitmentTimeTable timeTable) {
        return RecruitmentTimeTableResponseDTO.builder()
                .id(timeTable.getId())
                .recruitmentId(timeTable.getRecruitmentId())
                .date(timeTable.getDate())
                .timeSlot(timeTable.getTimeSlot())
                .maxCnt(timeTable.getMaxCnt())
                .count(timeTable.getCount())
                .createdAt(timeTable.getCreatedAt())
                .build();
    }
}