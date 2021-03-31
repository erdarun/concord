package com.walmartlabs.concord.runtime.v2.serializer;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2020 Walmart Inc.
 * -----
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =====
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.walmartlabs.concord.runtime.v2.model.ProcessDefinition;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.walmartlabs.concord.runtime.v2.serializer.SerializerUtils.writeNotEmptyObjectField;

public class DurationSerializer extends StdSerializer<Duration> {

    private static final long serialVersionUID = 1L;

    public DurationSerializer() {
        this(null);
    }

    public DurationSerializer(Class<Duration> t) {
        super(t);
    }

    @Override
    public void serialize(Duration value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value != null) {
            gen.writeString(value.toString());
        }
    }
}
