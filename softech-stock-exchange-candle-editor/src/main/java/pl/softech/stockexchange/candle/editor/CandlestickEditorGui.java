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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pl.softech.stockexchange.candle.editor.dad.DragAndDropSupport;
import pl.softech.stockexchange.candle.editor.meta.MetaDataChangeListener;
import pl.softech.stockexchange.candle.editor.meta.PatternMetaDataEditor;
import pl.softech.stockexchange.candle.editor.xml.XmlCandlestickWidgetLoader;
import pl.softech.stockexchange.candle.editor.xml.XmlDefinitionBuilder;
import pl.softech.stockexchange.candle.pattern.ICandlePatternMetaData;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class CandlestickEditorGui {

    private final JPanel mainPanel;
    private final JFrame frame;
    private final PatternMetaDataEditor pMetaDataEditor;
    private JTabbedPane scenePane;
    private JButton btnNew;
    private JButton btnSave;
    private JButton btnOpen;
    private JMenuItem mFileItemNew;
    private JMenuItem mFileItemOpen;
    private JMenuItem mFileItemSave;

    private final List<CandlestickScene> candlestickScenes;

    public CandlestickEditorGui() {

        pMetaDataEditor = new PatternMetaDataEditor();

        candlestickScenes = new LinkedList<CandlestickScene>();

        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(900, 600));
        mainPanel.setLayout(new BorderLayout(0, 0));

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        mainPanel.add(toolBar, BorderLayout.NORTH);

        btnNew = new JButton("New");
        toolBar.add(btnNew);

        btnSave = new JButton("Save");
        toolBar.add(btnSave);
        
        btnOpen = new JButton("Open");
        toolBar.add(btnOpen);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setResizeWeight(0.5);
        JPanel rightPane = new JPanel();
        rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.PAGE_AXIS));
        JPanel palette = new JPanel();
        rightPane.add(palette);
        rightPane.add(pMetaDataEditor.getPanel());
        splitPane.setRightComponent(rightPane);

        initPalette(palette);

        mainPanel.add(splitPane);

        scenePane = new JTabbedPane(JTabbedPane.TOP);
        splitPane.setLeftComponent(scenePane);

        CandlestickScene scene = new CandlestickScene();
        registerScene(scene);
        candlestickScenes.add(scene);
        scenePane.addTab("New 1", null, scene.getScene(), null);
        scenePane.setTabComponentAt(0, new ButtonTabComponent(scenePane, candlestickScenes));

        splitPane.setDividerLocation(650);

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu menuFile = new JMenu("File");
        menuBar.add(menuFile);

        mFileItemNew = new JMenuItem("New");
        menuFile.add(mFileItemNew);

        mFileItemOpen = new JMenuItem("Open");
        menuFile.add(mFileItemOpen);

        mFileItemSave = new JMenuItem("Save");
        menuFile.add(mFileItemSave);
        frame.setContentPane(getMainPanel());
        frame.pack();

        initListeners();
    }

    private void initPalette(JPanel palette) {
        palette.setLayout(new FlowLayout());

        for (PaletteCandelstickWidgets w : PaletteCandelstickWidgets.values()) {
            CandlestickWidget widget = w.widget();
            DragAndDropSupport.addSupport(widget);
            palette.add(widget.getWidget());

        }

    }

    private CandlestickScene getSelectedScene() {
        int index = scenePane.getSelectedIndex();
        if (index < 0) {
            return null;
        }
        return candlestickScenes.get(index);
    }

    private void registerScene(final CandlestickScene scene) {
        pMetaDataEditor.register(new MetaDataChangeListener() {

            @Override
            public void changePerformed(ICandlePatternMetaData meta) {
                scene.setMetaData(meta);

            }
        }, scene.getMetaData());
    }

    private CandlestickScene createNew(String name, int atPosition) {
        CandlestickScene scene = new CandlestickScene();
        candlestickScenes.add(scene);
        scenePane.addTab(name, null, scene.getScene(), null);
        scenePane.setTabComponentAt(atPosition, new ButtonTabComponent(scenePane, candlestickScenes));
        scenePane.setSelectedIndex(atPosition);
        return scene;
    }
    
    private void initListeners() {

        scenePane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {

                CandlestickScene scene = getSelectedScene();

                if (scene != null) {
                    registerScene(scene);
                }
            }
        });

        final ActionListener newSceneAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                int index = scenePane.getComponentCount();
                createNew("New " + index, index - 1);
            }
        };

        mFileItemNew.addActionListener(newSceneAction);
        btnNew.addActionListener(newSceneAction);

        ActionListener saveSceneAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                CandlestickScene scene = getSelectedScene();

                if (scene == null) {
                    return;
                }

                XmlDefinitionBuilder builder = new XmlDefinitionBuilder(scene.getWidgets(), scene.getMetaData());
                try {

                    System.out.println(builder.build().getXmlContent());

                    final JFileChooser fc = new JFileChooser();
                    int returnValue = fc.showSaveDialog(frame);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        builder.build().save(file);
                    }

                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error during saving", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        };

        btnSave.addActionListener(saveSceneAction);
        mFileItemSave.addActionListener(saveSceneAction);

        ActionListener openSceneAction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    final JFileChooser fc = new JFileChooser();
                    int returnValue = fc.showOpenDialog(frame);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        XmlCandlestickWidgetLoader loader = new XmlCandlestickWidgetLoader();
                        loader.load(file);
                        int index = scenePane.getComponentCount();
                        CandlestickScene scene = createNew(file.getName(), index - 1);
                        scene.setMetaData(loader.getMetaData());
                        registerScene(scene);
                        for (CandlestickWidget cw : loader.getCandlestickWidgets()) {
                            scene.addWidget(cw);
                        }

                    }

                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error during opening file", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            }

        };
        btnOpen.addActionListener(openSceneAction);
        mFileItemOpen.addActionListener(openSceneAction);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JFrame getFrame() {
        return frame;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new CandlestickEditorGui().getFrame().setVisible(true);
            }
        });

    }

}
