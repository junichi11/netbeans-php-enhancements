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
package com.junichi11.netbeans.php.enhancements.editor.completion;

import com.junichi11.netbeans.php.enhancements.options.PHPEnhancementsOptions;
import com.junichi11.netbeans.php.enhancements.utils.Utils;
import java.util.Arrays;
import java.util.List;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.php.editor.lexer.LexUtilities;
import org.netbeans.modules.php.editor.lexer.PHPTokenId;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;

/**
 *
 * @author junichi11
 */
@MimeRegistration(mimeType = Utils.PHP_MIME_TYPE, service = CompletionProvider.class)
public class UppercaseCompletionProvider implements CompletionProvider {

    public UppercaseCompletionProvider() {
    }

    @Override
    public CompletionTask createTask(int queryType, JTextComponent component) {
        if (queryType != CompletionProvider.COMPLETION_QUERY_TYPE) {
            return null;
        }
        return new AsyncCompletionTask(new AsyncCompletionQueryImpl(), component);
    }

    @Override
    public int getAutoQueryTypes(JTextComponent component, String typedText) {
        return 0;
    }

    static class AsyncCompletionQueryImpl extends AsyncCompletionQuery {

        private static final List<PHPTokenId> CONST_IGNORE_LIST = Arrays.asList(PHPTokenId.WHITESPACE, PHPTokenId.PHP_STRING);
        private static final List<PHPTokenId> DEFINE_IGNORE_LIST = Arrays.asList(PHPTokenId.WHITESPACE, PHPTokenId.PHP_CONSTANT_ENCAPSED_STRING, PHPTokenId.PHP_TOKEN);

        public AsyncCompletionQueryImpl() {
        }

        @Override
        protected void query(CompletionResultSet resultSet, Document doc, int caretOffset) {
            try {
                PHPEnhancementsOptions options = PHPEnhancementsOptions.getInstance();
                if (options.isToUppercaseConst()) {
                    if (checkConst(resultSet, doc, caretOffset)) {
                        return;
                    }
                }

                if (options.isToUppercaseDefine()) {
                    checkDefine(resultSet, doc, caretOffset);
                }
            } finally {
                resultSet.finish();
            }
        }

        private boolean checkConst(CompletionResultSet resultSet, Document doc, int caretOffset) {
            TokenSequence<PHPTokenId> ts = Utils.getTokenSequence(doc, caretOffset);
            ts.move(caretOffset);
            ts.movePrevious();
            Token<PHPTokenId> caretToken = ts.token();
            if (caretToken == null) {
                return false;
            }
            String caretText = caretToken.text().toString();
            int startPosition = ts.offset();
            if (caretText.equals("const") || caretText.equals(caretText.toUpperCase())) { // NOI18N
                return false;
            }
            Token<? extends PHPTokenId> findPreviousToken = LexUtilities.findPrevious(ts, CONST_IGNORE_LIST);
            if (findPreviousToken == null || findPreviousToken.id() != PHPTokenId.PHP_CONST) {
                return false;
            }
            resultSet.addItem(new UppercaseCompletionItem(caretText.toUpperCase(), startPosition));
            return true;
        }

        private boolean checkDefine(CompletionResultSet resultSet, Document doc, int caretOffset) {
            TokenSequence<PHPTokenId> ts = Utils.getTokenSequence(doc, caretOffset);
            ts.move(caretOffset);
            ts.moveNext();
            Token<PHPTokenId> caretToken = ts.token();
            if (caretToken == null) {
                return false;
            }
            String caretText = caretToken.text().toString();
            int startPosition = ts.offset();
            if (caretText.equals(caretText.toUpperCase()) || caretToken.id() != PHPTokenId.PHP_CONSTANT_ENCAPSED_STRING) {
                return false;
            }
            Token<? extends PHPTokenId> findPreviousToken = LexUtilities.findPrevious(ts, DEFINE_IGNORE_LIST);
            if (findPreviousToken == null || findPreviousToken.id() != PHPTokenId.PHP_STRING) {
                return false;
            }
            String tokenText = findPreviousToken.text().toString();
            if (!tokenText.equals("define") && !tokenText.equals("defined")) { // NOI18N
                return false;
            }
            resultSet.addItem(new UppercaseCompletionItem(caretText.toUpperCase(), startPosition));
            return true;
        }

    }

}
