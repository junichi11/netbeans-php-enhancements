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

import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.netbeans.modules.php.editor.lexer.LexUtilities;
import org.netbeans.modules.php.editor.lexer.PHPTokenId;

/**
 *
 * @author junichi11
 */
public final class Utils {

    public static final String PHP_MIME_TYPE = "text/x-php5"; // NOI18N
    public static final String HTML_MIME_TYPE = "text/html"; // NOI18N

    private Utils() {
    }

    public static boolean isPHP(JTextComponent editor) {
        String mimeType = NbEditorUtilities.getMimeType(editor);
        if (mimeType == null || mimeType.isEmpty()) {
            return false;
        }
        return PHP_MIME_TYPE.equals(mimeType);
    }

    public static boolean isPHP(Document document) {
        return NbEditorUtilities.getMimeType(document).equals(PHP_MIME_TYPE);
    }

    public static boolean isHtml(JTextComponent editor) {
        String mimeType = NbEditorUtilities.getMimeType(editor);
        if (mimeType == null || mimeType.isEmpty()) {
            return false;
        }
        return HTML_MIME_TYPE.equals(mimeType);
    }

    public static TokenSequence<PHPTokenId> getTokenSequence(Document document, int offset) {
        TokenSequence<PHPTokenId> ts = null;
        AbstractDocument ad = (AbstractDocument) document;
        ad.readLock();
        try {
            ts = LexUtilities.getPHPTokenSequence(document, offset);
        } finally {
            ad.readUnlock();
        }
        return ts;
    }
}
