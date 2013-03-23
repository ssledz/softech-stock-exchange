/*
 * Copyright 2013 Sławomir Śledź <slawomir.sledz@sof-tech.pl>.
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
package pl.softech.stockexchange.candle.editor.dad;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import pl.softech.stockexchange.candle.editor.CandlestickScene;
import pl.softech.stockexchange.candle.editor.CandlestickWidget;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class DragAndDropSupport {

    public static void addSupport(CandlestickWidget widget) {
        widget.getWidget().setTransferHandler(new CWidgetTransferHandler());
        widget.getWidget().addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
               JComponent c = (JComponent) e.getSource();
               c.getTransferHandler().exportAsDrag(c, e, TransferHandler.COPY);
            }
        });
    }

    public static void addSceneSupport(CandlestickScene scene) {
        scene.getScene().setTransferHandler(new SceneTransferHandler(scene));
    }

}
