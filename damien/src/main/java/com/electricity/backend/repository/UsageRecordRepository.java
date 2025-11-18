package com.electricity.backend.repository;

import com.electricity.backend.entity.UsageRecord;
import com.electricity.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UsageRecordRepository extends JpaRepository<UsageRecord, Long> {

    List<UsageRecord> findByUserAndUsageDateBetweenOrderByUsageDateDesc(
            User user, LocalDate startDate, LocalDate endDate);

    @Query("SELECT ur FROM UsageRecord ur WHERE ur.user = :user AND " +
           "YEAR(ur.usageDate) = :year AND MONTH(ur.usageDate) = :month " +
           "ORDER BY ur.usageDate DESC")
    List<UsageRecord> findByUserAndYearAndMonth(
            @Param("user") User user,
            @Param("year") int year,
            @Param("month") int month);

    @Query("SELECT SUM(ur.unitsConsumed) FROM UsageRecord ur WHERE ur.user = :user AND " +
           "YEAR(ur.usageDate) = :year AND MONTH(ur.usageDate) = :month")
    Double findTotalUsageByUserAndYearAndMonth(
            @Param("user") User user,
            @Param("year") int year,
            @Param("month") int month);

    @Query("SELECT ur FROM UsageRecord ur WHERE ur.user = :user " +
           "ORDER BY ur.usageDate DESC")
    List<UsageRecord> findByUserOrderByUsageDateDesc(@Param("user") User user);

    List<UsageRecord> findTop7ByUserOrderByUsageDateDesc(User user);

    @Query("SELECT COALESCE(SUM(ur.unitsConsumed), 0) FROM UsageRecord ur " +
           "WHERE ur.user = :user AND ur.usageDate >= :startDate")
    Double findTotalUsageSince(@Param("user") User user, @Param("startDate") LocalDate startDate);
}