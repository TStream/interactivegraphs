/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package query;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ian
 */
public class MDBConnect {
    private static Connection mdbConnection;
    private Map<Integer, String> answers;
    private Map<Integer, String> questions;
       
    public MDBConnect(String accessFilePath) 
    {
        //Get connection to database
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        }catch(Exception ex){
             System.out.println("Error");
             System.out.println(ex.getMessage());
        }
        try{
            mdbConnection = DriverManager.getConnection(
                    "jdbc:odbc:driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=" + accessFilePath);
            
        } catch (Exception ex) {
             ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        
        questions = GetQuestionOrAnswer("questions.csv");
        answers = GetQuestionOrAnswer("answers.csv");
    }
    
    public List<List<Object>> QueryRetrieve(String query) throws SQLException 
    {      
        Statement stmt = null;
        ResultSet rs = null;
        List<List<Object>> returnList = new ArrayList<List<Object>>();
        try 
        {   
            stmt = (Statement) mdbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY); 
            
            rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            
            if(!rs.isBeforeFirst())
                System.out.println("result set contains no rows");
          
            else
            {               
                while (rs.next()) 
                {
                    List<Object> row = new ArrayList<Object>();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++)
                        row.add(rs.getObject(i));
                    returnList.add(row);
                }
            }
            stmt.close();        
        }
        catch (SQLException e) { System.out.print(e); } 
        finally 
        { 
            if (stmt != null) { stmt.close(); }
            System.out.println(returnList.size());
            return returnList;
        }
    } 
    
    public String GetQuestion(Integer questionID)
    { 
        if (!questions.containsKey(questionID))
        {
            System.out.println(String.format("question ID %s not found", questionID));
            return String.format("Question # %s", questionID);
        }
        return questions.get(questionID);
    }
    
    public String GetAnswer(Integer answerID)
    {
        if (!answers.containsKey(answerID))
        {
            System.out.println(String.format("answer ID %s not found", answerID));
            return String.format("Answer # %s", answerID);
        }
        return answers.get(answerID);
    }
    
    private Map<Integer, String> GetQuestionOrAnswer(String filePath)
    {
        Map<Integer, String> returnList = new HashMap<Integer, String>();
        String line = "";
        
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            line = br.readLine();

            while (line != null) 
            {
                String lineIndex = line.substring(line.lastIndexOf(",") + 1);
                String lineString = line.substring(0, line.lastIndexOf(","));
                try
                {
                returnList.put(Integer.parseInt(lineIndex), lineString);
                }
                catch (Exception ex)
                { 
                    System.out.println(line);
                }
                line = br.readLine();
            }       
            br.close();
        }
        catch (Exception ex)
        {   
            System.out.println(ex.getMessage());    
        }
        
        return returnList;
    }
}
