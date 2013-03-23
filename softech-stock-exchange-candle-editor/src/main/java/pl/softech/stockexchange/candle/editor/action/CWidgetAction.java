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
package pl.softech.stockexchange.candle.editor.action;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import pl.softech.stockexchange.candle.editor.CandlestickScene;
import pl.softech.stockexchange.candle.editor.CandlestickWidget;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public abstract class CWidgetAction {

    public abstract void register(CandlestickWidget widget);

    public abstract void unregister(CandlestickWidget widget);

    public static CWidgetAction createMoveAction() {
        return createMoveAction(null);
    }

    public static CWidgetAction createMoveAction(final CandlestickScene cscene) {

        return new CWidgetAction() {

            private CandlestickWidget widget;

            private CandlestickScene scene = cscene;

            class MyMouseAdapter extends MouseAdapter {

                boolean move;

                @Override
                public void mouseDragged(MouseEvent e) {

                    if (!move) {
                        return;
                    }

                    widget.getMoveHandler().move(e.getLocationOnScreen());

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    widget.getMoveHandler().setStart(e.getLocationOnScreen());
                    move = widget.getMoveHandler().shouldMove(e.getPoint());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    widget.getMoveHandler().reset();
                }
            }

            private MyMouseAdapter myMouseAdapter;

            @Override
            public void register(CandlestickWidget widget) {
                this.widget = widget;
                this.widget.setMoveHandler(new CWidgetMoveHandler(widget, scene));
                this.myMouseAdapter = new MyMouseAdapter();
                widget.getWidget().addMouseMotionListener(myMouseAdapter);
                widget.getWidget().addMouseListener(myMouseAdapter);
            }

            @Override
            public void unregister(CandlestickWidget widget) {
                widget.getWidget().removeMouseMotionListener(myMouseAdapter);
                widget.getWidget().removeMouseListener(myMouseAdapter);

            }

        };

    }

    public static CWidgetAction createResizeAction() {

        return new CWidgetAction() {

            class MyMouseAdapter extends MouseAdapter {

                private final CandlestickWidget widget;

                private Point start;

                public MyMouseAdapter(CandlestickWidget widget) {
                    this.widget = widget;
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    CWidgetResizeHandler h = widget.getResizeHandler();

                    if (start != null) {

                        h.resize(start, e.getLocationOnScreen());

                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    CWidgetResizeHandler h = widget.getResizeHandler();

                    if (h.shouldPerformResize(e.getPoint())) {
                        start = e.getLocationOnScreen();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    start = null;
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    CWidgetResizeHandler h = widget.getResizeHandler();
                    h.setSelected(!h.isSelected());
                }

            }

            private MyMouseAdapter myMouseAdapter;

            @Override
            public void register(CandlestickWidget widget) {
                this.myMouseAdapter = new MyMouseAdapter(widget);
                widget.setResizeHandler(new CWidgetResizeHandler(widget));
                widget.getWidget().addMouseMotionListener(myMouseAdapter);
                widget.getWidget().addMouseListener(myMouseAdapter);
            }

            @Override
            public void unregister(CandlestickWidget widget) {
                widget.getWidget().removeMouseMotionListener(myMouseAdapter);
                widget.getWidget().removeMouseListener(myMouseAdapter);

            }

        };
    }

}
