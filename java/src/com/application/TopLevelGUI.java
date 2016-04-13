package com.application;

import com.algorithms.AllComboArray;
import com.algorithms.InputStack;
import com.algorithms.PathGenerator;
import com.miscgui.AboutGUI;
import com.miscgui.HelpTab;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.*;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by CheokHo on 25/01/2016.
 */
public class TopLevelGUI extends JFrame{

    private Object parent;
    private mxGraph graph;
    private ArrayList<GraphNode> nodeArray;
    private ArrayList<Edge> edgeArray;
    private JTextArea consoleArea;
    //private ArrayList<String> stackArray;
    private PDAVersionGUI pdaTypeGUI;
    private PDAInGUI pdaInputEdit;
    private TransitionRuleGUI transRule;
    private DefaultTableModel model;
    private JPanel topPanel;
    private JSplitPane splitPane;
    private InputStack inputStack;
    private int executionTime = 1000;
    private List<String> missingCombo;

    //private mxCell nodePressed;
    private mxCell cellReleased;
    private mxCell cellPressed;
    private boolean finished = false;

    private Edge edge = null;
    private String inputString = null;
    private String stackString = null;

    public TopLevelGUI(){
        super("Pushdown Automata Tool");
        setLayout(new BorderLayout());
        setIconImage(new ImageIcon(getClass().getResource("/com/icon/favicon.png")).getImage());
        graph = new mxGraph();
        nodeArray = new ArrayList<GraphNode>();
        edgeArray = new ArrayList<Edge>();
//        stackArray = new ArrayList<String>();
//        stackArray.add("$");
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        createGraphPane();
        createStackGUI();
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, createConsoleGUI());
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.7);
        createMenuBar();

        add(splitPane, BorderLayout.CENTER);
    }


    public void createMenuBar(){

         inputStack = new InputStack();


        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu file = new JMenu("File");
        JMenuItem menuNew = new JMenuItem("New");
        JMenuItem menuRun = new JMenuItem("Run Simulation");
        JMenuItem menuOpen = new JMenuItem("Open");
        JMenuItem menuSave = new JMenuItem("Save");
        JMenuItem menuClose = new JMenuItem("Close");
        JMenuItem menuExportPic = new JMenuItem("Export graph as image");
        JMenuItem menuEdges = new JMenuItem("Current transitions");

        JMenu tools = new JMenu("Tools");
        tools.add(menuRun);
        tools.add(menuExportPic);
        tools.add(menuEdges);

        JMenu help = new JMenu("Help");
        JMenuItem userGuide = new JMenuItem("User Guide");
        JMenuItem about = new JMenuItem("About");
        help.add(userGuide);
        help.add(about);

        menuEdges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowTransitionsGUI showTransitionsGUI = new ShowTransitionsGUI(getEdgeArray());
            }
        });

        userGuide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HelpTab helpTab = new HelpTab();
            }
        });

        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutGUI aboutGUI = new AboutGUI();
            }
        });


        menuClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        file.add(menuNew); file.add(menuOpen); file.add(menuSave); file.add(menuClose);

        menuNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (graph.getChildVertices(graph.getDefaultParent()) != null) {
                    int reply = JOptionPane.showConfirmDialog(null, "Creating a new PDA will clear the existing.", "Warning", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));
                        nodeArray.clear();
                        edgeArray.clear();
                        pdaTypeGUI = new PDAVersionGUI(getTopLevelGUI(), true);
                        if (pdaTypeGUI.getPdaInputGUI() != null) {
                            inputStack.setStackArray(pdaTypeGUI.getPdaInputGUI().getInputStack().getStackArray());
                            inputStack.setInputArray(pdaTypeGUI.getPdaInputGUI().getInputStack().getInputArray());
                        }
                    }
                }
            }
        });

        menuRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AllComboArray allComboArray = new AllComboArray();
                boolean testRunnable = false;
                boolean containsInitial = false;
                boolean containsAccept = false;
                //needs better check.
                if (PDAVersionGUI.isNdpda == null || inputStack.getStackArray() == null || inputStack.getInputArray() == null) {
                    JOptionPane.showMessageDialog(getFocusOwner(), "No PDA is specified. No simulation can be run. \nPlease create a new PDA first.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    ArrayList<String> allInputStackCombo = allComboArray.getAllCombinations(inputStack.getInputArray(), inputStack.getStackArray());
                    missingCombo = allInputStackCombo;
                    //System.out.println("All input stack combo: " + allInputStackCombo);
                    for (GraphNode n : nodeArray) {
                        //System.out.println("Current input-stack combo: " + n.getOutGoingCombo());
                        missingCombo.removeAll(n.getOutGoingCombo());
                        if (n.isInitial) {
                            containsInitial = true;
                        }
                        if (n.isAccept) {
                            containsAccept = true;
                        }
//                        if (n.getOutGoingCombo().containsAll(allInputStackCombo) && allInputStackCombo.containsAll(n.getOutGoingCombo())) {
                        if (missingCombo.isEmpty()) {
                            testRunnable = true;
                        } else {
                            testRunnable = false;
                        }

                    }
                    if (PDAVersionGUI.isNdpda) {
                        testRunnable = true;
                    }
                    if (testRunnable == true && containsAccept == true && containsInitial == true) {
                        model.setRowCount(0);
                        model.addRow(new String[]{"$"});
                        model.fireTableDataChanged();
                        RunSimGUI runSimGUI = new RunSimGUI(getFocusOwner(), inputStack.getInputArray());
                        runSimGUI.showRunSimGUI();
                        getTextArea().append("\nRunning simulation on: " + runSimGUI.getInput()+"\n");
                        if (PDAVersionGUI.isNdpda) {
                            getTextArea().append("Preparing to run simulation. Specified graph is: Non Deterministic\n");
                        } else {
                            getTextArea().append("Preparing to run simulation. Specified graph is: Deterministic\n");
                        }

                        GraphNode tempNode = null;
                        boolean accept = false;
                        for (GraphNode n: getNodeArray()) {
                            if (n.isInitial && n.isAccept) {
                                tempNode = n;
                                accept = true;
                            } else if (n.isInitial && !n.isAccept) {
                                tempNode = n;
                                accept = false;
                            }
                        }
                        if (runSimGUI.getInput().equals("") && accept==true) {
                            getTextArea().append("RESULT: SUCCESS. '" + tempNode.toString() + "' is an accepting state.\n");
                        } else if  (runSimGUI.getInput().equals("") && accept==false) {
                            getTextArea().append("RESULT: FAILURE. '" + tempNode.toString() + "' is not an accepting state.\n");
                        }


                        SwingWorker<Void, Void> worker = null;
                        AlgorithmRunner algorithmRunner = new AlgorithmRunner(executionTime, runSimGUI, getModel(), getGraph(), nodeArray, edgeArray, getTextArea(), worker);
                        if (PDAVersionGUI.isNdpda) {
                            GraphNode node=null;
                            for (GraphNode n: nodeArray) {
                                if (n.isInitial) {
                                    node=n;
                                    break;
                                }
                            }

                            //create $ stack array here.

                            PathGenerator pathGenerator = new PathGenerator();
                            ArrayList<String> stackArray = new ArrayList<String>();
                            stackArray.add(0, "$");
                            pathGenerator.setStackArray(stackArray);
                            StringBuilder path=new StringBuilder();
                            path.append(node.toString());
                            pathGenerator.setStringBuilder(path);

                            algorithmRunner.ndpdaAlgorithm(runSimGUI.getInput(),node, pathGenerator);

                            //System.out.println("PATH GENERATOR: "+algorithmRunner.getPathGenerators(runSimGUI));

                            Set<String> temp = new HashSet<String>();
                            ArrayList<PathGenerator> paths = new ArrayList<PathGenerator>();
                            if (runSimGUI.getInput() != null && runSimGUI.getInput().length() > 0) {
                                for (PathGenerator p : algorithmRunner.getPathGenerators(runSimGUI)) {
                                    if (temp.add(p.getPath().toString())) {
                                        paths.add(p);
                                    }
                                    //System.out.println("stackarray at end: "+p.getStackArray()+" | transition operations:"+p.getStackOperations());
                                }
                            }
                            System.out.println("paths:"+paths.toString());

                            ArrayList<GraphNode> accepters = new ArrayList<GraphNode>();
                            for (GraphNode n: nodeArray) {
                                if (n.isAccept) {
                                    accepters.add(n);
                                }
                            }
                            for (PathGenerator p: paths) {
                                for (GraphNode n1: accepters) {
                                    if (p.toString().length() == runSimGUI.getInput().length()+1 && p.toString().endsWith(n1.toString())) {
                                        p.getPath().append(" ACCEPT");
                                    } else if (p.toString().length() == runSimGUI.getInput().length()+1 && !p.toString().endsWith(n1.toString())) {
                                        p.getPath().append(" FAILURE");
                                    } else if (p.toString().length() != runSimGUI.getInput().length()+1) {
                                        p.getPath().append(" STUCK");
                                    }
                                }
                            }
                            if (paths.isEmpty() == false) {
                                boolean inputAccepted = false;
                                Object[] possibilities = paths.toArray();
                                for (PathGenerator p: paths) {
                                    if (p.toString().contains("ACCEPT")) {
                                        inputAccepted=true;
                                        break;
                                    }else {
                                        inputAccepted=false;
                                    }
                                }
                                if (inputAccepted) {
                                    JOptionPane.showMessageDialog(getContentPane(), "Input string is accepted by this machine.");
                                } else {
                                    JOptionPane.showMessageDialog(getContentPane(), "Input string is rejected by this machine.");
                                }

                                PathGenerator result = (PathGenerator) JOptionPane.showInputDialog(getFocusOwner(), "Path(s) found are shown below. Please select which you would like to run.", "Simulation successful", JOptionPane.INFORMATION_MESSAGE, null, possibilities, paths.get(0));

                                if (result !=null && result.toString().length() >0) {
                                    //TODO run algorithm stuffs here. n.b there can be same paths with multiple routes.
                                    int i = result.getPath().indexOf(" ");
                                    if (i != -1) {
                                        result.getPath().delete(i, i + result.getPath().length());
                                    }
                                    algorithmRunner.runNDPDAPath(result);
                                }

                            } else if (runSimGUI.getInput() !=null && runSimGUI.getInput().length() > 0){
                                JOptionPane.showMessageDialog(getContentPane(), "No path found. This means there is no available transition to be taken.", "Simulation failed", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            algorithmRunner.dpdaAlgorithm(PDAVersionGUI.isNdpda);
                        }

                    } else if (containsAccept == false || containsInitial == false) {
                        JOptionPane.showMessageDialog(getFocusOwner(), "There is no initial and/or accepting state in your PDA.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        System.out.println("Missing combo: "+missingCombo);
                        JOptionPane.showMessageDialog(getFocusOwner(), "You have not specified all the transition rules required to run a deterministic PDA simulation.\nMissing combination(s) is: "+missingCombo+", where the left value is the input and the right value is the top stack.\nPlease ensure that every state contains an outgoing rule with each missing combination.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        menuExportPic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(){
                    public void approveSelection() {
                        File f = getSelectedFile();
                        if (f.exists() && getDialogType() == SAVE_DIALOG) {
                            int result = JOptionPane.showConfirmDialog(this,
                                    "This file already exists, do you wish to overwrite?", "Existing file",
                                    JOptionPane.YES_NO_CANCEL_OPTION);
                            switch (result) {
                                case JOptionPane.YES_OPTION:
                                    super.approveSelection();
                                    return;
                                case JOptionPane.CANCEL_OPTION:
                                    cancelSelection();
                                    return;
                                default:
                                    return;
                            }
                        }
                        super.approveSelection();
                    }
                };
                FileNameExtensionFilter filter = new FileNameExtensionFilter("png image (*.png)", "png");
                fileChooser.setFileFilter(filter);
                if (fileChooser.showSaveDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
                    String fileName = fileChooser.getSelectedFile().toString();
                    if (!fileName.endsWith(".png")) {
                        fileName+=".png";
                    }
                    BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 1, Color.WHITE, true, null);
                    try {
                        if (image == null) {
                            JOptionPane.showMessageDialog(getFocusOwner(), "No graph found; cannot export as image.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                         else {
                            ImageIO.write(image, "png", new File(fileName));
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        menuSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser(){
                    public void approveSelection() {
                        File f = getSelectedFile();
                        if (f.exists() && getDialogType() == SAVE_DIALOG) {
                            int result = JOptionPane.showConfirmDialog(this,
                                    "This file already exists, do you wish to overwrite?", "Existing file",
                                    JOptionPane.YES_NO_CANCEL_OPTION);
                            switch (result) {
                                case JOptionPane.YES_OPTION:
                                    super.approveSelection();
                                    return;
                                case JOptionPane.CANCEL_OPTION:
                                    cancelSelection();
                                    return;
                                default:
                                    return;
                            }
                        }
                        super.approveSelection();
                    }
                };
                FileNameExtensionFilter filter = new FileNameExtensionFilter("xml file (*.xml)", "xml");
                fileChooser.setFileFilter(filter);
                String type="";
                if (PDAVersionGUI.isNdpda != null && PDAVersionGUI.isNdpda) {
                    type="Non-Deterministic";
                } else if (PDAVersionGUI.isNdpda != null){
                    type="Deterministic";
                }
                fileChooser.setDialogTitle("Save "+type+" PDA");
                if (fileChooser.showSaveDialog(fileChooser) == fileChooser.APPROVE_OPTION) {
                    String fileName = fileChooser.getSelectedFile().toString();
                    if (!fileName.endsWith(".xml")) {
                        fileName+=".xml";
                    }
                    String inputString = inputStack.getInputArray().toString()
                    .replace(",", "")  //remove the commas
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim();           //remove trailing spaces from partially initialized arrays

                    ArrayList<String> temp = inputStack.getStackArray();
                    for (String s:temp) {
                        if(s.equals("$")) {
                            temp.remove(s);
                            break;
                        }
                    }
                    String stackString = temp.toString()
                            .replace(",", "")  //remove the commas
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim();           //remove trailing spaces from partially initialized arrays
                    File file = new File(fileName);
                    try(FileWriter fw = new FileWriter(file)) {
                        fw.write("<"+type+"_pda>\r\n");
                        fw.write("  <input_alphabet>"+inputString+"</input_alphabet>\r\n");
                        fw.write("  <stack_alphabet>"+stackString+"</stack_alphabet>\r\n");
                        for (GraphNode n: nodeArray) {
                            fw.write("  <node name=\""+n.toString()+"\">\r\n");
                            fw.write("      <is_initial>"+n.isInitial()+"</is_initial>\r\n");
                            fw.write("      <is_accept>"+n.isAccept()+"</is_accept>\r\n");
                            fw.write("      <x_pos>"+graph.getView().getState(n.getNode()).getX()+"</x_pos>\r\n");
                            fw.write("      <y_pos>"+graph.getView().getState(n.getNode()).getY()+"</y_pos>\r\n");
                            fw.write("  </node>\r\n");
                        }
                        for (Edge edge:edgeArray) {
                            fw.write("  <edge>\r\n");
                            fw.write("      <rule>"+edge.toString()+"</rule>\r\n");
                            fw.write("      <from_node>"+edge.getFromNode()+"</from_node>\r\n");
                            fw.write("      <to_node>"+edge.getToNode()+"</to_node>\r\n");
                            fw.write("  </edge>\r\n");
                            //fw.write(edge.toString() + "  " + edge.getFromNode() + edge.getToNode()+"\r\n");
                        }
                        //fw.write(inputStack.getInputArray()+"\r\n"+inputStack.getStackArray()+"\r\n");
                        fw.write("</"+type+"_pda>\r\n");
                        fw.flush();
                        fw.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        });

        menuOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(TopLevelGUI.this) == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = fileChooser.getSelectedFile();
                        DocumentBuilderFactory dbFactory
                                = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        Document doc = dBuilder.parse(file);
                        doc.getDocumentElement().normalize();
                        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

                        if (doc.getDocumentElement().getNodeName().startsWith("Non-Deterministic")) {
                            PDAVersionGUI.isNdpda=true;
                        } else if (doc.getDocumentElement().getNodeName().startsWith("Deterministic")) {
                            PDAVersionGUI.isNdpda=false;
                        }

                        graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));
                        nodeArray.clear();
                        edgeArray.clear();


                        NodeList nList = doc.getElementsByTagName("node");

                        for (int i = 0; i < nList.getLength(); i++) {
                            Node xmlNode = nList.item(i);
                            System.out.println("\nCurrent Element :" + xmlNode.getNodeName());
                            if (xmlNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element element = (Element) xmlNode;
                                double x=Double.parseDouble(element.getElementsByTagName("x_pos").item(0).getTextContent());
                                double y=Double.parseDouble(element.getElementsByTagName("y_pos").item(0).getTextContent());
                                boolean isInitial;
                                if (element.getElementsByTagName("is_initial").item(0).getTextContent().equals("true")) {
                                    isInitial=true;
                                } else {
                                    isInitial=false;
                                }
                                boolean isAccept;
                                if (element.getElementsByTagName("is_accept").item(0).getTextContent().equals("true")) {
                                    isAccept=true;
                                } else {
                                    isAccept=false;
                                }
                                String state=element.getAttribute("name");
                                getTopLevelGUI().createNode((int) x, (int) y, state, isAccept, isInitial);
                                System.out.println(state);
                                System.out.println(isInitial);
                                System.out.println(isAccept);
                                System.out.println(x);
                                System.out.println(y);
                            }
                        }

                        inputString = doc.getElementsByTagName("input_alphabet").item(0).getTextContent();
                        stackString = doc.getElementsByTagName("stack_alphabet").item(0).getTextContent();

                        NodeList eList = doc.getElementsByTagName("edge");

                        for (int q=0; q<eList.getLength(); q++) {
                            Node xmlNode = eList.item(q);
                            System.out.println("\nCurrent Element :" + xmlNode.getNodeName());

                            if (xmlNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element element = (Element) xmlNode;
                                String rule = element.getElementsByTagName("rule").item(0).getTextContent();
                                String splitRule = rule
                                        .replace(",", "")  //remove the commas
                                        .replace("{", "")  //remove the right bracket
                                        .replace("}", "")  //remove the left bracket
                                        .trim();           //remove trailing spaces from partially initialized arrays
                                String split[] = splitRule.split("\\s+");

                                String fromNode = element.getElementsByTagName("from_node").item(0).getTextContent();
                                String toNode = element.getElementsByTagName("to_node").item(0).getTextContent();

                                if (inputString != null && stackString != null) {
                                    inputStack.setInputArray(new ArrayList(Arrays.asList(inputString.split("\\s+"))));
                                    inputStack.setStackArray(new ArrayList(Arrays.asList(stackString.split("\\s+"))));
                                    inputStack.getStackArray().add("$");
                                }

                                Object from=null;
                                Object to=null;


                                for (GraphNode n: nodeArray) {
                                    if(graph.getView().getState(n.getNode()).getLabel().equals(fromNode)) {
                                        from=n.getNode();
                                        n.addOutgoingInput(split[0]);
                                        n.addOutgoingTopStack(split[1]);
                                        n.addOutgoingCombo(split[0] + split[1]);
                                        n.addOutgoingEdgeRule(rule);
                                        n.addToFromCombo(toNode + fromNode);
                                        //System.out.println("test"+n.getToFromCombo());
                                    }
                                    if(graph.getView().getState(n.getNode()).getLabel().equals(toNode)) {
                                        to=n.getNode();

                                    }
                                    System.out.println(n.getOutGoingEdgeRule());
                                }

                                addEdge(graph, rule, from, to, Integer.parseInt(split[0]), split[1], split[2]);
                            }


                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        JMenu edit = new JMenu("Edit");
        JMenuItem menuPDAType = new JMenuItem("PDA Type");
        JMenu execTime = new JMenu("Simulation delay");

        JMenuItem exec1000 = new JMenuItem("1 second");
        JMenuItem exec3000 = new JMenuItem("3 seconds");
        JMenuItem exec5000 = new JMenuItem("5 seconds");

        exec1000.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executionTime=1000;
            }
        });
        exec3000.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executionTime=3000;
            }
        });
        exec5000.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executionTime = 5000;
            }
        });

        execTime.add(exec1000); execTime.add(exec3000); execTime.add(exec5000);



        menuPDAType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PDAVersionGUI(getTopLevelGUI(), false);
            }
        });
        JMenuItem defAlphabets = new JMenuItem("Input/Stack Alphabet");

        defAlphabets.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pdaInputEdit = new PDAInGUI(getTopLevelGUI(), true);
                if (finished) {
                    ArrayList<String> inputAlph = inputStack.getInputArray();
                    String inputString = inputAlph.toString()
                            .replace(",", "")  //remove the commas
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim();           //remove trailing spaces from partially initialized arrays
                    ArrayList<String> stackAlph = inputStack.getStackArray();

                    for (String s:stackAlph) {
                        if(s.equals("$")) {
                            stackAlph.remove(s);
                            break;
                        }
                    }
                    String stackString = stackAlph.toString()
                            .replace(",", "")  //remove the commas
                            .replace("[", "")  //remove the right bracket
                            .replace("]", "")  //remove the left bracket
                            .trim();           //remove trailing spaces from partially initialized arrays
                    pdaInputEdit.setInputField(inputString);
                    pdaInputEdit.setStackField(stackString);
                }
                pdaInputEdit.setInputStack(inputStack);
                inputStack.setInputArray(pdaInputEdit.getInputStack().getInputArray());
                inputStack.setStackArray(pdaInputEdit.getInputStack().getStackArray());
                if (pdaInputEdit.getInputStack().getInputArray()!=null && pdaInputEdit.getInputStack().getStackArray()!=null && inputString!=null && stackString!=null) {
                    pdaInputEdit.setInputField(inputString);
                    pdaInputEdit.setStackField(stackString);
                }
                pdaInputEdit.setVisible(true);
            }
        });

        JMenuItem menuDelete = new JMenuItem("Clear Graph");
        menuDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));
                nodeArray.clear();
                edgeArray.clear();
            }
        });

        edit.add(menuPDAType); edit.add(defAlphabets); edit.add(execTime); edit.add(menuDelete);

        menuBar.add(file); menuBar.add(edit); menuBar.add(tools);menuBar.add(help);

    }

    public void createStackGUI() {
        JPanel stackPanel = new JPanel();
        stackPanel.setLayout(new FlowLayout());
        Object columnName[] = {"Stack"};
        model = new DefaultTableModel(columnName, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        //model.addColumn("Stack", stackArray.toArray());
        model.addRow(new String[]{"$"});

        JTable stackTable = new JTable(model);
        stackTable.setRowHeight(25);
        stackTable.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 12));
        stackTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        stackTable.setForeground(Color.decode("#770000"));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        stackTable.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        //stackTable.setTableHeader(null);
        JScrollPane scrollPane= new JScrollPane(stackTable);
        scrollPane.setPreferredSize(new Dimension(150, 530));
        stackPanel.add(scrollPane);
        topPanel.add(stackPanel, BorderLayout.EAST);
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public JPanel createConsoleGUI() {
        JPanel consolePanel = new JPanel();

        consolePanel.setLayout(new BorderLayout());
        consoleArea = new JTextArea();
        JPopupMenu copymenu = new JPopupMenu();
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem clear = new JMenuItem("Clear console");
        copymenu.add(copy); copymenu.add(clear);
        consoleArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) || e.isControlDown()) {
                    copymenu.show(e.getComponent(), e.getX(), e.getY());

                    copy.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String selection = consoleArea.getSelectedText();
                            StringSelection data = new StringSelection(selection);
                            Clipboard clipboard =
                                    Toolkit.getDefaultToolkit().getSystemClipboard();
                            clipboard.setContents(data, data);
                        }
                    });

                    clear.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            consoleArea.setText("");
                        }
                    });
                }
            }
        });
        consoleArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(consoleArea);
        consolePanel.add(scrollPane, BorderLayout.CENTER);
        //add(consolePanel, BorderLayout.SOUTH);
        return consolePanel;
    }

    public JTextArea getTextArea() {
        return consoleArea;
    }


    public void createGraphPane(){
        parent = graph.getDefaultParent();

        mxGraphComponent graphComponent = new mxGraphComponent(graph) {
            @Override
            protected mxConnectionHandler createConnectionHandler() {
                return new mxConnectionHandler(this) {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        super.mouseReleased(e);
                    }
                };
            }
        };


        //This handles edge creation handlers.
        graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxEventSource.mxIEventListener() {
            @Override
            public void invoke(Object sender, mxEventObject evt) {
                //System.out.println("edge: " + evt.getProperty("cell"));
                graph.getModel().remove(evt.getProperty("cell"));
            }
        });


        JPopupMenu popup = new JPopupMenu();
        JMenuItem delete = new JMenuItem("Delete");
        JMenu newstuff = new JMenu("New");
        JMenuItem initialNode = new JMenuItem("Initial Node");
        JMenuItem normalNode = new JMenuItem("Normal Node");
        JMenuItem acceptingNode = new JMenuItem("Accepting Node");
        JMenuItem bothNode = new JMenuItem("Accept & Initial Node");

        initialNode.addActionListener(new NewNodeGUI(true, false, getTopLevelGUI()));
        normalNode.addActionListener(new NewNodeGUI(false, false, getTopLevelGUI()));
        acceptingNode.addActionListener(new NewNodeGUI(false, true, getTopLevelGUI()));
        bothNode.addActionListener(new NewNodeGUI(true, true, getTopLevelGUI()));


        newstuff.add(initialNode);
        newstuff.add(normalNode);
        newstuff.add(acceptingNode);
        newstuff.add(bothNode);

        popup.add(delete);
        popup.add(newstuff);



        //This handles node creation handlers.

        NewNodeGUI newNodeGUI;
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    cellPressed = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
                    if (cellPressed != null) {
                        //System.out.println("Left Click Cell Value: " + cellPressed.getValue().toString());
                    }
                }
                if (SwingUtilities.isRightMouseButton(e) || e.isControlDown()) {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                    newstuff.setEnabled(true);
                    delete.setEnabled(false);
                    NewNodeGUI.setCoord(e.getX(), e.getY());

                    cellPressed = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
                    if (cellPressed != null) {
                        //System.out.println("Right Click Cell Value: " + cellPressed.getValue().toString());
                        delete.setEnabled(true);
                        newstuff.setEnabled(false);
                        popup.show(e.getComponent(), e.getX(), e.getY());
                        delete.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // TODO Auto-generated method stub
                                graph.removeCells(new Object[]{cellPressed});
                                for (int i = 0; i < nodeArray.size(); i++) {
                                    if (nodeArray.get(i).toString().equals(cellPressed.getValue().toString())) {
                                        for (int x = 0; x < edgeArray.size(); x++) {
                                            if (nodeArray.get(i).equals(edgeArray.get(x).getFromNode()) || nodeArray.get(i).equals(edgeArray.get(x).getToNode())) {
                                                edgeArray.remove(x);
                                            }
                                        }
                                        nodeArray.remove(i);
                                    }
                                }
                                for (int i = 0; i < edgeArray.size(); i++) {
                                    if (edgeArray.get(i).toString().equals(cellPressed.getValue().toString())) {
                                        //doesnt enter this if statement when i create edge from load.
                                        for (GraphNode n : nodeArray) {
                                            for (String s : n.getOutGoingTopStacks()) {
                                                if (s.equals(edgeArray.get(i).getEdgeTopStack())) {
                                                    n.getOutGoingTopStacks().remove(s);
                                                    break;
                                                }
                                            }
                                            for (String s1 : n.getOutgoingInputs()) {
                                                int value = Integer.parseInt(s1);
                                                if (value == edgeArray.get(i).getEdgeTopInput()) {
                                                    n.getOutgoingInputs().remove(s1);
                                                    break;
                                                }
                                            }
                                            for (String s2 : n.getOutGoingCombo()) {
                                                if (s2.equals(edgeArray.get(i).getEdgeTopInput() + edgeArray.get(i).getEdgeTopStack())) {
                                                    n.getOutGoingCombo().remove(s2);
                                                    break;
                                                }
                                            }
                                            for (String s3 : n.getOutGoingEdgeRule()) {
                                                if (s3.equals(edgeArray.get(i).toString())) {
                                                    n.getOutGoingEdgeRule().remove(s3);
                                                    break;
                                                }
                                            }
                                            for (String s4 : n.getToFromCombo()) {
                                                if (s4.equals(edgeArray.get(i).getToNode().toString() + edgeArray.get(i).getFromNode().toString())) {
                                                    n.getToFromCombo().remove(s4);
                                                    break;
                                                }
                                            }
                                            //System.out.println("COMBO   :"+n.getOutGoingCombo());
                                        }
                                        edgeArray.remove(i);
                                    }
                                }
                                System.out.println("Updated Edge on delete: " + edgeArray.toString());
                                System.out.println("Updated Node on delete: " + nodeArray.toString());
                                repaint();
                            }
                        });
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
                cellReleased = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
                if (SwingUtilities.isLeftMouseButton(e) && cellPressed != null) {
                    if (inputStack.getInputArray() == null || inputStack.getStackArray() == null) {
                        JOptionPane.showMessageDialog(getFocusOwner(), "Please specify an input/stack alphabet before creating transition rules.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else if(PDAVersionGUI.isNdpda == null) {
                        JOptionPane.showMessageDialog(getFocusOwner(), "Please specify a deterministic/non deterministic graph before creating transition rules.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else if (cellReleased != null && cellReleased.isVertex() && e.getClickCount() == 2) {
                        //if (!pdaTypeGUI.isNdpda()) { //this is a deterministic PDA so each node must only have 1 rule for each input.
                        transRule = new TransitionRuleGUI(getTopLevelGUI(), cellPressed.getValue().toString(), cellReleased.getValue().toString(), inputStack.getStackArray(), inputStack.getInputArray(), PDAVersionGUI.isNdpda);

                        //graph.insertEdge(parent, null, "self loop", nodePressed, (Object) cellReleased);
                    } else if (cellReleased != null && cellReleased.isVertex() && !cellPressed.getValue().equals(cellReleased.getValue())) {
                        //if (!pdaTypeGUI.isNdpda()) { //this is a deterministic PDA so each node must only have 1 rule for each input.
                        transRule = new TransitionRuleGUI(getTopLevelGUI(), cellPressed.getValue().toString(), cellReleased.getValue().toString(), inputStack.getStackArray(), inputStack.getInputArray(), PDAVersionGUI.isNdpda);
                    }
                    //System.out.println(nodeArray.get(0).toString() +"   "+nodeArray.get(0).getOutgoingInputs()+"    "+nodeArray.get(0).getOutGoingTopStacks());
                }
            }
        });
        topPanel.add(graphComponent, BorderLayout.CENTER);
    }

    public Object createNode(int x,int y, String state, boolean isAccepting, boolean isInitial) {
        graph.getModel().beginUpdate();
        Object node=null;
        GraphNode newNode;
        try
        {
            if (isInitial && !isAccepting) {
                //System.out.println("init - not accept");
                node = graph.insertVertex(parent, null, state, x, y, 80, 60, "shape=ellipse;perimeter=ellipsePerimeter;fillColor=#4CC417;strokeColor=#0C090A;fontColor=#770000;fontSize=16;fontStyle=4");
            }
            else if (isAccepting && !isInitial) {
                //System.out.println("not init - accept");
                node = graph.insertVertex(parent, null, state, x, y, 80, 60, "shape=doubleEllipse; perimeter=ellipsePerimeter;fillColor=#3090C7;strokeColor=#0C090A;fontColor=;fontSize=16;fontStyle=4");
            }
            else if (isAccepting && isInitial) {
                //System.out.println("init - accept");
                node = graph.insertVertex(parent, null, state, x, y, 80, 60, "shape=doubleEllipse;perimeter=ellipsePerimeter;fillColor=#4CC417;strokeColor=#0C090A;fontColor=#770000;fontSize=16;fontStyle=4");
            }
            else if (!isInitial && !isAccepting) {
                //System.out.println("not init - not accept");
                node = graph.insertVertex(parent, null, state, x, y, 80, 60, "shape=ellipse; perimeter=ellipsePerimeter;fillColor=#3090C7;strokeColor=#0C090A;fontColor=#770000;fontSize=16;fontStyle=4");
            }
            newNode = new GraphNode(node, state, isInitial, isAccepting);
            nodeArray.add(newNode);

            graph.setVertexLabelsMovable(false);
            graph.setCellsEditable(false);
        }
        finally
        {
            graph.getModel().endUpdate();
        }
        return node;
    }

    public void addEdge(mxGraph graph, String transRule, Object from, Object to, int topInput, String topStack, String transOperation) {
        graph.getModel().beginUpdate();
        try {
            Object parent = graph.getDefaultParent();

            mxIGraphLayout layout = new mxParallelEdgeLayout(graph, 30);
            layout.execute(parent);

            Map<String, Object> edgeStyle = new HashMap<String, Object>();
            edgeStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
            edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
            edgeStyle.put(mxConstants.STYLE_STROKECOLOR, "#0C090A");
            edgeStyle.put(mxConstants.STYLE_FONTCOLOR, "#0C090A");
            edgeStyle.put(mxConstants.STYLE_ROUNDED, true);
            edgeStyle.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_SIDETOSIDE);

            mxStylesheet stylesheet = new mxStylesheet();
            stylesheet.setDefaultEdgeStyle(edgeStyle);
            graph.setStylesheet(stylesheet);


            graph.setCellsBendable(true);
            graph.setCellsDisconnectable(false);
            graph.setEdgeLabelsMovable(false);

            //graph.insertEdge(parent, null, transRule, (Object) getTopLevelGUI().getCellPressed(), (Object) getTopLevelGUI().getCellReleased());
            graph.insertEdge(parent, null, transRule, from, to);

            GraphNode fromNode = null;
            GraphNode toNode = null;


            for (GraphNode node : getTopLevelGUI().getNodeArray()) {
                if (node.toString().equals(graph.getView().getState(from).getLabel())) {
                    fromNode = node;
                }
                if (node.toString().equals(graph.getView().getState(to).getLabel())) {
                    toNode = node;
                }

            }
            edge = new Edge(transRule);
            edge.setFromNode(fromNode);
            edge.setToNode(toNode);
            edge.setEdgeTopInput(topInput);
            edge.setEdgeTopStack(topStack);
            edge.setTransitionOperation(transOperation);

            edgeArray.add(edge);
            System.out.println("Edge from: " + edge.getFromNode() + " Edge to: " + edge.getToNode());
            System.out.println("added edge array: " + edgeArray);

        } finally {
            graph.getModel().endUpdate();
        }
    }


    public ArrayList<GraphNode> getNodeArray() {
        return nodeArray;
    }

    public ArrayList<Edge> getEdgeArray() { return edgeArray; }

    public mxCell getCellPressed() {
        return cellPressed;
    }

    public mxCell getCellReleased() {
        return cellReleased;
    }

    public mxGraph getGraph() {
        return graph;
    }
    public TopLevelGUI getTopLevelGUI() {
        return this;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Edge getEdge() {
        return edge;
    }

}
