package com.walmartlabs.concord.server.process;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2018 Walmart Inc.
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

import com.walmartlabs.concord.project.model.ProjectDefinition;
import com.walmartlabs.concord.server.org.policy.PolicyEntry;
import com.walmartlabs.concord.server.process.keys.AttachmentKey;
import com.walmartlabs.concord.server.process.keys.HeaderKey;

import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.*;

public class Payload {

    public static final HeaderKey<UUID> PARENT_INSTANCE_ID = HeaderKey.register("_parentInstanceId", UUID.class);
    public static final HeaderKey<UUID> ORGANIZATION_ID = HeaderKey.register("_orgId", UUID.class);
    public static final HeaderKey<UUID> PROJECT_ID = HeaderKey.register("_projectId", UUID.class);
    public static final HeaderKey<UUID> REPOSITORY_ID = HeaderKey.register("_repoId", UUID.class);
    public static final HeaderKey<String> ENTRY_POINT = HeaderKey.register("_entryPoint", String.class);
    public static final HeaderKey<String> INITIATOR = HeaderKey.register("_initiator", String.class);
    public static final HeaderKey<UUID> INITIATOR_ID = HeaderKey.register("_initiatorId", UUID.class);
    public static final HeaderKey<Path> WORKSPACE_DIR = HeaderKey.register("_workspace", Path.class);
    public static final HeaderKey<Path> BASE_DIR = HeaderKey.register("_baseDir", Path.class);
    public static final HeaderKey<Map> REQUEST_DATA_MAP = HeaderKey.register("_meta", Map.class);
    public static final HeaderKey<String> RESUME_EVENT_NAME = HeaderKey.register("_resumeEventName", String.class);
    public static final HeaderKey<ProjectDefinition> PROJECT_DEFINITION = HeaderKey.register("_projectDef", ProjectDefinition.class);
    public static final HeaderKey<List<String>> ACTIVE_PROFILES = HeaderKey.registerList("_activeProfiles");
    public static final HeaderKey<ProcessKind> PROCESS_KIND = HeaderKey.register("_processKind", ProcessKind.class);
    public static final HeaderKey<Set<String>> PROCESS_TAGS = HeaderKey.registerSet("_processTags");
    public static final HeaderKey<Set<String>> OUT_EXPRESSIONS = HeaderKey.registerSet("_outExpr");
    public static final HeaderKey<Map<String, Object>> REQUIREMENTS = HeaderKey.registerMap("_requirements");
    public static final HeaderKey<PolicyEntry> POLICY = HeaderKey.register("_policy", PolicyEntry.class);
    public static final HeaderKey<Set<String>> PROCESS_HANDLERS = HeaderKey.registerSet("_processHandlers");

    public static final AttachmentKey WORKSPACE_ARCHIVE = AttachmentKey.register("archive");

    private final UUID instanceId;
    private final Timestamp createdAt;
    private final Map<String, Object> headers;
    private final Map<String, Path> attachments;

    public Payload(UUID instanceId, Timestamp createdAt) {
        this.instanceId = instanceId;
        this.createdAt = createdAt;
        this.headers = Collections.emptyMap();
        this.attachments = Collections.emptyMap();
    }

    private Payload(Payload old, Map<String, Object> headers, Map<String, Path> attachments) {
        this.instanceId = old.instanceId;
        this.createdAt = old.createdAt;
        this.headers = Objects.requireNonNull(headers, "Headers map cannot be null");
        this.attachments = Objects.requireNonNull(attachments, "Attachments map cannot be null");
    }

    public UUID getInstanceId() {
        return instanceId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public <T> T getHeader(HeaderKey<T> key) {
        return key.cast(headers.get(key.name()));
    }

    public <T> T getHeader(HeaderKey<T> key, T defaultValue) {
        Object v = headers.get(key.name());
        if (v == null) {
            return defaultValue;
        }
        return key.cast(v);
    }

    @SuppressWarnings("unchecked")
    public <T> T getHeader(String key) {
        return (T) headers.get(key);
    }

    public Map<String, Object> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public <T> Payload putHeader(HeaderKey<T> key, T value) {
        Map<String, Object> m = new HashMap<>(headers);
        m.put(key.name(), key.cast(value));
        return new Payload(this, m, this.attachments);
    }

    public Payload putHeaders(Map<String, Object> values) {
        Map<String, Object> m = new HashMap<>(headers);
        m.putAll(values);
        return new Payload(this, m, this.attachments);
    }

    public Payload removeHeader(HeaderKey<?> key) {
        Map<String, Object> m = new HashMap<>(headers);
        m.remove(key.name());
        return new Payload(this, m, this.attachments);
    }

    @SuppressWarnings("unchecked")
    public Payload mergeValues(HeaderKey<Map> key, Map values) {
        Map o = getHeader(key);
        Map n = new HashMap(o != null ? o : Collections.emptyMap());
        n.putAll(values);
        return putHeader(key, n);
    }

    public Path getAttachment(AttachmentKey key) {
        return key.cast(attachments.get(key.name()));
    }

    public Map<String, Path> getAttachments() {
        return Collections.unmodifiableMap(attachments);
    }

    public Payload putAttachment(AttachmentKey key, Path value) {
        Map<String, Path> m = new HashMap<>(attachments);
        m.put(key.name(), value);
        return new Payload(this, this.headers, m);
    }

    public Payload putAttachments(Map<String, Path> values) {
        Map<String, Path> m = new HashMap<>(attachments);
        m.putAll(values);
        return new Payload(this, this.headers, m);
    }

    public Payload removeAttachment(AttachmentKey key) {
        if (!attachments.containsKey(key.name())) {
            return this;
        }

        return removeAttachment(key.name());
    }

    public Payload removeAttachment(String key) {
        Map<String, Path> m = new HashMap<>(attachments);
        m.remove(key);
        return new Payload(this, this.headers, m);
    }

    public Payload clearAttachments() {
        return new Payload(this, this.headers, Collections.emptyMap());
    }

    @Override
    public String toString() {
        return "Payload{" +
                "instanceId=" + instanceId +
                ", headers=" + headers +
                ", attachments=" + attachments +
                '}';
    }
}
