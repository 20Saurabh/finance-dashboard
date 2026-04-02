package com.finance.dashboard.config;

import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.entity.FinancialRecord.RecordType;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.entity.User.Role;
import com.finance.dashboard.entity.User.Status;
import com.finance.dashboard.repository.FinancialRecordRepository;
import com.finance.dashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FinancialRecordRepository recordRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return; // Already seeded
        }

        log.info("Seeding initial data...");

        // Create users
        User admin = userRepository.save(User.builder()
                .name("Admin User")
                .email("admin@finance.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .status(Status.ACTIVE)
                .build());

        User analyst = userRepository.save(User.builder()
                .name("Analyst User")
                .email("analyst@finance.com")
                .password(passwordEncoder.encode("analyst123"))
                .role(Role.ANALYST)
                .status(Status.ACTIVE)
                .build());

        userRepository.save(User.builder()
                .name("Viewer User")
                .email("viewer@finance.com")
                .password(passwordEncoder.encode("viewer123"))
                .role(Role.VIEWER)
                .status(Status.ACTIVE)
                .build());

        // Create sample financial records
        List<FinancialRecord> records = List.of(
                buildRecord(new BigDecimal("5000.00"), RecordType.INCOME, "Salary", LocalDate.now().minusMonths(1).withDayOfMonth(1), "Monthly salary", admin),
                buildRecord(new BigDecimal("1200.00"), RecordType.EXPENSE, "Rent", LocalDate.now().minusMonths(1).withDayOfMonth(5), "Monthly rent", admin),
                buildRecord(new BigDecimal("350.00"), RecordType.EXPENSE, "Groceries", LocalDate.now().minusMonths(1).withDayOfMonth(10), "Monthly groceries", admin),
                buildRecord(new BigDecimal("200.00"), RecordType.EXPENSE, "Utilities", LocalDate.now().minusMonths(1).withDayOfMonth(15), "Electric and water", admin),
                buildRecord(new BigDecimal("800.00"), RecordType.INCOME, "Freelance", LocalDate.now().minusMonths(1).withDayOfMonth(20), "Freelance project", admin),
                buildRecord(new BigDecimal("5000.00"), RecordType.INCOME, "Salary", LocalDate.now().withDayOfMonth(1), "Monthly salary", admin),
                buildRecord(new BigDecimal("1200.00"), RecordType.EXPENSE, "Rent", LocalDate.now().withDayOfMonth(5), "Monthly rent", admin),
                buildRecord(new BigDecimal("150.00"), RecordType.EXPENSE, "Dining", LocalDate.now().withDayOfMonth(8), "Restaurants and takeout", admin),
                buildRecord(new BigDecimal("500.00"), RecordType.INCOME, "Investment", LocalDate.now().withDayOfMonth(10), "Stock dividend", admin),
                buildRecord(new BigDecimal("90.00"), RecordType.EXPENSE, "Subscriptions", LocalDate.now().withDayOfMonth(12), "Netflix, Spotify, etc.", admin)
        );

        recordRepository.saveAll(records);
        log.info("Seeded {} users and {} records", 3, records.size());
        log.info("Admin credentials: admin@finance.com / admin123");
        log.info("Analyst credentials: analyst@finance.com / analyst123");
        log.info("Viewer credentials: viewer@finance.com / viewer123");
    }

    private FinancialRecord buildRecord(BigDecimal amount, RecordType type, String category,
                                        LocalDate date, String notes, User user) {
        return FinancialRecord.builder()
                .amount(amount)
                .type(type)
                .category(category)
                .date(date)
                .notes(notes)
                .createdBy(user)
                .deleted(false)
                .build();
    }
}
