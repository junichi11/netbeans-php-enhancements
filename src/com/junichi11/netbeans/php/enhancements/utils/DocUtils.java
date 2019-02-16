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
package com.junichi11.netbeans.php.enhancements.utils;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 *
 * @author junichi11
 */
public final class DocUtils {

    private DocUtils() {
    }

    public static void remove(int startOffset, int removeLength, Document document) throws BadLocationException {
        if (startOffset > 0 && removeLength > 0) {
            document.remove(startOffset, removeLength);
        }
    }
}
