/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package query;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

/**
 *
 * @author Ian
 */
public class QueryBuilder {
    
    private List<Select> selects;
    private List<Table> tables;
    private List<Where> whereClauses;
    private List<OrderBy> orderBys;
    private List<Join> joins;
    
    public QueryBuilder()
    {
        selects = new ArrayList<Select>();
        tables = new ArrayList<Table>();
        whereClauses = new ArrayList<Where>();
        orderBys = new ArrayList<OrderBy>();
        joins = new ArrayList<Join>();
    }
    
    public interface Operators 
    {
        String Equals = " = ";
        String NotEqual = " != ";
        String GreatEqual = " >= "; 
        String LessEqual = " <= ";
        String Greater = " > ";
        String Lesser = " < ";
        String NotNull = " is not null ";
        String IsNull = " is null ";
        String Like = " like ";
        String Not = " not ";
    }
    
    public interface JoinTypes
    {
        String inner = " inner join ";
        String leftjoin = " left outer join ";
        String rightjoin = " right outer join ";
    }
    
    public enum SelectOperations
    {
        count("count($$$)"/* as '###'"*/),
        distinct("distinct $$$"),
        countDistinct("count(distinct $$$)"/*as '###'"*/),
        average("Avg($$$)"),
        max ("Max($$$)"),
        min ("Min($$$)"),
        sum ("Sum($$$)"),
        none("$$$");
        
        String text;  
        private SelectOperations(final String text) {
            this.text = text;            
        }
        
        @Override
        public String toString() {
            return text;
        }
    }
    

// <editor-fold defaultstate="collapsed" desc="query item structures">
    
    public class Select
    {
        private String select;
        private String alias;
        private SelectOperations type;
        
        public Select(String select, String alias, SelectOperations type)
        {
            this.select = select;
            this.alias = alias;
            this.type = type;
        }        
        
        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            
            return DoWork(sb.append(alias).append('.').append(select).toString(), type);
        }
        
        public String nonTypeToString(){
             StringBuilder sb = new StringBuilder();
             String s = sb.append(alias).append('.').append(select).toString();
             return s;
        }
        public String noAliasToString(){
             StringBuilder sb = new StringBuilder();
             String s = sb.append(select).toString();
             return s;
            
        }
        
        
        private String DoWork(String select, SelectOperations type)
        {
            String base;
            if (type.equals(SelectOperations.count))
                base = SelectOperations.count.toString();           
            else if (type.equals(SelectOperations.countDistinct))
                base = SelectOperations.countDistinct.toString();           
            else if (type.equals(SelectOperations.distinct))
                base = SelectOperations.distinct.toString();      
            else if (type.equals(SelectOperations.average))
                base = SelectOperations.average.toString();
            else if (type.equals(SelectOperations.max))
                base = SelectOperations.max.toString();
            else if (type.equals(SelectOperations.min))
                base = SelectOperations.min.toString();
            else if (type.equals(SelectOperations.sum))
                base = SelectOperations.sum.toString();
            else
                
                base = SelectOperations.none.toString();
            
            base = base.replace("$$$", select);
            
            if (base.contains("###"))
                
                base = base.replaceAll("###", String.format("%s_count", select));
                
            
            return base;
        }
    }
     
    public class Where
    {
        private String alias;
        private String column;
        private String value;      
        private String oper;
        
        public Where(String als, String col, String val, String op)
        {
            alias = als;
            column = col;
            value = val;
            /*if val is not a digit then add an ' at the start and end of the string so 
            the database treats it as a string and not an integer*/
            if (Pattern.matches("[0-9]+", value) == false && value.length() > 0){
                if (value.contains("(")){
                    
                }
                else{
                value = "'"+val+"'"; 
                System.out.println("This is the new value: " + value);
                }
            }
            oper = op;
        }
        
        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            if (oper.equals(Operators.IsNull) || oper.equals(Operators.NotNull))
                return sb.append(alias).append('.').append(column).append(oper.toString()).toString();
            
            return sb.append(alias).append('.').append(column).append(oper.toString()).append(value).toString();
        }
    }
    
    public class Table
    {
        private String table;
        private String alias;
        
        public Table(String tbl, String als)
        {
            table = tbl;
            alias = als;
        }
        
        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            return sb.append(table).append(" as ").append(alias).toString();
        }
    }
    
    public class Join
    {
        Select initial;
        Select onSecond;
        String join;
        
        
        public Join(String alias1, String column1, String alias2, String column2, String j)
        {
            initial = new Select(alias1, column1, SelectOperations.none);
            onSecond = new Select(alias2, column2, SelectOperations.none);
            join = j;
            
        }
        
        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append(" on ").append(initial.alias).append('.').append(initial.select);
            sb.append(" = ").append(onSecond.alias).append('.').append(onSecond.select).append(" ");
            
            
            return sb.toString();
        }
    }
    
    public class OrderBy
    {
        public Select order;
        public String orderDir;
        
        public OrderBy(String column, String als, String ord)
        {
            order = new Select(column, als, SelectOperations.none);
            orderDir = ord;
        }        
        
        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append(order.alias).append('.').append(order.select);
            sb.append(' ').append(orderDir);
            return sb.toString();
        }
    }
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="addition query items">  
    
    public void writeCommand(String Command){
        
    }
    
    public void addSelect(String select, String alias, SelectOperations type)
    {
        selects.add(new Select(select, alias, type));      
    }
    
    public void removeSelect(String select)
    {
        for (int i = 0; i < selects.size(); i++)
            if (selects.get(i).select.equals(select))
            {
                selects.remove(i);
                return;
            }
        System.out.println("Unable to remove entry, does not exist");
    }
    
    public void addTable(String table, String alias)
    {
        for(Table t : tables)
            if (t.table.equals(table))
            {
                System.out.println("Table already exists");
                return;
            }    
        tables.add(new Table(table, alias));
    }
    
    public void removeTable(String table)
    {
        for (int i = 0; i < tables.size(); i++)
            if (tables.get(i).table.equals(table))
            {
                tables.remove(i);
                return;
            }
        System.out.println("Unable to remove table entry, does not exist");
    }
    
    public void AddWhere(String alias, String column, String value, String oper)
    {
        whereClauses.add(new Where(alias, column, value, oper));
    }
    
    public void removeWhere(String col)
    {
        for (int i = 0; i < whereClauses.size(); i++)
            if (whereClauses.get(i).column.equals(col))
            {
                whereClauses.remove(i);
                return;
            }
        System.out.println("Unable to remove where entry, does not exist");
    }
    
    public void AddWhereOr(){
        whereClauses.add(new Where("","","","Or"));
    }
    
    public void AddJoin(String alias1, String column1, String alias2, String column2, String join)
    {
        joins.add(new Join(column1, alias1, column2, alias2, join));
    }
    
    public void removeJoin(String column1, String column2)
    {
        for (int i = 0; i < joins.size(); i++)
            if (joins.get(i).initial.select.equals(column1) && joins.get(i).onSecond.select.equals(column2))
            {
                joins.remove(i);
                return;
            }
        System.out.println("Unable to remove where entry, does not exist");
    }
    
    public void addOrderBy(String select, String alias, String order)
    {
        orderBys.add(new OrderBy(select, alias, order));      
    }
    
    public void removeOrderBy(String column)
    {
        for (int i = 0; i < selects.size(); i++)
            if (selects.get(i).select.equals(column))
            {
                selects.remove(i);
                return;
            }
        System.out.println("Unable to remove entry, does not exist");
    }
    
    private boolean CheckAlias(String alias)
    {
        for (Table t : tables)
            if (t.alias.equals(alias))
                return true;
        
        return false;
    }
    
