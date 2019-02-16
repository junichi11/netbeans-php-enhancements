/*
 * Copyright 2019 junichi11.
 *
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
 */
package com.junichi11.netbeans.php.enhancements.editor.completion;

import java.util.HashMap;
import java.util.List;
import org.netbeans.api.annotations.common.CheckForNull;

/**
 *
 * @author junichi11
 */
public enum SuperGlobalArray {

    ENV {
                @Override
                List<Parameter> get(String filter) {
                    return Parameters.ENVS;
                }
            };

    private static final HashMap<String, SuperGlobalArray> STRING_TO_ENUM = new HashMap<>();

    static {
        for (SuperGlobalArray array : values()) {
            STRING_TO_ENUM.put(array.toString(), array);
        }
    }

    /**
     * Get candidate list for code completion.
     *
     * @param paramIndex index of parameter
     * @return candidate list for code completion
     */
    abstract List<Parameter> get(String filter);

    /**
     * Get SuperGlobal from string.
     *
     * @param name super global variable name
     * @return {@code SuperGlobalArray} if name exists, {@code null} otherwise.
     */
    @CheckForNull
    public static SuperGlobalArray fromString(String name) {
        return STRING_TO_ENUM.get(name);
    }

    public String getProperFilter(String filter) {
        return filter;
    }

    @Override
    public String toString() {
        return String.format("$_%s", this.name()); // NOI18N
    }
}
