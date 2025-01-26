package backend.backend.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class OpenAiRequest {
    @Builder.Default
    private String model = "gpt-4o-mini";
    private List<Message> messages;
    private Response_format response_format;

    @Getter
    @Setter
    @Builder
    public static class Response_format {
        private String type;
    }

    @Getter
    @Setter
    @Builder
    public static class Message {
        private String role;
        private List<Content> content;
    }

    @Getter
    @Setter
    @Builder
    public static class Content {
        private String type;
        private Image_url image_url;
        private String text;
    }

    @Getter
    @Setter
    @Builder
    public static class Image_url {
        private String url;
    }
}
