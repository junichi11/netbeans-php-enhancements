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

import com.junichi11.netbeans.php.enhancements.options.DummyImageOptions;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle;

/**
 *
 * @author junichi11
 */
public class DummyImagePanel extends JPanel {

    private static final long serialVersionUID = 7315133425669880521L;
    private static final DummyImagePanel INSTANCE = new DummyImagePanel();

    /**
     * Creates new form DummyImagePanel
     */
    public DummyImagePanel() {
        initComponents();
        init();
    }

    private void init() {
        load();
    }

    private void setColorBox(Color color) {
        colorPanel.setBackground(color);
        colorPanel.setVisible(true);
    }

    public static DummyImagePanel getDefault() {
        return INSTANCE;
    }

    @NbBundle.Messages("DummyImagePanel.Dialog.Title=Generate dummy image")
    public DialogDescriptor showDialog() {
        DialogDescriptor descriptor = new DialogDescriptor(this, Bundle.DummyImagePanel_Dialog_Title());
        Dialog dialog = DialogDisplayer.getDefault().createDialog(descriptor);
        dialog.setVisible(true);
        dialog.dispose();
        return descriptor;
    }

    public Color getColor() {
        return Color.decode(getHexColorCode());
    }

    public String getColorCode() {
        return colorTextField.getText();
    }

    public String getHexColorCode() {
        return getColorCode().replace("#", "0x"); // NOI18N
    }

    public int getImageHeight() {
        return (Integer) heightSpinner.getValue();
    }

    public int getImageWidth() {
        return (Integer) widthSpinner.getValue();
    }

    public boolean isOverwrite() {
        return overwriteCheckBox.isSelected();
    }

    public int getOpacity() {
        return (Integer) opacitySpinner.getValue();
    }

    public String getFileNamePrefix() {
        String text = fileNamePrefixTextField.getText();
        return text == null ? "" : text.trim(); // NOI18N
    }

    public String getFileNameSuffix() {
        String text = fileNameSuffixTextField.getText();
        return text == null ? "" : text.trim(); // NOI18N
    }

