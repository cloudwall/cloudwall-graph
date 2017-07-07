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

import javax.activation.MimeType;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;

/**
 * Logical source for loading graph data.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public interface GraphInput {
    /**
     * Gets the underlying content type for this source.
     */
    @Nonnull
    MimeType getContentType();

    /**
     * Opens a reader on the stream with the platform's default character set.
     */
    @Nonnull
    Reader getReader() throws IOException;
}
