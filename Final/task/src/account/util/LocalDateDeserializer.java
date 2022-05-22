package account.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.time.LocalDate;

@Slf4j
public class LocalDateDeserializer extends StdDeserializer<LocalDate> {

    public LocalDateDeserializer() {
        super(LocalDate.class);
    }

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        try {
            return StringToDate(jsonParser.getText());
        } catch(NumberFormatException ex) {
            log.warn("Format of date is not valid. should be mm-YYYY");
            return null;
        }
    }

    public static LocalDate StringToDate(String date) {
        String[] monthAndYear = date.split("-");
        int month = Integer.valueOf(monthAndYear[0]);
        int year = Integer.valueOf(monthAndYear[1]);
        if(month < 1 || month > 12) {
            log.warn("month value of the date is not valid. (valid values: from 1 to 12)");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return LocalDate.of(year, month, 1);
    }
}