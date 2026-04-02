package com.finance.dashboard.service;

import com.finance.dashboard.dto.request.FinancialRecordRequest;
import com.finance.dashboard.dto.response.FinancialRecordResponse;
import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.entity.FinancialRecord.RecordType;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.exception.ResourceNotFoundException;
import com.finance.dashboard.repository.FinancialRecordRepository;
import com.finance.dashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialRecordService {

    private final FinancialRecordRepository recordRepository;
    private final UserRepository userRepository;

    @Transactional
    public FinancialRecordResponse createRecord(FinancialRecordRequest request) {
        User currentUser = getCurrentUser();

        FinancialRecord record = FinancialRecord.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory().trim())
                .date(request.getDate())
                .notes(request.getNotes())
                .createdBy(currentUser)
                .deleted(false)
                .build();

        record = recordRepository.save(record);
        log.info("Record created: id={}, type={}, amount={}", record.getId(), record.getType(), record.getAmount());
        return toResponse(record);
    }

    @Transactional(readOnly = true)
    public Page<FinancialRecordResponse> getRecords(
            LocalDate startDate,
            LocalDate endDate,
            String category,
            RecordType type,
            Pageable pageable) {

        return recordRepository
                .findWithFilters(startDate, endDate, category, type, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public FinancialRecordResponse getRecordById(Long id) {
        FinancialRecord record = findActiveRecord(id);
        return toResponse(record);
    }

    @Transactional
    public FinancialRecordResponse updateRecord(Long id, FinancialRecordRequest request) {
        FinancialRecord record = findActiveRecord(id);

        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory().trim());
        record.setDate(request.getDate());
        record.setNotes(request.getNotes());

        record = recordRepository.save(record);
        log.info("Record updated: id={}", record.getId());
        return toResponse(record);
    }

    @Transactional
    public void deleteRecord(Long id) {
        FinancialRecord record = findActiveRecord(id);
        record.setDeleted(true); // soft delete
        recordRepository.save(record);
        log.info("Record soft-deleted: id={}", id);
    }

    @Transactional(readOnly = true)
    public Page<FinancialRecordResponse> searchRecords(String keyword, Pageable pageable) {
        return recordRepository.searchByKeyword(keyword, pageable).map(this::toResponse);
    }

    // --- Helpers ---

    private FinancialRecord findActiveRecord(Long id) {
        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Financial record", id));
        if (record.isDeleted()) {
            throw new ResourceNotFoundException("Financial record", id);
        }
        return record;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }

    public FinancialRecordResponse toResponse(FinancialRecord record) {
        return FinancialRecordResponse.builder()
                .id(record.getId())
                .amount(record.getAmount())
                .type(record.getType())
                .category(record.getCategory())
                .date(record.getDate())
                .notes(record.getNotes())
                .createdByName(record.getCreatedBy().getName())
                .createdByEmail(record.getCreatedBy().getEmail())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .build();
    }
}
