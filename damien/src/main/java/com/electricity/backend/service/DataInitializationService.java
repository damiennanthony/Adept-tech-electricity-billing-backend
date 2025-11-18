package com.electricity.backend.service;

import com.electricity.backend.entity.User;
import com.electricity.backend.entity.UsageRecord;
import com.electricity.backend.repository.UserRepository;
import com.electricity.backend.repository.UsageRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

@Service
public class DataInitializationService implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UsageRecordRepository usageRecordRepository;
    private final PasswordEncoder passwordEncoder;
    private final BillingService billingService;

    @Autowired
    public DataInitializationService(
            UserRepository userRepository,
            UsageRecordRepository usageRecordRepository,
            PasswordEncoder passwordEncoder,
            BillingService billingService
    ) {
        this.userRepository = userRepository;
        this.usageRecordRepository = usageRecordRepository;
        this.passwordEncoder = passwordEncoder;
        this.billingService = billingService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            initializeData();
        }
    }

    private void initializeData() {
        // Create admin user
        User admin = new User();
        admin.setAccountNumber("ADMIN001");
        admin.setUsername("Administrator");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(User.Role.ADMIN);
        userRepository.save(admin);

        // Create sample users
        String[] accountNumbers = {"USR001", "USR002", "USR003", "USR004", "USR005"};
        String[] usernames = {"John Doe", "Jane Smith", "Robert Johnson", "Emily Davis", "Michael Wilson"};

        Random random = new Random();

        for (int i = 0; i < accountNumbers.length; i++) {
            User user = new User();
            user.setAccountNumber(accountNumbers[i]);
            user.setUsername(usernames[i]);
            user.setPassword(passwordEncoder.encode("password123"));
            user.setRole(User.Role.USER);
            User savedUser = userRepository.save(user);

            // Generate usage data for last 6 months
            generateUsageDataForUser(savedUser, random);
        }

        System.out.println("Sample data initialized successfully!");
        System.out.println("Admin credentials - Account: ADMIN001, Password: admin123");
        System.out.println("User credentials - Account: USR001-USR005, Password: password123");
    }

    private void generateUsageDataForUser(User user, Random random) {
        LocalDate startDate = LocalDate.now().minusMonths(6);
        LocalDate endDate = LocalDate.now();

        // Generate monthly patterns with some randomness
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            // Skip some days to make data more realistic (not every day has usage)
            if (random.nextDouble() < 0.7) { // 70% chance of usage on any given day
                double baseUsage = 15 + random.nextGaussian() * 5; // Base around 15 kWh with variation

                // Add seasonal variation
                int month = date.getMonthValue();
                if (month >= 12 || month <= 2) { // Winter months
                    baseUsage *= 1.3;
                } else if (month >= 6 && month <= 8) { // Summer months
                    baseUsage *= 1.2;
                }

                // Ensure positive usage
                double unitsConsumed = Math.max(1.0, baseUsage);

                UsageRecord record = new UsageRecord();
                record.setUser(user);
                record.setUsageDate(date);
                record.setUnitsConsumed(unitsConsumed);

                BillingService.BillingResult billing = billingService.calculateBill(unitsConsumed);
                record.setBillAmount(billing.getBillAmount());
                record.setBillingTier(billing.getBillingTier());

                usageRecordRepository.save(record);
            }
        }
    }
}