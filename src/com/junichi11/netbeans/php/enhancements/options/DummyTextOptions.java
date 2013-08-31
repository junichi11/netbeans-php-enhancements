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
