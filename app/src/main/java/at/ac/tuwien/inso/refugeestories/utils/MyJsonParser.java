package at.ac.tuwien.inso.refugeestories.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by mtraxler on 17.12.2015.
 */
public class MyJsonParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public MyJsonParser() { }

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T parseJson(String jsonContent, Class<T> expected) {
        try {
            return objectMapper.readValue(jsonContent, expected);
        } catch (IOException e) {
            throw new RuntimeException("Could not read json values!", e);
        }
    }

    public static String toJson(Object object) {
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, object);
            return writer.getBuffer().toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not write json!", e);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                //ignore..
            }
        }
    }
}
