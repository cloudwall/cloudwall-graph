/*
 * (C) Copyright 2017 Kyle F. Downey.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cloudwall.graph.io;

import javax.activation.FileTypeMap;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Source used when reading from a File, HTTP URL or other external location. Note that automatic content type
 * identification depends on the filename extension, so it is not guaranteed to work if the file extension does
 * not match one of the supported types (currently: DOT, TLP, GML).
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
@SuppressWarnings("WeakerAccess")
public class ExternalGraphInput implements GraphInput {
    private final URL location;
    private final MimeType contentType;

    /**
     * Factory method for the common case of creating a GraphInput from a file.
     */
    public static GraphInput forFile(File f) throws MalformedURLException, MimeTypeParseException {
        return new ExternalGraphInput(f.toURI().toURL(), new MimetypesFileTypeMap());
    }

    public ExternalGraphInput(@Nonnull URL location, @Nonnull MimeType contentType) {
        this.location = location;
        this.contentType = contentType;
    }

    public ExternalGraphInput(@Nonnull URL location, @Nonnull FileTypeMap fileTypeMap) throws MimeTypeParseException {
        this.location = location;
        String fileName = location.getFile();
        String contentTypeTxt = fileTypeMap.getContentType(fileName);
        this.contentType = new MimeType(contentTypeTxt);
    }

    @Nonnull
    @Override
    public MimeType getContentType() {
        return contentType;
    }

    @Nonnull
    @Override
    public Reader getReader() throws IOException {
        return new InputStreamReader(location.openStream());
    }
}
