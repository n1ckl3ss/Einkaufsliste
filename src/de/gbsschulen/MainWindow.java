package de.gbsschulen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

public class MainWindow extends JFrame {

    private JPanel jpNorth, jpSouth;
    private JComboBox<ArticleEntry> jComboBox;
    private JTextField jtxtAmount;
    private JButton jbtnAddEntry, jbtnDelete;
    private JLabel jlblCompletePrice;
    private JMenuBar jMenuBar;
    private JMenu jMenuFile;
    private JMenuItem jmiNew, jmiSave, jmiClose;
    private JTable jTable;
    private JScrollPane jScrollPane;
    private MyTableModel myTableModel;
    private DAO dao;
    private JFileChooser jFileChooser;

    public MainWindow() throws HeadlessException, SQLException {
        super("Einkaufsliste");
        this.dao = new DAO();
        initComponents();
        initMenu();
        initEvents();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setSize(700,500);

        this.pack();
        this.setVisible(true);
    }

    private void initEvents() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                terminate();
            }
        });

        this.jmiClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                terminate();
            }
        });

        this.jbtnAddEntry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEntry();
                refreshWindow();
            }
        });

        this.jmiNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTable();
                refreshWindow();
            }
        });

        this.jmiSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTableData();
            }
        });

        this.jbtnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeEntry();
            }
        });
    }

    private void removeEntry() {
        int selectedRow = jTable.getSelectedRow();

        if(selectedRow < 0){
            return;
        }

        String des = (String) this.myTableModel.getValueAt(selectedRow, 1);
        this.myTableModel.remove(des);

    }

    private void saveTableData() {
        int result = this.jFileChooser.showSaveDialog(this);

        if(result == JFileChooser.CANCEL_OPTION){
            return;
        }

        File selectedFile = this.jFileChooser.getSelectedFile();

        try {
            this.myTableModel.saveTableData(selectedFile);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,"Datei konnte nicht gepseichert werden","Fehler",JOptionPane.ERROR_MESSAGE);
        }

        JOptionPane.showMessageDialog(this,"Datei wurde erfolgreich gespeichert!","Info",JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearTable() {
        this.myTableModel = new MyTableModel();
        this.jTable.setModel(this.myTableModel);
    }

    private void terminate(){
        int confirmResult = JOptionPane.showConfirmDialog(MainWindow.this, "Wollen Sie wirklich Beenden?", "Beenden", JOptionPane.YES_NO_OPTION);
        if(confirmResult == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }

    private void refreshWindow(){
        this.jlblCompletePrice.setText(String.format(Locale.GERMANY,"%.2f",this.myTableModel.getCompletePrice()));
    }

    private void addEntry(){
        if(jComboBox.getSelectedIndex() == 0){
            return;
        }
        String input = jtxtAmount.getText();
        int amount = 0;
        try{
            amount = Integer.parseInt(input);
        }catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this,"Falsche Eingabe","Fehler",JOptionPane.WARNING_MESSAGE);
            this.jtxtAmount.setText("1");
            return;
        }

        if(amount > 0){
            ArticleEntry articleEntry = (ArticleEntry) jComboBox.getSelectedItem();
            ArticleEntry newArticleEntry = new ArticleEntry(articleEntry.getDesignation(),articleEntry.getPrice(),amount);
            articleEntry.setAmount(amount);
            this.myTableModel.addEntry(newArticleEntry);
            this.jComboBox.setSelectedIndex(0);
            this.jtxtAmount.setText("1");
        }
    }

    private void initMenu() {
        this.jMenuBar = new JMenuBar();
        this.jMenuFile = new JMenu("Datei");
        this.jmiNew = new JMenuItem("Neu");
        this.jmiSave = new JMenuItem("Speichern");
        this.jmiClose = new JMenuItem("Beenden");

        this.jMenuFile.add(this.jmiNew);
        this.jMenuFile.add(this.jmiSave);
        this.jMenuFile.add(this.jmiClose);

        this.jMenuBar.add(this.jMenuFile);

        this.setJMenuBar(this.jMenuBar);
    }

    private void initComponents() {
        this.jFileChooser = new JFileChooser();

        this.jpNorth = new JPanel();
        this.jComboBox = new JComboBox<>();


        fillComboBox();

        this.jtxtAmount = new JTextField(2);
        this.jtxtAmount.setText("1");
        this.jbtnAddEntry = new JButton("Eintragen");
        this.jbtnDelete = new JButton("Löschen");

        this.jpNorth.add(this.jComboBox);
        this.jpNorth.add(new JLabel("Anzahl:"));
        this.jpNorth.add(this.jtxtAmount);
        this.jpNorth.add(this.jbtnAddEntry);
        this.jpNorth.add(this.jbtnDelete);

        this.myTableModel = new MyTableModel();
        this.jTable = new JTable(this.myTableModel);
        this.jScrollPane = new JScrollPane(this.jTable);

        this.jpSouth = new JPanel();
        this.jpSouth.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.jpSouth.add(new JLabel("Gesamtpreis:"));
        this.jlblCompletePrice = new JLabel("0.00");
        this.jpSouth.add(new JLabel("Euro"));
        this.jpSouth.add(this.jlblCompletePrice);

        this.add(this.jpNorth,BorderLayout.NORTH);
        this.add(this.jScrollPane,BorderLayout.CENTER);
        this.add(this.jpSouth,BorderLayout.SOUTH);
    }

    private void fillComboBox() {
        this.jComboBox.addItem(new ArticleEntry("Bitte Auswählen",0,0));
        try {
            this.dao.findArticle("%%");
            for (ArticleEntry articleEntry : dao.getArticleEntries()) {
                this.jComboBox.addItem(articleEntry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                dao.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            MainWindow mainWindow = new MainWindow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
