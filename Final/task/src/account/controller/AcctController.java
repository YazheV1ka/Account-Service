package account.controller;

import account.model.Payment;
import account.model.User;
import account.service.PaymentService;
import account.service.UserService;
import account.util.StatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@RestController
@RequestMapping("/api/acct")
public class AcctController {

    private PaymentService paymentService;
    private UserService userService;

    @Autowired
    public AcctController(PaymentService paymentService, UserService userService) {
        this.paymentService = paymentService;
        this.userService = userService;
    }

    @PostMapping("/payments")
    public StatusResponse payments(@RequestBody List<@Valid Payment> payments) {
        Supplier<UsernameNotFoundException> usernameNotFound =
                () -> new UsernameNotFoundException("username not found in database");

        log.info("saving payments {}", payments);
        try {
            payments.forEach(payment -> {
                String email = payment.getEmployee();
                Optional<User> optUser = userService.findByEmail(email);
                User user = optUser.orElseThrow(usernameNotFound);
                payment.setUser(user);
                paymentService.save(payment);
            });
            StatusResponse response = new StatusResponse();
            response.setStatus("Added successfully!");
            return response;
        }catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/payments")
    public StatusResponse changeSalary(@RequestBody Payment payment){
        Supplier<ResponseStatusException> badRequest =
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST);

        log.info("trying to update the payment: {}, for the user {}", payment, payment.getEmployee());
        try{
            Optional<Payment> optPayment = paymentService.findByEmployeeAndPeriod(
                    payment.getEmployee(), payment.getPeriod());
            Payment originalPayment = optPayment.orElseThrow(badRequest);
            originalPayment.setSalary(payment.getSalary());
            paymentService.save(originalPayment);
            StatusResponse response = new StatusResponse();
            response.setStatus("Updated successfully!");
            return response;
        }catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}