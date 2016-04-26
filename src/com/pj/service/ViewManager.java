package com.pj.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;

import com.pj.model.Operation;

public class ViewManager {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void crateOperationView(List<Operation> ops) {
		int i = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("create view opreationview as ");
		for (Operation op : ops) {
			sql.append("select " + op.getAsset_id() + "," + op.getAsset_type()
					+ "," + op.getOp_dept() + "," + op.getOp_id() + ","
					+ op.getOp_end_time() + "," + op.getOp_strt_time()
					+ " from " + op.getTables() );
			if(op.getRules()==null){
				sql.append(" where "+op.getRules());
			}
			if (i != ops.size() - 1) {
				sql.append(" union ");
			}
			i++;
		}

		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(
				sql.toString());
		query.executeUpdate();

	}
}
