package account.repository;

import account.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByUserIdOrderByPeriodDesc(int id);
    Optional<Payment> findByEmployeeAndPeriod(String employee, LocalDate period);
}