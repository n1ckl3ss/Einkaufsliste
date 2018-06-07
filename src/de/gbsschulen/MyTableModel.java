package de.gbsschulen;

import javax.swing.table.AbstractTableModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class MyTableModel extends AbstractTableModel {
    private static  final String[] COLUMNS = {"Anzahl","Artikel","Einzelprteis","Preis"};

    private List<ArticleEntry> listEntries;


    public MyTableModel() {
        this.listEntries = new ArrayList<>();
/*        this.listEntries.add(new ArticleEntry("Apfel",0.45,2));
        this.listEntries.add(new ArticleEntry("Birne",0.55,5));
        this.listEntries.add(new ArticleEntry("Bananen",0.77,7));*/
    }

    @Override
    public int getRowCount() {
        return listEntries.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ArticleEntry articleEntry = this.listEntries.get(rowIndex);

        switch (columnIndex){
            case 0:
                return articleEntry.getAmount();
            case 1:
                return articleEntry.getDesignation();
            case 2:
                return String.format(Locale.GERMANY,"%.2f",articleEntry.getPrice());
            case 3:
                return String.format(Locale.GERMANY,"%.2f",articleEntry.getPrice() * articleEntry.getAmount());
        }

        return "";
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    public void addEntry(ArticleEntry articleEntry){
        Iterator<ArticleEntry> iterator = this.listEntries.iterator();

        while (iterator.hasNext()){
            ArticleEntry nextAE = iterator.next();
            if(nextAE.getDesignation().equals(articleEntry.getDesignation())){
                System.out.println(nextAE.getAmount());
                System.out.println(articleEntry.getAmount());
                nextAE.setAmount(nextAE.getAmount() + articleEntry.getAmount());
                this.fireTableDataChanged();
                return;
            }
        }

        this.listEntries.add(articleEntry);
        this.fireTableDataChanged();
    }

    public void saveTableData(File file) throws IOException {
        BufferedWriter bw = null;
        bw = new BufferedWriter(new FileWriter(file));

        for (ArticleEntry entry : listEntries) {
            bw.write(entry.getDesignation() + "," + entry.getAmount() + "," + entry.getPrice() + "," + (entry.getPrice() + entry.getAmount()));
            bw.newLine();
        }

        if(bw != null){
            bw.close();
        }
    }

    public double getCompletePrice(){
        double result = 0;

        for (ArticleEntry listEntry : listEntries) {
            result += listEntry.getPrice() * listEntry.getAmount();
        }

        return result;
    }

    public void remove(String designation){
        Iterator<ArticleEntry> iterator = this.listEntries.iterator();

        while (iterator.hasNext()){
            if(iterator.next().getDesignation().equals(designation)) {
                iterator.remove();
                this.fireTableDataChanged();
                return;
            }
        }
    }


}
