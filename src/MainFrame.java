package src;

import java.sql.*;
import java.time.Year;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.*;


public class MainFrame extends JFrame implements ActionListener{
    
    /*
     * holds the default title for the MainFrame
     */
    private static final String DEFAULT_TITLE = "EXPENSE TRACKER";
    /*
     * holds the default height for the MainFrame
     */
    private static final int DEFAULT_HEIGHT = 800;
    /*
     * holds the default width for the MainFrame
     */
    private static final int DEFAULT_WIDTH = 800;
    
    /*
     * URL,USER,PASSWORD for SQL connectivity
     */
    static final String url = "jdbc:mysql://localhost:3306/Sampanna";
    static final String user = "root";
    static final String password = "Kashi16sadan@";

    static{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(-1);//connection with driver unsuccessfull
        }
    }

    /* Input fields */
    private JComboBox<Integer> dateInputDay;
    private JComboBox<Integer> dateInputMonth;
    private JComboBox<Integer> dateInputYear;
    private JTextField itemInput;
    private JTextField commentInput;
    private JTextField costInput;
    private JButton addButton;
    
    private JTextField dayCostInput;
    private JTextField monthCostInput;
    private JTextField yearCostInput;
    private JTextField dayAvgCostInput;
    private JTextField monthAvgCostInput;
    private JTextField yearAvgCostInput;

    private JComboBox<Integer> fromDateInputDay;
    private JComboBox<Integer> fromDateInputMonth;
    private JComboBox<Integer> fromDateInputYear;
    private JComboBox<Integer> toDateInputDay;
    private JComboBox<Integer> toDateInputMonth;
    private JComboBox<Integer> toDateInputYear;
    private JButton displayButton;

    /* SQL fields */
    private Connection connectionObject;
    private Statement statementObject;
    private String table;
    
    /**
	 * constructor which initializes the MainFrame 
	 */
    public MainFrame(){
        this(DEFAULT_TITLE,DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }

    /**
	 * constructor which initializes the MainFrame
	 * 
	 * @param title
	 * represents title of MainFrame 
     * @param width
	 * represents width of MainFrame
     * @param height
	 * represents of height
	 */
    public MainFrame(String title, int width, int height){
        sqlConnect();
        initFrame(title, width, height);
    }

    //Establish connections with SQL DB
    private boolean sqlConnect(){
        try{
            connectionObject = DriverManager.getConnection(url,user,password);
            statementObject = connectionObject.createStatement();
            table = "expense";
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(-1);//unsuccesfull attempt to get connection
        }
        return false;
    }

    //creates a new Frame with given title,width and height
    private void initFrame(String title,int width, int height){
        setTitle(title);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setLocation(getXOrigin(width),getYOrigin(height));
        setVisible(true);
        setSize(width,height);
        setResizable(true);
        setFocusable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    //returns x origin for given width
    private static int getXOrigin(int width){      
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        int halfScrWidth = (int)(dimension.getWidth()/2);
        int xOrigin = halfScrWidth-(width/2);
        return xOrigin;
    }

    //returns y origin for a given height
    private static int getYOrigin(int height){
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        int halfScrHeight = (int)(dimension.getHeight()/2);
        int yOrigin = halfScrHeight-(height/2);
        return yOrigin;
    }
    
    //returns JComboBox Object with given range of values
    private JComboBox<Integer> getComboBox(int start ,int end, int curValue){  
        Vector <Integer>values = new Vector<>();
        for (int i = start; i <= end; i++)values.add(i);

        JComboBox <Integer>jComboBox=new JComboBox<>(values);
        jComboBox.setSelectedItem(curValue);
        return jComboBox;
    }

    //returns JTextField Object with given text,color and border
    private JTextField getTextField(String text,Color color, Border border){
        JTextField newTextField = new JTextField();
        newTextField.setText(text);
        newTextField.setOpaque(true);
        newTextField.setBorder(border);
        newTextField.setFont(new Font("PlayFair",Font.BOLD,15));
        newTextField.setBackground(color);
        newTextField.setForeground(Color.black);
        newTextField.setVisible(true);
        newTextField.setHorizontalAlignment(JTextField.LEFT);
        return newTextField;
    }

    //returns JLabel Object with given text,color and border
    private JLabel getLabel(String text,Color color, Border border){
        JLabel newLabel = new JLabel();
        newLabel.setText(text);
        newLabel.setOpaque(true);
        newLabel.setBorder(border);
        newLabel.setFont(new Font("PlayFair",Font.BOLD,15));
        newLabel.setBackground(color);
        newLabel.setForeground(Color.black);
        newLabel.setHorizontalAlignment(JTextField.CENTER);	
        newLabel.setVerticalAlignment(JTextField.CENTER);
        newLabel.setVisible(true);
        return newLabel;
    }

    //returns JButton Object with given text,color,border and size
    private JButton getButton(String text, Color color, Border border, int size){
        JButton newButton = getButton(color, border);
        newButton.setText(text);
        newButton.setFont(new Font("PlayFair",Font.BOLD,size));
        return newButton;
    }

    //returns JButton Object with given color and border
    private JButton getButton(Color color, Border border){
        JButton newButton = new JButton();
        newButton.setBackground(color);
        newButton.setBorder(border);
        newButton.setVisible(true);
        newButton.addActionListener(this);
        return newButton;
    }

    //returns JPanel Object with given row,col,hGap,vGap,color,border
    private JPanel getPanel(int row, int col, int hGap, int vGap, Color color, Border border){
        JPanel newPanel = new JPanel();
        newPanel.setLayout(new GridLayout(row,col,hGap,vGap));
        newPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        newPanel.setBackground(color);
        newPanel.setBorder(border);
        newPanel.setVisible(true);
        return newPanel;
    }

    //returns addPanel
    private JPanel getAddPanel(){
        Border blackBorder = BorderFactory.createLineBorder(Color.black);
        Border emptyBorder = BorderFactory.createEmptyBorder();
        
        JPanel mainPanel = getPanel(6, 1, 10, 10, Color.yellow, blackBorder);
        mainPanel.setBackground(Color.yellow);

        JPanel datePanel = getPanel(1, 2, 10, 10, Color.yellow, emptyBorder);
        datePanel.add(getLabel("DATE", Color.orange, blackBorder));
        JPanel dateInput = getPanel(1,3,10,10,Color.yellow,blackBorder);
        Calendar now = Calendar.getInstance();
        dateInputDay = getComboBox(1,31,now.get(Calendar.DATE)); 
        dateInputMonth = getComboBox(1,12,now.get(Calendar.MONTH)+1);
        dateInputYear = getComboBox(1950,2050,now.get(Calendar.YEAR));
        dateInput.add(dateInputDay);
        dateInput.add(dateInputMonth);
        dateInput.add(dateInputYear);
        datePanel.add(dateInput);

        JPanel itemPanel = getPanel(1, 2, 10, 10, Color.yellow, emptyBorder);
        itemInput = getTextField("", Color.white, blackBorder);
        itemPanel.add(getLabel("ITEM", Color.orange, blackBorder));
        itemPanel.add(itemInput);

        JPanel commentPanel = getPanel(1, 2, 10, 10, Color.yellow, emptyBorder);
        commentInput = getTextField("", Color.white, blackBorder);
        commentPanel.add(getLabel("COMMENT", Color.orange, blackBorder));
        commentPanel.add(commentInput);

        JPanel costPanel = getPanel(1, 2, 10, 10, Color.yellow, emptyBorder);
        costInput = getTextField("", Color.white, blackBorder);
        costPanel.add(getLabel("COST", Color.orange, blackBorder));
        costPanel.add(costInput);

        addButton = getButton("ADD",Color.red,blackBorder,15);

        mainPanel.add(getLabel("ADD ITEM",Color.white,blackBorder));
        mainPanel.add(datePanel);
        mainPanel.add(itemPanel);
        mainPanel.add(commentPanel);
        mainPanel.add(costPanel);
        mainPanel.add(addButton);

        return mainPanel;
    }
    
    //returns addPanel
    private JPanel getCostPanel(){
        Border blackBorder = BorderFactory.createLineBorder(Color.black);
        Border emptyBorder = BorderFactory.createEmptyBorder();
        
        JPanel mainPanel = getPanel(7, 1, 10, 10, Color.yellow, blackBorder);

        JPanel dayCostPanel = getPanel(1, 2, 10, 10, Color.yellow, emptyBorder);
        dayCostPanel.add(getLabel("COST TODAY", Color.orange, blackBorder));
        dayCostInput = getTextField("", Color.white, blackBorder); 
        dayCostPanel.add(dayCostInput);
        
        JPanel monthCostPanel = getPanel(1, 2, 10, 10, Color.yellow, emptyBorder);
        monthCostPanel.add(getLabel("COST THIS MONTH", Color.orange, blackBorder));
        monthCostInput = getTextField("", Color.white, blackBorder); 
        monthCostPanel.add(monthCostInput);
        
        JPanel yearCostPanel = getPanel(1, 2, 10, 10, Color.yellow, emptyBorder);
        yearCostPanel.add(getLabel("COST THIS YEAR", Color.orange, blackBorder));
        yearCostInput = getTextField("", Color.white, blackBorder);
        yearCostPanel.add(yearCostInput);
        
        JPanel dayAvgCostPanel = getPanel(1, 2, 10, 10, Color.yellow, emptyBorder);
        dayAvgCostPanel.add(getLabel("DAILY AVG COST", Color.orange, blackBorder));
        dayAvgCostInput = getTextField("", Color.white, blackBorder);
        dayAvgCostPanel.add(dayAvgCostInput);
        
        JPanel monthAvgCostPanel = getPanel(1, 2, 10, 10, Color.yellow, emptyBorder);
        monthAvgCostPanel.add(getLabel("MONTHLY AVG COST", Color.orange, blackBorder));
        monthAvgCostInput = getTextField("", Color.white, blackBorder);
        monthAvgCostPanel.add(monthAvgCostInput);
        
        JPanel yearAvgCostPanel = getPanel(1, 2, 10, 10, Color.yellow, emptyBorder);
        yearAvgCostPanel.add(getLabel("YEARLY AVG COST", Color.orange, blackBorder));
        yearAvgCostInput = getTextField("", Color.white, blackBorder);  
        yearAvgCostPanel.add(yearAvgCostInput);
        
        mainPanel.add(getLabel("COST DETAILS",Color.white,blackBorder));
        mainPanel.add(dayCostPanel);
        mainPanel.add(monthCostPanel);
        mainPanel.add(yearCostPanel);
        mainPanel.add(dayAvgCostPanel);
        mainPanel.add(monthAvgCostPanel);
        mainPanel.add(yearAvgCostPanel);

        return mainPanel;
    }

    //returns addPanel
    private JPanel getDisplayPanel(){
        Border blackBorder = BorderFactory.createLineBorder(Color.black);
        Border emptyBorder = BorderFactory.createEmptyBorder();
        
        JPanel mainPanel = getPanel(5, 1, 10, 10, Color.yellow, blackBorder);
        
        JPanel fromDatePanel = getPanel(1, 2, 10, 10, Color.yellow, emptyBorder);
        fromDatePanel.add(getLabel("FROM DATE", Color.orange, blackBorder));
        JPanel fromDateInput = getPanel(1,3,10,10,Color.yellow,blackBorder);
        Calendar now = Calendar.getInstance();
        fromDateInputDay = getComboBox(1,31,now.get(Calendar.DATE));
        fromDateInputMonth = getComboBox(1,12,now.get(Calendar.MONTH)+1);
        fromDateInputYear = getComboBox(1950,2050,now.get(Calendar.YEAR));  
        fromDateInput.add(fromDateInputDay);
        fromDateInput.add(fromDateInputMonth);
        fromDateInput.add(fromDateInputYear);
        fromDatePanel.add(fromDateInput);
        
        JPanel toDatePanel = getPanel(1, 2, 10, 10, Color.yellow, emptyBorder);
        toDatePanel.add(getLabel("TO DATE", Color.orange, blackBorder));
        JPanel toDateInput = getPanel(1,3,10,10,Color.yellow,blackBorder);
        toDateInputDay = getComboBox(1,31,now.get(Calendar.DATE));
        toDateInputMonth = getComboBox(1,12,now.get(Calendar.MONTH)+1);
        toDateInputYear = getComboBox(1950,2050,now.get(Calendar.YEAR));  
        toDateInput.add(toDateInputDay);
        toDateInput.add(toDateInputMonth);
        toDateInput.add(toDateInputYear);
        toDatePanel.add(toDateInput);

        displayButton = getButton("DISPLAY",Color.red, blackBorder,15);

        mainPanel.add(getLabel("DISPLAY RESULT",Color.white,blackBorder));
        mainPanel.add(fromDatePanel);
        mainPanel.add(toDatePanel);
        mainPanel.add(displayButton);

        return mainPanel;
    }

    /**
     * displays the GUI
     */
    public void display(){
        //DisplayFrame frame = new DisplayFrame();
        //frame.display(600,600,getXOrigin(600),getYOrigin(600));
        Border blackBorder = BorderFactory.createLineBorder(Color.black);
        Border emptyBorder = BorderFactory.createEmptyBorder();
        JPanel mainPanel = getPanel(1, 2, 10, 10, Color.pink, blackBorder);
        JPanel leftPanel = getPanel(3, 1, 20, 20, Color.pink, emptyBorder);
        JPanel panel1 = getAddPanel();
        JPanel panel2 = getDisplayPanel();
        JPanel panel3 = getCostPanel();
        leftPanel.add(panel1);
        leftPanel.add(panel2);
        leftPanel.add(panel3);
        mainPanel.add(leftPanel);
        mainPanel.add(new JPanel());
        //add(mainPanel);
        add(leftPanel);
        setVisible(true);
    }

    private int insertQuery(String date, String item, String comment, double cost)throws SQLException{
        String query = String.format("INSERT INTO %s VALUES('%s','%s','%s',%f)",table,date,item,comment,cost);
        return statementObject.executeUpdate(query);
    }

    private String getDate(String year, String month, String day){
        String date = year+"-"+month+"-"+day;
        return date;
    }
   
    private String[] getHeader() throws SQLException{
        String countQuery = String.format("SELECT COUNT(COLUMN_NAME) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = N'%s'",table);
        ResultSet countResult = statementObject.executeQuery(countQuery);
        countResult.next();
        int col = countResult.getInt(1);
    
        String result[] = new String[col];

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

    void costUpdate() {
        try{
            Calendar now = Calendar.getInstance();
            int day = now.get(Calendar.DATE);
            int month = now.get(Calendar.MONTH)+1;
            int year = now.get(Calendar.YEAR);
            String date = getDate(Integer.toString(year),Integer.toString(month),Integer.toString(day));
            
            String dailyCostQuery = String.format("SELECT SUM(cost) FROM %s "+
            "WHERE %s = '%s'",table,getHeader(0),date);
            
            String monthlyCostQuery = String.format("SELECT SUM(cost) FROM %s "+
            "WHERE MONTH(%s) = '%s'",table,getHeader(0),Integer.toString(month));
            
            String yearlyCostQuery = String.format("SELECT SUM(cost) FROM %s "+
            "WHERE YEAR(%s) = '%s'",table,getHeader(0),Integer.toString(year));
            
            String dailyAvgCostQuery = String.format("SELECT AVG(cost) FROM %s "+
            "WHERE %s = '%s'",table,getHeader(0),date);
        
            String monthlyAvgCostQuery = String.format("SELECT AVG(cost) FROM %s "+
            "WHERE MONTH(%s) = '%s'",table,getHeader(0),Integer.toString(month));
            
            String yearlyAvgCostQuery = String.format("SELECT AVG(cost) FROM %s "+
            "WHERE YEAR(%s) = '%s'",table,getHeader(0),Integer.toString(year));
            
            ResultSet rs = statementObject.executeQuery(dailyCostQuery);rs.next();
            dayCostInput.setText(rs.getString(1));
            rs = statementObject.executeQuery(monthlyCostQuery);rs.next();
            monthCostInput.setText(rs.getString(1));
            rs = statementObject.executeQuery(yearlyCostQuery);rs.next();
            yearCostInput.setText(rs.getString(1));

            rs = statementObject.executeQuery(dailyAvgCostQuery);rs.next();
            dayAvgCostInput.setText(rs.getString(1));
            rs = statementObject.executeQuery(monthlyAvgCostQuery);rs.next();
            monthAvgCostInput.setText(rs.getString(1));
            rs = statementObject.executeQuery(yearlyAvgCostQuery);rs.next();
            yearAvgCostInput.setText(rs.getString(1));
        }
        catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == addButton){
            try{
                addButton.setSelected(false);
                String date = getDate(dateInputYear.getSelectedItem().toString(),dateInputMonth.getSelectedItem().toString(),dateInputDay.getSelectedItem().toString());
                String item = itemInput.getText();
                String comment = commentInput.getText();
                double cost = Double.valueOf(costInput.getText());
                int result = insertQuery(date,item,comment,cost);
                costUpdate();
                if(result > 0)JOptionPane.showMessageDialog(this,"Addition successful");
            }
            catch(NumberFormatException nfe){
                JOptionPane.showMessageDialog(this,"Invalid cost input");
            }
            catch(SQLException sqle){
                JOptionPane.showMessageDialog(this,sqle.getMessage());
            }
        }
        else if(e.getSource() == displayButton){
            try{
                String fromDate = getDate(fromDateInputYear.getSelectedItem().toString(),fromDateInputMonth.getSelectedItem().toString(),fromDateInputDay.getSelectedItem().toString());
                String toDate = getDate(toDateInputYear.getSelectedItem().toString(),toDateInputMonth.getSelectedItem().toString(),toDateInputDay.getSelectedItem().toString());
                DisplayFrame frame = new DisplayFrame(this,statementObject,table,fromDate,toDate);
                int width = 600,height = 600;
                frame.display(width,height,getXOrigin(width),getYOrigin(height));
                costUpdate();
            }
            catch(SQLException sqle){
                sqle.printStackTrace();
            }
        }
    }



}
