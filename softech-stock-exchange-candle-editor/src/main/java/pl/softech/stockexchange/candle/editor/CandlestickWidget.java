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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import pl.softech.stockexchange.candle.Candle;
import pl.softech.stockexchange.candle.editor.action.CWidgetAction;
import pl.softech.stockexchange.candle.editor.action.CWidgetMoveHandler;
import pl.softech.stockexchange.candle.editor.action.CWidgetResizeHandler;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class CandlestickWidget implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private static int DEFAULT_INSETS = 5;

    public class CWidget extends JComponent {

        private static final long serialVersionUID = 1L;

        private Insets insets = new Insets(DEFAULT_INSETS, DEFAULT_INSETS, DEFAULT_INSETS, DEFAULT_INSETS);

        private CWidget(Point location) {
            setLocation(location);
            Dimension size = new Dimension(60, 200);
            setSize(size);
            setMaximumSize(size);
            setPreferredSize(size);
        }

        private Rectangle2D getDrawingArea() {

            Rectangle bounds = getBounds();
            return new Rectangle2D.Double(//
                    insets.left,//
                    insets.top,//
                    bounds.getWidth() - insets.left - insets.right,//
                    bounds.getHeight() - insets.top - insets.bottom//
            );

        }

        public Rectangle bodyBounds;

        public CandlestickWidget getCandlestickWidget() {
            return CandlestickWidget.this;
        }

        @Override
        public void paintComponent(Graphics g) {

            Point2D[] resizePoints = new Point2D[4];

            Graphics2D g2 = (Graphics2D) g;

            g2.setColor(candlestick.isBlack() ? Color.BLACK : Color.WHITE);

            Rectangle2D bounds = getDrawingArea();

            Stroke strokeBase = g2.getStroke();
            Stroke stroke = new BasicStroke((float) bounds.getWidth() * 0.075f);

            double len = candlestick.getLen();
            double frac = bounds.getHeight() / len;
            double y = bounds.getY();
            
            resizePoints[0] = new Point2D.Double(bounds.getCenterX(), y);
            
            double height = frac * candlestick.getHighShadowLen();
            Line2D highShadow = new Line2D.Double(bounds.getCenterX(), y, bounds.getCenterX(), y + height);

            g2.setStroke(stroke);
            g2.draw(highShadow);

            y += height;
            
            resizePoints[1] = new Point2D.Double(bounds.getCenterX(), y);
            
            height = frac * candlestick.getBodyLen();
            
            resizePoints[2] = new Point2D.Double(bounds.getCenterX(), y + height);

            if (candlestick.getBodyLen() > 0) {

                Rectangle2D rect = new Rectangle2D.Double(//
                        bounds.getX(),//
                        y,//
                        bounds.getWidth(),//
                        height//
                );

                bodyBounds = rect.getBounds();

                g2.setStroke(strokeBase);
                g2.fill(rect);
            } else {

                Line2D body = new Line2D.Double(bounds.getX(), y, bounds.getMaxX(), y);

                g2.setStroke(stroke);
                g2.draw(body);

            }

            y += height;
            
            height = frac * candlestick.getLowShadowLen();

            resizePoints[3] = new Point2D.Double(bounds.getCenterX(), y + height);
            
            Line2D lowShadow = new Line2D.Double(bounds.getCenterX(), y, bounds.getCenterX(), y + height);

            g2.setStroke(stroke);
            g2.draw(lowShadow);

            g2.setStroke(strokeBase);

           if(resizeHandler != null) {
               resizeHandler.handle(g2, resizePoints);
           }

        }

    }

    private CWidget widget;
    private Candle candlestick;
    private CWidgetResizeHandler resizeHandler;
    private CWidgetMoveHandler moveHandler;

    public CandlestickWidget(Candle candlestick) {
        this(candlestick, new Point(10, 10),//
                new CWidgetAction[] {//
                CWidgetAction.createMoveAction(),//
                        CWidgetAction.createResizeAction() //
                }//
        );
    }

    public CandlestickWidget(Candle candlestick, CWidgetAction[] actions) {
        this(candlestick, new Point(10, 10), actions);
    }

    public CandlestickWidget(Candle candlestick, Point location, CWidgetAction[] actions) {
        this.widget = new CWidget(location);
        this.candlestick = candlestick;

        if (actions != null) {

            for (CWidgetAction action : actions) {
                addAction(action);
            }
        }
    }
    
    public CWidgetResizeHandler getResizeHandler() {
        return resizeHandler;
    }

    public void setResizeHandler(CWidgetResizeHandler resizeHandler) {
        this.resizeHandler = resizeHandler;
    }

    public CWidgetMoveHandler getMoveHandler() {
        return moveHandler;
    }

    public void setMoveHandler(CWidgetMoveHandler moveHandler) {
        this.moveHandler = moveHandler;
    }

    public void setLocation(Point point) {
        widget.setLocation(point);
    }

    public Rectangle getBodyBounds() {
        return widget.bodyBounds;
    }

    public Rectangle getCandleBounds() {
        return widget.getDrawingArea().getBounds();
    }
    
    public void addAction(CWidgetAction action) {
        action.register(this);
    }

    public void removeAction(CWidgetAction action) {
        action.unregister(this);
    }

    public JComponent getWidget() {
        return widget;
    }
    
    

    @Override
    public CandlestickWidget clone() {

        CandlestickWidget clone;
        try {
            clone = (CandlestickWidget) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }

        clone.candlestick = candlestick.clone();
        clone.widget = clone.new CWidget(widget.getLocation());

        return clone;
    }

    public Candle getCandlestick() {
        return candlestick;
    }
    
    public static void main(String[] args) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.GRAY);

        int x = 10;
        for (PaletteCandelstickWidgets w : PaletteCandelstickWidgets.values()) {
            CandlestickWidget widget = w.widget();
            widget.addAction(CWidgetAction.createMoveAction());
            widget.addAction(CWidgetAction.createResizeAction());
            widget.setLocation(new Point(x, 10));
            x += 100;
            panel.add(widget.getWidget());
        }

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setContentPane(panel);
        f.setSize(new Dimension(400, 400));
        f.setVisible(true);
    }

    

}
