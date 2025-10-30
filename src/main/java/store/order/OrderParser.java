package store.order;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;
import java.util.ArrayList;

public final class OrderParser {

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.setVisibility(
            MAPPER.getSerializationConfig()
                .getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
        );
    }

    private OrderParser() {}

    public static List<OrderItem> parseItemsJson(String json) {
        if (json == null || json.isBlank()) return new ArrayList<>();
        try {
            return MAPPER.readValue(json, new TypeReference<List<OrderItem>>() {});
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing items JSON: " + e.getMessage());
            System.err.println("JSON content: " + json);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static String writeItemsJson(List<OrderItem> items) {
        if (items == null) return null;
        try {
            return MAPPER.writeValueAsString(items);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
