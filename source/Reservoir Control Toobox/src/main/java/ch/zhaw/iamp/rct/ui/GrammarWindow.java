/*
 * Copyright © 2014 René Bernhardsgrütter, Christoph Walter Senn at Zurich
 * University of Applied Sciences; 2014 Helmut Hauser at University of Zurich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the “Software”), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ch.zhaw.iamp.rct.ui;

import ch.zhaw.iamp.rct.App;
import ch.zhaw.iamp.rct.Controller;
import ch.zhaw.iamp.rct.grammar.GrammarParser;
import ch.zhaw.iamp.rct.grammar.GraphDeveloper;
import ch.zhaw.iamp.rct.grammar.ParserError;
import ch.zhaw.iamp.rct.grammar.InitialisationParser;
import ch.zhaw.iamp.rct.util.Storage;
import ch.zhaw.iamp.rct.util.Components;
import ch.zhaw.iamp.rct.util.Dialogs;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.apache.commons.io.FileUtils;

public class GrammarWindow extends javax.swing.JFrame {

    private enum StorageKey {

        WINDOW_STATE,
        WINDOW_HEIGHT,
        WINDOW_WIDTH,
        WINDOW_POSITION_X,
        WINDOW_POSITION_Y,
        GRAMMAR_FILE_PATH,
        INITIALISATION_FILE_PATH,
        EDITOR_FONT_SIZE,
        IS_GRAMMAR_HELP_VISIBLE,
        GRAMMAR_HELP_POSITION_X,
        GRAMMAR_HELP_POSITION_Y,
        IS_INITIALISATION_HELP_VISIBLE,
        INITIALISATION_HELP_POSITION_X,
        INITIALISATION_HELP_POSITION_Y
    }
    private static final long serialVersionUID = 1L;
    public final static String STORAGE_FILENAME = "grammarWindow.obj";
    final static String DEFAULT_COORDINATES = "-/-";
    private Controller controller;
    private NetworkGraphDisplay graphPanel;
    HashMap<StorageKey, String> storageValues;
    Storage storage;
    private GrammarParser grammarParser;
    private InitialisationParser initialisationParser;
    private GraphDeveloper graphDeveloper;

    public GrammarWindow() {
        initComponents();
        initEmptyGraphAndParsers();
        createStorage();
        restoreValues();
        refeshHighligthing();
        hideSliderValue();
    }

    private void initEmptyGraphAndParsers() {
        grammarParser = new GrammarParser();
        initialisationParser = new InitialisationParser(grammarParser);
        graphPanel = new NetworkGraphDisplay();
        graphPanel.setGrammarWindow(this);
        graphPanel.setNetworkGraph(grammarParser.getGraph());
        graphPanelWrapper.add(graphPanel);
    }

    @SuppressWarnings({"unchecked"})
    private void createStorage() {
        storage = new Storage(App.getConfigDirectoryPath() + STORAGE_FILENAME);
        storageValues = new HashMap<>();

        File storageFile = new File(App.getConfigDirectoryPath() + STORAGE_FILENAME);

        if (storageFile.exists()) {
            storageValues = (HashMap<StorageKey, String>) storage.restore();
        } else {
            storage.store(storageValues);
        }
    }

    private void restoreValues() {
        for (StorageKey key : StorageKey.values()) {
            if (!storageValues.containsKey(key)) {
                continue;
            }

            switch (key) {
                case WINDOW_STATE:
                    setState(Integer.valueOf(storageValues.get(key)));
                    break;
                case WINDOW_HEIGHT:
                    String height = storageValues.get(StorageKey.WINDOW_HEIGHT);
                    String width = storageValues.get(StorageKey.WINDOW_WIDTH);

                    if (height != null && width != null) {
                        setPreferredSize(new Dimension(Integer.parseInt(width), Integer.parseInt(height)));
                    }
                    break;
                case WINDOW_POSITION_X:
                    String xPos = storageValues.get(StorageKey.WINDOW_POSITION_X);
                    String yPos = storageValues.get(StorageKey.WINDOW_POSITION_Y);

                    if (xPos != null && yPos != null) {
                        setLocation(Integer.parseInt(xPos), Integer.parseInt(yPos));
                    }
                    break;
                case GRAMMAR_FILE_PATH:
                    grammarFileTextField.setText(storageValues.get(key));
                    Components.moveCurserToEndOfTextField(grammarFileTextField);
                    grammarSaveButton.setEnabled(true);

                    if (grammarFileTextField.getText().length() > 0) {
                        grammarReloadButton.setEnabled(true);
                        grammarReloadButton.doClick();
                    }
                    break;
                case INITIALISATION_FILE_PATH:
                    initialisationFileTextField.setText(storageValues.get(key));
                    Components.moveCurserToEndOfTextField(initialisationFileTextField);
                    initialisationSaveButton.setEnabled(true);

                    if (initialisationFileTextField.getText().length() > 0) {
                        initialisationReloadButton.setEnabled(true);
                        initialisationReloadButton.doClick();
                    }
                    break;
                case EDITOR_FONT_SIZE:
                    setEditorFontSizes(Integer.valueOf(storageValues.get(key)));
                    break;
            }
        }
    }

    private void refeshHighligthing() {
        grammarTextPaneKeyReleased(null);
        initialisationTextPaneKeyReleased(null);
    }

    /**
     * This has to be done this way because there is a bug in the SliderUI. See:
     * http://stackoverflow.com/questions/4460840/remove-value-displaying-over-thumb-in-jslider
     */
    private void hideSliderValue() {
        try {
            Class<?> sliderUIClass = Class.forName("javax.swing.plaf.synth.SynthSliderUI");
            final Field paintValue = sliderUIClass.getDeclaredField("paintValue");
            paintValue.setAccessible(true);
            paintValue.set(zoomSlider.getUI(), false);
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException ex) {
            System.out.println("Could not hide the slide value: " + ex.getMessage());
        }
    }

    public void setCoordinatesLabel(double xPosition, double yPosition) {
        double roudedX = Math.round(xPosition*100d)/100d;
        double roudedY = Math.round(yPosition*100d)/100d;
        coordinates.setText(roudedX + "/" + roudedY);
    }

    public void resetCoordinatesLabel() {
        coordinates.setText(DEFAULT_COORDINATES);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public GrammarParser getGrammarParser() {
        return grammarParser;
    }

    public String getDirectoryOfInitalisationFile() {
        String filePath = initialisationFileTextField.getText();

        if (filePath.isEmpty()) {
            return "";
        }

        File file = new File(filePath);

        if (file.isFile()) {
            return file.getParent();
        }

        if (file.isDirectory()) {
            return file.getPath();
        }

        return "";
    }

    public double getZoomLevel() {
        return zoomSlider.getValue() / 100d;
    }

    public void restoreHelpWindows() {
        boolean isGrammarHelpVisible = "true".equals(storageValues.get(StorageKey.IS_GRAMMAR_HELP_VISIBLE));
        boolean isInitialisationHelpVisible = "true".equals(storageValues.get(StorageKey.IS_INITIALISATION_HELP_VISIBLE));

        if (isGrammarHelpVisible) {
            grammarHelpButton.doClick();
        }

        if (isInitialisationHelpVisible) {
            initialisationHelpButton.doClick();
        }
    }
    
    public void repaintGraphPanel() {
        graphPanel.setNetworkGraph(grammarParser.getGraph());
        graphPanel.repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grammarSplitPane = new javax.swing.JSplitPane();
        editorWrapperPanel = new javax.swing.JPanel();
        editorWrapperSplitPane = new javax.swing.JSplitPane();
        grammarEditorPanel = new javax.swing.JPanel();
        grammarTextPaneSplitPane = new javax.swing.JScrollPane();
        grammarTextPane = new javax.swing.JTextPane();
        grammarFileTextField = new javax.swing.JTextField();
        grammarFileSelectButton = new javax.swing.JButton();
        grammarSaveButton = new javax.swing.JButton();
        grammarReloadButton = new javax.swing.JButton();
        increaseGrammarFontSizeButton = new javax.swing.JButton();
        decreaseGrammarFontSizeButton = new javax.swing.JButton();
        grammarHelpButton = new javax.swing.JButton();
        initialisationEditorPanel = new javax.swing.JPanel();
        initialisationFileTextField = new javax.swing.JTextField();
        initialisationFileSelectButton = new javax.swing.JButton();
        initialisationEditorSplitPane = new javax.swing.JSplitPane();
        initialisationTextPaneScrollPane = new javax.swing.JScrollPane();
        initialisationTextPane = new javax.swing.JTextPane();
        initialisationOutputScrollPane = new javax.swing.JScrollPane();
        initialisationOutputTextArea = new javax.swing.JTextArea();
        increaseInitialisationFontSizeButton = new javax.swing.JButton();
        decreaseInitialisationFontSizeButton = new javax.swing.JButton();
        initialisationSaveButton = new javax.swing.JButton();
        initialisationReloadButton = new javax.swing.JButton();
        initialisationHelpButton = new javax.swing.JButton();
        visualisationPanel = new javax.swing.JPanel();
        graphPanelWrapper = new javax.swing.JPanel();
        coordinatesLabel = new javax.swing.JLabel();
        coordinates = new javax.swing.JLabel();
        generateButton = new javax.swing.JButton();
        zoomSlider = new javax.swing.JSlider();
        zoomValue = new javax.swing.JLabel();
        loadGraphButton = new javax.swing.JButton();
        saveAndUseButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Graph Generator");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(800, 500));

        grammarSplitPane.setDividerLocation(450);
        grammarSplitPane.setContinuousLayout(true);
        grammarSplitPane.setMinimumSize(new java.awt.Dimension(600, 1));

        editorWrapperSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        editorWrapperSplitPane.setResizeWeight(0.8);
        editorWrapperSplitPane.setContinuousLayout(true);

        grammarEditorPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Grammar"));

        grammarTextPane.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        grammarTextPane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                grammarTextPaneKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                grammarTextPaneKeyTyped(evt);
            }
        });
        grammarTextPaneSplitPane.setViewportView(grammarTextPane);

        grammarFileTextField.setEditable(false);

        grammarFileSelectButton.setText("Select...");
        grammarFileSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grammarFileSelectButtonActionPerformed(evt);
            }
        });

        grammarSaveButton.setText("Save");
        grammarSaveButton.setEnabled(false);
        grammarSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grammarSaveButtonActionPerformed(evt);
            }
        });

        grammarReloadButton.setText("Reload");
        grammarReloadButton.setEnabled(false);
        grammarReloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grammarReloadButtonActionPerformed(evt);
            }
        });

        increaseGrammarFontSizeButton.setText("<html><small>A</small>A</html>");
        increaseGrammarFontSizeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                increaseGrammarFontSizeButtonActionPerformed(evt);
            }
        });

        decreaseGrammarFontSizeButton.setText("<html>A<small>A</small></html>");
        decreaseGrammarFontSizeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                decreaseGrammarFontSizeButtonActionPerformed(evt);
            }
        });

        grammarHelpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/zhaw/iamp/rct/ui/help.png"))); // NOI18N
        grammarHelpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grammarHelpButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout grammarEditorPanelLayout = new javax.swing.GroupLayout(grammarEditorPanel);
        grammarEditorPanel.setLayout(grammarEditorPanelLayout);
        grammarEditorPanelLayout.setHorizontalGroup(
            grammarEditorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grammarEditorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(grammarEditorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(grammarTextPaneSplitPane)
                    .addGroup(grammarEditorPanelLayout.createSequentialGroup()
                        .addComponent(grammarFileTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(grammarFileSelectButton))
                    .addGroup(grammarEditorPanelLayout.createSequentialGroup()
                        .addComponent(increaseGrammarFontSizeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(decreaseGrammarFontSizeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(grammarHelpButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 240, Short.MAX_VALUE)
                        .addComponent(grammarSaveButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(grammarReloadButton)))
                .addContainerGap())
        );
        grammarEditorPanelLayout.setVerticalGroup(
            grammarEditorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grammarEditorPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(grammarEditorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(grammarFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(grammarFileSelectButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(grammarTextPaneSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(grammarEditorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(increaseGrammarFontSizeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(decreaseGrammarFontSizeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(grammarReloadButton)
                    .addComponent(grammarSaveButton)
                    .addComponent(grammarHelpButton))
                .addGap(6, 6, 6))
        );

        editorWrapperSplitPane.setLeftComponent(grammarEditorPanel);

        initialisationEditorPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Initialisation"));

        initialisationFileTextField.setEditable(false);

        initialisationFileSelectButton.setText("Select...");
        initialisationFileSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initialisationFileSelectButtonActionPerformed(evt);
            }
        });

        initialisationEditorSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        initialisationEditorSplitPane.setResizeWeight(0.3);
        initialisationEditorSplitPane.setContinuousLayout(true);

        initialisationTextPane.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        initialisationTextPane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                initialisationTextPaneKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                initialisationTextPaneKeyTyped(evt);
            }
        });
        initialisationTextPaneScrollPane.setViewportView(initialisationTextPane);

        initialisationEditorSplitPane.setTopComponent(initialisationTextPaneScrollPane);

        initialisationOutputTextArea.setEditable(false);
        initialisationOutputTextArea.setColumns(20);
        initialisationOutputTextArea.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        initialisationOutputTextArea.setLineWrap(true);
        initialisationOutputScrollPane.setViewportView(initialisationOutputTextArea);

        initialisationEditorSplitPane.setRightComponent(initialisationOutputScrollPane);

        increaseInitialisationFontSizeButton.setText("<html><small>A</small>A</html>");
        increaseInitialisationFontSizeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                increaseInitialisationFontSizeButtonActionPerformed(evt);
            }
        });

        decreaseInitialisationFontSizeButton.setText("<html>A<small>A</small></html>");
        decreaseInitialisationFontSizeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                decreaseInitialisationFontSizeButtonActionPerformed(evt);
            }
        });

        initialisationSaveButton.setText("Save");
        initialisationSaveButton.setEnabled(false);
        initialisationSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initialisationSaveButtonActionPerformed(evt);
            }
        });

        initialisationReloadButton.setText("Reload");
        initialisationReloadButton.setEnabled(false);
        initialisationReloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initialisationReloadButtonActionPerformed(evt);
            }
        });

        initialisationHelpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/zhaw/iamp/rct/ui/help.png"))); // NOI18N
        initialisationHelpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initialisationHelpButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout initialisationEditorPanelLayout = new javax.swing.GroupLayout(initialisationEditorPanel);
        initialisationEditorPanel.setLayout(initialisationEditorPanelLayout);
        initialisationEditorPanelLayout.setHorizontalGroup(
            initialisationEditorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(initialisationEditorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(initialisationEditorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(initialisationEditorPanelLayout.createSequentialGroup()
                        .addComponent(initialisationFileTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(initialisationFileSelectButton))
                    .addComponent(initialisationEditorSplitPane)
                    .addGroup(initialisationEditorPanelLayout.createSequentialGroup()
                        .addComponent(increaseInitialisationFontSizeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(decreaseInitialisationFontSizeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(initialisationHelpButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 240, Short.MAX_VALUE)
                        .addComponent(initialisationSaveButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(initialisationReloadButton)))
                .addContainerGap())
        );
        initialisationEditorPanelLayout.setVerticalGroup(
            initialisationEditorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(initialisationEditorPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(initialisationEditorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(initialisationFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(initialisationFileSelectButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(initialisationEditorSplitPane)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(initialisationEditorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(increaseInitialisationFontSizeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(decreaseInitialisationFontSizeButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(initialisationSaveButton)
                    .addComponent(initialisationReloadButton)
                    .addComponent(initialisationHelpButton))
                .addGap(6, 6, 6))
        );

        editorWrapperSplitPane.setBottomComponent(initialisationEditorPanel);

        javax.swing.GroupLayout editorWrapperPanelLayout = new javax.swing.GroupLayout(editorWrapperPanel);
        editorWrapperPanel.setLayout(editorWrapperPanelLayout);
        editorWrapperPanelLayout.setHorizontalGroup(
            editorWrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(editorWrapperSplitPane, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        editorWrapperPanelLayout.setVerticalGroup(
            editorWrapperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(editorWrapperSplitPane)
        );

        grammarSplitPane.setLeftComponent(editorWrapperPanel);

        visualisationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Visualisation"));
        visualisationPanel.setMinimumSize(new java.awt.Dimension(100, 0));
        visualisationPanel.setPreferredSize(new java.awt.Dimension(100, 47));

        graphPanelWrapper.setPreferredSize(new java.awt.Dimension(100, 0));
        graphPanelWrapper.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                graphPanelWrapperMouseWheelMoved(evt);
            }
        });
        graphPanelWrapper.setLayout(new javax.swing.BoxLayout(graphPanelWrapper, javax.swing.BoxLayout.LINE_AXIS));

        coordinatesLabel.setText("Coordinates (x/y):");

        coordinates.setText("-/-");

        generateButton.setText("Generate");
        generateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButtonActionPerformed(evt);
            }
        });

        zoomSlider.setMaximum(300);
        zoomSlider.setMinimum(50);
        zoomSlider.setValue(100);
        zoomSlider.setMaximumSize(new java.awt.Dimension(32767, 20));
        zoomSlider.setMinimumSize(new java.awt.Dimension(38, 20));
        zoomSlider.setPreferredSize(new java.awt.Dimension(204, 22));
        zoomSlider.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                zoomSliderMouseWheelMoved(evt);
            }
        });
        zoomSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                zoomSliderStateChanged(evt);
            }
        });

        zoomValue.setText("100");

        javax.swing.GroupLayout visualisationPanelLayout = new javax.swing.GroupLayout(visualisationPanel);
        visualisationPanel.setLayout(visualisationPanelLayout);
        visualisationPanelLayout.setHorizontalGroup(
            visualisationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(visualisationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(visualisationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(visualisationPanelLayout.createSequentialGroup()
                        .addComponent(graphPanelWrapper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(visualisationPanelLayout.createSequentialGroup()
                        .addComponent(coordinatesLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(coordinates)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(generateButton)
                        .addGap(18, 18, 18)
                        .addComponent(zoomValue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(zoomSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        visualisationPanelLayout.setVerticalGroup(
            visualisationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(visualisationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(graphPanelWrapper, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                .addGap(8, 8, 8)
                .addGroup(visualisationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(visualisationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(coordinatesLabel)
                        .addComponent(coordinates)
                        .addComponent(zoomValue)
                        .addComponent(generateButton))
                    .addComponent(zoomSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        grammarSplitPane.setRightComponent(visualisationPanel);

        loadGraphButton.setText("Load Graph...");
        loadGraphButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadGraphButtonActionPerformed(evt);
            }
        });

        saveAndUseButton.setText("Save and Use");
        saveAndUseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAndUseButtonActionPerformed(evt);
            }
        });

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(loadGraphButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveAndUseButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(closeButton)
                .addContainerGap())
            .addComponent(grammarSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 968, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(grammarSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(closeButton)
                    .addComponent(saveAndUseButton)
                    .addComponent(loadGraphButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void grammarFileSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grammarFileSelectButtonActionPerformed
        File selection = Dialogs.selectFileFromDialog(this, grammarFileTextField.getText(), true);

        if (selection != null) {
            grammarFileTextField.setText(selection.getAbsolutePath());
            boolean readable = readFileOrShowError(selection.getAbsolutePath(), grammarTextPane);
            grammarSaveButton.setEnabled(true);
            grammarReloadButton.setEnabled(readable);
        }
    }//GEN-LAST:event_grammarFileSelectButtonActionPerformed

    private void initialisationFileSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_initialisationFileSelectButtonActionPerformed
        File selection = Dialogs.selectFileFromDialog(this, initialisationFileTextField.getText(), true);

        if (selection != null) {
            initialisationFileTextField.setText(selection.getAbsolutePath());
            boolean readable = readFileOrShowError(selection.getAbsolutePath(), initialisationTextPane);
            initialisationSaveButton.setEnabled(true);
            initialisationReloadButton.setEnabled(readable);
        }
    }//GEN-LAST:event_initialisationFileSelectButtonActionPerformed

    private void initialisationReloadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_initialisationReloadButtonActionPerformed
        boolean readable = readFileOrShowError(initialisationFileTextField.getText(), initialisationTextPane);
        initialisationReloadButton.setEnabled(readable);
    }//GEN-LAST:event_initialisationReloadButtonActionPerformed

    private void grammarSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grammarSaveButtonActionPerformed
        boolean writable = writeFileOrShowError(grammarFileTextField.getText(), grammarTextPane);
        grammarReloadButton.setEnabled(writable);
    }//GEN-LAST:event_grammarSaveButtonActionPerformed

    private void grammarReloadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grammarReloadButtonActionPerformed
        boolean readable = readFileOrShowError(grammarFileTextField.getText(), grammarTextPane);
        grammarReloadButton.setEnabled(readable);
    }//GEN-LAST:event_grammarReloadButtonActionPerformed

    private void initialisationSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_initialisationSaveButtonActionPerformed
        boolean writable = writeFileOrShowError(initialisationFileTextField.getText(), initialisationTextPane);
        initialisationReloadButton.setEnabled(writable);
    }//GEN-LAST:event_initialisationSaveButtonActionPerformed

    private boolean readFileOrShowError(String file, JTextComponent target) {
        File source = new File(file);
        if (source.exists()) {
            try {
                target.setText(FileUtils.readFileToString(source));
                target.setEnabled(true);
                target.requestFocus();
                return true;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "The file could not be read: " + ex.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                target.setEnabled(false);
            }
        } else {
            target.setText("");
            target.setEnabled(true);
            target.requestFocus();
        }

        return false;
    }

    private boolean writeFileOrShowError(String file, JTextComponent source) {
        try {
            FileUtils.writeStringToFile(new File(file), source.getText());
            source.requestFocus();
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "The file could not be written: " + ex.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }

    private void increaseGrammarFontSizeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_increaseGrammarFontSizeButtonActionPerformed
        increaseEditorFontSizes();
    }//GEN-LAST:event_increaseGrammarFontSizeButtonActionPerformed

    private void decreaseGrammarFontSizeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_decreaseGrammarFontSizeButtonActionPerformed
        decreaseFontSizes();
    }//GEN-LAST:event_decreaseGrammarFontSizeButtonActionPerformed

    private void increaseInitialisationFontSizeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_increaseInitialisationFontSizeButtonActionPerformed
        increaseEditorFontSizes();
    }//GEN-LAST:event_increaseInitialisationFontSizeButtonActionPerformed

    private void decreaseInitialisationFontSizeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_decreaseInitialisationFontSizeButtonActionPerformed
        decreaseFontSizes();
    }//GEN-LAST:event_decreaseInitialisationFontSizeButtonActionPerformed

    private void saveAndUseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAndUseButtonActionPerformed
        controller.useGrammarWindowGraph();
        dispose();
    }//GEN-LAST:event_saveAndUseButtonActionPerformed

    private void grammarTextPaneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_grammarTextPaneKeyReleased
        if (evt != null && isArrowKeyCode(evt.getKeyCode())) {
            return;
        }

        try {
            grammarParser.parse(grammarTextPane.getText());
        } catch (Exception ex) {
            System.out.println("Caught exception during parsing: " + ex.getMessage());
            return;
        }

        grammarTextPane.getHighlighter().removeAllHighlights();

        if (grammarParser.hasErrors()) {
            for (ParserError error : grammarParser.getErrors()) {
                highlightErrorLine(error.getLineIndex(), grammarTextPane);
            }
        } else {
            graphPanel.setNetworkGraph(grammarParser.getGraph());
            graphPanel.repaint();
        }

        initialisationTextPaneKeyReleased(null);
    }//GEN-LAST:event_grammarTextPaneKeyReleased

    private void initialisationTextPaneKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_initialisationTextPaneKeyReleased
        if (evt != null && isArrowKeyCode(evt.getKeyCode())) {
            return;
        }

        try {
            initialisationParser.parse(initialisationTextPane.getText());
        } catch (Exception ex) {
            System.out.println("Caught exception during parsing: " + ex.getMessage());
            return;
        }

        initialisationTextPane.getHighlighter().removeAllHighlights();

        if (initialisationParser.hasErrors()) {
            for (ParserError error : initialisationParser.getErrors()) {
                highlightErrorLine(error.getLineIndex(), initialisationTextPane);
            }
        } else {
            initialisationOutputTextArea.setText(initialisationParser.getOutputString());
            graphDeveloper = new GraphDeveloper(grammarParser, initialisationParser.getOutputString());
            graphDeveloper.develop();
            graphPanel.repaint();
        }
    }//GEN-LAST:event_initialisationTextPaneKeyReleased

    private boolean isArrowKeyCode(int keyCode) {
        return keyCode == KeyEvent.VK_LEFT
                || keyCode == KeyEvent.VK_UP
                || keyCode == KeyEvent.VK_RIGHT
                || keyCode == KeyEvent.VK_DOWN;
    }

    private void zoomSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_zoomSliderStateChanged
        zoomValue.setText("" + zoomSlider.getValue());
        graphPanel.repaint();
    }//GEN-LAST:event_zoomSliderStateChanged

    private void graphPanelWrapperMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_graphPanelWrapperMouseWheelMoved
        zoomSlider.setValue(zoomSlider.getValue() - evt.getWheelRotation() * 4);
    }//GEN-LAST:event_graphPanelWrapperMouseWheelMoved

    private void zoomSliderMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_zoomSliderMouseWheelMoved
        zoomSlider.setValue(zoomSlider.getValue() - evt.getWheelRotation() * 4);
    }//GEN-LAST:event_zoomSliderMouseWheelMoved

    private void grammarTextPaneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_grammarTextPaneKeyTyped
        if (evt.getKeyChar() == KeyEvent.VK_TAB) {
            if (evt.isShiftDown()) {
                removeTabIfPossilbe(evt, grammarTextPane);
            } else {
                insertTabAsSpace(evt, grammarTextPane);
            }
        }
    }//GEN-LAST:event_grammarTextPaneKeyTyped

    private void initialisationTextPaneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_initialisationTextPaneKeyTyped
        if (evt.getKeyChar() == KeyEvent.VK_TAB) {
            if (evt.isShiftDown()) {
                removeTabIfPossilbe(evt, initialisationTextPane);
            } else {
                insertTabAsSpace(evt, initialisationTextPane);
            }
        }
    }//GEN-LAST:event_initialisationTextPaneKeyTyped

    private void grammarHelpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grammarHelpButtonActionPerformed
        if (controller == null) {
            return;
        }

        String xPosition = storageValues.get(StorageKey.GRAMMAR_HELP_POSITION_X);
        String yPosition = storageValues.get(StorageKey.GRAMMAR_HELP_POSITION_Y);

        if (xPosition != null && yPosition != null) {
            int x = Integer.parseInt(xPosition);
            int y = Integer.parseInt(yPosition);

            controller.showGrammarHelpWindow(x, y);
        } else {
            controller.showGrammarHelpWindow(30, 30);
        }
    }//GEN-LAST:event_grammarHelpButtonActionPerformed

    private void initialisationHelpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_initialisationHelpButtonActionPerformed
        if (controller == null) {
            return;
        }

        String xPosition = storageValues.get(StorageKey.INITIALISATION_HELP_POSITION_X);
        String yPosition = storageValues.get(StorageKey.INITIALISATION_HELP_POSITION_Y);

        if (xPosition != null && yPosition != null) {
            int x = Integer.parseInt(xPosition);
            int y = Integer.parseInt(yPosition);

            controller.showInitialisationHelpWindow(x, y);
        } else {
            controller.showInitialisationHelpWindow(30, 30);
        }
    }//GEN-LAST:event_initialisationHelpButtonActionPerformed

    private void generateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateButtonActionPerformed
        KeyEvent generateEvent = new KeyEvent(this, KeyEvent.VK_F5, System.currentTimeMillis(), 0, KeyEvent.VK_F5, '\t');
        grammarTextPaneKeyReleased(generateEvent);
    }//GEN-LAST:event_generateButtonActionPerformed

    private void loadGraphButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadGraphButtonActionPerformed
        controller.showLoadGraphWindow();
    }//GEN-LAST:event_loadGraphButtonActionPerformed

    private void insertTabAsSpace(KeyEvent event, JTextComponent component) {
        try {
            int caretPostion = component.getCaretPosition() - 1;
            component.getDocument().remove(caretPostion, 1);
            component.getDocument().insertString(caretPostion, "    ", null);
            event.consume();
        } catch (BadLocationException ex) {
            System.out.println("Could not insert a tab: " + ex.getMessage());
        }
    }

    private void removeTabIfPossilbe(KeyEvent event, JTextComponent component) {
        try {
            Document doc = component.getDocument();
            int caretPostion = component.getCaretPosition();
            int lineStartIndex = doc.getText(0, caretPostion).lastIndexOf('\n') + 1;
            lineStartIndex = lineStartIndex < 0 ? 0 : lineStartIndex;
            lineStartIndex = lineStartIndex >= doc.getLength() ? doc.getLength() - 1 : lineStartIndex;
            int scanEndIndex = lineStartIndex + 4 <= doc.getLength() ? lineStartIndex + 4 : doc.getLength();

            for (int i = 0; i < 4 && i + lineStartIndex < scanEndIndex; i++) {
                if (doc.getText(lineStartIndex, 1).matches(" ")) {
                    doc.remove(lineStartIndex, 1);
                } else if (doc.getText(lineStartIndex, 1).matches("\t")) {
                    doc.remove(lineStartIndex, 1);
                    break;
                } else {
                    break;
                }
            }

            event.consume();
        } catch (BadLocationException ex) {
            System.out.println("Could not insert a tab: " + ex.getMessage());
        }
    }

    private void highlightErrorLine(int lineIndex, JTextComponent component) {
        String text = component.getText() + "\n";
        int startPosition = 0;
        int endPosition = 0;

        for (int i = 0; i <= lineIndex; i++) {
            startPosition = endPosition > 0 ? endPosition + 1 : 0;
            endPosition = text.indexOf('\n', startPosition);
        }

        try {
            DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
            component.getHighlighter().addHighlight(startPosition, endPosition, highlightPainter);
        } catch (BadLocationException ex) {
            System.out.println("Unable to highlight line with index " + lineIndex + ": " + ex.getMessage());
        }
    }

    private void increaseEditorFontSizes() {
        int newFontSize = grammarTextPane.getFont().getSize() + 2;
        setEditorFontSizes(newFontSize);
    }

    private void decreaseFontSizes() {
        int newFontSize = grammarTextPane.getFont().getSize() - 2;
        setEditorFontSizes(newFontSize);
    }

    private void setEditorFontSizes(float fontSize) {
        Font currentFont = grammarTextPane.getFont();
        Font newFont = currentFont.deriveFont(fontSize);

        grammarTextPane.setFont(newFont);
        initialisationTextPane.setFont(newFont);
        initialisationOutputTextArea.setFont(newFont);
    }
    
    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        pack();
    }

    @Override
    public void dispose() {
        storeValues();
        closeHelpWindows();
        super.dispose();
    }

    private void storeValues() {
        storageValues.put(StorageKey.WINDOW_STATE, "" + getState());
        storageValues.put(StorageKey.WINDOW_HEIGHT, "" + getSize().height);
        storageValues.put(StorageKey.WINDOW_WIDTH, "" + getSize().width);
        storageValues.put(StorageKey.WINDOW_POSITION_X, "" + getX());
        storageValues.put(StorageKey.WINDOW_POSITION_Y, "" + getY());
        storageValues.put(StorageKey.GRAMMAR_FILE_PATH, grammarFileTextField.getText());
        storageValues.put(StorageKey.INITIALISATION_FILE_PATH, initialisationFileTextField.getText());
        storageValues.put(StorageKey.EDITOR_FONT_SIZE, "" + grammarTextPane.getFont().getSize());
        storageValues.put(StorageKey.IS_GRAMMAR_HELP_VISIBLE, "" + controller.isGrammarHelpWindowVisible());

        if (controller.getGrammarHelpWindow() != null) {
            storageValues.put(StorageKey.GRAMMAR_HELP_POSITION_X, "" + controller.getGrammarHelpWindow().getX());
            storageValues.put(StorageKey.GRAMMAR_HELP_POSITION_Y, "" + controller.getGrammarHelpWindow().getY());
        }

        storageValues.put(StorageKey.IS_INITIALISATION_HELP_VISIBLE, "" + controller.isInitialisationHelpWindowVisible());

        if (controller.getInitialisationHelpWindow() != null) {
            storageValues.put(StorageKey.INITIALISATION_HELP_POSITION_X, "" + controller.getInitialisationHelpWindow().getX());
            storageValues.put(StorageKey.INITIALISATION_HELP_POSITION_Y, "" + controller.getInitialisationHelpWindow().getY());
        }

        storage.store(storageValues);
    }

    private void closeHelpWindows() {
        if (controller.isGrammarHelpWindowVisible()) {
            controller.getGrammarHelpWindow().dispose();
        }

        if (controller.isInitialisationHelpWindowVisible()) {
            controller.getInitialisationHelpWindow().dispose();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    javax.swing.JLabel coordinates;
    private javax.swing.JLabel coordinatesLabel;
    private javax.swing.JButton decreaseGrammarFontSizeButton;
    private javax.swing.JButton decreaseInitialisationFontSizeButton;
    private javax.swing.JPanel editorWrapperPanel;
    private javax.swing.JSplitPane editorWrapperSplitPane;
    private javax.swing.JButton generateButton;
    private javax.swing.JPanel grammarEditorPanel;
    private javax.swing.JButton grammarFileSelectButton;
    private javax.swing.JTextField grammarFileTextField;
    private javax.swing.JButton grammarHelpButton;
    private javax.swing.JButton grammarReloadButton;
    private javax.swing.JButton grammarSaveButton;
    private javax.swing.JSplitPane grammarSplitPane;
    private javax.swing.JTextPane grammarTextPane;
    private javax.swing.JScrollPane grammarTextPaneSplitPane;
    private javax.swing.JPanel graphPanelWrapper;
    private javax.swing.JButton increaseGrammarFontSizeButton;
    private javax.swing.JButton increaseInitialisationFontSizeButton;
    private javax.swing.JPanel initialisationEditorPanel;
    private javax.swing.JSplitPane initialisationEditorSplitPane;
    private javax.swing.JButton initialisationFileSelectButton;
    javax.swing.JTextField initialisationFileTextField;
    private javax.swing.JButton initialisationHelpButton;
    private javax.swing.JScrollPane initialisationOutputScrollPane;
    private javax.swing.JTextArea initialisationOutputTextArea;
    private javax.swing.JButton initialisationReloadButton;
    private javax.swing.JButton initialisationSaveButton;
    private javax.swing.JTextPane initialisationTextPane;
    private javax.swing.JScrollPane initialisationTextPaneScrollPane;
    private javax.swing.JButton loadGraphButton;
    private javax.swing.JButton saveAndUseButton;
    private javax.swing.JPanel visualisationPanel;
    private javax.swing.JSlider zoomSlider;
    private javax.swing.JLabel zoomValue;
    // End of variables declaration//GEN-END:variables
}
