package com.github.chrisprice.phonegapbuild.api;
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU General Public License Version 2 only ("GPL") or
 * the Common Development and Distribution License("CDDL") (collectively, the "License"). You may not use this file
 * except in compliance with the License. You can obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html or packager/legal/LICENSE.txt. See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each file and include the License file at
 * packager/legal/LICENSE.txt.
 * 
 * GPL Classpath Exception: Oracle designates this particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that accompanied this code.
 * 
 * Modifications: If applicable, add the following below the License Header, with the fields enclosed by brackets []
 * replaced by your own identifying information: "Portions Copyright [year] [name of copyright owner]"
 * 
 * Contributor(s): If you wish your version of this file to be governed by only the CDDL or only the GPL Version 2,
 * indicate your decision by adding "[Contributor] elects to include this software in this distribution under the [CDDL
 * or GPL Version 2] license." If you don't indicate a single choice of license, a recipient has the option to
 * distribute your version of this file under either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above. However, if you add GPL Version 2 code and therefore, elected the GPL Version 2
 * license, then the option applies only if the new code is made subject to such option by the copyright holder.
 */
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Providers;

import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.Boundary;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import com.sun.jersey.multipart.impl.MultiPartWriter;

@Produces("multipart/*")
public class PatchedMultiPartWriter extends MultiPartWriter {

  private static final Annotation[] EMPTY_ANNOTATIONS = new Annotation[0];

  private final Providers providers;

  public PatchedMultiPartWriter(@Context Providers providers) {
    super(providers);
    this.providers = providers;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public void writeTo(MultiPart entity, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
      MultivaluedMap<String, Object> headers, OutputStream stream) throws IOException, WebApplicationException {
    // Verify that there is at least one body part
    if ((entity.getBodyParts() == null) || (entity.getBodyParts().size() < 1)) {
        throw new WebApplicationException(new IllegalArgumentException("Must specify at least one body part"));
    }

    // If our entity is not nested, make sure the MIME-Version header is set
    if (entity.getParent() == null) {
        Object value = headers.getFirst("MIME-Version");
        if (value == null) {
            headers.putSingle("MIME-Version", "1.0");
        }
    }

    // Initialize local variables we need
    final Writer writer = new BufferedWriter(new OutputStreamWriter(stream)); // FIXME - charset???

    // Determine the boundary string to be used, creating one if needed
    final MediaType boundaryMediaType = Boundary.addBoundary(mediaType);
    if (boundaryMediaType != mediaType) {
        headers.putSingle(HttpHeaders.CONTENT_TYPE, boundaryMediaType);
    }

    final String boundaryString = boundaryMediaType.getParameters().get("boundary");
    // Iterate through the body parts for this message
    boolean isFirst = true;
    for (final BodyPart bodyPart : entity.getBodyParts()) {

        // Write the leading boundary string
        if (isFirst) {
            isFirst = false;
            writer.write("--");
        } else {
            writer.write("\r\n--");
        }
        writer.write(boundaryString);
        writer.write("\r\n");

        // Write the headers for this body part
        final MediaType bodyMediaType = bodyPart.getMediaType();
        if (bodyMediaType == null) {
            throw new WebApplicationException(new IllegalArgumentException("Missing body part media type"));
        }
        final MultivaluedMap<String, String> bodyHeaders = bodyPart.getHeaders();
        bodyHeaders.putSingle("Content-Type", bodyMediaType.toString());

        if (bodyHeaders.getFirst("Content-Disposition") == null
                && bodyPart.getContentDisposition() != null) {
            bodyHeaders.putSingle("Content-Disposition", bodyPart.getContentDisposition().toString());
        }

        // Iterate for the nested body parts
        for (final Map.Entry<String, List<String>> entry : bodyHeaders.entrySet()) {

            // Only headers that match "Content-*" are allowed on body parts
            if (!entry.getKey().toLowerCase().startsWith("content-")) {
                throw new WebApplicationException(new IllegalArgumentException("Invalid body part header '" + entry.getKey() + "', only Content-* allowed"));
            }

            // http://community.phonegap.com/nitobi/topics/unable_to_create_a_new_app_using_the_phonegap_build_api_v0
            // http://hc.apache.org/httpcomponents-client-ga/httpmime/clover/org/apache/http/entity/mime/HttpMultipart.html?line=191#src-191
            // copy logic from the second link because of the first link
            if (!(entry.getKey().equals("Content-Disposition") || (bodyPart instanceof FileDataBodyPart && entry.getKey()
                .equals("Content-Type")))) {
              continue;
            }

            // Write this header and its value(s)
            writer.write(entry.getKey());
            writer.write(':');
            boolean first = true;
            for (String value : entry.getValue()) {
                if (first) {
                    writer.write(' ');
                    first = false;
                } else {
                    writer.write(',');
                }
                writer.write(value);
            }
            writer.write("\r\n");
        }

        // Mark the end of the headers for this body part
        writer.write("\r\n");
        writer.flush();

        // Write the entity for this body part
        Object bodyEntity = bodyPart.getEntity();
        if (bodyEntity == null) {
            throw new WebApplicationException(
                    new IllegalArgumentException("Missing body part entity of type '" + bodyMediaType + "'"));
        }

        Class bodyClass = bodyEntity.getClass();
        if (bodyEntity instanceof BodyPartEntity) {
            bodyClass = InputStream.class;
            bodyEntity = ((BodyPartEntity) bodyEntity).getInputStream();
        }

        final MessageBodyWriter bodyWriter = providers.getMessageBodyWriter(
                bodyClass,
                bodyClass,
                EMPTY_ANNOTATIONS,
                bodyMediaType);

        if (bodyWriter == null) {
            throw new WebApplicationException(
                    new IllegalArgumentException(
                    "No MessageBodyWriter for body part of type '" +
                    bodyEntity.getClass().getName() + "' and media type '" +
                    bodyMediaType + "'"));
        }

        bodyWriter.writeTo(
                bodyEntity,
                bodyClass,
                bodyClass,
                EMPTY_ANNOTATIONS,
                bodyMediaType,
                bodyHeaders,
                stream);
    }

    // Write the final boundary string
    writer.write("\r\n--");
    writer.write(boundaryString);
    writer.write("--\r\n");
    writer.flush();
  }
}
