package org.swarmcom.jsynapse.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentUpload {
    @JsonProperty("content_uri")
    private String contentUri;

    public ContentUpload(String contentUri) {
        this.contentUri = contentUri;
    }
}
