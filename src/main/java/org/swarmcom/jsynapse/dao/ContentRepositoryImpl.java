/*
 * (C) Copyright 2015 eZuce Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.swarmcom.jsynapse.dao;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Repository;
import org.swarmcom.jsynapse.domain.Content;
import org.swarmcom.jsynapse.service.content.ContentResource;

@Repository
public class ContentRepositoryImpl implements ContentRepository {

    @Autowired
    private ContentJpaRepository contentJpaRepository;

    @Override
    public String upload(InputStream inputStream, String fileName, String contentType) {
        try {
            byte[] data = inputStream.readAllBytes();
            Content content = new Content(fileName, contentType, data);
            Content savedContent = contentJpaRepository.save(content);
            return savedContent.getId();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload do arquivo", e);
        }
    }

    @Override
    public ContentResource download(String mediaId) {
        return contentJpaRepository.findById(mediaId)
                .map(content -> {
                    ByteArrayResource resource = new ByteArrayResource(content.getData());
                    return new ContentResource(content.getContentType(), content.getSize(), resource);
                })
                .orElse(null);
    }
}
