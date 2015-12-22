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
package com.junichi11.netbeans.php.enhancements.editor.typinghooks;

import com.junichi11.netbeans.php.enhancements.options.PHPEnhancementsOptions;
import javax.swing.text.BadLocationException;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.php.editor.lexer.LexUtilities;
import org.netbeans.modules.php.editor.lexer.PHPTokenId;
import org.netbeans.spi.editor.typinghooks.TypedTextInterceptor;

/**
 *
 * @author junichi11
 */
public class PhpTypedTextInterceptorEx implements TypedTextInterceptor {

    private boolean isObjectOperator;
    private boolean isDoubleArrowOperator;

    @Override
    public boolean beforeInsert(Context context) throws BadLocationException {
        return false;
    }

    @Override
    public void insert(MutableContext context) throws BadLocationException {
        PHPEnhancementsOptions options = PHPEnhancementsOptions.getInstance();
        isObjectOperator = options.isObjectOperator();
        isDoubleArrowOperator = options.isDoubleArrowOperator();
        if (!isObjectOperator && !isDoubleArrowOperator) {
            return;
        }
        BaseDocument document = (BaseDocument) context.getDocument();
        int caretOffset = context.getOffset();
        char ch = context.getText().charAt(0);
        if (!isEqual(ch) && !isMinus(ch) || caretOffset == 0) {
            return;
        }

        // get token
        TokenSequence<PHPTokenId> ts = LexUtilities.getPHPTokenSequence(document, caretOffset);
        if (ts == null) {
            return;
        }
        ts.move(caretOffset);
        if (!ts.moveNext() && !ts.movePrevious()) {
            return;
        }
        handleChar(context, ch, ts);
    }

    private void handleChar(MutableContext context, char ch, TokenSequence<PHPTokenId> ts) {
        if (isMinus(ch) && isObjectOperator) {
            ts.movePrevious();
            Token<PHPTokenId> token = ts.token();
            if (token == null) {
                return;
            }
            PHPTokenId id = token.id();

            if (id != PHPTokenId.PHP_VARIABLE && id != PHPTokenId.PHP_STRING) {
                return;
            }

            // X: $this -
            // O: $this-
            int tokenOffset = ts.offset();
            int tokenEndOffset = tokenOffset + token.text().length();
            if (tokenEndOffset != context.getOffset()) {
                return;
            }
            String text = ch + ">"; // NOI18N
            context.setText(text, text.length());
            return;
        }

        if (isEqual(ch) && isDoubleArrowOperator) {
            Token<PHPTokenId> caretToken = ts.token();
            if (caretToken == null || caretToken.id() == PHPTokenId.PHP_CONSTANT_ENCAPSED_STRING) {
                return;
            }
            int caretOffset = context.getOffset();
            if (isInArray(ts, caretOffset)) {
                String text = ch + ">"; // NOI18N
                context.setText(text, text.length());
            }
        }
    }

    private boolean isInArray(TokenSequence<PHPTokenId> ts, int caretOffset) {
        ts.move(caretOffset);
        int newArrayBalacne = 0;
        int oldArrayBalacne = 0;
        while(ts.movePrevious()) {
            Token<PHPTokenId> token = ts.token();
            if (token.id() == PHPTokenId.PHP_SEMICOLON) { // terminator
                break;
            }
            if (isLeftBracket(token)) { // [
                if (newArrayBalacne == 0) {
                    return true;
                }
                newArrayBalacne--;
            } else if(isRightBracket(token)) { // ]
                newArrayBalacne++;
            } else if(isLeftBrace(token)) { // (
                if (oldArrayBalacne == 0) {
                    // array(
                    if(!ts.movePrevious()) {
                        break;
                    }
                    PHPTokenId id = ts.token().id();
                    if (id == PHPTokenId.WHITESPACE) {
                        if (!ts.movePrevious()) {
                            break;
                        }
                    }
                    return ts.token().id() == PHPTokenId.PHP_ARRAY;
                }
                oldArrayBalacne--;
            } else if(isRightBrace(token)) { // )
                oldArrayBalacne++;
            }
        }
        return false;
    }

    @Override
    public void afterInsert(Context context) throws BadLocationException {
    }

    @Override
    public void cancelled(Context context) {
    }

    private boolean isMinus(char ch) {
        return ch == '-';
    }

    private boolean isEqual(char ch) {
        return ch == '=';
    }

    private static boolean isLeftBracket(Token<PHPTokenId> token) {
        return token.id() == PHPTokenId.PHP_TOKEN && token.text().toString().equals("["); // NOI18N
    }

    private static boolean isRightBracket(Token<PHPTokenId> token) {
        return token.id() == PHPTokenId.PHP_TOKEN && token.text().toString().equals("]"); // NOI18N
    }

    private static boolean isLeftBrace(Token<PHPTokenId> token) {
        return token.id() == PHPTokenId.PHP_TOKEN && token.text().toString().equals("("); // NOI18N
    }

    private static boolean isRightBrace(Token<PHPTokenId> token) {
        return token.id() == PHPTokenId.PHP_TOKEN && token.text().toString().equals(")"); // NOI18N
    }

    @MimeRegistration(mimeType = "text/x-php5", service = TypedTextInterceptor.Factory.class)
    public static class Factory implements TypedTextInterceptor.Factory {

        @Override
        public TypedTextInterceptor createTypedTextInterceptor(MimePath mimePath) {
            return new PhpTypedTextInterceptorEx();
        }

    }
}
