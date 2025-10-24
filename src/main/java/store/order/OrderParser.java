package store.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.ArrayList;

public final class OrderParser {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private OrderParser() {}

    public static List<OrderItem> parseItemsJson(String json) {
        if (json == null || json.isBlank()) return new ArrayList<>();
        try {
            return MAPPER.readValue(json, new TypeReference<List<OrderItem>>() {});
        } catch (JsonProcessingException e) {
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
