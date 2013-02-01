/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.sql.*;

import com.chinamobile.cmpp3_0.protocol.message.*;
import com.ylear.sp.cmpp.frame.Config;

/**
 *
 * @author Administrator
 */
public class DbLog {
    private boolean flag=false;
    private String url;
    private String user;
    private String pwd;
    private final static Object lock=new Object();
    private static DbLog log;

    private DbLog(String driverName,String url,String user,String pwd){
        try {
            Class.forName(driverName);
            flag=true;
        } catch (ClassNotFoundException ex) {
            System.out.println("装载驱动器失败："+ex.getException());
        }
        this.url=url;
        this.user=user;
        this.pwd=pwd;

    }

    /*public static DbLog getLog(){
        if(log==null){
            synchronized (lock) {
                String driverName = Config.getInstance().mysqlDriver;
                String url = Config.getInstance().mysqlUrl;
                String user = Config.getInstance().mysqlUser;
                String pwd = Config.getInstance().mysqlPasswd;
                if (log == null) {
                    log = new DbLog(driverName, url, user, pwd);
                }
            }
        }
        return log;

    }*/

    public boolean insertTemp(String from,String to,String msg,String param,long recvtime){
        boolean bret=false;
        if(!flag)
            return bret;
        Connection conn=null;
        PreparedStatement psm=null;
        try{
            conn=DriverManager.getConnection(url, user, pwd);
            psm = conn.prepareStatement("insert into sms.temp values(null,?,?,?,?,?)");
            psm.setString(1, from);//from
            psm.setString(2, to);//to
            psm.setString(3, msg);//msg
            psm.setString(4,param);//param
            psm.setTimestamp(5, new java.sql.Timestamp(recvtime));
            int updateCount = psm.executeUpdate();
            if (updateCount > 0) {
                bret=true;
            }
            psm.close();
            conn.close();
            psm=null;
            conn=null;
            
        }catch(SQLException ex){
            ex.printStackTrace(System.out);
        }
        finally{
            try{
                if(psm!=null){
                    psm.close();
                }
                if(conn!=null){
                    conn.close();
                }

            }catch(SQLException ex){
                ex.printStackTrace(System.out);
            }
        }
        return bret;
    }

    public boolean insertMO(DeliverMessage dm){
        boolean bret=false;
        if(!flag)
            return bret;
         Connection conn=null;
        PreparedStatement psm=null;
        try{
            conn=DriverManager.getConnection(url, user, pwd);
            psm = conn.prepareStatement("insert into sms.mo_log values(?,?,?,?,?,?)");
            psm.setString(1,dm.getDeliver().MsgID);//msgdi
            psm.setString(2,dm.getDeliver().LinkID);//linkid
            psm.setString(3,dm.getDeliver().SrcTermID);//from
            psm.setString(4,dm.getDeliver().DestTermID);//to
            psm.setString(5,dm.getDeliver().MsgContent);//msg
            psm.setTimestamp(6, new java.sql.Timestamp(dm.getTimeStamp()));//recvtime
            int updateCount = psm.executeUpdate();
            if (updateCount > 0) {
                bret=true;
            }
            psm.close();
            conn.close();
            psm=null;
            conn=null;

        }catch(SQLException ex){
            ex.printStackTrace(System.out);
        }finally{
            try{
                if(psm!=null){
                    psm.close();
                }
                if(conn!=null){
                    conn.close();
                }

            }catch(SQLException ex){
                ex.printStackTrace(System.out);
            }
        }
        return bret;
    }
    public boolean insertMT(SubmitMessage sm,SubmitRespMessage srm){
        boolean bret=false;
        if(!flag)
            return bret;
         Connection conn=null;
        PreparedStatement psm=null;
        try{
            conn=DriverManager.getConnection(url, user, pwd);
            psm = conn.prepareStatement("insert into sms.mt_log values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            psm.setString(1,srm.getMsgId());//msgid
            psm.setString(2,sm.getSubmit().linkID);//linkid
            psm.setString(3,sm.getSubmit().srcTermID);//from
            psm.setString(4,sm.getSubmit().destTermID[0]);//to
            psm.setInt(5,sm.getSubmit().msgFmt);//msgformat
            psm.setInt(6,sm.getSubmit().msgLength);//msglength
            psm.setString(7,sm.getSubmit().getMsgContent());//msg
            psm.setString(8,sm.getSubmit().feeType);//feetype
            psm.setString(9,sm.getSubmit().feeCode);//feecode
            psm.setString(10,sm.getSubmit().serviceID);//serviceid
            psm.setInt(11,srm.getStatus());//status
            psm.setString(12,"");//stat
            psm.setString(13,"");//err
            psm.setInt(14,sm.getTryTimes());//trytimes
            psm.setTimestamp(15, new java.sql.Timestamp(sm.getTimeStamp()));//recvtime
            int updateCount = psm.executeUpdate();
            if (updateCount > 0) {
                bret=true;
            }
            psm.close();
            conn.close();
            psm=null;
            conn=null;

        }catch(SQLException ex){
            ex.printStackTrace(System.out);
        }finally{
            try{
                if(psm!=null){
                    psm.close();
                }
                if(conn!=null){
                    conn.close();
                }

            }catch(SQLException ex){
                ex.printStackTrace(System.out);
            }
        }
        return bret;
    }

    public boolean updateMT(String msgid,String stat,String err){
        boolean bret=false;
        if(!flag)
            return bret;
         Connection conn=null;
        PreparedStatement psm=null;
        try{
            conn=DriverManager.getConnection(url, user, pwd);
            psm = conn.prepareStatement("update sms.mt_log set stat=?,err=?  where msgid=?");
            psm.setString(1,stat);//stat
            psm.setString(2,err);//err
            psm.setString(3,msgid);//msgid
            int updateCount = psm.executeUpdate();
            if (updateCount > 0) {
                bret=true;
            }
            psm.close();
            conn.close();
            psm=null;
            conn=null;

        }catch(SQLException ex){
            ex.printStackTrace(System.out);
        }finally{
            try{
                if(psm!=null){
                    psm.close();
                }
                if(conn!=null){
                    conn.close();
                }

            }catch(SQLException ex){
                ex.printStackTrace(System.out);
            }
        }
        return bret;
    }

    public static void main(String[] args){
        
      
    }
   

}
