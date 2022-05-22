package account.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"employee", "period"})})
public class Payment {

    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank
    private String employee;

    private LocalDate period;

    @Min(value = 0, message = "salary must not be negative!")
    private long salary;

    @ManyToOne
    @JoinColumn
    private User user;
}