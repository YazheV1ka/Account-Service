package account.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class DateByMonthAndDayConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(String monthAndYear) {
        String[] tokens = monthAndYear.split("-");
        int month = Integer.valueOf(tokens[0]);
        int year = Integer.valueOf(tokens[1]);
        LocalDate date = LocalDate.of(year,month,0);
        return date;
    }
}