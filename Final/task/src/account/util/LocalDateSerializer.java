package account.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.time.LocalDate;

public class LocalDateSerializer extends StdSerializer<LocalDate> {

    public LocalDateSerializer() {
        super(LocalDate.class);
    }

    @Override
    public void serialize(LocalDate date, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(dateToString(date));
    }

    private String dateToString(LocalDate date) {
        String month = date.getMonth().toString().toLowerCase();
        String firstLetter = month.substring(0,1);
        month = month.replace(firstLetter, firstLetter.toUpperCase());
        String result = month + "-" + date.getYear();

        return result;
    }
}