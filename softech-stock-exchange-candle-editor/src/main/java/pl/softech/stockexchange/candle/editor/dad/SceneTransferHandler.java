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

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import javax.swing.TransferHandler;
import pl.softech.stockexchange.candle.editor.CandlestickScene;
import pl.softech.stockexchange.candle.editor.CandlestickWidget;
import pl.softech.stockexchange.candle.editor.action.CWidgetAction;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class SceneTransferHandler extends TransferHandler {

    private static final long serialVersionUID = 1L;

    private final CandlestickScene scene;

    public SceneTransferHandler(CandlestickScene scene) {
        this.scene = scene;
    }

    @Override
    public boolean canImport(TransferSupport support) {
        if (!support.isDataFlavorSupported(CWidgetTransferable.DATA_FLAVOR)) {
            return false;
        }

        try {
            CandlestickWidget widget = (CandlestickWidget) support.getTransferable().getTransferData(
                    CWidgetTransferable.DATA_FLAVOR);
            
            Point dp = support.getDropLocation().getDropPoint();
            
            Rectangle wbounds = widget.getWidget().getBounds();
            Rectangle sbounds = scene.getScene().getVisibleRect();
            
            //horizontal check
            if(dp.x <= 0 || dp.x + wbounds.getWidth() >= sbounds.getMaxX()) {
                return false;
            }
            //vertical check
            if(dp.y <= 0 || dp.y + wbounds.getHeight() >= sbounds.getMaxY()) {
                return false;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (!support.isDrop()) {
            return false;
        }

        Transferable t = support.getTransferable();

        try {
            CandlestickWidget widget = (CandlestickWidget) t.getTransferData(CWidgetTransferable.DATA_FLAVOR);
            DropLocation dl = support.getDropLocation();
            widget = widget.clone();
            widget.addAction(CWidgetAction.createMoveAction(scene));
            widget.addAction(CWidgetAction.createResizeAction());
            widget.setLocation(dl.getDropPoint());
            scene.addWidget(widget);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
