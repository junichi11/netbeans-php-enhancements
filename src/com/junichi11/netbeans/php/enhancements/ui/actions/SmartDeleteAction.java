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
    private final Set<TokenId> availableIds = new HashSet<TokenId>(Arrays.asList(
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
        TokenSequence ts = getTokenSequence(document, offset);
        if (ts == null) {
            return;
        }

        Token token = ts.token();
        TokenId id = token.id();
        String primaryCategory = id.primaryCategory();
        boolean isString = primaryCategory.equals("string"); // NOI18N
        if (!availableIds.contains(id) && !isString) {
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

    private TokenSequence getTokenSequence(Document document, int offset) {
        AbstractDocument ad = (AbstractDocument) document;
        ad.readLock();
        TokenSequence tokenSequence;
        try {
            TokenHierarchy th = TokenHierarchy.get(document);
            tokenSequence = th.tokenSequence();
        } finally {
            ad.readUnlock();
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
