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
package com.junichi11.netbeans.php.enhancements.ui.actions;

import com.junichi11.netbeans.php.enhancements.utils.DocUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.annotations.common.CheckForNull;
import org.netbeans.api.editor.EditorRegistry;
import org.netbeans.api.html.lexer.HTMLTokenId;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.php.editor.lexer.PHPTokenId;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.EditorCookie;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "PHP",
        id = "com.junichi11.netbeans.php.enhancements.ui.actions.SmartDeleteAction")
@ActionRegistration(
        displayName = "#CTL_SmartDeleteAction")
@ActionReference(path = "Shortcuts", name = "DS-BACK_SPACE")
@Messages("CTL_SmartDeleteAction=Smart Delete")
public final class SmartDeleteAction implements ActionListener {

    private final EditorCookie context;
    private static final Set<? extends TokenId> AVAILABLEIDS = new HashSet<>(Arrays.asList(
            PHPTokenId.PHP_CONSTANT_ENCAPSED_STRING,
            PHPTokenId.PHP_VARIABLE,
            PHPTokenId.PHP_STRING,
            HTMLTokenId.VALUE));

    public SmartDeleteAction(EditorCookie context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        JTextComponent editor = EditorRegistry.lastFocusedComponent();
        if (editor == null) {
            return;
        }

        Document document = editor.getDocument();
        if (document == null) {
            return;
        }

        // caret position is end of token
        // i.e. if it is start of next token, string is not removed.
        // so, caret position - 1
        // e.g. $something[caret is here]
        int offset = editor.getCaretPosition() - 1;
        if (offset < 0) {
            offset = 0;
        }
        // get token sequence
        TokenSequence<? extends TokenId> ts = getTokenSequence(document, offset);
        if (ts == null) {
            return;
        }

        Token<? extends TokenId> token = ts.token();
        TokenId id = token.id();
        String primaryCategory = id.primaryCategory();
        boolean isString = primaryCategory.equals("string"); // NOI18N
        if (!AVAILABLEIDS.contains(id) && !isString) {
            return;
        }
        String text = token.text().toString();
        int startOffset = ts.offset() + 1;
        int removeLength = 0;
        int textLength = text.length();
        // string
        // "something" or 'something' -> "" or ''
        if (isString || id == HTMLTokenId.VALUE) {
            if (wrapWith(text, "\"") || wrapWith(text, "'")) { // NOI18N
                if (textLength <= 2) {
                    // "" or ''
                    removeLength = textLength;
                    startOffset = ts.offset();
                } else {
                    removeLength = textLength - 2;
                }
            } else {
                removeLength = textLength;
                startOffset = ts.offset();
            }
        }

        // variable
        // $something -> $
        if (id == PHPTokenId.PHP_VARIABLE) {
            removeLength = textLength - 1;
        }

        // php string
        // e.g. DEFINE
        if (id == PHPTokenId.PHP_STRING) {
            startOffset = ts.offset();
            removeLength = textLength;
        }

        try {
            // remove string
            DocUtils.remove(startOffset, removeLength, document);
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private boolean wrapWith(String target, String wrapString) {
        return target.startsWith(wrapString) && target.endsWith(wrapString);
    }

    @CheckForNull
    private TokenSequence<? extends TokenId> getTokenSequence(Document document, int offset) {
        AbstractDocument ad = (AbstractDocument) document;
        ad.readLock();
        TokenSequence<? extends TokenId> tokenSequence;
        try {
            TokenHierarchy<Document> th = TokenHierarchy.get(document);
            if (th == null) {
                return null;
            }
            tokenSequence = th.tokenSequence();
        } finally {
            ad.readUnlock();
        }
        if (tokenSequence == null) {
            return null;
        }
        tokenSequence.move(offset);
        tokenSequence.moveNext();

        while (tokenSequence.embedded() != null) {
            tokenSequence = tokenSequence.embedded();
            tokenSequence.move(offset);
            tokenSequence.moveNext();
        }
        return tokenSequence;
    }
}
