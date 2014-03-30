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

import static com.junichi11.netbeans.php.enhancements.editor.completion.Parameters.CHARSETS;
import static com.junichi11.netbeans.php.enhancements.editor.completion.Parameters.DATE_FORMATS;
import static com.junichi11.netbeans.php.enhancements.editor.completion.Parameters.ENCODINGS;
import static com.junichi11.netbeans.php.enhancements.editor.completion.Parameters.HTTP_HEADER_RESPONSES;
import static com.junichi11.netbeans.php.enhancements.editor.completion.Parameters.HTTP_STATUS_CODES;
import static com.junichi11.netbeans.php.enhancements.editor.completion.Parameters.MB_LANGUAGES;
import static com.junichi11.netbeans.php.enhancements.editor.completion.Parameters.PHPINI_DIRECTIVES;
import static com.junichi11.netbeans.php.enhancements.editor.completion.Parameters.SUBSTCHARS;
import static com.junichi11.netbeans.php.enhancements.editor.completion.Parameters.TIMEZONES;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.netbeans.api.annotations.common.CheckForNull;

/**
 * Enum for functions.
 *
 * @author junichi11
 */
public enum Function {

    HEADER("header") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 0:
                            Matcher matcher = HTTP_VERSION_PATTERN.matcher(filter);
                            if (matcher.find()) {
                                return HTTP_STATUS_CODES;
                            }
                            return HTTP_HEADER_RESPONSES;
                        default:
                            return Collections.emptyList();
                    }
                }

                @Override
                public String getProperFilter(String filter) {
                    Matcher matcher = HTTP_VERSION_PATTERN.matcher(filter);
                    if (matcher.find()) {
                        return matcher.group("code"); // NOI18N
                    }
                    return super.getProperFilter(filter);
                }
            },
    DATE("date") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 0:
                            return DATE_FORMATS;
                        default:
                            return Collections.emptyList();
                    }
                }

                @Override
                public String getProperFilter(String filter) {
                    int lastIndexOfSpace = filter.lastIndexOf(' '); // NOI18N
                    if (lastIndexOfSpace != -1) {
                        return filter.substring(lastIndexOfSpace + 1);
                    }
                    return filter;
                }

            },
    DATE_FORMAT("date_format") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 1:
                            return DATE_FORMATS;
                        default:
                            return Collections.emptyList();
                    }
                }

                @Override
                public String getProperFilter(String filter) {
                    return DATE.getProperFilter(filter);
                }
            },
    DATE_DEFAULT_TIMEZONE_SET("date_default_timezone_set") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 0:
                            return TIMEZONES;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    INI_SET("ini_set") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 0:
                            return PHPINI_DIRECTIVES;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    INI_GET("ini_get") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 0:
                            return PHPINI_DIRECTIVES;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    SETLOCALE("setlocale") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    // multibyte functions
    MB_CHECK_ENCODING("mb_check_encoding") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 1:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_CONVERT_CASE("mb_convert_case") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 2:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_CONVERT_ENCODING("mb_convert_encoding") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 1:
                        case 2:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_CONVERT_KANA("mb_convert_kana") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 2:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_CONVERT_VARIABLES("mb_convert_variables") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 0:
                        case 1:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_DECODE_NUMERICENTITY("mb_decode_numericentity") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 2:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_DETECT_ENCODING("mb_detect_encoding") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 1:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_DETECT_ORDER("mb_detect_order") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 0:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_ENCODE_MIMEHEADER("mb_encode_mimeheader") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 1:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_ENCODE_NUMERICENTITY("mb_encode_numericentity") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 2:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_ENCODING_ALIASES("mb_encoding_aliases") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 0:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_HTTP_OUTPUT("mb_http_output") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 0:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_INTERNAL_ENCODING("mb_internal_encoding") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 0:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_LANGUAGE("mb_language") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 0:
                            return MB_LANGUAGES;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_PREFERRED_MIME_NAME("mb_preferred_mime_name") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 0:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_REGEX_ENCODING("mb_regex_encoding") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 0:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_STRCUT("mb_strcut") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 3:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_STRIMWIDTH("mb_strimwidth") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 4:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_STRIPOS("mb_stripos") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 3:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_STRISTR("mb_stristr") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 3:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_STRLEN("mb_strlen") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 1:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_STRPOS("mb_strpos") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 3:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_STRRCHR("mb_strrchr") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 3:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_STRRICHR("mb_strrichr") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    return MB_STRRCHR.get(paramIndex, filter);
                }
            },
    MB_STRRIPOS("mb_strripos") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 3:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_STRRPOS("mb_strrpos") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    return MB_STRRIPOS.get(paramIndex, filter);
                }
            },
    MB_STRSTR("mb_strstr") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    return MB_STRISTR.get(paramIndex, filter);
                }
            },
    MB_STRTOLOWER("mb_strtolower") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 1:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_STRTOUPPER("mb_strtoupper") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    return MB_STRTOLOWER.get(paramIndex, filter);
                }
            },
    MB_STRWIDTH("mb_strwidth") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    return MB_STRTOLOWER.get(paramIndex, filter);
                }
            },
    MB_SUBSTITUTE_CHARACTER("mb_substitute_character") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    return SUBSTCHARS;
                }
            },
    MB_SUBSTR_COUNT("mb_substr_count") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 2:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    MB_SUBSTR("mb_substr") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 3:
                            return ENCODINGS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    HTMLENTITIES("htmlentities") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    switch (paramIndex) {
                        case 2:
                            return CHARSETS;
                        default:
                            return Collections.emptyList();
                    }
                }
            },
    HTMLSPECIALCHARS("htmlspecialchars") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    return HTMLENTITIES.get(paramIndex, filter);
                }
            },
    DATE_TIME__FORMAT("DateTime::format") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    return DATE.get(paramIndex, filter);
                }

                @Override
                public String getProperFilter(String filter) {
                    return DATE.getProperFilter(filter);
                }
            },
    DATE_TIME_IMMUTABLE__FORMAT("DateTimeImmutable::format") { // NOI18N
                @Override
                List<Parameter> get(int paramIndex, String filter) {
                    return DATE.get(paramIndex, filter);
                }

                @Override
                public String getProperFilter(String filter) {
                    return DATE.getProperFilter(filter);
                }
            },;

    private final String name;
    private static final Map<String, Function> STRING_TO_ENUM = new HashMap<String, Function>();
    public static final Pattern HTTP_VERSION_PATTERN = Pattern.compile("(?<version>\\AHTTP/\\d\\.\\d)\\s+(?<code>.*)\\z"); // NOI18N

    static {
        for (Function function : values()) {
            STRING_TO_ENUM.put(function.toString(), function);
        }
    }

    private Function(String name) {
        this.name = name;
    }

    /**
     * Get candidate list for code completion.
     *
     * @param paramIndex index of parameter
     * @return candidate list for code completion
     */
    abstract List<Parameter> get(int paramIndex, String filter);

    /**
     * Get Function from string.
     *
     * @param name function name
     * @return {@code Funcitons} if function name exists, {@code null}
     * otherwise.
     */
    @CheckForNull
    public static Function fromString(String name) {
        return STRING_TO_ENUM.get(name);
    }

    public String getProperFilter(String filter) {
        return filter;
    }

    @Override
    public String toString() {
        return name;
    }
}
