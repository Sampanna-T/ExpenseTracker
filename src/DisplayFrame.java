package src;

import java.sql.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class DisplayFrame extends JFrame{

    private Statement statementObject;
    private String table;
    private String fromDate;
    private String toDate;
    private MainFrame mainFrame;
    private JButton deleteButton;

    public DisplayFrame(MainFrame mainFrame,Statement statementObject, String table, String fromDate, String toDate) throws SQLException{
        this.statementObject = statementObject;
        this.table = table;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.mainFrame = mainFrame;
        this.deleteButton = new JButton("DELETE"){{setBackground(Color.red);}};
    }
    
    //returns number of rows in the table
    private int getRowCount() throws SQLException{
        String countQuery = String.format("SELECT COUNT(*) FROM %s"+
        " WHERE %s BETWEEN '%s' and '%s'",table,getHeader(0),fromDate,toDate);
        ResultSet countResult = statementObject.executeQuery(countQuery);
        countResult.next();
        return countResult.getInt(1);
    }

    //returns number of columns in the table
    private int getColCount() throws SQLException{
        String countQuery = String.format("SELECT COUNT(COLUMN_NAME) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = N'%s'",table);
        ResultSet countResult = statementObject.executeQuery(countQuery);
        countResult.next();
        return countResult.getInt(1);
    }

    //returns array of all the column names
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
    
    //returns column name for a given column 0 is the first column
    private String getHeader(int col) throws SQLException{
        String header[] = getHeader();
        return header[col];
    }

    //returns entire data in 2D array format
    private Object[][] getData() throws SQLException{
        int col = getColCount();
        Object result[][] = new Object[getRowCount()][col];
        String resultQuery = String.format("SELECT * FROM %s"+
        " WHERE %s BETWEEN '%s' and '%s' ORDER BY %s",table,getHeader(0),fromDate,toDate,getHeader(0));
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
    
    //returns tuple for a given row
    private Object[] getRow(JTable curTable, int row){
        if(row < 0 || row >= curTable.getRowCount())return new Object[0];

        int col = curTable.getColumnCount();
        Object result[] = new String[col];
        for(int i = 0; i < col; i++)result[i] = curTable.getValueAt(row,i);

        return result;
    }
    
    //returns a condition to execute updateQuery
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

    //edited value will be updated in the database
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

    //selected row will be deleted
    private int deleteQuery(String date,String item){
        try{
            String dateCol = getHeader(0);
            String itemCol = getHeader(1);
            String query = String.format("delete from %s where"+
            " %s = '%s' and %s = '%s';",table,dateCol,date,itemCol,item);
            return statementObject.executeUpdate(query);
        }
        catch(SQLException sqle){
            sqle.printStackTrace();
            return -1;
        }
    }

    //updates the table with values
    private void updateTable(JTable table) throws SQLException{
        
    }

    //copy 

    /**
     * Displays the Expense summary Frame
     * @param width
     * represents the width of the Frame
     * @param height
     * represents the height of the Frame
     * @param xPos
     * represents starting horizontal position of the frame
     * @param yPos
     * represents starting vertical position of the frame
     * @throws SQLException
     * In case of invalid entries by user this exception will be raised
     */
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
        
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 5;
        gc.weighty = 5;

        gc.gridx = 0;
        gc.gridy = 0;
        add(scrollPane,gc);
        gc.gridx = 0;
        gc.gridy = 1;
        add(deleteButton,gc);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(table.getSelectedRow() != -1){
                    int row = table.getSelectedRow();
                    String date = table.getValueAt(row, 0).toString();
                    String item = table.getValueAt(row,1).toString();
                    int result = deleteQuery(date,item);
                    try{
                        updateTable(table);
                        mainFrame.costUpdate();
                        JOptionPane.showMessageDialog(DisplayFrame.this,"Deletion successful");
                        setVisible(false);
                        dispose();
                    }
                    catch(SQLException sqle){
                        sqle.printStackTrace();
                    }
                    catch(Exception exc){
                        exc.printStackTrace();
                    }
                }
            }
        });

        setSize(width, height);
        setLocation(xPos, yPos);
        setTitle("EXPENSE SUMMARY");
        setVisible(true);
    }
}
