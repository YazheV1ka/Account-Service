package account.jsonmapper;

import account.util.LocalDateDeserializer;
import account.util.LocalDateSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.time.LocalDate;

public class LocalDateObjectMapper extends ObjectMapper {
    public LocalDateObjectMapper() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        simpleModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        registerModule(simpleModule);
    }
}