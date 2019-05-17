package platfrom.web.handler;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

/*** エラーレスポンスのマッピング用クラス(Json) */

@Value
public class ErrorResponse {
    @JsonProperty("error")
    private Error error;

    public ErrorResponse(String code, String message) {
        this.error = new Error(code, message);
    }

    @Value
    private class Error {
        @JsonProperty("code")
        private final String code;
        @JsonProperty("message")
        private final String message;
    }
}

