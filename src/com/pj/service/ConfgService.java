package com.pj.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.mapping.Table;

public class ConfgService {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
 public boolean isConfg(){
	 String sql="show table status where comment='view' ";
	 SQLQuery query=sessionFactory.getCurrentSession().createSQLQuery(sql);
	 List list=query.list();
	 System.out.println(list);
	 Set set=new HashSet<String>();
	 for(int i=0;i<list.size();i++){
		 Object[] obj = (Object[])list.get(i);
		 System.out.println(obj[0]);
		 set.add(obj[0]);
	 }
	 if(set.contains("repairview")&&set.contains("operationview")&&set.contains("scrapview"))
		 return true;
	 else return false;
 }
	
	
}
