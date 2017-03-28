package ui;

/**
 * Created by Farima on 7/16/2016.
 */


import biz.BizFacade;
import biz.Result;
import biz.WorkUnit;
import biz.Worker;
import logging.MasterLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;

public class MainForm {
    BizFacade bizFacade=BizFacade.getInstance();
    DefaultListModel<String> mdlWorkers=new DefaultListModel<>();
    Properties masterProperties=new Properties();
    File chosenFile=null;
    JFrame frameStart = new JFrame("Master Start");
    JFrame frameResult = new JFrame("Master Start");
    ArrayList<String> availableWorkers=new ArrayList<>();
    private void callBiz(String filePath,String algorithmName)
    {
        if(bizFacade!=null){

        HashMap<WorkUnit,Result> resultHashMap=new HashMap<>();
        bizFacade.loadWorkers();
        resultHashMap=bizFacade.startWorking(filePath,algorithmName);
        frameStart.setVisible(false);
        showResultForm(resultHashMap);

        }
    }


    public static void main(String[] args) {

        MasterLogger.getInstance().saveLog("Master started at " + LocalDateTime.now(), Level.INFO);
        MainForm mainform=new MainForm();
        mainform.availableWorkers=BizFacade.getInstance().reportAvailableWorkers();
        mainform.loadProperties();
        /** Make the main frameStart**/
        mainform.initialize(mainform);

    }

    private void initialize(MainForm mainform)
    {
        frameStart.setSize(600, 400);
        frameStart.setLayout(new GridLayout(2, 2));
        frameStart.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frameStart.add(mainform.makeWorkersPart());
        frameStart.add(mainform.makeWorkUnitsPart());
        frameStart.setVisible(true);
    }

    private void showResultForm(HashMap<WorkUnit,Result> resultHashMap)
    {
        frameResult.setSize(800, 300);
        frameResult.setLayout(new GridLayout(resultHashMap.size() + 1, 3));
        frameResult.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frameResult.setVisible(true);

        for (WorkUnit workUnit:resultHashMap.keySet())
        {
            StringBuilder workUnitResult=new StringBuilder();
            workUnitResult.append(workUnit.getName()).append(" has been done by Worker").
                    append(((Result)resultHashMap.get(workUnit)).getWorkerName()).append(" at ").
                    append(((Result) resultHashMap.get(workUnit)).getProcessTime()).append(" MiliSeconds and its result is ").
                    append(((Result) resultHashMap.get(workUnit)).getFinalResult());
            frameResult.add(new JLabel(workUnitResult.toString()));
        }

        int sum=BizFacade.getInstance().doIntegration(resultHashMap);
        MasterLogger.getInstance().saveLog("Responses received and integrated at " + LocalDateTime.now(), Level.INFO);
        frameResult.add(new JLabel("The final and integrated result is: " + sum));
    }

        /**Method for making a panel in which user can add or remove workers**/
        JPanel makeWorkersPart() {
        /** Make the panel for adding workers**/
        JPanel panelWorkers = new JPanel();
        panelWorkers.setBorder(BorderFactory.createLineBorder(Color.black));
        panelWorkers.setLayout(new FlowLayout());
       /**panel for IP and Port
       make and add IP Part**/
        JPanel pnlIpPort = new JPanel(new GridLayout(3, 2));

        JTextField txtIp = new JTextField(16);
        txtIp.addKeyListener(new KeyListener() {//text field that just accepts numbers and dot
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (c != '.' && (c < '0' || c > '9') && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE)
                    e.consume();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                return;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                return;
            }
        });

