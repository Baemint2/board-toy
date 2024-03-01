package org.example.board.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.example.board.domain.posts.entity.Category;

import java.io.IOException;

public class CategoryDeserializer extends JsonDeserializer<Category> {
    @Override
    public Category deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String value = jsonParser.getText();
        if(value.isEmpty()) {
            return null;
        }
        try {
            return Category.valueOf(value.toUpperCase());
        } catch (IllegalStateException e) {
            return null;
        }
    }
}
