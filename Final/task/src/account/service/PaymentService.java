package account.service;

import account.model.Payment;
import account.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> findByUserIdOrderByPeriodDesc(int id){
        List<Payment> payments = paymentRepository.findByUserIdOrderByPeriodDesc(id);
        if(payments.isEmpty()){
            payments = Collections.emptyList();
        }
        return payments;

    }
    @Transactional
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Optional<Payment> findByEmployeeAndPeriod(String employee, LocalDate period) {
        return paymentRepository.findByEmployeeAndPeriod(employee.toLowerCase(), period);
    }
}