    public String getFileNameWithExt() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFileNamePrefix());
        sb.append(getImageWidth());
        sb.append("x"); // NOI18N
        sb.append(getImageHeight());
        sb.append(getFileNameSuffix());
        sb.append("."); // NOI18N
        sb.append(getExt());

        return sb.toString();
    }

    public String getFormat() {
        // TODO add other formats support
        return "PNG";
    }

    private String getExt() {
        // TODO add other formats support
        String format = getFormat();
        String ext = "png"; // NOI18N
        if ("PNG".equals(format)) { // NOI18N
            return ext;
        }

        throw new AssertionError();
    }

    private String toHexCode(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        String hexRed = toHexString(red);
        String hexGreen = toHexString(green);
        String hexBlue = toHexString(blue);

        return hexRed + hexGreen + hexBlue;
    }

    private String toHexString(int value) {
        String hex = Integer.toHexString(value);
        if (hex.length() == 1) {
            hex = "0" + hex; // NOI18N
        }
        return hex;
    }

    private void load() {
        DummyImageOptions options = DummyImageOptions.getInstance();
        widthSpinner.setValue(options.getImageWidth());
        heightSpinner.setValue(options.getImageHeight());
        colorTextField.setText(options.getColor());
        opacitySpinner.setValue(options.getOpacity());
        fileNamePrefixTextField.setText(options.getFileNamePrefix());
        fileNameSuffixTextField.setText(options.getFileNameSuffix());
        overwriteCheckBox.setSelected(options.isOverwrite());
        setColorBox(getColor());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        widthLabel = new javax.swing.JLabel();
        widthSpinner = new javax.swing.JSpinner();
        heightLabel = new javax.swing.JLabel();
        heightSpinner = new javax.swing.JSpinner();
        colorLabel = new javax.swing.JLabel();
        colorTextField = new javax.swing.JTextField();
        colorPanel = new javax.swing.JPanel();
        overwriteCheckBox = new javax.swing.JCheckBox();
        opacityLabel = new javax.swing.JLabel();
        opacitySpinner = new javax.swing.JSpinner();
        fileNamePrefixLabel = new javax.swing.JLabel();
        fileNamePrefixTextField = new javax.swing.JTextField();
        fileNameSuffixLabel = new javax.swing.JLabel();
        fileNameSuffixTextField = new javax.swing.JTextField();
        saveAsDefaultButton = new javax.swing.JButton();
        loadDefaultButton = new javax.swing.JButton();

        org.openide.awt.Mnemonics.setLocalizedText(widthLabel, org.openide.util.NbBundle.getMessage(DummyImagePanel.class, "DummyImagePanel.widthLabel.text")); // NOI18N

        widthSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(640), Integer.valueOf(1), null, Integer.valueOf(1)));

        org.openide.awt.Mnemonics.setLocalizedText(heightLabel, org.openide.util.NbBundle.getMessage(DummyImagePanel.class, "DummyImagePanel.heightLabel.text")); // NOI18N

        heightSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(480), Integer.valueOf(1), null, Integer.valueOf(1)));

        org.openide.awt.Mnemonics.setLocalizedText(colorLabel, org.openide.util.NbBundle.getMessage(DummyImagePanel.class, "DummyImagePanel.colorLabel.text")); // NOI18N

        colorTextField.setEditable(false);
        colorTextField.setText(org.openide.util.NbBundle.getMessage(DummyImagePanel.class, "DummyImagePanel.colorTextField.text")); // NOI18N
        colorTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                colorTextFieldMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout colorPanelLayout = new javax.swing.GroupLayout(colorPanel);
        colorPanel.setLayout(colorPanelLayout);
        colorPanelLayout.setHorizontalGroup(
            colorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        colorPanelLayout.setVerticalGroup(
            colorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        org.openide.awt.Mnemonics.setLocalizedText(overwriteCheckBox, org.openide.util.NbBundle.getMessage(DummyImagePanel.class, "DummyImagePanel.overwriteCheckBox.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(opacityLabel, org.openide.util.NbBundle.getMessage(DummyImagePanel.class, "DummyImagePanel.opacityLabel.text")); // NOI18N

        opacitySpinner.setModel(new javax.swing.SpinnerNumberModel(100, 0, 100, 1));

        org.openide.awt.Mnemonics.setLocalizedText(fileNamePrefixLabel, org.openide.util.NbBundle.getMessage(DummyImagePanel.class, "DummyImagePanel.fileNamePrefixLabel.text")); // NOI18N

        fileNamePrefixTextField.setText(org.openide.util.NbBundle.getMessage(DummyImagePanel.class, "DummyImagePanel.fileNamePrefixTextField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(fileNameSuffixLabel, org.openide.util.NbBundle.getMessage(DummyImagePanel.class, "DummyImagePanel.fileNameSuffixLabel.text")); // NOI18N

        fileNameSuffixTextField.setText(org.openide.util.NbBundle.getMessage(DummyImagePanel.class, "DummyImagePanel.fileNameSuffixTextField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(saveAsDefaultButton, org.openide.util.NbBundle.getMessage(DummyImagePanel.class, "DummyImagePanel.saveAsDefaultButton.text")); // NOI18N
        saveAsDefaultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsDefaultButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(loadDefaultButton, org.openide.util.NbBundle.getMessage(DummyImagePanel.class, "DummyImagePanel.loadDefaultButton.text")); // NOI18N
        loadDefaultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadDefaultButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(widthLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(widthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(heightLabel))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(colorLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(colorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(colorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(heightSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(opacityLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(opacitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(overwriteCheckBox)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(fileNamePrefixLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileNamePrefixTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(fileNameSuffixLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fileNameSuffixTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(widthLabel)
                            .addComponent(widthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(heightLabel)
                            .addComponent(heightSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(colorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(colorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(opacityLabel)
                            .addComponent(opacitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(colorLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fileNamePrefixLabel)
                    .addComponent(fileNamePrefixTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fileNameSuffixLabel)
                    .addComponent(fileNameSuffixTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(overwriteCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveAsDefaultButton)
                    .addComponent(loadDefaultButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void colorTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorTextFieldMouseClicked
        Color color = JColorChooser.showDialog(null, "Select color", getColor());
        if (color != null) {
            String hexColor = "#" + toHexCode(color); // NOI18N
            colorTextField.setText(hexColor);
            setColorBox(getColor());
        }
    }//GEN-LAST:event_colorTextFieldMouseClicked

    private void saveAsDefaultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsDefaultButtonActionPerformed
        DummyImageOptions options = DummyImageOptions.getInstance();
        options.setImageWidth(getImageWidth());
        options.setImageHeight(getImageHeight());
        options.setOpacity(getOpacity());
        options.setColor(getColorCode());
        options.setFileNamePrefix(getFileNamePrefix());
        options.setFileNameSuffix(getFileNameSuffix());
        options.setOverwrite(isOverwrite());
    }//GEN-LAST:event_saveAsDefaultButtonActionPerformed

    private void loadDefaultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadDefaultButtonActionPerformed
        load();
    }//GEN-LAST:event_loadDefaultButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel colorLabel;
    private javax.swing.JPanel colorPanel;
    private javax.swing.JTextField colorTextField;
    private javax.swing.JLabel fileNamePrefixLabel;
    private javax.swing.JTextField fileNamePrefixTextField;
    private javax.swing.JLabel fileNameSuffixLabel;
    private javax.swing.JTextField fileNameSuffixTextField;
    private javax.swing.JLabel heightLabel;
    private javax.swing.JSpinner heightSpinner;
    private javax.swing.JButton loadDefaultButton;
    private javax.swing.JLabel opacityLabel;
    private javax.swing.JSpinner opacitySpinner;
    private javax.swing.JCheckBox overwriteCheckBox;
    private javax.swing.JButton saveAsDefaultButton;
    private javax.swing.JLabel widthLabel;
    private javax.swing.JSpinner widthSpinner;
    // End of variables declaration//GEN-END:variables
}
