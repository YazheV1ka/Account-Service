package account.controller;

import account.model.Payment;
import account.model.User;
import account.service.PaymentService;
import account.service.UserService;
import account.util.EmployeePayment;
import account.util.LocalDateDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/empl")
public class EmplController {

    private UserService userService;
    private PaymentService paymentService;

    @Autowired
    public EmplController(UserService userService, PaymentService paymentService) {
        this.userService = userService;
        this.paymentService = paymentService;
    }

    @GetMapping("/payment")
    public ResponseEntity getPayments(Authentication authentication, @RequestParam(required = false) String period) {
        Supplier<ResponseStatusException> badRequest =
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST);
        User user = userService.findByEmail(authentication.getName()).get();

        if (period != null) {
            log.info("finding payment for period:" + period + " for the user: " + user.getName());
            LocalDate date = LocalDateDeserializer.StringToDate(period);

            Optional<Payment> optPayment = paymentService.findByEmployeeAndPeriod(user.getEmail(), date);
            Payment payment = optPayment.orElseThrow(badRequest);

            EmployeePayment employeePayment = new EmployeePayment();
            employeePayment.setName(user.getName());
            employeePayment.setLastname(user.getLastname());
            employeePayment.setPeriod(payment.getPeriod());
            employeePayment.setSalary(calculateSalaryToString(payment.getSalary()));
            return new ResponseEntity(employeePayment, HttpStatus.OK);
        }

        log.info("finding payments for user {}", user.getName());
        List<Payment> payments = paymentService.findByUserIdOrderByPeriodDesc(user.getId());
        List<EmployeePayment> employeePayments =
                payments.stream()
                        .map(payment -> {
                            return new EmployeePayment(user.getName(),
                                    user.getLastname(),
                                    payment.getPeriod(),
                                    calculateSalaryToString(payment.getSalary()));
                        })
                        .collect(Collectors.toList());
        log.info("payments found: {}", employeePayments);
        return new ResponseEntity(employeePayments, HttpStatus.OK);
    }

    private String calculateSalaryToString(long salary){
        long cents = salary % 100;
        long dollars = salary/100;
        return dollars  + " dollar(s) " + cents + " cent(s)";
    }
}