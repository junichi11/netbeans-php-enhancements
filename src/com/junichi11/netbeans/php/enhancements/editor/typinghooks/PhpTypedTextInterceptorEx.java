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
package com.junichi11.netbeans.php.enhancements.editor.typinghooks;

import com.junichi11.netbeans.php.enhancements.options.PHPEnhancementsOptions;
import com.junichi11.netbeans.php.enhancements.utils.Utils;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
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
    private static final List<PHPTokenId> AFTER_AS = Arrays.asList(PHPTokenId.PHP_VARIABLE, PHPTokenId.WHITESPACE, PHPTokenId.PHP_AS);

    private static final Logger LOGGER = Logger.getLogger(PhpTypedTextInterceptorEx.class.getName());

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
        // ->
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

        // =>
        if (isEqual(ch) && isDoubleArrowOperator) {
            Token<PHPTokenId> caretToken = ts.token();
            if (caretToken == null || caretToken.id() == PHPTokenId.PHP_CONSTANT_ENCAPSED_STRING) {
                return;
            }
            if (!ts.movePrevious()) {
                return;
            }
            Token<PHPTokenId> previoutsToken = ts.token();
            int caretOffset = context.getOffset();
            if (isInArray(ts, caretOffset)
                    || isAfterAs(ts, caretOffset)
                    || isInArrowFunction(ts, caretOffset)
                    || addInMatchExpression(ts, caretOffset)) {
                if (previoutsToken.id() == PHPTokenId.PHP_OPERATOR || isIgnoredContext(previoutsToken)) {
                    // in case of =>|, just remove ">"
                    if (LexUtilities.textEquals(previoutsToken.text(), '=', '>')) {
                        Document document = context.getDocument();
                        if (document != null) {
                            try {
                                int removeOffset = caretOffset - 1;
                                if (removeOffset >= 0) {
                                    document.remove(removeOffset, 1);
                                    context.getComponent().setCaretPosition(removeOffset);
                                }
                            } catch (BadLocationException ex) {
                                LOGGER.log(Level.WARNING, "incorrect position:" + ex.offsetRequested(), ex); // NOI18N
                            }
                        }
                    }
                    // in case of =|, ==| and ===|, etc. do nothing
                    return;
                }
                String text = ch + ">"; // NOI18N
                context.setText(text, text.length());
            }
        }
    }

    /**
     * Check whether the caret position is inside an array. Both array() and []
     * are checked.
     * <br />
     * <b>NOTE:</b>In case of [], not sure whether it is declaration or access.
     *
     * @param ts the token sequence
     * @param caretOffset the caret offset
     * @return {@code true} if it is inside the array, otherwise {@code false}
     */
    private static boolean isInArray(TokenSequence<PHPTokenId> ts, int caretOffset) {
        ts.move(caretOffset);
        int newArrayBalance = 0;
        int oldArrayBalance = 0;
        while (ts.movePrevious()) {
            Token<PHPTokenId> token = ts.token();
            if (token.id() == PHPTokenId.PHP_SEMICOLON) { // terminator
                break;
            }
            if (isLeftBracket(token)) { // [
                if (newArrayBalance == 0) {
                    return true;
                }
                newArrayBalance--;
            } else if (isRightBracket(token)) { // ]
                newArrayBalance++;
            } else if (isLeftBrace(token)) { // (
                if (oldArrayBalance == 0) {
                    // array(
                    if (!ts.movePrevious()) {
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
                oldArrayBalance--;
            } else if (isRightBrace(token)) { // )
                oldArrayBalance++;
            }
        }
        return false;
    }

    /**
     * Check whether the caret position is after "as".
     *
     * @param ts the token sequence
     * @param caretOffset the caret offset
     * @return {@code true} if it is after "as", otherwise {@code false}
     */
    private static boolean isAfterAs(TokenSequence<PHPTokenId> ts, int caretOffset) {
        ts.move(caretOffset);
        if (ts.movePrevious()
                && ts.token().id() != PHPTokenId.WHITESPACE) {
            ts.moveNext();
        }

        return AFTER_AS.stream()
                .noneMatch((tokenId) -> (!ts.movePrevious() || tokenId != ts.token().id()));
    }

    private static boolean isInArrowFunction(TokenSequence<PHPTokenId> ts, int caretOffset) {
        ts.move(caretOffset);
        int braceBalance = 0;
        while (ts.movePrevious()) {
            Token<PHPTokenId> token = ts.token();
            if (token.id() == PHPTokenId.PHP_SEMICOLON) { // terminator
                break;
            }
            if (isRightBrace(token)) {
                braceBalance++;
            } else if (isLeftBrace(token)) {
                braceBalance--;
            } else if (token.id() == PHPTokenId.PHP_FN) {
                if (braceBalance == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean addInMatchExpression(TokenSequence<PHPTokenId> ts, int caretOffset) {
        ts.move(caretOffset);
        int braceBalance = 0;
        int curlyBalance = 0;
        int bracketBalance = 0;
        int commaCount = 0;
        while (ts.movePrevious()) {
            Token<PHPTokenId> token = ts.token();
            if (token.id() == PHPTokenId.PHP_SEMICOLON) { // terminator
                break;
            }
            if (isRightBrace(token)) {
                braceBalance++;
            } else if (isLeftBrace(token)) {
                braceBalance--;
            } else if (isRightCurlyBrace(token)) {
                curlyBalance++;
            } else if (isLeftCurlyBrace(token)) {
                curlyBalance--;
            } else if (isRightBracket(token)) {
                bracketBalance++;
            } else if (isLeftBracket(token)) {
                bracketBalance--;
            } else if (isComma(token)) {
                if (bracketBalance == 0
                        && bracketBalance == 0
                        && curlyBalance == 0) {
                    commaCount++;
                }
            } else if (LexUtilities.textEquals(token.text(), '=', '>')) {
                // e.g. condition() => $test =^
                if (bracketBalance == 0
                        && bracketBalance == 0
                        && curlyBalance == 0
                        && commaCount == 0) {
                    return false;
                }
            } else if (token.id() == PHPTokenId.PHP_MATCH) {
                if (braceBalance == 0
                        && bracketBalance == 0
                        && curlyBalance == -1) {
                    return true;
                }
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

    /**
     * Check whether the char is '-'.
     *
     * @param ch the char
     * @return {@code true} if it is '-', otherwise {@code false}
     */
    private static boolean isMinus(char ch) {
        return ch == '-';
    }

    /**
     * Check whether the char is '='.
     *
     * @param ch the char
     * @return {@code true} if it is '=', otherwise {@code false}
     */
    private static boolean isEqual(char ch) {
        return ch == '=';
    }

    /**
     * Check whether a previous token is specific one. If the previous token +
     * "=" is another operator, it should be ignored.
     *
     * @param token the previous token
     * @return {@code true} the previous token + "=" is another operator,
     * otherwise {@code false}
     */
    private static boolean isIgnoredContext(Token<PHPTokenId> token) {
        if (token.id() == PHPTokenId.PHP_TOKEN) {
            String tokenText = token.text().toString();
            switch (tokenText) {
                // fall-through
                case "=": // NOI18N
                case "<": // NOI18N
                case ">": // NOI18N
                case "!": // NOI18N
                case "+": // NOI18N
                case "-": // NOI18N
                case "%": // NOI18N
                case "/": // NOI18N
                case "*": // NOI18N
                case "&": // NOI18N
                case "|": // NOI18N
                case "^": // NOI18N
                case ".": // NOI18N
                    return true;
                default:
                    break;
            }
        }
        return false;
    }

    /**
     * Check whether the token is "[".
     *
     * @param token the token
     * @return {@code true} if it is "[", otherwise {@code false}
     */
    private static boolean isLeftBracket(Token<PHPTokenId> token) {
        return token.id() == PHPTokenId.PHP_TOKEN && LexUtilities.textEquals(token.text(), '[');
    }

    /**
     * Check whether the token is "]".
     *
     * @param token the token
     * @return {@code true} if it is "]", otherwise {@code false}
     */
    private static boolean isRightBracket(Token<PHPTokenId> token) {
        return token.id() == PHPTokenId.PHP_TOKEN && LexUtilities.textEquals(token.text(), ']');
    }

    /**
     * Check whether the token is "(".
     *
     * @param token the token
     * @return {@code true} if it is "(", otherwise {@code false}
     */
    private static boolean isLeftBrace(Token<PHPTokenId> token) {
        return token.id() == PHPTokenId.PHP_TOKEN && LexUtilities.textEquals(token.text(), '(');
    }

    /**
     * Check whether the token is ")".
     *
     * @param token the token
     * @return {@code true} if it is ")", otherwise {@code false}
     */
    private static boolean isRightBrace(Token<PHPTokenId> token) {
        return token.id() == PHPTokenId.PHP_TOKEN && LexUtilities.textEquals(token.text(), ')');
    }

    /**
     * Check whether the token is "{".
     *
     * @param token the token
     * @return {@code true} if it is "{", otherwise {@code false}
     */
    private static boolean isLeftCurlyBrace(Token<PHPTokenId> token) {
        return token.id() == PHPTokenId.PHP_CURLY_OPEN;
    }

    /**
     * Check whether the token is "}".
     *
     * @param token the token
     * @return {@code true} if it is "}", otherwise {@code false}
     */
    private static boolean isRightCurlyBrace(Token<PHPTokenId> token) {
        return token.id() == PHPTokenId.PHP_CURLY_CLOSE;
    }

    /**
     * Check whether the token is ",".
     *
     * @param token the token
     * @return {@code true} if it is ",", otherwise {@code false}
     */
    private static boolean isComma(Token<PHPTokenId> token) {
        return token.id() == PHPTokenId.PHP_TOKEN && LexUtilities.textEquals(token.text(), ',');
    }

    @MimeRegistration(mimeType = Utils.PHP_MIME_TYPE, service = TypedTextInterceptor.Factory.class)
    public static class Factory implements TypedTextInterceptor.Factory {

        @Override
        public TypedTextInterceptor createTypedTextInterceptor(MimePath mimePath) {
            return new PhpTypedTextInterceptorEx();
        }

    }
}
