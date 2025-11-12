package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public  class DataReader {
    /**
     * Reads a JSON object from a resource file.
     *
     * @param resourceName the name of the resource file (e.g., "data.json")
     * @return a JSONObject parsed from the resource file
     */
    public static JSONObject readFromResource(String resourceName) {
        JSONParser parser = new JSONParser();
        try (Reader reader = new InputStreamReader(
                Objects.requireNonNull(DataReader.class.getClassLoader().getResourceAsStream(resourceName)),
                StandardCharsets.UTF_8)) {
            return (JSONObject) parser.parse(reader);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read resource: " + resourceName, e);
        }
    }
}