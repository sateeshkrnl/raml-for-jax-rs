/*
 * Copyright 2013-2017 (c) MuleSoft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.raml.emitter.plugins;

import org.raml.api.RamlMediaType;
import org.raml.api.RamlResourceMethod;
import org.raml.emitter.types.TypeRegistry;
import org.raml.utilities.IndentedAppendable;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by Jean-Philippe Belanger on 3/25/17. Just potential zeroes and ones
 */
public class DefaultResponseHandler implements ResponseHandler {


  @Override
  public int handlesResponses(RamlResourceMethod method) {
    return 0;
  }

  @Override
  public void writeResponses(TypeRegistry typeRegistry, IndentedAppendable writer,
                             RamlResourceMethod method, TypeSelector selector)
      throws IOException {

    if (!method.getProducedType().isPresent()) {
      return;
    }
    Type type = method.getProducedType().get().getType();

    // We have no clue what the error responses are, however, we want to generate
    // well formed raml, so we pick one.
    writer.appendLine("200:");
    writer.indent();

    writer.appendLine("body:");
    for (RamlMediaType producedMediaType : method.getProducedMediaTypes()) {

      writer.indent();

      TypeHandler typeHandler = selector.pickTypeWriter(method, producedMediaType);
      typeHandler.writeType(typeRegistry, writer, producedMediaType, method, type);
      writer.outdent();
    }

    writer.outdent();
  }
}
