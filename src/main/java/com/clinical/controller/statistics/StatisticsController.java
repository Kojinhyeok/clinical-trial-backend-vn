package com.clinical.controller.statistics;

import com.clinical.dto.statistics.CumulativeDataRequestDTO;
import com.clinical.dto.statistics.CumulativeDataResponseDTO;
import com.clinical.service.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<Map<String, Integer>> getStatistics() {
        return ResponseEntity.ok(statisticsService.getMainStatistics());
    }

    @GetMapping("/cumulative")
    public ResponseEntity<List<CumulativeDataResponseDTO>> getAllCumulativeData() {
        return ResponseEntity.ok(statisticsService.getAllCumulativeData());
    }

    @GetMapping("/cumulative/{id}")
    public ResponseEntity<CumulativeDataResponseDTO> getCumulativeDataById(@PathVariable Long id) {
        return ResponseEntity.ok(statisticsService.getCumulativeDataById(id));
    }

    @PostMapping("/cumulative")
    public ResponseEntity<CumulativeDataResponseDTO> createCumulativeData(@RequestBody CumulativeDataRequestDTO.Create request) {
        return ResponseEntity.ok(statisticsService.createCumulativeData(request));
    }

    @PutMapping("/cumulative/{id}")
    public ResponseEntity<CumulativeDataResponseDTO> updateCumulativeData(@PathVariable Long id, @RequestBody CumulativeDataRequestDTO.Update request) {
        return ResponseEntity.ok(statisticsService.updateCumulativeData(id, request));
    }

    @DeleteMapping("/cumulative/{id}")
    public ResponseEntity<Void> deleteCumulativeData(@PathVariable Long id) {
        statisticsService.deleteCumulativeData(id);
        return ResponseEntity.ok().build();
    }
}