package org.swarmcom.jsynapse.service.content;

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.swarmcom.jsynapse.dao.ContentRepository;
import org.swarmcom.jsynapse.domain.ContentUpload;

import java.io.InputStream;

import static java.lang.String.format;

@RequiredArgsConstructor(onConstructor_ = {@Inject})
@Service
@Validated
public class ContentServiceImpl implements ContentService {
    @Value("${jsynapse.domain}")
    private String domain;

    private final ContentRepository contentRepository;

    @Override
    public ContentUpload upload(InputStream body, String fileName, String contentType) {
        String mediaId = contentRepository.upload(body, fileName, contentType);
        return new ContentUpload(format("mxc://%s/%s", domain, mediaId));
    }

    @Override
    public ContentResource download(String serverName, String mediaId) {
        return contentRepository.download(mediaId);
    }

    @Override
    public ContentResource getThumbnail(String serverName, String mediaId, Integer width, Integer height, String method) {
        // TODO use thumbnailator to resize images / figure out better ways to do this
        return contentRepository.download(mediaId);
    }
}
