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

import com.junichi11.netbeans.php.enhancements.utils.Utils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.modules.php.editor.lexer.LexUtilities;
import org.netbeans.modules.php.editor.lexer.PHPTokenId;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;

@ActionID(
        category = "Refactoring",
        id = "com.junichi11.netbeans.php.enhancements.ui.actions.ConvertToPhpShortArraySyntaxAction"
)
@ActionRegistration(
        displayName = "#CTL_ConvertToPhpShortArraySyntaxAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 3300),
    @ActionReference(path = "Projects/Actions", position = 1800),
    @ActionReference(path = "Loaders/text/x-php5/Actions", position = 1800),
    @ActionReference(path = "Loaders/folder/any/Actions", position = 1800),})
@Messages("CTL_ConvertToPhpShortArraySyntaxAction=Convert to short array syntax")
public final class ConvertToPhpShortArraySyntaxAction implements ActionListener {

    private final List<DataObject> context;
    private final StringBuilder sb = new StringBuilder();
    private final List<Integer> balanceStack = new LinkedList<Integer>();
    private boolean isArraySyntax = false;
    private boolean isChanged = false;
    private int arrayCount = 0;
    private int balance = 0;
    private TokenSequence<PHPTokenId> ts;

    public ConvertToPhpShortArraySyntaxAction(List<DataObject> context) {
        this.context = context;
    }

    @NbBundle.Messages({
        "ConvertToPhpShortArraySyntaxAction.action.confirmation=If you select folders, run for all files within them recursively. Recommend to backup folders or to use version management system. Do you really want to run this action?",
        "ConvertToPhpShortArraySyntaxAction.action.complete=Convert to PHP short array syntax: complete",
        "ConvertToPhpShortArraySyntaxAction.perform.progress=Converting to short array syntax",})
    @Override
    public void actionPerformed(ActionEvent ev) {
        // confirmation if directories are selected
        for (DataObject dataObject : context) {
            FileObject fileObject = dataObject.getPrimaryFile();
            if (fileObject.isFolder()) {
                NotifyDescriptor.Confirmation confirmation = new NotifyDescriptor.Confirmation(
                        Bundle.ConvertToPhpShortArraySyntaxAction_action_confirmation(),
                        NotifyDescriptor.OK_CANCEL_OPTION
                );
                if (DialogDisplayer.getDefault().notify(confirmation) != NotifyDescriptor.OK_OPTION) {
                    return;
                }
                break;
            }
        }

        // show progress bar
        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                ProgressHandle handle = ProgressHandleFactory.createHandle(Bundle.ConvertToPhpShortArraySyntaxAction_perform_progress());
                try {
                    handle.start();
                    for (DataObject dataObject : context) {
                        actionPerformed(dataObject);
                    }
                } finally {
                    handle.finish();
                }

                // show complete dialog
                NotifyDescriptor.Message message = new NotifyDescriptor.Message(
                        Bundle.ConvertToPhpShortArraySyntaxAction_action_complete(),
                        NotifyDescriptor.INFORMATION_MESSAGE
                );
                DialogDisplayer.getDefault().notify(message);
            }
        });
    }

    public void actionPerformed(DataObject dataObject) {
        // recursive
        FileObject fileObject = dataObject.getPrimaryFile();
        if (fileObject.isFolder()) {
            for (FileObject child : fileObject.getChildren()) {
                try {
                    actionPerformed(DataObject.find(child));
                } catch (DataObjectNotFoundException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }

        // get EditorCookie
        EditorCookie ec = dataObject.getLookup().lookup(EditorCookie.class);
        if (ec == null) {
            return;
        }

        try {
            // get document
            StyledDocument document = getDocument(ec);
            if (!Utils.isPHP(document)) {
                return;
            }
            final StyledDocument sdoc = document;
            ts = Utils.getTokenSequence(sdoc, 0);
            if (ts == null) {
                return;
            }
            ts.move(0);

            NbDocument.runAtomic(sdoc, new Runnable() {

                @Override
                public void run() {
                    init();

                    while (ts.moveNext()) {
                        Token token = ts.token();
                        handleToken(token);
                    }
                    if (!isChanged) {
                        return;
                    }

                    try {
                        // XXX line feed is added to last token when document is devided to tokens
                        int length = sb.length();
                        if (length > 0) {
                            // delete last position
                            sb.deleteCharAt(length - 1);
                        }
                        sdoc.remove(0, sdoc.getLength());
                        sdoc.insertString(0, sb.toString(), null);
                    } catch (BadLocationException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });

            // save
            ec.saveDocument();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void init() {
        isArraySyntax = false;
        isChanged = false;
        balance = 0;
        arrayCount = 0;
        balanceStack.clear();
        sb.setLength(0);
    }

    private StyledDocument getDocument(EditorCookie ec) throws IOException {
        StyledDocument document = ec.getDocument();
        if (document == null) {
            document = ec.openDocument();
        }
        return document;
    }

    private void handleToken(Token token) {
        // array
        if (token.id() == PHPTokenId.PHP_ARRAY) {
            handleArray();
            return;
        }

        if (arrayCount > 0) {
            // open
            if (LexUtilities.textEquals(token.text(), '(')) { // NOI18N
                handleOpen();
                return;
            }

            // close
            if (LexUtilities.textEquals(token.text(), ')')) { // NOI18N
                handleClose();
                return;
            }
        }

        // default
        sb.append(token.text());
    }

    private void handleArray() {
        /*
         * case 1: function parameter type e.g. public function something(array $param){}
         * case 2: cast e.g. (array) $something
         * case 3: array('something');
         */
        Token<? extends PHPTokenId> nextToken = LexUtilities.findNext(ts, Arrays.asList(PHPTokenId.WHITESPACE, PHPTokenId.PHP_ARRAY));
        if (LexUtilities.textEquals(nextToken.text(), '(')) { // NOI18N
            isArraySyntax = true;
            arrayCount++;
        } else {
            sb.append("array"); // NOI18N
        }
        LexUtilities.findPreviousToken(ts, Arrays.asList(PHPTokenId.PHP_ARRAY));
    }

    private void handleOpen() {
        if (isArraySyntax) {
            isChanged = true;
            isArraySyntax = false;
            balanceStack.add(0, balance);
            balance = 0;
            sb.append('['); // NOI18N
            return;
        }
        sb.append('('); // NOI18N
        balance++;
    }

    private void handleClose() {
        if (balance == 0) {
            sb.append(']'); // NOI18N
            arrayCount--;
            if (!balanceStack.isEmpty()) {
                balance = balanceStack.get(0);
                balanceStack.remove(0);
            }
            return;
        }
        sb.append(')'); // NOI18N
        balance--;
    }

}
