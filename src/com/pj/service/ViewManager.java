package com.pj.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import com.pj.model.Operation;
import com.pj.model.Repair;
import com.pj.model.Scrap;

public class ViewManager {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private void createView(String sql)throws MySQLSyntaxErrorException{
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(
				sql.toString());
		query.executeUpdate();
	}
	public boolean crateOperationView(List<Operation> ops)  {
		int i = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("create view operationview as ");
		for (Operation op : ops) {
			sql.append("select " + op.getAsset_id() + " as asset_id," + op.getAsset_type()
					+ " as asset_type," + op.getOp_dept() + " as op_dept," + op.getOp_id() + " as op_id,"
					+ op.getOp_end_time() + " as op_end_time," + op.getOp_strt_time()
					+ " as op_strt_time from " + op.getTables());
			if (op.getRules() == null) {
				sql.append(" where " + op.getRules());
			}
			if (i != ops.size() -1) {
				sql.append(" union ");
			}
			i++;
		}

	try{
		createView(sql.toString());
		return true;
	}
	catch(Exception e){
		if(e.getCause().getMessage().equals("Table 'opreationview' already exists"))
			return true;
		else return false;
	}
	}

	public boolean createrepair(List<Repair> repairs) {
		int i = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("create view repairview as ");
		for (Repair repair : repairs) {
			sql.append("select " + repair.getAsset_id() + " as asset_id,"
					+ repair.getAsset_type() + " as asset_type," + repair.getDecl_time()
					+ " as decl_time," + repair.getRep_cost() + " as rep_cost,"
					+ repair.getRep_end_time() + " as rep_end_time,"
					+ repair.getRep_strt_time() + " as rep_strt_time," + repair.getRepair_id()
					+ " as repair_id from " + repair.getTables());
			if (repair.getRules() == null) {
				sql.append(" where " + repair.getRules());
			}
			if (i != repairs.size() - 1) {
				sql.append(" union ");
			}
			i++;
		}

		try{
			createView(sql.toString());
			return true;
		}
		catch(Exception e){
			System.out.println("message"+e.getCause().getMessage());
			if(e.getCause().getMessage().equals("Table 'repairview' already exists"))
				return true;
			else return false;
		}

	}

	public boolean createscrap(List<Scrap> scraps) {
		int i = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("create view scrapview as ");
		for (Scrap scrap : scraps) {
			sql.append("select " + scrap.getAsset_id() + " as asset_id,"
					+ scrap.getAsset_type() + " as asset_type," + scrap.getScrap_id() + " as scrap_id,"
					+ scrap.getScrap_time() + " as scrap_time from " + scrap.getTables());
			if (scrap.getRules() == null) {
				sql.append(" where " + scrap.getRules());
			}
			if (i != scraps.size() - 1) {
				sql.append(" union ");
			}
			i++;
		}

		try{
			createView(sql.toString());
			return true;
		}
		catch(Exception e){
			System.out.println("message"+e.getCause().getMessage());
			if(e.getCause().getMessage().equals("Table 'scrapview' already exists"))
				return true;
			else return false;
		}
	}
}
