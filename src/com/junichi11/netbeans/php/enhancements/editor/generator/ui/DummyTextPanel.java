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
package com.junichi11.netbeans.php.enhancements.editor.generator.ui;

import com.junichi11.netbeans.php.enhancements.options.DummyTextOptions;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 *
 * @author junichi11
 */
public class DummyTextPanel extends JPanel {

    private static final long serialVersionUID = 5438353469413806727L;
    private static final DummyTextPanel INSTANCE = new DummyTextPanel();

    /**
     * Creates new form DummyTextPanel
     */
    public DummyTextPanel() {
        initComponents();

        init();
    }

    public static DummyTextPanel getDefault() {
        return INSTANCE;
    }

    private void init() {
        load();

        // right alignment
        JSpinner.DefaultEditor textLengthSpinnerEditor = (JSpinner.DefaultEditor) textLengthSpinner.getEditor();
        textLengthSpinnerEditor.getTextField().setHorizontalAlignment(JTextField.RIGHT);
        JSpinner.DefaultEditor loopCountSpinnerEditor = (JSpinner.DefaultEditor) loopCountSpinner.getEditor();
        loopCountSpinnerEditor.getTextField().setHorizontalAlignment(JTextField.RIGHT);
        setEnabledLoopGroup();
    }

    private void load() {
        DummyTextOptions options = DummyTextOptions.getInstance();
        textTextArea.setText(options.getText());
        loopCheckBox.setSelected(options.isLoop());
        textLengthRadioButton.setSelected(options.isTextLength());
        loopCountRadioButton.setSelected(options.isLoopCount());
        textLengthSpinner.setValue(options.getTextLength());
        loopCountSpinner.setValue(options.getLoopCount());
    }

    public boolean isLoop() {
        return loopCheckBox.isSelected();
    }

    public boolean isLoopCount() {
        return loopCountRadioButton.isSelected();
    }

    public int getLoopCount() {
        return (Integer) loopCountSpinner.getValue();
    }

    public boolean isTextLength() {
        return textLengthRadioButton.isSelected();
    }

    public int getTextLength() {
        return (Integer) textLengthSpinner.getValue();
    }

    public String getText() {
        return textTextArea.getText();
    }

    public String getInsertText() {
        String text = getText();
        String insertText = ""; // NOI18N

        if (text == null || text.isEmpty()) {
            return insertText;
        }

        if (!isLoop()) {
            return text;
        }

        // loop
        if (isLoopCount()) {
            insertText = getInsertTextWithLoopCount(text);
        } else if (isTextLength()) {
            insertText = getInsertTextWithTextLength(text);
        }

        return insertText;
    }

    private String getInsertTextWithLoopCount(String text) {
        StringBuilder sb = new StringBuilder();
        int loopCount = getLoopCount();
        for (int i = 0; i < loopCount; i++) {
            sb.append(text);
        }
        return sb.toString();
    }

    private String getInsertTextWithTextLength(String text) {
        StringBuilder sb = new StringBuilder();
        int textLength = getTextLength();
        sb.append(text);
        while (sb.length() < textLength) {
            sb.append(sb.toString());
        }
        sb.delete(textLength, sb.length());
        return sb.toString();
    }

    private void setEnabledLoopGroup() {
        boolean isSelected = loopCheckBox.isSelected();
        loopCountSpinner.setEnabled(isSelected);
        textLengthSpinner.setEnabled(isSelected);
        loopCountRadioButton.setEnabled(isSelected);
        textLengthRadioButton.setEnabled(isSelected);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loopButtonGroup = new javax.swing.ButtonGroup();
        textLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textTextArea = new javax.swing.JTextArea();
        loopCheckBox = new javax.swing.JCheckBox();
        textLengthRadioButton = new javax.swing.JRadioButton();
        loopCountRadioButton = new javax.swing.JRadioButton();
        saveAsDefaultButton = new javax.swing.JButton();
        loadDefaultButton = new javax.swing.JButton();
        textLengthSpinner = new javax.swing.JSpinner();
        loopCountSpinner = new javax.swing.JSpinner();

        org.openide.awt.Mnemonics.setLocalizedText(textLabel, org.openide.util.NbBundle.getMessage(DummyTextPanel.class, "DummyTextPanel.textLabel.text")); // NOI18N

        textTextArea.setColumns(20);
        textTextArea.setRows(5);
        jScrollPane1.setViewportView(textTextArea);

        org.openide.awt.Mnemonics.setLocalizedText(loopCheckBox, org.openide.util.NbBundle.getMessage(DummyTextPanel.class, "DummyTextPanel.loopCheckBox.text")); // NOI18N
        loopCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loopCheckBoxActionPerformed(evt);
            }
        });

        loopButtonGroup.add(textLengthRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(textLengthRadioButton, org.openide.util.NbBundle.getMessage(DummyTextPanel.class, "DummyTextPanel.textLengthRadioButton.text")); // NOI18N

        loopButtonGroup.add(loopCountRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(loopCountRadioButton, org.openide.util.NbBundle.getMessage(DummyTextPanel.class, "DummyTextPanel.loopCountRadioButton.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(saveAsDefaultButton, org.openide.util.NbBundle.getMessage(DummyTextPanel.class, "DummyTextPanel.saveAsDefaultButton.text")); // NOI18N
        saveAsDefaultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsDefaultButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(loadDefaultButton, org.openide.util.NbBundle.getMessage(DummyTextPanel.class, "DummyTextPanel.loadDefaultButton.text")); // NOI18N
        loadDefaultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadDefaultButtonActionPerformed(evt);
            }
        });

        textLengthSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(100), Integer.valueOf(1), null, Integer.valueOf(1)));

        loopCountSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(5), Integer.valueOf(1), null, Integer.valueOf(1)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(textLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(loopCheckBox)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textLengthRadioButton)
                            .addComponent(loopCountRadioButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(textLengthSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(loopCountSpinner))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(loadDefaultButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveAsDefaultButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loopCheckBox)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textLengthRadioButton)
                    .addComponent(textLengthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loopCountRadioButton)
                    .addComponent(loopCountSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveAsDefaultButton)
                    .addComponent(loadDefaultButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void loopCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loopCheckBoxActionPerformed
        setEnabledLoopGroup();
    }//GEN-LAST:event_loopCheckBoxActionPerformed

    private void saveAsDefaultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsDefaultButtonActionPerformed
        DummyTextOptions options = DummyTextOptions.getInstance();
        options.setLoop(isLoop());
        options.setLoopCount(isLoopCount());
        options.setLoopCount(getLoopCount());
        options.setText(getText());
        options.setTextLength(isTextLength());
        options.setTextLength(getTextLength());
    }//GEN-LAST:event_saveAsDefaultButtonActionPerformed

    private void loadDefaultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadDefaultButtonActionPerformed
        load();
        setEnabledLoopGroup();
    }//GEN-LAST:event_loadDefaultButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton loadDefaultButton;
    private javax.swing.ButtonGroup loopButtonGroup;
    private javax.swing.JCheckBox loopCheckBox;
    private javax.swing.JRadioButton loopCountRadioButton;
    private javax.swing.JSpinner loopCountSpinner;
    private javax.swing.JButton saveAsDefaultButton;
    private javax.swing.JLabel textLabel;
    private javax.swing.JRadioButton textLengthRadioButton;
    private javax.swing.JSpinner textLengthSpinner;
    private javax.swing.JTextArea textTextArea;
    // End of variables declaration//GEN-END:variables

}
