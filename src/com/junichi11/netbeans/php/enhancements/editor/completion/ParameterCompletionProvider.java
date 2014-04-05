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

import com.junichi11.netbeans.php.enhancements.utils.Utils;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.annotations.common.CheckForNull;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.csl.api.DeclarationFinder;
import org.netbeans.modules.csl.api.ElementHandle;
import org.netbeans.modules.csl.api.ParameterInfo;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.netbeans.modules.parsing.api.ParserManager;
import org.netbeans.modules.parsing.api.ResultIterator;
import org.netbeans.modules.parsing.api.Source;
import org.netbeans.modules.parsing.api.UserTask;
import org.netbeans.modules.parsing.spi.ParseException;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.php.editor.csl.DeclarationFinderImpl;
import org.netbeans.modules.php.editor.lexer.LexUtilities;
import org.netbeans.modules.php.editor.lexer.PHPTokenId;
import org.netbeans.modules.php.editor.model.Model;
import org.netbeans.modules.php.editor.model.ParameterInfoSupport;
import org.netbeans.modules.php.editor.parser.PHPParseResult;
import org.netbeans.spi.editor.completion.CompletionDocumentation;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;
import org.openide.filesystems.FileObject;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

/**
 *
 * @author junichi11
 */
@MimeRegistration(mimeType = Utils.PHP_MIME_TYPE, service = CompletionProvider.class)
public class ParameterCompletionProvider implements CompletionProvider {

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

        public AsyncCompletionQueryImpl() {
        }

        @Override
        protected void query(CompletionResultSet resultSet, Document doc, final int caretOffset) {
            try {
                TokenSequence<PHPTokenId> ts = Utils.getTokenSequence(doc, caretOffset);
                if (ts == null) {
                    return;
                }
                ts.move(caretOffset);
                ts.moveNext();

                Token<PHPTokenId> token = ts.token();
                if (token == null) {
                    return;
                }

                PHPTokenId id = token.id();
                if (id != PHPTokenId.PHP_CONSTANT_ENCAPSED_STRING) {
                    return;
                }
                // current input text
                String currentInputText = token.text().toString();
                String inputText = ""; // NOI18N
                if (currentInputText.length() >= 2) {
                    int tokenStartPosition = ts.offset();
                    int endIndex = caretOffset - tokenStartPosition;
                    if (endIndex > 1) {
                        inputText = currentInputText.substring(1, endIndex);
                    }
                }
                String filterText = inputText;

                // get context
                Context context = getContext(ts, doc);
                if (context == null) {
                    return;
                }
                List<Parameter> parameters;
                ParameterFilter parameterFilter = null;
                switch (context.getType()) {
                    case FUNCTION:
                        String functionName = context.getName();
                        Function function = Function.fromString(functionName);
                        if (function == null) {
                            return;
                        }

                        FileObject fileObject = NbEditorUtilities.getFileObject(doc);
                        if (fileObject == null) {
                            return;
                        }

                        // get parameter index
                        int parameterIndex = getParameterIndex(fileObject, caretOffset);
                        if (parameterIndex < 0) {
                            return;
                        }
                        parameters = function.get(parameterIndex, filterText);
                        filterText = function.getProperFilterText(parameterIndex, filterText);
                        parameterFilter = function.getParameterFilter(parameterIndex, filterText, inputText);
                        break;
                    case ARRAY:
                        SuperGlobalArray superGlobal = SuperGlobalArray.fromString(context.getName());
                        if (superGlobal == null) {
                            return;
                        }
                        parameters = superGlobal.get(filterText);
                        break;
                    default:
                        return;
                }

                if (parameterFilter == null) {
                    parameterFilter = ParameterFilter.DEFAULT_FILTER;
                }

                for (Parameter parameter : parameters) {
                    if (parameterFilter.accept(parameter, filterText, inputText)) {
                        resultSet.addItem(new ParameterCompletionItem(caretOffset, filterText, parameter));
                    }
                }
            } catch (ParseException ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                resultSet.finish();
            }
        }

