import src.MainFrame;
import java.sql.*;
import java.util.Arrays;
import java.util.Random;

public class Main{
    public static void main(String[] args) throws Exception{
        MainFrame frame = new MainFrame();
        
        /*DatabaseOperation db = new DatabaseOperation();
        for(int i = 0; i < 1000; i++)
        db.insert(db.getRandomDay(),db.getRandomMonth(), db.getRandomYear(2015,2022), db.getRandomItem(), "", db.getRandomInt(1, 1000));
        */
    }
}


class DatabaseOperation{
    private String url;
    private String user;
    private String password;

    private static String item[] = {"watch","wallet","Idli","Vada","Chapathi","Meals","Apple","Mango","Banana","pineapple","chicken","mutton","egg rice","Vada pav","Pav bajji","samosa","poori","dosa","panner manchuri","paratha","mobile","cards","keyboard","mouse","monitor","chair","fan","laptop","sofa","dinning table","wardrobe","mirror","show piece","egg"};
    
    static{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(Exception e){
            e.printStackTrace();
            System.exit(-1);//connection with driver unsuccessfull
        }
    }
    DatabaseOperation(){
        this.url = "jdbc:mysql://localhost:3306/Sampanna";
        this.user = "root";
        this.password = "Kashi16sadan@";
    }

    DatabaseOperation(String url, String user, String password){
        this.url = url;
        this.user = user;
        this.password = password; 
    }

    public void insert(int day, int month, int year, String item, String comment, double cost) throws SQLException{
        Connection con = DriverManager.getConnection(url,user,password);
        
        Statement st = con.createStatement();
        String table = "expense";
        String date = Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(day);
        String query = String.format("INSERT IGNORE INTO %s VALUES ('%s','%s','%s','%f')",table,date,item,comment,cost);
        int result = st.executeUpdate(query);

        st.close();
        con.close();
    
    }

    public static int getRandomInt(int from, int to){
        int randomValue = (int)(Math.random()*(to-from+1)+from);   
        return randomValue;
    }

    public static String getRandomItem(){
        return item[getRandomInt(0,item.length-1)];
    }

    public static int getRandomDay(){
        return getRandomInt(1, 31);
    }

    public static int getRandomMonth(){
        return getRandomInt(1,12);
    }

    public static int getRandomYear(int from, int to){
        return getRandomInt(from, to);
    }
}