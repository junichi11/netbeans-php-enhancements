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
public final class DummyTextOptions {

    private static final DummyTextOptions INSTANCE = new DummyTextOptions();
    private static final String DUMMY_TEXT = "dummy.text"; // NOI18N
    private static final String TEXT = "text"; // NOI18N
    private static final String LOOP_COUNT = "loop.count"; // NOI18N
    private static final String USE_LOOP_COUNT = "use.loop.count"; // NOI18N
    private static final String TEXT_LENGTH = "text.length"; // NOI18N
    private static final String USE_TEXT_LENGTH = "use.text.length"; // NOI18N
    private static final String LOOP = "loop"; // NOI18N

    private DummyTextOptions() {
    }

    public static DummyTextOptions getInstance() {
        return INSTANCE;
    }

    public String getText() {
        return getPreferences().get(TEXT, ""); // NOI18N
    }

    public void setText(String text) {
        getPreferences().put(TEXT, text); // NOI18N
    }

    public boolean isLoop() {
        return getPreferences().getBoolean(LOOP, true);
    }

    public void setLoop(boolean isLoop) {
        getPreferences().putBoolean(LOOP, isLoop);
    }

    public boolean isTextLength() {
        return getPreferences().getBoolean(USE_TEXT_LENGTH, true);
    }

    public void setTextLength(boolean isTextLength) {
        getPreferences().putBoolean(USE_TEXT_LENGTH, isTextLength);
    }

    public int getTextLength() {
        return getPreferences().getInt(TEXT_LENGTH, 100);
    }

    public void setTextLength(int length) {
        getPreferences().putInt(TEXT_LENGTH, length);
    }

    public boolean isLoopCount() {
        return getPreferences().getBoolean(USE_LOOP_COUNT, false);
    }

    public void setLoopCount(boolean isLoopCount) {
        getPreferences().putBoolean(USE_LOOP_COUNT, isLoopCount);
    }

    public int getLoopCount() {
        return getPreferences().getInt(LOOP_COUNT, 5);
    }

    public void setLoopCount(int count) {
        getPreferences().putInt(LOOP_COUNT, count);
    }

    private Preferences getPreferences() {
        return NbPreferences.forModule(DummyTextOptions.class).node(DUMMY_TEXT);
    }
}
