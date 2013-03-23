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
package pl.softech.stockexchange.candle.editor.xml;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import pl.softech.stockexchange.candle.Candle;
import pl.softech.stockexchange.candle.editor.CandlestickWidget;
import pl.softech.stockexchange.candle.editor.PaletteCandelstickWidgets;
import pl.softech.stockexchange.candle.pattern.ICandlePatternMetaData;
import pl.softech.stockexchange.candle.pattern.ICandlePatternMetaData.Direction;
import pl.softech.stockexchange.candle.pattern.ICandlePatternMetaData.Reliability;
import pl.softech.stockexchange.candle.pattern.ICandlePatternMetaData.Type;
import pl.softech.stockexchange.candle.xml.BoundsAttributes;
import pl.softech.stockexchange.candle.xml.XmlDefinitionLoader;
import pl.softech.stockexchange.model.PropertyName;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class XmlCandlestickWidgetLoader  extends XmlDefinitionLoader {

    private List<CandlestickWidget> candlestickWidgets = new LinkedList<CandlestickWidget>();
    private ICandlePatternMetaData metaData;

    public XmlCandlestickWidgetLoader load(File file) throws Exception {
        super.load(file);
        return this;

    }

    public CandlestickWidget[] getCandlestickWidgets() {
        return candlestickWidgets.toArray(new CandlestickWidget[0]);
    }

    @Override
    protected Candle readCandlestick(Node node) throws Exception {
        Candle candle = super.readCandlestick(node);
        
        CandlestickWidget cw = new CandlestickWidget(candle);
        
        loadBoundsAttributes(node.getAttributes(), cw);
        
        candlestickWidgets.add(cw);
        
        return candle;
    }

    
    
    private void loadBoundsAttributes(NamedNodeMap nnm, CandlestickWidget cw) throws Exception {
        
        BoundsAttributes bounds = new BoundsAttributes();
        
        for (Method m : BoundsAttributes.class.getMethods()) {

            if (m.getParameterTypes().length > 0) {
                continue;
            }

            PropertyName meta = m.getAnnotation(PropertyName.class);

            if (meta == null) {
                continue;
            }

            String att = nnm.getNamedItem(meta.name()).getNodeValue();

            if (m.getReturnType() == int.class) {
                int value = Integer.parseInt(att);
                Method setter = getSetterForGetter(BoundsAttributes.class, m);
                setter.invoke(bounds, value);
            }

        }
        
        cw.getWidget().setBounds(bounds.getBounds());
        
    }
    
    public static void main(String[] args) throws Exception {
        TreeSet<CandlestickWidget> candlestickWidgets = new TreeSet<CandlestickWidget>(
                new Comparator<CandlestickWidget>() {

                    @Override
                    public int compare(CandlestickWidget o1, CandlestickWidget o2) {

                        return (int) (o1.getWidget().getBounds().getX() - o2.getWidget().getBounds().getX());
                    }
                });

        int x = 0;
        for (PaletteCandelstickWidgets w : PaletteCandelstickWidgets.values()) {
            CandlestickWidget widget = w.widget();
            widget.setLocation(new Point(x++, 10));
            candlestickWidgets.add(widget);
        }

        ICandlePatternMetaData meta = new ICandlePatternMetaData() {

            @Override
            public Type getType() {
                return Type.REVERSAL;
            }

            @Override
            public int getBarCount() {
                return 3;
            }

            @Override
            public Direction getDirection() {
                return Direction.BULLISH;
            }

            @Override
            public Reliability getReliability() {
                return Reliability.STRONG;
            }

        };

        XmlDefinitionBuilder builder = new XmlDefinitionBuilder(candlestickWidgets, meta);

        System.out.println(builder.build().getXmlContent());

        ByteArrayInputStream in = new ByteArrayInputStream(builder.getXmlContent().getBytes());

        XmlCandlestickWidgetLoader loader = new XmlCandlestickWidgetLoader();
        loader.loadXml(in);

        builder = new XmlDefinitionBuilder(Arrays.asList(loader.getCandlestickWidgets()), loader.getMetaData());
        System.out.println(builder.build().getXmlContent());
    }

}
