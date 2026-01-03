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

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;
import org.swarmcom.jsynapse.service.content.ContentResource;

import java.io.InputStream;

@Repository
public class ContentRepositoryImpl implements ContentRepository {
    @Autowired
    GridFsTemplate gridFsTemplate;

    @Override
    public String upload(InputStream content, String fileName, String contentType) {
        var fileId = gridFsTemplate.store(content, fileName, contentType);
        return fileId.toString();
    }

    @Override
    public ContentResource download(String mediaId) {
        GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(mediaId)));

        if (file == null) {
            return null;
        }

        GridFsResource gridFsResource = gridFsTemplate.getResource(file);

        try {
            return new ContentResource(gridFsResource.getContentType(), gridFsResource.contentLength(), gridFsResource);
        } catch (Exception e) {
            // Handle exceptions (such as I/O issues)
            return null;
        }
    }
}
