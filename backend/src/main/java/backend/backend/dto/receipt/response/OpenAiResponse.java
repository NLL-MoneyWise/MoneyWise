package backend.backend.dto.receipt.response;


import lombok.Getter;

import java.util.List;

@Getter
public class OpenAiResponse {
    private List<Choice> choices;

    @Getter
    public static class Choice {
        private int index;
        private Message message;
    }

    @Getter
    public static class Message {
        private String role;
        private String content;
    }
}
