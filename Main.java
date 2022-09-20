import src.MainFrame;
import java.sql.*;
import java.util.Arrays;

public class Main{
    static final String url = "jdbc:mysql://localhost:3306/Sampanna";
    static final String user = "root";
    static final String password = "Kashi16sadan@";
    public static void main(String[] args) throws Exception{
        MainFrame frame = new MainFrame();
        frame.display();
        
        /*Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url,user,password);
        
        Statement st = con.createStatement();
        String table = "expense";
        String countQuery = String.format("SELECT COUNT(*) FROM %s",table);
        ResultSet countResult = st.executeQuery(countQuery);
        countResult.next();

        int row = countResult.getInt(1);
        int col = 4;
        Object result[][] = new Object[row][col];

        String resultQuery = String.format("SELECT * FROM %s",table);
        ResultSet resultSet = st.executeQuery(resultQuery);
        System.out.println(row);
        int index = 0;
        while(resultSet.next()){
            for(int i = 0; i < col; i++){
                result[index][i] = resultSet.getString(i+1);
            }
            index++;
        }
        for(Object []arr:result)
            System.out.println(Arrays.toString(arr));
        st.close();
        con.close();*/
    }
}