        /**
         * Get parameter index.
         *
         * @param fileObject not null
         * @param caretOffset > 0
         * @return index number if it is found, -1 otherwse.
         * @throws ParseException
         */
        private int getParameterIndex(FileObject fileObject, final int caretOffset) throws ParseException {
            final Set<ParameterInfo> info = new HashSet<ParameterInfo>();
            ParserManager.parse(Collections.singleton(Source.create(fileObject)), new UserTask() {

                @Override
                public void run(ResultIterator resultIterator) throws Exception {
                    Parser.Result result = resultIterator.getParserResult();
                    if (result == null) {
                        return;
                    }
                    PHPParseResult parseResult = (PHPParseResult) result;
                    Model model = parseResult.getModel();
                    ParameterInfoSupport parameterInfoSupport = model.getParameterInfoSupport(caretOffset);
                    ParameterInfo parameterInfo = parameterInfoSupport.getParameterInfo();
                    if (parameterInfo != null) {
                        info.add(parameterInfo);
                    }
                }
            });
            int parameterIndex = -1;
            if (!info.isEmpty()) {
                for (ParameterInfo parameterInfo : info) {
                    parameterIndex = parameterInfo.getCurrentIndex();
                    break;
                }
                info.clear();
            }
            return parameterIndex;
        }
    }

    @CheckForNull
    private static Context getContext(TokenSequence<PHPTokenId> ts, Document document) {
        String name = ""; // NOI18N
        int startOffset = ts.offset();
        // array
        while (ts.movePrevious()) {
            Token<PHPTokenId> token = ts.token();
            PHPTokenId id = token.id();
            if (id == PHPTokenId.PHP_SEMICOLON) {
                return null;
            }
            if (id == PHPTokenId.PHP_TOKEN && LexUtilities.textEquals(token.text(), '[')) { // NOI18N
                ts.movePrevious();
                Token<PHPTokenId> variableToken = ts.token();
                if (variableToken.id() == PHPTokenId.PHP_VARIABLE) {
                    name = variableToken.text().toString();
                    return new Context(name, Context.Type.ARRAY);
                }
            }
            break;
        }

        // function
        int balance = 0;
        ts.move(startOffset);
        while (ts.movePrevious()) {
            Token<PHPTokenId> token = ts.token();
            PHPTokenId id = token.id();
            if (id == PHPTokenId.PHP_SEMICOLON) {
                return null;
            }
            if (id == PHPTokenId.PHP_TOKEN) {
                if (LexUtilities.textEquals(token.text(), ')')) { // NOI18N
                    balance++;
                } else if (LexUtilities.textEquals(token.text(), '(')) { // NOI18N
                    if (balance == 0) {
                        break;
                    }
                    balance--;
                }
            }
        }
        ts.movePrevious();
        Token<PHPTokenId> token = ts.token();
        if (token == null) {
            return null;
        }
        PHPTokenId id = token.id();
        if (id == PHPTokenId.PHP_STRING) {
            name = token.text().toString();
        }

        // method?
        if (!name.isEmpty()) {
            final int offset = ts.offset();
            ts.movePrevious();
            Token<PHPTokenId> previousToken = ts.token();
            if (previousToken != null) {
                PHPTokenId previousTokenId = previousToken.id();
                // static method
                if (previousTokenId == PHPTokenId.PHP_PAAMAYIM_NEKUDOTAYIM) {
                    ts.movePrevious();
                    Token<PHPTokenId> classNameToken = ts.token();
                    if (classNameToken != null && classNameToken.id() == PHPTokenId.PHP_STRING) {
                        name = classNameToken.text().toString() + "::" + name; // NOI18N
                    }
                }

                // method
                if (previousTokenId == PHPTokenId.PHP_OBJECT_OPERATOR) {
                    name = withClassName(document, offset, name);
                }
            }
        }
        return new Context(name, Context.Type.FUNCTION);
    }

    private static String withClassName(Document document, final int offset, String name) {
        final Set<DeclarationFinder.DeclarationLocation> locations = new HashSet<DeclarationFinder.DeclarationLocation>();
        try {
            ParserManager.parse(Collections.singleton(Source.create(document)), new UserTask() {

                @Override
                public void run(ResultIterator resultIterator) throws Exception {
                    Parser.Result result = resultIterator.getParserResult();
                    if (result == null) {
                        return;
                    }
                    PHPParseResult parseResult = (PHPParseResult) result;
                    DeclarationFinder.DeclarationLocation location = DeclarationFinderImpl.findDeclarationImpl(parseResult, offset);
                    if (location != null) {
                        locations.add(location);
                    }
                }
            });
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        }
        for (DeclarationFinder.DeclarationLocation location : locations) {
            ElementHandle element = location.getElement();
            if (element != null) {
                String in = element.getIn();
                if (in != null && !in.isEmpty()) {
                    return String.format("%s::%s", in, name); // NOI18N
                }
            }
        }
        return name;
    }

    private static class Context {

        private final String name;
        private final Type type;

        public enum Type {

            FUNCTION,
            ARRAY;
        }

        public Context(String name, Type type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public Type getType() {
            return type;
        }

    }

    static class ParameterCompletionItem implements CompletionItem {

        private final int caretOffset;
        private final String text;
        private final String description;
        private final String category;
        private final String filter;
        private static final ImageIcon icon = ImageUtilities.loadImageIcon("com/junichi11/netbeans/php/enhancements/resources/parameter16.png", false); // NOI18N

        public ParameterCompletionItem(int caretOffset, String filter, Parameter parameter) {
            this.text = parameter.getName();
            this.description = parameter.getDescription();
            this.category = parameter.getCategory();
            this.caretOffset = caretOffset;
            this.filter = filter;
        }

        @Override
        public void defaultAction(JTextComponent component) {
            final StyledDocument document = (StyledDocument) component.getDocument();
            NbDocument.runAtomic(document, new Runnable() {

                @Override
                public void run() {
                    int filterLength = filter.length();
                    int startOffset = caretOffset - filterLength;
                    try {
                        document.remove(startOffset, filterLength);
                        document.insertString(startOffset, text, null);
                        Completion.get().hideAll();
                    } catch (BadLocationException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }

        @Override
        public void processKeyEvent(KeyEvent evt) {
            if (evt.getKeyChar() == '/') {
                Completion.get().showCompletion();
            }
        }

        @Override
        public int getPreferredWidth(Graphics graphics, Font font) {
            return CompletionUtilities.getPreferredWidth(getLeftHtmlText(), getRightHtmlText(), graphics, font);
        }

        @Override
        public void render(Graphics g, Font defaultFont, Color defaultColor, Color backgroundColor, int width, int height, boolean selected) {
            CompletionUtilities.renderHtml(getIcon(), getLeftHtmlText(), getRightHtmlText(), g, defaultFont, defaultColor, width, height, selected);
        }

        private ImageIcon getIcon() {
            return icon;
        }

        private String getLeftHtmlText() {
            return text.replaceAll(String.format("(?i)(%s)", filter), "<b>$1</b>"); // NOI18N
        }

        private String getRightHtmlText() {
            return category;
        }

        @Override
        public CompletionTask createDocumentationTask() {
            if (description == null || description.isEmpty()) {
                return null;
            }
            return new AsyncCompletionTask(new AsyncCompletionQuery() {

                @Override
                protected void query(CompletionResultSet resultSet, Document doc, int caretOffset) {
                    resultSet.setDocumentation(new CompletionDocumentationImpl(ParameterCompletionItem.this));
                    resultSet.finish();
                }

            });
        }

        @Override
        public CompletionTask createToolTipTask() {
            return null;
        }

        @Override
        public boolean instantSubstitution(JTextComponent component) {
            return false;
        }

        @Override
        public int getSortPriority() {
            return 0;
        }

        @Override
        public CharSequence getSortText() {
            return text;
        }

        @Override
        public CharSequence getInsertPrefix() {
            return text;
        }

        public String getDescription() {
            return description;
        }

    }

    static class CompletionDocumentationImpl implements CompletionDocumentation {

        private final String text;

        public CompletionDocumentationImpl(ParameterCompletionItem item) {
            this.text = item.getDescription();
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public URL getURL() {
            return null;
        }

        @Override
        public CompletionDocumentation resolveLink(String link) {
            return null;
        }

        @Override
        public Action getGotoSourceAction() {
            return null;
        }
    }

}
