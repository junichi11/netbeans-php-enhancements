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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.junichi11.netbeans.php.enhancements.options.PHPEnhancementsOptions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author junichi11
 */
public final class Parameters {

    private static final Logger LOGGER = Logger.getLogger(Parameters.class.getName());

    public static final Map<String, List<Parameter>> PARAMETER_MAP = new HashMap<>();
    public static final List<Parameter> DATE_FORMATS = new ArrayList<>();
    public static final List<Parameter> TIMEZONES = new ArrayList<>();
    public static final List<Parameter> PHPINI_DIRECTIVES = new ArrayList<>();
    public static final List<Parameter> HTTP_HEADER_RESPONSES = new ArrayList<>();
    public static final List<Parameter> HTTP_STATUS_CODES = new ArrayList<>();
    public static final List<Parameter> HTTP_CHARSETS = new ArrayList<>();
    public static final List<Parameter> HTTP_METHODS = new ArrayList<>();
    public static final List<Parameter> HTTP_CACHE_CONTROL_DIRECTIVES = new ArrayList<>();
    public static final List<Parameter> HTTP_LANGUAGES = new ArrayList<>();
    public static final List<Parameter> ENCODINGS = new ArrayList<>();
    public static final List<Parameter> CHARSETS = new ArrayList<>();
    public static final List<Parameter> SUBSTCHARS = new ArrayList<>();
    public static final List<Parameter> MB_LANGUAGES = new ArrayList<>();
    public static final List<Parameter> MB_KANA_CONVERSIONS = new ArrayList<>();
    public static final List<Parameter> MB_GET_INFO_TYPES = new ArrayList<>();
    public static final List<Parameter> MB_HTTP_INPUT_TYPES = new ArrayList<>();
    public static final List<Parameter> SESSION_CACHE_LIMITERS = new ArrayList<>();
    public static final List<Parameter> MEDIA_TYPES = new ArrayList<>();
    public static final List<Parameter> ENVS = new ArrayList<>();

    private Parameters() {
    }

    static {
        if (PHPEnhancementsOptions.getInstance().isParametersCodeCompletion()) {
            load();
        }
    }

    private static void load() {
        PARAMETER_MAP.clear();
        buildParameterMap();
        buildParameters();
        for (String lang : Locale.getISOLanguages()) {
            HTTP_LANGUAGES.add(new Parameter(lang, "", "")); // NOI18N
        }
        PARAMETER_MAP.clear();
    }

    public static void reload() {
        clear();
        load();
    }

    public static void clear() {
        PARAMETER_MAP.clear();
        buildParameterMap();
        for (Map.Entry<String, List<Parameter>> entry : PARAMETER_MAP.entrySet()) {
            List<Parameter> list = entry.getValue();
            list.clear();
        }
        PARAMETER_MAP.clear();
    }

    private static void buildParameterMap() {
        // function
        PARAMETER_MAP.put("date_formats", DATE_FORMATS); // NOI18N
        PARAMETER_MAP.put("timezones", TIMEZONES); // NOI18N
        PARAMETER_MAP.put("phpini_directives", PHPINI_DIRECTIVES); // NOI18N
        PARAMETER_MAP.put("http_status_codes", HTTP_STATUS_CODES); // NOI18N
        PARAMETER_MAP.put("http_header_responses", HTTP_HEADER_RESPONSES); // NOI18N
        PARAMETER_MAP.put("http_charsets", HTTP_CHARSETS); // NOI18N
        PARAMETER_MAP.put("http_methods", HTTP_METHODS); // NOI18N
        PARAMETER_MAP.put("http_cache_control_directives", HTTP_CACHE_CONTROL_DIRECTIVES); // NOI18N
        PARAMETER_MAP.put("encodings", ENCODINGS); // NOI18N
        PARAMETER_MAP.put("charsets", CHARSETS); // NOI18N
        PARAMETER_MAP.put("substchars", SUBSTCHARS); // NOI18N
        PARAMETER_MAP.put("mb_languages", MB_LANGUAGES); // NOI18N
        PARAMETER_MAP.put("mb_kana_conversions", MB_KANA_CONVERSIONS); // NOI18N
        PARAMETER_MAP.put("mb_get_info_types", MB_GET_INFO_TYPES); // NOI18N
        PARAMETER_MAP.put("mb_http_input_types", MB_HTTP_INPUT_TYPES); // NOI18N
        PARAMETER_MAP.put("session_cache_limiters", SESSION_CACHE_LIMITERS); // NOI18N
        PARAMETER_MAP.put("media_types", MEDIA_TYPES); // NOI18N

        // array
        PARAMETER_MAP.put("envs", ENVS); // NOI18N
    }

    private static void buildParameters() {
        Gson gson = new Gson();
        for (Map.Entry<String, List<Parameter>> entry : PARAMETER_MAP.entrySet()) {
            String target = entry.getKey();
            List<Parameter> list = entry.getValue();
            list.clear();
            String filePath = String.format("resources/%s.json", target); // NOI18N
            URL resource = Function.class.getResource(filePath);
            if (resource == null) {
                continue;
            }
            try {
                InputStream inputStream = resource.openStream();
                JsonReader jsonReader = new JsonReader(new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))); // NOI18N
                try {
                    Type type = new TypeToken<ArrayList<Parameter>>() {
                    }.getType();
                    ArrayList<Parameter> parameters = gson.fromJson(jsonReader, type);
                    list.addAll(parameters);
                } finally {
                    inputStream.close();
                    jsonReader.close();
                }
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, ex.getMessage());
            }
        }
    }
}
