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

import java.awt.datatransfer.Transferable;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import pl.softech.stockexchange.candle.editor.CandlestickWidget.CWidget;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class CWidgetTransferHandler extends TransferHandler {

    private static final long serialVersionUID = 1L;
    
    @Override
    public boolean canImport(TransferSupport support) {
        return false;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        CWidget cWidget = (CWidget) c; 
        return new CWidgetTransferable(cWidget.getCandlestickWidget());
    }

    @Override
    public Icon getVisualRepresentation(Transferable t) {
        return super.getVisualRepresentation(t);
    }

    @Override
    public int getSourceActions(JComponent c) {
        return COPY;
    }
    
    
    
    
}
