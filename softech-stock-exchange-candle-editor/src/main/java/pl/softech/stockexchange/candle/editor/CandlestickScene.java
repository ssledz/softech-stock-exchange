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
package pl.softech.stockexchange.candle.editor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import pl.softech.stockexchange.candle.editor.action.CWidgetMotionListenerImpl;
import pl.softech.stockexchange.candle.editor.action.CWidgetMoveEvent;
import pl.softech.stockexchange.candle.editor.action.CWidgetMoveListener;
import pl.softech.stockexchange.candle.editor.dad.CWidgetDropEvent;
import pl.softech.stockexchange.candle.editor.dad.CWidgetDropListener;
import pl.softech.stockexchange.candle.editor.dad.DragAndDropSupport;
import pl.softech.stockexchange.candle.pattern.CandlestickPatternMetaDataImpl;
import pl.softech.stockexchange.candle.pattern.ICandlePatternMetaData;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class CandlestickScene {

    private final JPanel scene;

    private final Collection<CandlestickWidget> candlestickWidgets;

    private final List<CWidgetMoveListener> moveListeners;
    private final List<CWidgetDropListener> dropListeners;

    private final CandlestickPatternMetaDataImpl metaData;

    public CandlestickScene() {
        this.candlestickWidgets = new LinkedList<CandlestickWidget>();
        scene = new JPanel();
        scene.setLayout(null);
        this.moveListeners = new LinkedList<CWidgetMoveListener>();
        this.dropListeners = new LinkedList<CWidgetDropListener>();
        this.metaData = new CandlestickPatternMetaDataImpl() {
            @Override
            public int getBarCount() {
                return candlestickWidgets.size();
            }
        };
        DragAndDropSupport.addSceneSupport(this);
        initListeners();
    }

    private void initListeners() {
        MouseAdapter l = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                for (CandlestickWidget cw : candlestickWidgets) {
                    // Clear selection
                    cw.getResizeHandler().setSelected(false);

                }
            }
        };
        scene.addMouseListener(l);
        CWidgetMotionListenerImpl ll = new CWidgetMotionListenerImpl(this);
        moveListeners.add(ll);
        dropListeners.add(ll);
    }

    public void addWidget(CandlestickWidget widget) {
        candlestickWidgets.add(widget);
        scene.add(widget.getWidget());
        fireCWidgetDrop(new CWidgetDropEvent(widget));
        scene.repaint();
    }

    public void setMetaData(ICandlePatternMetaData metaData) {
        this.metaData.derive(metaData);
    }

    public Collection<CandlestickWidget> getWidgets() {
        return candlestickWidgets;
    }

    public JPanel getScene() {
        return scene;
    }

    public ICandlePatternMetaData getMetaData() {
        return metaData;
    }

    public void fireCWidgetMove(CWidgetMoveEvent event) {
        for (CWidgetMoveListener l : moveListeners) {
            l.movePerformed(event);
        }
    }

    public void fireCWidgetDrop(CWidgetDropEvent event) {
        for (CWidgetDropListener l : dropListeners) {
            l.dropPerformed(event);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (CandlestickWidget w : candlestickWidgets) {
            builder.append(w.getWidget().getBounds()).append("\n");
        }
        return builder.toString();
    }

}
