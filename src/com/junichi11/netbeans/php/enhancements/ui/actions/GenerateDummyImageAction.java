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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.loaders.DataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Images",
        id = "com.junichi11.netbeans.php.enhancements.ui.actions.GenerateDummyImageAction")
@ActionRegistration(
        displayName = "#CTL_GenerateDummyImageAction")
@ActionReference(path = "Loaders/folder/any/Actions", position = 1800)
@Messages("CTL_GenerateDummyImageAction=Generate Dummy Image")
public final class GenerateDummyImageAction implements ActionListener {

    private static final Logger LOGGER = Logger.getLogger(GenerateDummyImageAction.class.getName());
    private final DataObject context;

    public GenerateDummyImageAction(DataObject context) {
        this.context = context;
    }
    @NbBundle.Messages({
        "# {0} - file name",
        "GenerateDummyImageAction.File.Exists.Warning=File({0}) already exisits.",
        "GenerateDummyImageAction.File.Exists.Title=Warning"
    })
    @Override
    public void actionPerformed(ActionEvent ev) {
        FileObject directory = context.getPrimaryFile();
        if (directory == null) {
            return;
        }

        // panel
        DummyImagePanel panel = DummyImagePanel.getDefault();
        DialogDescriptor descriptor = panel.showDialog();

        if (descriptor.getValue() == DialogDescriptor.OK_OPTION) {
            int width = panel.getImageWidth();
            int height = panel.getImageHeight();

            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            // color
            Color color = panel.getColor();
            int alpha = (int) (255 * panel.getOpacity() * 0.01);
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();

            // draw rectangular
            graphics.setColor(color);
            graphics.fillRect(0, 0, width, height);

            // draw size
            graphics.setColor(Color.decode("0xffffff")); // NOI18N
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Font font = new Font(Font.SANS_SERIF, Font.BOLD, getDrawStringFontSize(height));
            graphics.setFont(font);
            drawSize(width, height, font, graphics);

            graphics.dispose();

            // get file
            String fileNameWithExt = panel.getFileNameWithExt();
            FileObject fileObject = directory.getFileObject(fileNameWithExt);

            if (fileObject != null && !panel.isOverwrite()) {
                // show dialog
                showWarningDialog(fileNameWithExt);
                LOGGER.log(Level.WARNING, Bundle.GenerateDummyImageAction_File_Exists_Warning(fileNameWithExt));
                return;
            }

            try {
                // write
                FileObject createData = FileUtil.createData(directory, fileNameWithExt);
                ImageIO.write(bufferedImage, panel.getFormat(), FileUtil.toFile(createData));
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    private int getDrawStringFontSize(int height) {
        int size = 15;
        if (height >= 600) {
            size = 30;
        } else if (height >= 300) {
            size = 20;
        }
        return size;
    }

    private void drawSize(int width, int height, Font font, Graphics2D graphics) {
        String drawString = width + " x " + height; // NOI18N

        Rectangle2D rectangle = font.getStringBounds(drawString, graphics.getFontRenderContext());
        int drawStringWidth = (int) rectangle.getWidth();
        int drawStringHeight = (int) rectangle.getHeight();
        if (width > drawStringWidth && height > drawStringHeight) {
            int prefferedWidth = (width - drawStringWidth) / 2;
            int prefferedHeight = height / 2;
            graphics.drawString(drawString, prefferedWidth, prefferedHeight);
        }
    }

    private void showWarningDialog(String fileNameWithExt) {
        NotifyDescriptor.Message message = new NotifyDescriptor.Message(
                Bundle.GenerateDummyImageAction_File_Exists_Warning(fileNameWithExt),
                NotifyDescriptor.WARNING_MESSAGE);
        Object response = DialogDisplayer.getDefault().notify(message);
    }
}
