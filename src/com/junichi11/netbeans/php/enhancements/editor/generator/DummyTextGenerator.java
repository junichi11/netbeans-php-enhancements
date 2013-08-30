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
package com.junichi11.netbeans.php.enhancements.editor.generator;

import com.junichi11.netbeans.php.enhancements.editor.generator.ui.DummyTextPanel;
import java.awt.Dialog;
import java.util.Collections;
import java.util.List;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.editor.mimelookup.MimeRegistrations;
import org.netbeans.lib.editor.codetemplates.api.CodeTemplate;
import org.netbeans.lib.editor.codetemplates.api.CodeTemplateManager;
import org.netbeans.spi.editor.codegen.CodeGenerator;
import org.netbeans.spi.editor.codegen.CodeGeneratorContextProvider;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

public class DummyTextGenerator implements CodeGenerator {

    JTextComponent textComp;

    /**
     *
     * @param context containing JTextComponent and possibly other items
     * registered by {@link CodeGeneratorContextProvider}
     */
    private DummyTextGenerator(Lookup context) { // Good practice is not to save Lookup outside ctor
        textComp = context.lookup(JTextComponent.class);
    }

    @MimeRegistrations({
        @MimeRegistration(mimeType = "text/x-php5", service = CodeGenerator.Factory.class),
        @MimeRegistration(mimeType = "text/html", service = CodeGenerator.Factory.class)
    })
    public static class Factory implements CodeGenerator.Factory {

        @Override
        public List<? extends CodeGenerator> create(Lookup context) {
            JTextComponent jtc = context.lookup(JTextComponent.class);
            if (jtc == null) {
                return Collections.emptyList();
            }
            return Collections.singletonList(new DummyTextGenerator(context));
        }
    }

    /**
     * The name which will be inserted inside Insert Code dialog
     */
    @NbBundle.Messages("DummyTextGenerator.DisplayName=Dummy Text")
    @Override
    public String getDisplayName() {
        return Bundle.DummyTextGenerator_DisplayName();
    }

    /**
     * This will be invoked when user chooses this Generator from Insert Code
     * dialog
     */
    @Override
    public void invoke() {
        Document document = textComp.getDocument();
        if (document == null) {
            return;
        }

        // create dialog
        DummyTextPanel dummyTextPanel = DummyTextPanel.getDefault();
        DialogDescriptor descriptor = new DialogDescriptor(dummyTextPanel, Bundle.DummyTextGenerator_DisplayName());
        Dialog dialog = DialogDisplayer.getDefault().createDialog(descriptor);
        dialog.setVisible(true);
        dialog.dispose();

        if (descriptor.getValue() == DialogDescriptor.OK_OPTION) {
            // text for inserting
            String text = dummyTextPanel.getInsertText();
            if (text == null || text.isEmpty()) {
                return;
            }

            // insert code with CodeTemplateManager
            CodeTemplateManager templateManager = CodeTemplateManager.get(document);
            CodeTemplate codeTemplate = templateManager.createTemporary(text);
            codeTemplate.insert(textComp);
        }
    }
}
