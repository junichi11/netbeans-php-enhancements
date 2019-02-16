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
public final class DummyImageOptions {

    private static final DummyImageOptions INSTANCE = new DummyImageOptions();
    private static final String DUMMY_IMAGE = "dummy.image"; // NOI18N
    private static final String IMAGE_WIDTH = "image.width"; // NOI18N
    private static final String IMAGE_HEIGHT = "image.height"; // NOI18N
    private static final String IMAGE_OPACITY = "image.opacity"; // NOI18N
    private static final String IMAGE_COLOR = "image.color"; // NOI18N
    private static final String FILE_PREFIX = "file.prefix"; // NOI18N
    private static final String FILE_SUFFIX = "file.suffix"; // NOI18N
    private static final String FILE_OVERWRITE = "file.overwrite"; // NOI18N

    private DummyImageOptions() {
    }

    public static DummyImageOptions getInstance() {
        return INSTANCE;
    }

    public int getImageWidth() {
        return getPreferences().getInt(IMAGE_WIDTH, 640);
    }

    public void setImageWidth(int width) {
        getPreferences().putInt(IMAGE_WIDTH, width);
    }

    public int getImageHeight() {
        return getPreferences().getInt(IMAGE_HEIGHT, 480);
    }

    public void setImageHeight(int height) {
        getPreferences().putInt(IMAGE_HEIGHT, height);
    }

    public int getOpacity() {
        return getPreferences().getInt(IMAGE_OPACITY, 100);
    }

    public void setOpacity(int opacity) {
        getPreferences().putInt(IMAGE_OPACITY, opacity);
    }

    public String getColor() {
        return getPreferences().get(IMAGE_COLOR, "#c0c0c0"); // NOI18N
    }

    public void setColor(String color) {
        getPreferences().put(IMAGE_COLOR, color);
    }

    public String getFileNamePrefix() {
        return getPreferences().get(FILE_PREFIX, "dummy_"); // NOI18N
    }

    public void setFileNamePrefix(String prefix) {
        getPreferences().put(FILE_PREFIX, prefix);
    }

    public String getFileNameSuffix() {
        return getPreferences().get(FILE_SUFFIX, ""); // NOI18N
    }

    public void setFileNameSuffix(String suffix) {
        getPreferences().put(FILE_SUFFIX, suffix);
    }

    public boolean isOverwrite() {
        return getPreferences().getBoolean(FILE_OVERWRITE, false);
    }

    public void setOverwrite(boolean isOverwrite) {
        getPreferences().putBoolean(FILE_OVERWRITE, isOverwrite);
    }

    private Preferences getPreferences() {
        return NbPreferences.forModule(DummyImageOptions.class).node(DUMMY_IMAGE);
    }
}
