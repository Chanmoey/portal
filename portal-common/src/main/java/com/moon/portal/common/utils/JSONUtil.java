package com.moon.portal.common.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Chanmoey
 * @date 2023年06月02日
 */
public class JSONUtil {

    private JSONUtil() {
    }

    public static final String CODE = "code";

    public static final String STATUS = "status";

    public static final String DATA = "data";

    public static final String MESSAGE = "message";

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final JsonFactory jsonFactory = mapper.getFactory();

    public static ObjectNode createObjectNode() {
        return mapper.createObjectNode();
    }

    public static String toJONString(ObjectNode objectNode) {
        try {
            return mapper.writeValueAsString(objectNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