// </editor-fold>   
    
    
    public String getQuery()
    {
        StringBuilder sb = new StringBuilder();
        List<Select> Group = new ArrayList<Select>();
        
        for (Select sel : selects)
        {
            //notGroup.add(sel);
            if (!CheckAlias(sel.alias))
                System.out.println("A matching table alias was not found: " + sel.select);
            
            if (sel.type.equals(SelectOperations.none) || 
                   sel.type.equals(SelectOperations.distinct)){
                Group.add(sel);
            }
        }
        
        for (Join jo: joins) 
            if (jo.initial.alias.equals(jo.onSecond.alias) || !CheckAlias(jo.initial.alias) || !CheckAlias(jo.onSecond.alias))
                System.out.println("Join operands  not given a proper alias: " + jo.initial.select);
        
        for (Where w : whereClauses)
        if (!CheckAlias(w.alias))
            System.out.println("Where column was not given a proper alias: " + w.column);
        
        sb.append("select ");
        for (Select s : selects)
            sb.append(s.toString()).append(", ");     
        sb.delete(sb.lastIndexOf(","), sb.length());
        
        sb.append(" from ");
        for (int i = 0; i < tables.size(); i++)
        {
            if (i == 0)
                sb.append(tables.get(0).toString());

            else
            {
                sb.append(joins.get(i - 1).join.toString());
                sb.append(tables.get(i).toString());
                sb.append(joins.get(i - 1).toString());
            }  
        }
        
        if (whereClauses.size() > 0)
        {
            sb.append(" where ");
            for (Where whr : whereClauses)
                if (whr.oper.equals("Or")){
                    sb.append(" or ");
                    int i = sb.lastIndexOf(" or ");
                    sb.delete(i-5, i);
  
                }
                else{
                sb.append(whr.toString()).append(" and ");
                }
            //System.out.println(sb);
            sb.delete(sb.lastIndexOf(" and "), sb.length());
        }
        
        if (Group.size() > 0)
        {
            sb.append(" group by ");
            for (Select sel : selects)
                if (Group.contains(sel)){
                    if (sel.toString().contains("distinct") ){
                        sb.append(sel.nonTypeToString()).append(", ");
                        
                    }
                    else{
                        sb.append(sel.toString()).append(", ");
                    }
                }
            //System.out.println(sb);
            sb.delete(sb.lastIndexOf(", "), sb.length());
        }
        
        if (orderBys.size() > 0)
        {
        sb.append(" order by ");
            for (OrderBy ord : orderBys){
                if (ord.toString().contains("count")){
                    System.out.println("COUNT");
                    sb.append(ord.toString()).append(", ");
                    int i = sb.lastIndexOf(".count");
                    sb.delete(i-1, i+1);
                    
                }
                else {
                    sb.append(ord.toString()).append(", ");
                }
                
            }
            
            sb.delete(sb.lastIndexOf(", "), sb.length());
            //System.out.println(sb);
        }     
        return sb.toString();
    }
}
