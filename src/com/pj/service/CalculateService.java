package com.pj.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import utils.Model;
import utils.TimeUtils;

import com.pj.model.Operation;

public class CalculateService {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Map<String, Double> calculateUtilization(Map<String, Double> map,
			String type, String mode) {
		String hql = "from Operation op where op.asset_type=? ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, type);
		List<Operation> ops = query.list();
		String startTime = "2030-12-30 23-59-59";
		String endTime = "1999-01-01 01-01-01";
		Map<String, Long> assetTime = new HashMap<String, Long>();
		Map<String, Long> assetSumTime = new HashMap<String, Long>();
		Set<String> set = new HashSet<String>();
		for (Operation op : ops) {
			startTime = TimeUtils.getMinTime(startTime, op.getOp_strt_time());
			endTime = TimeUtils.getMaxTime(endTime, op.getOp_end_time());
			Long time = TimeUtils.getDeltaTime(op.getOp_strt_time(),
					op.getOp_end_time());
			if (assetTime.get(op.getAsset_id()) == null) {
				assetTime.put(op.getAsset_id(), time);
			} else {
				long temp = assetTime.get(op.getAsset_id());
				assetTime.put(op.getAsset_id(), time + temp);
			}
		}
		if (mode.equals(Model.Asset)) {
			for (Operation op : ops) {
				if (!set.contains(op.getAsset_id())) {
					set.add(op.getAsset_id());
					long sumTime = TimeUtils.getSumTime(startTime, endTime,
							op.getOp_dept());
					double utilization = assetTime.get(op.getAsset_id())
							/ sumTime * 100;
					map.put(op.getAsset_id(), utilization);
				}
			}
		}

		return map;
	}
}
