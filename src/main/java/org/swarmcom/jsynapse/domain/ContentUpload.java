package org.swarmcom.jsynapse.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

//@Entity

@Getter
@Setter
public class ContentUpload {

    @JsonProperty("content_uri")
    private String contentUri;

    public ContentUpload(String contentUri) {
        this.contentUri = contentUri;
    }
}
