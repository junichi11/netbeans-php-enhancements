/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2014 Sun Microsystems, Inc.
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