        pnlIpPort.add(new JLabel("New Worker IP"));
        pnlIpPort.add(txtIp);
       /** make and add Port Part**/
        JTextField txtPort = new JTextField();
        txtPort.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ((c < '0' || c > '9') && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE)
                    e.consume();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                return;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                return;
            }
        });

        pnlIpPort.add(new JLabel("New Worker Port"));
        pnlIpPort.add(txtPort);

        /**Build the panel containing the list of the workers**/
        mdlWorkers=loadWorkers();
        JList<String> listWorkers =new JList<>(mdlWorkers);
        JPanel pnlWorkerList=new JPanel();
        JScrollPane pane1=new JScrollPane(listWorkers);

        /**make and add 'add' button **/
        JButton btnAddWorker = new JButton("Add");
        pnlIpPort.add(btnAddWorker);
        btnAddWorker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtIp.getText().length() > 0 && txtPort.getText().length() > 0) {
                    if (availableWorkers.contains(txtIp.getText()+":"+txtPort.getText())&& !((DefaultListModel)listWorkers.getModel()).contains(txtIp.getText()+":"+txtPort.getText()))
                    {
                    bizFacade.setWorker(txtIp.getText() + ":" + txtPort.getText());
                    mdlWorkers=loadWorkers();
                    listWorkers.setModel(mdlWorkers);
                    }
                    else if (!availableWorkers.contains(txtIp.getText()+":"+txtPort.getText()))
                        JOptionPane.showMessageDialog(panelWorkers,txtIp.getText()+":"+txtPort.getText()+" is not an available worker!");
                }
            }
        });

        /**make and add 'remove' button **/
        JButton btnRemoveWorker = new JButton("Remove");
        pnlIpPort.add(btnRemoveWorker);
        btnRemoveWorker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtIp.getText().length() > 0 && txtPort.getText().length() > 0) {
                    bizFacade.removeWorker(txtIp.getText() + ":" + txtPort.getText());
                    mdlWorkers=loadWorkers();
                    listWorkers.setModel(mdlWorkers);
                }
            }
        });

        pnlWorkerList.add(pane1);

        panelWorkers.add(pnlIpPort);
        panelWorkers.add(pnlWorkerList);
        return panelWorkers;
    }

    public  DefaultListModel<String>  loadWorkers()
    {
        DefaultListModel<String> result=new DefaultListModel<>();
        ArrayList<Worker> workerArrayList=BizFacade.getInstance().loadWorkers();
        if (!workerArrayList.isEmpty())
        {
            for (Worker w:workerArrayList)
                result.addElement(w.getIp()+":"+w.getPort());
        }
        return result;
    }

    public  DefaultListModel<String> addWorkerToList (String workerName,DefaultListModel<String> previousModel) {
        DefaultListModel<String> result=previousModel;
        result.addElement(workerName);
        return result;
    }

    private JPanel makeWorkUnitsPart()
    {
        JPanel panelWorkUnits = new JPanel();
        panelWorkUnits.setBorder(BorderFactory.createLineBorder(Color.black));
        panelWorkUnits.setLayout(new BoxLayout(panelWorkUnits, BoxLayout.Y_AXIS));

        JButton btnFileChooser=new JButton("Choose File");
        JLabel lblSelectedFile=new JLabel("");

        btnFileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser=new JFileChooser();
                fileChooser.setCurrentDirectory(new File(masterProperties.getProperty("fileHome")));
                int returnValue=fileChooser.showOpenDialog(panelWorkUnits);
                if (returnValue==JFileChooser.APPROVE_OPTION) {
                    chosenFile = fileChooser.getSelectedFile();
                    if (getFileExtension(chosenFile).equals("txt"))
                    lblSelectedFile.setText(chosenFile.getName());
                    else lblSelectedFile.setText("Wrong File Format!");
                }
            }
        });

        JList listAlgorithms=new JList(loadAlgorithmClasses());
        JScrollPane pane1=new JScrollPane(listAlgorithms);

        JButton btnStart=new JButton("Start Work");
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listAlgorithms.getSelectedValue() != null && chosenFile != null && getFileExtension(chosenFile).equals("txt"))
                    callBiz(chosenFile.getPath(), listAlgorithms.getSelectedValue().toString());
            }
        });

        JPanel pnlFileChooser=new JPanel();
        pnlFileChooser.setLayout(new BoxLayout(pnlFileChooser, BoxLayout.LINE_AXIS));
        pnlFileChooser.add(btnFileChooser);
        pnlFileChooser.add(lblSelectedFile);
        panelWorkUnits.add(pnlFileChooser);

        JPanel pnlAlgorithm=new JPanel();
        pnlAlgorithm.setLayout(new BoxLayout(pnlAlgorithm, BoxLayout.LINE_AXIS));
        pnlAlgorithm.add(new JLabel("Select your desired algorithm from the list:"));
        pnlAlgorithm.add(pane1);
        panelWorkUnits.add(pnlAlgorithm);

        JPanel pnlStart=new JPanel();
        pnlStart.setLayout(new BoxLayout(pnlStart,BoxLayout.LINE_AXIS));
        pnlStart.add(btnStart);
        panelWorkUnits.add(pnlStart);
        return panelWorkUnits;


    }

    public  DefaultListModel<String>  loadAlgorithmClasses()
    {
        DefaultListModel<String> result=new DefaultListModel<>();
        try{
            File algorithmsDir=new File(masterProperties.getProperty("algorithmsClassFilePath"));
            File[] algorithmsList=algorithmsDir.listFiles();
            for (int i = 0; i <algorithmsList.length ; i++) {
                if (!algorithmsList[i].getName().equals("ProcessAlgorithm.class"))
                    result.addElement(algorithmsList[i].getName());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;

    }

    private String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }


    private void loadProperties()
    {
        try(FileInputStream fis=new FileInputStream(new File("master.properties")))
        {
            masterProperties.load(fis);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}



