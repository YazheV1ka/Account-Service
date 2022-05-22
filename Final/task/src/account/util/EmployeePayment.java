package account.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePayment {
    private String name;
    private String lastname;
    private LocalDate period;
    private String salary;
}