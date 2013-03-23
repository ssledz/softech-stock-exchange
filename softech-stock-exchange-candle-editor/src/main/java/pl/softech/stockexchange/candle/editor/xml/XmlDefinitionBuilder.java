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
import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pl.softech.stockexchange.candle.Candle;
import pl.softech.stockexchange.candle.editor.CandlestickWidget;
import pl.softech.stockexchange.candle.editor.PaletteCandelstickWidgets;
import pl.softech.stockexchange.candle.pattern.ICandlePatternMetaData;
import pl.softech.stockexchange.candle.pattern.ICandlePatternMetaData.Direction;
import pl.softech.stockexchange.candle.pattern.ICandlePatternMetaData.Reliability;
import pl.softech.stockexchange.candle.pattern.ICandlePatternMetaData.Type;
import pl.softech.stockexchange.candle.pattern.impl2.XmlCandlePattern;
import pl.softech.stockexchange.candle.xml.BoundsAttributes;
import pl.softech.stockexchange.model.FInstrument;
import pl.softech.stockexchange.model.PropertyName;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class XmlDefinitionBuilder {

    
    private final CandlestickWidget[] candlestickWidgets;
    private final ICandlePatternMetaData metaData;

    private String xmlContent;

    public XmlDefinitionBuilder(Collection<CandlestickWidget> candlestickWidgets,
            ICandlePatternMetaData metaData) {
        
        this.candlestickWidgets = candlestickWidgets.toArray(new CandlestickWidget[candlestickWidgets.size()]);
        Arrays.sort(this.candlestickWidgets, new Comparator<CandlestickWidget>() {

            @Override
            public int compare(CandlestickWidget o1, CandlestickWidget o2) {
                return (int)(o1.getWidget().getBounds().getX() - o2.getWidget().getBounds().getX());
            }
        });
        
        this.metaData = metaData;
    }

    public XmlDefinitionBuilder build() throws Exception {

        buildXml();

        return this;

    }

    private String documentTOString(Document doc) throws Exception {
        DOMSource domSource = new DOMSource(doc);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter sw = new StringWriter();
        StreamResult sr = new StreamResult(sw);
        transformer.transform(domSource, sr);
        String xml = sw.toString();
        return xml;

    }

    private void addRootAtributes(Element root) throws Exception {

        if(metaData == null) {
            return;
        }
        
        for (Method m : ICandlePatternMetaData.class.getMethods()) {

            if (m.getParameterTypes().length > 0) {
                continue;
            }

            PropertyName meta = m.getAnnotation(PropertyName.class);
            if (meta == null) {
                continue;
            }
            root.setAttribute(meta.name(), "" + m.invoke(metaData, new Object[0]));

        }
    }

    private void addAtributes(Element element, Candle candlestick) throws Exception {

        for (Method m : FInstrument.class.getMethods()) {

            if (m.getParameterTypes().length > 0) {
                continue;
            }

            PropertyName meta = m.getAnnotation(PropertyName.class);

            if (meta == null) {
                continue;
            }

            element.setAttribute(meta.name(), "" + m.invoke(candlestick.instrument(), new Object[0]));

        }
    }

    private void addBoundsAtribute(Element element, Rectangle bounds) throws Exception {
        BoundsAttributes ba = new BoundsAttributes(bounds);
        
        for (Method m : BoundsAttributes.class.getMethods()) {

            if (m.getParameterTypes().length > 0) {
                continue;
            }

            PropertyName meta = m.getAnnotation(PropertyName.class);

            if (meta == null) {
                continue;
            }

            element.setAttribute(meta.name(), "" + m.invoke(ba, new Object[0]));

        }
        
    }
    
    private void buildXml() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation impl = builder.getDOMImplementation();
        Document doc = impl.createDocument(null, null, null);

        Element root = doc.createElement(XmlCandlePattern.ROOT_ELEMENT_NAME);
        addRootAtributes(root);
        doc.appendChild(root);

        for (CandlestickWidget cw : candlestickWidgets) {

            Element element = doc.createElement(XmlCandlePattern.CANDLESTICK_ELEMENT_NAME);
            addAtributes(element, cw.getCandlestick());
            addBoundsAtribute(element, cw.getWidget().getBounds());
            root.appendChild(element);
        }

        xmlContent = documentTOString(doc);
    }

    public String getXmlContent() {
        return xmlContent;
    }

    public void save(File file) throws Exception {
        BufferedWriter fout = null;
        try {
            fout = new BufferedWriter(new FileWriter(file));
            fout.write(xmlContent);
        } finally {

            if (fout != null) {
                fout.close();
            }
        }
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

    }
}
