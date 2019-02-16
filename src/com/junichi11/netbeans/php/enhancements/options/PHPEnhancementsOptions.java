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
package com.junichi11.netbeans.php.enhancements.options;

import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 *
 * @author junichi11
 */
public final class PHPEnhancementsOptions {

    private static final PHPEnhancementsOptions INSTANCE = new PHPEnhancementsOptions();
    private static final String OBJECT_OPERATOR_TYPINGHOOK = "object.operator.typinghook"; // NOI18N
    private static final String DOUBLE_ARROW_TYPINGHOOK = "double.arrow.operator.typinghook"; // NOI18N
    private static final String TO_UPPERCASE_CONST = "uppercase.const"; // NOI18N
    private static final String TO_UPPERCASE_DEFINE = "uppercase.define"; // NOI18N
    private static final String PARAMETERS_CODE_COMPLETION = "parameters.code.completion"; // NOI18N

    public static PHPEnhancementsOptions getInstance() {
        return INSTANCE;
    }

    private PHPEnhancementsOptions() {
    }

    public boolean isObjectOperator() {
        return getPreferences().getBoolean(OBJECT_OPERATOR_TYPINGHOOK, false);
    }

    public void setObjectOperator(boolean isObjectOperator) {
        getPreferences().putBoolean(OBJECT_OPERATOR_TYPINGHOOK, isObjectOperator);
    }

    public boolean isDoubleArrowOperator() {
        return getPreferences().getBoolean(DOUBLE_ARROW_TYPINGHOOK, false);
    }

    public void setDoubleArrowOperator(boolean isDoubleArrowOperator) {
        getPreferences().putBoolean(DOUBLE_ARROW_TYPINGHOOK, isDoubleArrowOperator);
    }

    public boolean isToUppercaseConst() {
        return getPreferences().getBoolean(TO_UPPERCASE_CONST, false);
    }

    public void setToUppercaseConst(boolean isToUppercaseConst) {
        getPreferences().putBoolean(TO_UPPERCASE_CONST, isToUppercaseConst);
    }

    public boolean isToUppercaseDefine() {
        return getPreferences().getBoolean(TO_UPPERCASE_DEFINE, false);
    }

    public void setToUppercaseDefine(boolean isToUppercaseDefine) {
        getPreferences().putBoolean(TO_UPPERCASE_DEFINE, isToUppercaseDefine);
    }

    public boolean isParametersCodeCompletion() {
        return getPreferences().getBoolean(PARAMETERS_CODE_COMPLETION, true);
    }

    public void setParametersCodeCompletion(boolean isParameters) {
        getPreferences().putBoolean(PARAMETERS_CODE_COMPLETION, isParameters);
    }

    private Preferences getPreferences() {
        return NbPreferences.forModule(PHPEnhancementsOptions.class);
    }
}
