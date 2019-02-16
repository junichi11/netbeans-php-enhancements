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
