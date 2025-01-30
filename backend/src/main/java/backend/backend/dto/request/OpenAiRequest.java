package backend.backend.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Text;

import java.util.List;

@Getter
@Setter
@Builder
public class OpenAiRequest {
    @Builder.Default
    private String model = "gpt-4o";
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
    }

    @Getter
    @Setter
    public static class TextContent extends Content{
        private String text;
        public TextContent(String type, String text) {
            super(type);
            this.text = text;
        }
    }

    @Getter
    @Setter
    public static class ImageContent extends Content{
        private Image_url image_url;
        public ImageContent(String type, Image_url image_url) {
            super(type);
            this.image_url = image_url;
        }
    }

    @Getter
    @Setter
    @Builder
    public static class Image_url {
        private String url;
    }
}
