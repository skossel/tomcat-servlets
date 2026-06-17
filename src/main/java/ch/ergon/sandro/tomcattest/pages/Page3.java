package ch.ergon.sandro.tomcattest.pages;

import ch.ergon.sandro.tomcattest.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class Page3 implements Page {

    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public Map<String, String> getPageValues() {
        Map<String, String> values = new HashMap<>();
        try {
            String url = "https://catalog.redhat.com/api/containers/v1/repositories/registry/registry.access.redhat.com/repository/ubi9/images" +
                    "?filter=architecture==amd64" +
                    "&sort_by=last_update_date[desc]" +
                    "&page_size=10" +
                    "&include=data.parsed_data.labels";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode root = mapper.readTree(response.body());
                JsonNode data = root.path("data");

                StringBuilder versionsHtml = new StringBuilder();
                if (data.isArray()) {
                    for (JsonNode image : data) {
                        JsonNode labels = image.path("parsed_data").path("labels");
                        String version = getValueOf(labels, "version");
                        String release = getValueOf(labels, "release");
                        if (version != null && release != null) {
                            versionsHtml.append("<li>").append(version).append("-").append(release).append("</li>");
                        }
                    }
                }
                values.put("versions", versionsHtml.toString());
            } else {
                values.put("versions", "<li>Error fetching versions: HTTP " + response.statusCode() + "</li>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            values.put("versions", "<li>Error fetching versions: " + e.getMessage() + "</li>");
        }

        return values;
    }

    private String getValueOf(JsonNode labels, String name) {
        if (labels.isArray()) {
            for (JsonNode label : labels) {
                if (name.equals(label.path("name").asText())) {
                    return label.path("value").asText();
                }
            }
        }
        return null;
    }
}
