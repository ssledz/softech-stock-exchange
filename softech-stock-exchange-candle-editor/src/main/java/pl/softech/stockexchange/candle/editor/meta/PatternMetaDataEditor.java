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
package pl.softech.stockexchange.candle.editor.meta;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import pl.softech.stockexchange.candle.pattern.ICandlePatternMetaData;
import pl.softech.stockexchange.candle.pattern.ICandlePatternMetaData.Direction;
import pl.softech.stockexchange.candle.pattern.ICandlePatternMetaData.Reliability;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class PatternMetaDataEditor {

    private final JPanel panel;
    private JComboBox typeComboBox;
    private JComboBox reliabilityComboBox;
    private JComboBox directionComboBox;
    
    private MetaDataChangeListener listener;

    public PatternMetaDataEditor() {
        panel = new JPanel();
        
        JLabel labelType = new JLabel("Type");
        labelType.setFont(new Font("Dialog", Font.BOLD, 12));
        
        reliabilityComboBox = new JComboBox(ICandlePatternMetaData.Reliability.values());
        
        JLabel labelDirection = new JLabel("Direction");
        
        JLabel labelReliability = new JLabel("Reliability");
        
        directionComboBox = new JComboBox(ICandlePatternMetaData.Direction.values());
        
        typeComboBox = new JComboBox(ICandlePatternMetaData.Type.values());
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
            gl_panel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                        .addComponent(labelType)
                        .addComponent(labelDirection)
                        .addComponent(labelReliability))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                        .addComponent(typeComboBox, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
                        .addComponent(directionComboBox, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
                        .addComponent(reliabilityComboBox, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(65, Short.MAX_VALUE))
        );
        gl_panel.setVerticalGroup(
            gl_panel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(labelType)
                        .addComponent(typeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(labelDirection)
                        .addComponent(directionComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(14)
                    .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(labelReliability)
                        .addComponent(reliabilityComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(22, Short.MAX_VALUE))
        );
        panel.setLayout(gl_panel);
        
        initListeners();
        
    }

    private void initListeners() {
        ActionListener l = new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                fireMetaDataChanged();
            }
        };
        
        typeComboBox.addActionListener(l);
        reliabilityComboBox.addActionListener(l);
        directionComboBox.addActionListener(l);
    } 
    
    private ICandlePatternMetaData create() {
        
        return new ICandlePatternMetaData() {

            @Override
            public Type getType() {
                return (Type) typeComboBox.getSelectedItem();
            }

            @Override
            public int getBarCount() {
                return 0;
            }

            @Override
            public Direction getDirection() {
                return (Direction) directionComboBox.getSelectedItem();
            }

            @Override
            public Reliability getReliability() {
                return (Reliability) reliabilityComboBox.getSelectedItem();
            }
            
        };
        
    }
    
    public JPanel getPanel() {
        return panel;
    }
    
    public void register(MetaDataChangeListener l, ICandlePatternMetaData metaData) {
        this.listener = null;
        this.typeComboBox.setSelectedItem(metaData.getType());
        this.directionComboBox.setSelectedItem(metaData.getDirection());
        this.reliabilityComboBox.setSelectedItem(metaData.getReliability());
        this.listener = l;
        panel.repaint();
    }
    
    private void fireMetaDataChanged() {
        if(listener == null) {
            return;
        }
        listener.changePerformed(create());
    }
    
}
