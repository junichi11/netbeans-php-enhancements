/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2013 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2013 Sun Microsystems, Inc.
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
