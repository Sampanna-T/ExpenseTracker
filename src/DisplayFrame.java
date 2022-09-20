package src;

import java.sql.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class DisplayFrame extends JFrame{

    private Statement statementObject;
    private String table;
    private String fromDate;
    private String toDate;
    private MainFrame mainFrame;

    public DisplayFrame(MainFrame mainFrame,Statement statementObject, String table, String fromDate, String toDate) throws SQLException{
        this.statementObject = statementObject;
        this.table = table;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.mainFrame = mainFrame;
    }
    
    private int getRowCount() throws SQLException{
        String countQuery = String.format("SELECT COUNT(*) FROM %s"+
        " WHERE %s BETWEEN '%s' and '%s'",table,getHeader(0),fromDate,toDate);
        ResultSet countResult = statementObject.executeQuery(countQuery);
        countResult.next();
        return countResult.getInt(1);
    }

    private int getColCount() throws SQLException{
        String countQuery = String.format("SELECT COUNT(COLUMN_NAME) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = N'%s'",table);
        ResultSet countResult = statementObject.executeQuery(countQuery);
        countResult.next();
        return countResult.getInt(1);
    }

    private String[] getHeader() throws SQLException{

        String result[] = new String[getColCount()];

        String resultQuery = String.format("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = N'%s'",table);
        ResultSet resultSet = statementObject.executeQuery(resultQuery);
        
        int index = 0;
        while(resultSet.next()){
            result[index++] = resultSet.getString(1).toUpperCase();
        }
        return result;
    }
    
    private String getHeader(int col) throws SQLException{
        String header[] = getHeader();
        return header[col];
    }

    private Object[][] getData() throws SQLException{
        int col = getColCount();
        Object result[][] = new Object[getRowCount()][col];
        String resultQuery = String.format("SELECT * FROM %s"+
        " WHERE %s BETWEEN '%s' and '%s'",table,getHeader(0),fromDate,toDate);
        ResultSet resultSet = statementObject.executeQuery(resultQuery);
        
        int index = 0;
        while(resultSet.next()){
            for(int i = 0; i < col; i++){
                result[index][i] = resultSet.getString(i+1);
            }
            index++;
        }
        return result;
    
    }
    
    private Object[] getRow(JTable curTable, int row){
        if(row < 0 || row >= curTable.getRowCount())return new Object[0];

        int col = curTable.getColumnCount();
        Object result[] = new String[col];
        for(int i = 0; i < col; i++)result[i] = curTable.getValueAt(row,i);

        return result;
    }
    
    private String getCondition(Object[] oldValue) throws SQLException{
        String []header = getHeader();
        String condition = "";
        for(int i = 0; i < oldValue.length; i++){
            condition += header[i];
            condition += "=";
            condition += "'"+oldValue[i]+"'";
            if(i!=oldValue.length-1)condition += " and ";
            else condition += ";";
        }
        return condition;
    }

    private int updateQuery(Object[] oldValue, String newValue, int col){
        try{
            String colName = getHeader(col);
            String condition = getCondition(oldValue);
            String query = String.format("UPDATE %s SET %s='%s' "+
            "WHERE %s",table,colName,newValue,condition);
            return statementObject.executeUpdate(query);
        }
        catch(SQLException sqle){
            sqle.printStackTrace();
            return -1;
        }
    }

    public void display(int width, int height, int xPos, int yPos) throws SQLException{
        String[] columns = getHeader();
        Object[][] data = getData();

        JTable table = new JTable(data, columns){
            
            @Override
            public void setValueAt(Object obj, int row, int col){
                Object oldRow[] = getRow(this,row);
                Object newValue = obj;
                super.setValueAt(obj,row,col);
                int result = updateQuery(oldRow,newValue.toString(),col);
                JOptionPane.showMessageDialog(this,"Updated entry successfully");
                mainFrame.costUpdate();
            }
        };
        JScrollPane scrollPane = new JScrollPane(table);//putting table inside scrollPane to make it scrollable
        //table.setFillsViewportHeight(true);

        JLabel heading = new JLabel("EXPENSE");
        heading.setFont(new Font("Arial",Font.TRUETYPE_FONT,36));
        
        //getContentPane() is like a layer on the gui to add objects
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(heading,BorderLayout.PAGE_START);
        getContentPane().add(scrollPane,BorderLayout.CENTER);
        
        table.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                int col = e.getColumn();
                int row = e.getFirstRow();
                Object result = table.getValueAt(row,col);
            }

        });

        setSize(width, height);
        setLocation(xPos, yPos);
        setVisible(true);
    }
}
