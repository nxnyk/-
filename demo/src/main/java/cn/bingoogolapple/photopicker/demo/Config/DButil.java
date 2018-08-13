package cn.bingoogolapple.photopicker.demo.Config;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by yinyu on 2018/5/12.
 */

public class DButil {

    final String home_IP = "192.168.1.100:3306";
    final String Sqlurl = "jdbc:mysql://" + home_IP + "/youershou";
    final String User = "root";
    final String Password = "960528";

    public Connection GetConnection() throws Exception
    {


            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(Sqlurl,User,Password);
            return con;
    }
    public void Closecon(Connection con) throws Exception
    {

        if(con!=null)
        {
            con.close();
        }


}
}
