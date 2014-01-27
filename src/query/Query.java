/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package query;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import query.QueryBuilder.SelectOperations;

/**
 *
 * @author Ian
 */
public class Query {

     private MDBConnect connection = new MDBConnect(".\\src\\query\\myDatabase.mdb");
     
     private QueryBuilder qb;

     public static void main(String[] args) throws SQLException {
     
      
     }

     public Map<String, Map<String, Integer>> playerAge(Map<String, List<String>> filters){
          //ALL PLAYER DETAILS
          QueryBuilder playerQb = new QueryBuilder();
          playerQb.addSelect("UserId", "us", SelectOperations.none);
          playerQb.addSelect("Gender", "us", SelectOperations.none);
          playerQb.addSelect("AgeRange", "us", SelectOperations.none);
          
          playerQb.addTable("Users", "us");
          
          if(filters != null)
               addUserFilter(filters,"us","UserId",playerQb);
          
          String s = playerQb.getQuery();
          
          List<List<Object>> result = null;
          try {
               result = connection.QueryRetrieve(s);
          } catch (Exception e) {
               System.out.println(e.getMessage());
          }
          //System.out.println("\n" + result);
          
          Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();
          Calendar today = Calendar.getInstance();
          
          for (List l : result) 
          {     
               Calendar compare = Calendar.getInstance();
               if (l.get(2) == null)
                   continue;
               
              String name = null;
              try 
              {
                  String dateToParse = l.get(2).toString();
                  Date comp = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH).parse(dateToParse);
                  compare.setTime(comp);
                  
                  int yearSplit = today.get(Calendar.YEAR) - compare.get(Calendar.YEAR);
                  
                  int[] ageRange = new int[] {14, 16, 18, 21, 25, 30, 35, 40}; 

                  for (int i = 0; i < ageRange.length - 1; i++)
                  {
                    if (yearSplit <= ageRange[0])
                    {
                        name = String.format("0. Under %d", ageRange[0]);
                        break;
                    }
                    else if (yearSplit > ageRange[ageRange.length - 1])
                    {
                        name = String.format("%d. Over %d", ageRange.length - 1, ageRange[ageRange.length - 1]);
                        break;
                    }
                    else if (yearSplit > ageRange[i] && yearSplit <= ageRange[i + 1])
                    {
                        name = String.format("%d. Between %d and %d", i + 1, ageRange[i], ageRange[i + 1]); 
                        break;
                    }   
                  }
              } 
              catch (ParseException ex)  { continue; }

               Map<String, Integer> values = null;
               if (map.containsKey(name)) 
               {
                    values = map.get(name);
                    
                    if (l.get(1) != null && values.containsKey(l.get(1).toString()))
                        values.put(l.get(1).toString(), values.get(l.get(1).toString()) + 1);
                    else if (l.get(1) != null)
                        values.put(l.get(1).toString(), 1);        
               } 
               else 
               {
                    values = new HashMap<String, Integer>();
                    if (l.get(1) != null)
                        values.put(l.get(1).toString(), 1);
               }
               map.put(name, values);
          }
          return map;
     }
     //Date and player who played
     public Map<String, List<String>>  datePlayer(Map<String, List<String>> filters) {
          QueryBuilder Qb = new QueryBuilder();
          Qb.addSelect("StartTime", "rd", SelectOperations.none);
          Qb.addSelect("PlayerId", "rd", SelectOperations.none);         
          Qb.addTable("Recordings", "rd"); 
          
           if(filters != null)
               for (String key : filters.keySet()) {
                   if (key.equals("Gender")){
                       addUserFilter(filters,"rd","PlayerId",Qb);
                   }
                   else{
                       addQuestionFilter(filters,"rd","PlayerId",Qb);
                   }
               }
               
           
          String s = Qb.getQuery();
          List<List<Object>> result = null;
          try {
               result = connection.QueryRetrieve(s);
          } catch (Exception e) {
               System.out.println(e.getMessage());
          }

          Map<String, List<String>> map = new HashMap<String, List<String>>();
          for (List l : result) {
              String date = l.get(0).toString();
              
               List<String> extend = new LinkedList<String>();
               
               if (map.containsKey(date.split(" ")[0]))
               {
                   extend = map.get(date.split(" ")[0]);
                   if (l.get(1) != null)
                        extend.add(l.get(1).toString());
               }
               else
               {
                   if (l.get(1) != null)
                        extend.add(l.get(1).toString());
                   map.put(date.split(" ")[0], extend);
               }
          } 
          return map;
     }

     public Map<String, Map<String, Integer>> correct(Map<String, List<String>> filters) {
          //ALL CORRECT ANSWERS 
         
          qb = new QueryBuilder();

          qb.addSelect("QuestionId", "rd", SelectOperations.none);
          qb.addSelect("Collision1", "rd", SelectOperations.none);
          qb.addSelect("Collision2", "rd", SelectOperations.none);
          qb.addSelect("Collision3", "rd", SelectOperations.none);
          
          qb.addTable("RecordingAnswers", "rd");
          
          qb.addOrderBy("QuestionId", "rd", "desc");
          qb.addOrderBy("Collision1", "rd", "desc");
          qb.addOrderBy("Collision2", "rd", "desc");
          qb.addOrderBy("Collision3", "rd", "desc");
          
          if(filters != null)
               addResultFilter(filters,"rd","RecordingId",qb);

          String s = qb.getQuery();
          
          List<List<Object>> result = null;
          try {
               result = connection.QueryRetrieve(s);
          } catch (Exception e) {
               System.out.println(e.getMessage());
          }
         
          //Total the Results for each question
          Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();
          for (List l : result) {
               String question = connection.GetQuestion(Integer.parseInt(l.get(0).toString()));
               Map<String, Integer> extend;
               if (map.containsKey(question)) {
                    extend = map.get(question);       
                    CorrectSub(l,extend);
               } 
               else 
               {
                    extend = new HashMap<String, Integer>();
                    CorrectSub(l, extend);
               } 
               map.put(question, extend);
          }
          
          return map;
     }
     
     private void CorrectSub(List<Object> checks, Map<String, Integer> questionID)
     {                   
        if (questionID.containsKey("Total"))
            questionID.put("Total", questionID.get("Total") + 1);
        else
            questionID.put("Total", 1);
         
         for (int i = 1; i < checks.size(); i++)
         {  
             if (checks.get(i) != null)
             {
                 String comp = String.format("Wrong %s time%s", i, i > 1 ? "s" : "");
                 if (questionID.containsKey(comp))
                     questionID.put(comp, questionID.get(comp) + 1);
                 else
                     questionID.put(comp, 1);
             }      
         }
     }

     public Map<String, Integer> answered(Map<String, List<String>> filters) {
          QueryBuilder qb = new QueryBuilder();
          qb.addSelect("QuestionId", "ra", SelectOperations.none);
          qb.addSelect("PlayerId", "ra", SelectOperations.none);
          qb.addTable("Recordings", "ra");
          qb.addOrderBy("QuestionId", "ra", "asc");
          
          if(filters != null)
               addQuestionFilter(filters,"ra","PlayerId",qb);

          String s = qb.getQuery();

          List<List<Object>> result = null;
          try {
               result = connection.QueryRetrieve(s);
          } catch (Exception e) {
               System.out.println(e.getMessage());
          }

          //Total the results for each question
          Map<String, Integer> map = new HashMap<String, Integer>();
          for (List l : result) {
               String i = l.get(0).toString();
               if (map.containsKey(i)) {
                    map.put(i, map.get(i) + 1);
               } else {
                    map.put(i, 1);
               }
          }

          return map;
     }
     
     /*users questions*/
     public List<List<Object>> totalPlayers(Map<String, List<String>> filters) //how many users have played the game
     {
         
         qb = new QueryBuilder();
         qb.addSelect("UserId", "use", SelectOperations.count);
         qb.addTable("users", "use");

         if(filters != null){
           for (String key : filters.keySet()) {
               List<String> values = filters.get(key);
               if (values != null) {
                    qb.AddWhere("use", key, values.get(0), QueryBuilder.Operators.Equals);
                    for (int i = 1; i < values.size(); i++) {
                        if (values.get(i).equals("null")){
                            qb.AddWhereOr();
                            qb.AddWhere("use", key, values.get(i), QueryBuilder.Operators.IsNull);
                        }
                        else if (values.get(i).equals("notnull")){
                            qb.AddWhereOr();
                            qb.AddWhere("use", key, values.get(i), QueryBuilder.Operators.NotNull);
                        }
                        else {
                            qb.AddWhereOr();
                            qb.AddWhere("use", key, values.get(i), QueryBuilder.Operators.Equals);
                        }
                         
                    }
               }
           }
         }
         
          String s = qb.getQuery();

        List<List<Object>> result = null;
        try {
             result = connection.QueryRetrieve(s);
        } catch (Exception e) {
             System.out.println(e.getMessage());
             return null;
        }
        
        //System.out.println("\n"+result);
        return result;
     }
      public List<List<Object>> playFrequency(Map<String, List<String>> filters) //how often do users play the game
     {
         /* how to read the data that this prints out:
          * example: [5, 4]
          * is read as: PLayerId 5, played the game 4 times 
          */
         qb = new QueryBuilder();
 
         qb.addSelect("PlayerId", "r", SelectOperations.distinct);
         qb.addSelect("PlayerId", "r", SelectOperations.count);
         qb.addTable("Recordings", "r");
         
         if(filters != null)
               for (String key : filters.keySet()) {
                   if (key.equals("Gender")){
                       addUserFilter(filters,"r","PlayerId",qb);
                   }
                   else{
                       addQuestionFilter(filters,"r","PlayerId",qb);
                   }
               }

          String s = qb.getQuery();

        List<List<Object>> result = null;
        try {
             result = connection.QueryRetrieve(s);
        } catch (Exception e) {
             System.out.println(e.getMessage());
             
        }
        return result;
     }
     
     
     public List<Integer> playerGenderCount(Map<String, List<String>> filters) //what are the demographics of the players
     {
        qb = new QueryBuilder();
         
        qb.addSelect("Gender", "g", SelectOperations.none);
        qb.addTable("users", "g");
        qb.AddWhere("g", "Gender", "", QueryBuilder.Operators.NotNull);
        
        if(filters != null)
               addUserFilter(filters,"g","UserId",qb);
        
        String s = qb.getQuery();

        List<List<Object>> result = null;
        try {
             result = connection.QueryRetrieve(s);
        } catch (Exception e) {
             System.out.println(e.getMessage());
             return null;
        }
        if(result == null) return null;
        
        List<Integer> countg = new ArrayList<Integer>();

        int m = 0, f = 0, u = 0;
        for(int i = 0; i < result.size(); i++)
        {
            if(result.get(i).size() > 0)
            {
                if(result.get(i).get(0).toString().equals("female"))
                {
                    f++;
                }else if(result.get(i).get(0).toString().equals("male"))
                {
                    m++;
                }else
                {
                    u++;
                }
            }
        }
        
        countg.add(m); //males
        countg.add(f); //females
        countg.add(u); //unavailable that has a birthdate
        
        //System.out.println(countg.get(0));
        
        return countg;
     }
     
     public Map<String, Integer> playerLocales(Map<String, List<String>> filters)
     {
         qb = new QueryBuilder();
         
        qb.addSelect("Locale", "g", SelectOperations.none);
        qb.addTable("users", "g");
        qb.AddWhere("g", "Locale", "", QueryBuilder.Operators.NotNull);
        
        if(filters != null)
               addUserFilter(filters,"g","UserId",qb);
        
        String s = qb.getQuery();

        List<List<Object>> result = null;
        try {
             result = connection.QueryRetrieve(s);
        } catch (Exception e) {
             System.out.println(e.getMessage());
             return null;
        }
        //System.out.println("A"+result.get(0));
        if(result == null) return null;
        
        //System.out.println("A"+result.get(0));
        
        return null;
        
     }
     
     public List<List<Object>> agkCorrelation(Map<String, List<String>> filters) //is there any correlation between selected avatar, gender, knowledge (per-round score or lifetime score)
     {
         
         /* how to read the data that this prints out:
          * example: [1, Sport1, female, 12494.0, 79190.0, 18031.0, 7.5]
          * is read as: PlayerId chose The avatart Sport1, is a female, avgerage score per game, lifetime score, high score, average collisions 
          * 
          */
         
         qb = new QueryBuilder();
         qb.addSelect("PlayerId", "up", SelectOperations.distinct);
         qb.addSelect("AvatarName", "up", SelectOperations.max);
         qb.addSelect("Gender", "upp", SelectOperations.max);
         //qb.addSelect("StartTime", "up", SelectOperations.none);
         //qb.addSelect("FinishTime", "up", SelectOperations.none);
         qb.addSelect("Score", "up", SelectOperations.average);
         qb.addSelect("LifetimeScore", "upp", SelectOperations.none);
         qb.addSelect("HighScore", "upp", SelectOperations.none);  
         qb.addSelect("Collisions", "up", SelectOperations.average);
         qb.addTable("Recordings", "up");
         qb.addTable("users", "upp");
         qb.AddJoin("up", "PlayerId","upp", "UserId", QueryBuilder.JoinTypes.inner);
         qb.AddWhere("up", "FinishTime", "", QueryBuilder.Operators.NotNull);
         
         if(filters != null){
           for (String key : filters.keySet()) {
               List<String> values = filters.get(key);
               if (values != null) {
                    qb.AddWhere("upp", key, values.get(0), QueryBuilder.Operators.Equals);
                    for (int i = 1; i < values.size(); i++) {
                        if (values.get(i).equals("null")){
                            qb.AddWhereOr();
                            qb.AddWhere("upp", key, values.get(i), QueryBuilder.Operators.IsNull);
                        }
                        else if (values.get(i).equals("notnull")){
                            qb.AddWhereOr();
                            qb.AddWhere("upp", key, values.get(i), QueryBuilder.Operators.NotNull);
                        }
                        else {
                            qb.AddWhereOr();
                            qb.AddWhere("upp", key, values.get(i), QueryBuilder.Operators.Equals);
                        }
                         
                    }
               }
           }
         }

          String s = qb.getQuery();

        List<List<Object>> result = null;
        try {
             result = connection.QueryRetrieve(s);
        } catch (Exception e) {
             System.out.println(e.getMessage());
             
        }
        //System.out.println("\n" + result);
        return result;
     }
         
         
     
     /*End users questions*/
     /*Knowledge questions*/
     public List<List<Object>> trafficKnowledge(Map<String, List<String>> filters) //how well do users know traffic rules and conditions and can we establish a baseline
     {
         /* how to read the data that this prints out:
          * example: [1418, 245] is read as: questionID 1418 was awnsered correctly on the first try 245 times
          * also it is sorted so that the first one is the easiest question and the last one is the hardest
          */
         
         qb = new QueryBuilder();
         qb.addSelect("QuestionId", "w", SelectOperations.none);
            
         qb.addSelect("QuestionId", "w", SelectOperations.count);
         
         qb.addTable("RecordingAnswers", "w");
         qb.AddWhere("w", "Collision1", "", QueryBuilder.Operators.IsNull);
         
         qb.addOrderBy("count(w.QuestionId)", "w", "desc");
         
         if(filters != null)
               addResultFilter(filters,"w","RecordingId",qb);
        
          String s = qb.getQuery();

        List<List<Object>> result = null;
        try {
             result = connection.QueryRetrieve(s);
        } catch (Exception e) {
             System.out.println(e.getMessage());
             
        }
        //System.out.println("\n"+result);
        return result;
     }
     
     public List<List<Object>> difficultCategories(Map<String, List<String>> filters) //are there certain categories of questions that all users find difficult, or some demographic finds difficult
     {
         
         /* how to read the data that this prints out:
          * example: [1110,91,28,8] is read as: for questionID 1110, there was 91 Collision1's,28 Collision2's,and 8 Collision3's
          */

         qb = new QueryBuilder();
       
         
         qb.addSelect("QuestionId", "w", SelectOperations.none);
         qb.addSelect("Collision1", "w", SelectOperations.count);
         qb.addSelect("Collision2", "w", SelectOperations.count);
         qb.addSelect("Collision3", "w", SelectOperations.count);    
         qb.addTable("recordingAnswers", "w");

         qb.AddWhere("w", "Collision1", "", QueryBuilder.Operators.NotNull);
         qb.AddWhereOr();
         qb.AddWhere("w", "Collision2", "", QueryBuilder.Operators.NotNull);
         qb.AddWhereOr();
         qb.AddWhere("w", "Collision3", "", QueryBuilder.Operators.NotNull);
         qb.addOrderBy("QuestionId", "w", "asc");
         //qb.addOrderBy("count(w.QuestionId)", "w", "desc");
         //qb.addOrderBy("Collision1", "w", "asc");
         
         if(filters != null)
               addResultFilter(filters,"w","RecordingId",qb);
         
          String s = qb.getQuery();

        List<List<Object>> result = null;
        try {
             result = connection.QueryRetrieve(s);
        } catch (Exception e) {
             System.out.println(e.getMessage());
             
        }
        //System.out.println("\n" +command +"\n" + result);
        
        return result;
        
     }             
     /*End Knowledge questions*/
     /*Game Content Questions*/
     public List<List<Object>> userPerformance(Map<String, List<String>> filters) //user performance on question by question basis
     {
          /* how to read the data that this prints out:
          * example:[4, 9/8/2013 11:01:55 AM, 09/08/2013 11:03:57, 11477.0, 111944.0, 16977.0, 7.5, 5]
          * is read as: PlayerId, max start time, max finish time, average score per game, Lifetime Score, High Score, average number of collisions, highest longest streak
          * 
          * problems:
          * StartTime and FinishTime are in different formats
          */
         qb = new QueryBuilder();
         qb.addSelect("PlayerId", "up", SelectOperations.distinct);
         qb.addSelect("StartTime", "up", SelectOperations.min);
         qb.addSelect("FinishTime", "up", SelectOperations.min);
         //qb.addSelect("StartTime", "up", SelectOperations.none);
         //qb.addSelect("FinishTime", "up", SelectOperations.none);
         qb.addSelect("Score", "up", SelectOperations.average);
         qb.addSelect("LifetimeScore", "upp", SelectOperations.none);
         qb.addSelect("HighScore", "upp", SelectOperations.none);  
         qb.addSelect("Collisions", "up", SelectOperations.average);
         qb.addSelect("LongestStreak", "up", SelectOperations.min);
         qb.addSelect("Gender", "upp", SelectOperations.none);  
         qb.addTable("Recordings", "up");
         qb.addTable("users", "upp");
         qb.AddJoin("up", "PlayerId","upp", "UserId", " inner join ");
         qb.AddWhere("up", "FinishTime", "", QueryBuilder.Operators.NotNull);
         
         
          String s = qb.getQuery();

        List<List<Object>> result = null;
        try {
             result = connection.QueryRetrieve(s);
        } catch (Exception e) {
             System.out.println(e.getMessage());
             
        }
        return result;
        //System.out.println("\n" + result);
         
     }
     
     public void consistentlyWrong(Map<String, List<String>> filters) //questions people are consistantly getting wrong
     {
         
         /* how to read the data that this prints out:
          * example 1: [1110,1112,null,null,45] is read as: for questionID 1110, 45 people choose option 1112 than got it right
          * example 2: [1110,1113,1112,null,1] is read as: for questionID 1110, 1 person choose option 1113, than 1112 than got it right
          * example 3: [1110,1113,1114,1112,1] is read as: for questionID 1110, 1 person choose option 1113,1114,1112 than got it right
          */
         qb = new QueryBuilder();
         qb.addSelect("QuestionId", "w", SelectOperations.none);
         qb.addSelect("Collision1", "w", SelectOperations.none);
         qb.addSelect("Collision2", "w", SelectOperations.none);
         qb.addSelect("Collision3", "w", SelectOperations.none);

         qb.addSelect("Collision1", "w", SelectOperations.count);

         qb.addTable("RecordingAnswers", "w");
         qb.AddWhere("w", "Collision1", "", QueryBuilder.Operators.NotNull);
         qb.addOrderBy("QuestionId", "w", "asc");
         
         if(filters != null)
               addResultFilter(filters,"w","RecordingId",qb);
         
          String s = qb.getQuery();

        List<List<Object>> result = null;
        try {
             result = connection.QueryRetrieve(s);
        } catch (Exception e) {
             System.out.println(e.getMessage());
             
        }
        //System.out.println(result);
     }
     
     public Map<String, Integer> questionAnswerTime(Map<String, List<String>> filters) //questions taking more time to answer than others?
     {  
          /* how to read the data that this prints out:
          * example:[1110, 1341.262910798122] is read as: the average time to complete questionID 1110 is 1341.26 milisec
          */
         
         qb = new QueryBuilder();
         qb.addSelect("QuestionId", "use", SelectOperations.distinct);  
         qb.addSelect("Seconds", "use", SelectOperations.average);
         qb.addTable("RecordingAnswers", "use");
         qb.AddWhere("use", "QuestionId", "", QueryBuilder.Operators.NotNull);
         qb.AddWhere("use", "Seconds", "0", QueryBuilder.Operators.Greater);
         
         if(filters != null)
               addResultFilter(filters,"use","RecordingId",qb);

          String s = qb.getQuery();

        List<List<Object>> result = null;
        try {
             result = connection.QueryRetrieve(s);
        } catch (Exception e) {
             System.out.println(e.getMessage());
             
        }
        Map<String, Integer> formattedResult = new HashMap<String, Integer>();
        for (List l : result)
        {
            if(l.size() < 2) continue;
            formattedResult.put(l.get(0).toString(), new Double(l.get(1).toString()).intValue());
        }
        return formattedResult;
        //System.out.println("\n"+result);
     }
     /*End game content questions*/
     
     public void females(){///Just used for testing 
         qb = new QueryBuilder();
         
        qb.addSelect("UserId", "g", SelectOperations.distinct);
        //qb.addSelect("UserId", "g", SelectOperations.countDistinct);
        qb.addSelect("Gender", "g", SelectOperations.none);
        qb.addTable("users", "g");
        qb.AddWhere("g", "Gender", "", QueryBuilder.Operators.NotNull);
        qb.AddWhere("g", "UserId", "", QueryBuilder.Operators.NotNull);
        qb.addOrderBy("UserId", "g", "asc");
        //qb.AddWhereOr();
                
        //qb.AddWhere("g", "Gender", "female", QueryBuilder.Operators.Equals);
        
        String s = qb.getQuery();

        List<List<Object>> result = null;
        try {
             result = connection.QueryRetrieve(s);
        } catch (Exception e) {
             System.out.println(e.getMessage());
             
        }
        //System.out.println("\n" +result);
        
        
     }
     
     public String getQuestion(int questionID)
     {
         String ques = connection.GetQuestion(questionID);
         
         return ques;
     }
     
     
     
     public Map<String, List<String>> playerQuestion(Map<String, List<String>> filters) {
          QueryBuilder Qb = new QueryBuilder();
          Qb.addSelect("PlayerId", "us", SelectOperations.none);
          Qb.addSelect("QuetionId", "us", SelectOperations.none);

          Qb.addTable("Recordings", "us");
          
          String s = Qb.getQuery();

          List<List<Object>> result = null;
          try {
               result = connection.QueryRetrieve(s);
          } catch (Exception e) {
               System.out.println(e.getMessage());
          }

          Map<String, List<String>> map = new HashMap<String, List<String>>();
          
          for (List l : result) {
	    String question = l.get(0).toString();

               List<String> extend = new LinkedList<String>();

               if (map.containsKey(question)) {
                    extend = map.get(question);
                    if (l.get(1) != null) {
                         extend.add(l.get(1).toString());
                    }
               } else {
                    if (l.get(1) != null) {
                         extend.add(l.get(1).toString());
                    }
                    map.put(question, extend);
               }

          }
          return map;
     }

  
    public Map<String, List<String>> playerInfo(Map<String, List<String>> filters){
        QueryBuilder Qb = new QueryBuilder();
          Qb.addSelect("UserId", "us", SelectOperations.none);
          Qb.addSelect("Gender", "us", SelectOperations.none);
 	Qb.addSelect("AgeRange", "us", SelectOperations.none);
 	Qb.addSelect("Locale", "us", SelectOperations.none);

          Qb.addTable("Users", "us");
          
          String s = Qb.getQuery();

          List<List<Object>> result = null;
          try {
               result = connection.QueryRetrieve(s);
          } catch (Exception e) {
               System.out.println(e.getMessage());
          }

          Map<String, List<String>> map = new HashMap<String, List<String>>();
          
          for (List l : result) {
	    String player = l.get(0).toString();

               List<String> extend = new LinkedList<String>();

               if (map.containsKey(player)) {
                    extend = map.get(player);
                    if (l.get(1) != null) {
                         extend.add(l.get(1).toString());
                    }
               } else {
                    if (l.get(1) != null) {
                         extend.add(l.get(1).toString());
                    }
                    map.put(player, extend);
               }

          }
          return map;
}

     //Adds filters in the format FILTER : VALUES ex Gender [MALE,FEMALE]
    public void addUserFilter(Map<String, List<String>> filters, String table, String colunm, QueryBuilder qb) {
          qb.addTable("users", "filter");
          qb.addSelect("userId", "filter", SelectOperations.none);
          for (String key : filters.keySet()) {
               List<String> values = filters.get(key);
               if (values != null) {
                   
                    qb.AddWhere("filter", key, values.get(0), QueryBuilder.Operators.Equals);
                    for (int i = 1; i < values.size(); i++) {
                        if (values.get(i).equals("null")){
                            qb.AddWhereOr();
                            qb.AddWhere("filter", key, values.get(i), QueryBuilder.Operators.IsNull);
                        }
                        else if (values.get(i).equals("notnull")){
                            qb.AddWhereOr();
                            qb.AddWhere("filter", key, values.get(i), QueryBuilder.Operators.NotNull);
                        }
                        else {
                            qb.AddWhereOr();
                            qb.AddWhere("filter", key, values.get(i), QueryBuilder.Operators.Equals);
                        }
                         
                    }
               }
          }
          qb.AddJoin(table, colunm, "filter", "userId", QueryBuilder.JoinTypes.inner);
     }
     
     
    public void addQuestionFilter(Map<String, List<String>> filters, String table,String colunm,QueryBuilder qb) {
          qb.addSelect("PlayerId", "filter", SelectOperations.distinct);
          qb.addTable("Recodings", "filter");
          qb.addSelect("PlayerId", "filter", SelectOperations.none);
          for (String key : filters.keySet()) {
               List<String> values = filters.get(key);
               if (values != null) {
                    qb.AddWhere("filter", key, "'"+values.get(0)+"'", QueryBuilder.Operators.Equals);
                    for (int i = 1; i < values.size(); i++) {
                         qb.AddWhereOr();
                         qb.AddWhere("filter", key, "'"+values.get(i)+"'", QueryBuilder.Operators.Equals);
                    }
               }
          }
          if (colunm.equals("RecordingId")){
              qb.AddJoin(table, colunm , "filter", "RecordingId", QueryBuilder.JoinTypes.inner);
          }
          else qb.AddJoin(table, colunm , "filter", "PlayerId", QueryBuilder.JoinTypes.inner);
     }
    
    public void addResultFilter(Map<String, List<String>> filters, String table,String colunm,QueryBuilder qb) {
          qb.addSelect("RecordingId", "filter", SelectOperations.distinct);
          qb.addTable("recodingAnswers", "filter");
          qb.addSelect("RecordingId", "filter", SelectOperations.none);
          for (String key : filters.keySet()) {
               List<String> values = filters.get(key);
               if (values != null) {
                    qb.AddWhere("filter", key, "'"+values.get(0)+"'", QueryBuilder.Operators.Equals);
                    for (int i = 1; i < values.size(); i++) {
                         qb.AddWhereOr();
                         qb.AddWhere("filter", key, "'"+values.get(i)+"'", QueryBuilder.Operators.Equals);
                    }
               }
          }
          qb.AddJoin(table, colunm , "filter", "RecordingId", QueryBuilder.JoinTypes.inner);
     }
    
     public List<List<Object>> totalPlayersSplit() //how many users have played the game
     {
         
         qb = new QueryBuilder();
         qb.addSelect("Gender", "use", SelectOperations.none);
         qb.addSelect("UserID", "use", SelectOperations.none);
         qb.addTable("users", "use");

        String s = qb.getQuery();

        List<List<Object>> result = null;
        try {
             result = connection.QueryRetrieve(s);
        } catch (Exception e) {
             System.out.println(e.getMessage());
             
        }
        
        System.out.println("\n"+result);
        return result;
        
     }
      
     public List<List<Object>> totalPlayersSplitLocale() //how many users have played the game
     {
         
         qb = new QueryBuilder();
         qb.addSelect("Locale", "use", SelectOperations.none);
         qb.addSelect("UserID", "use", SelectOperations.none);
         qb.addTable("users", "use");

        String s = qb.getQuery();

        List<List<Object>> result = null;
        try {
             result = connection.QueryRetrieve(s);
        } catch (Exception e) {
             System.out.println(e.getMessage());
             
        }
        
        System.out.println("\n"+result);
        return result;
        
     }
     
     public List<List <Object>> playStatisitcs() 
     {
        qb = new QueryBuilder();
 
        //SELECT MAX(COUNT(PlayerId)) FROM 
        String s = "SELECT COUNT(PlayerId) " +
                   "FROM Recordings GROUP BY PlayerId";   
        List<List<Object>> result = null;
        try {
             result = connection.QueryRetrieve(s);
        } catch (Exception e) {
             System.out.println(e.getMessage());
             
        }
        System.out.println("\n"+result);
        return result;
     }
}



