package com.pj.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import utils.API;
import utils.AnalyticType;
import utils.Model;
import utils.TimeUtils;

import com.pj.model.Cost;
import com.pj.model.Operation;
import com.pj.model.Repair;

public class CalculateService {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Map<String, Double> calculateUtilization(Map<String, Double> map,
			String type, String model) {
		String hql = "from Operation op where op.asset_type=? ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, type);
		List<Operation> ops = query.list();
		String startTime = API.STARTTIME;
		String endTime = API.ENDTIME;
		Map<String, Long> assetTime = new HashMap<String, Long>();
		// Map<String, Long> assetSumTime = new HashMap<String, Long>();
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
		if (model.equals(Model.Asset)) {
			for (Operation op : ops) {
				if (!set.contains(op.getAsset_id())) {
					hql = "select asset.asset_dept from Asset asset where asset.id=?";
					query = sessionFactory.getCurrentSession().createQuery(hql);
					query.setString(0, type);
					String dept = (String) query.uniqueResult();
					set.add(op.getAsset_id());
					long sumTime = TimeUtils.getSumTime(startTime, endTime,
							dept);
					double utilization = assetTime.get(op.getAsset_id())
							/ sumTime * 100;
					map.put(op.getAsset_id(), utilization);
				}
			}
		} else if (model.equals(Model.Type)) {
			long sumTime = 0;
			long utilizationTime = 0;
			for (Operation op : ops) {
				if (!set.contains(op.getAsset_id())) {
					hql = "select asset.asset_dept from Asset asset where asset.id=?";
					query = sessionFactory.getCurrentSession().createQuery(hql);
					query.setString(0, type);
					String dept = (String) query.uniqueResult();
					set.add(op.getAsset_id());
					sumTime = sumTime
							+ TimeUtils.getSumTime(startTime, endTime, dept);
					utilizationTime = utilizationTime
							+ assetTime.get(op.getAsset_id());
				}
			}
			double utilization = utilizationTime / sumTime * 100;
			map.put(type, utilization);
		}

		return map;
	}

	public Map<String, Double> calculateAvalOrFail(Map<String, Double> map,
			String type, String model, String analyticType) {
		String hql = "from Repair repair where repair.asset_type=? ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, type);
		List<Repair> repairs = query.list();
		String startTime = API.STARTTIME;
		String endTime = API.ENDTIME;
		Map<String, Long> assetTime = new HashMap<String, Long>();
		Set<String> set = new HashSet<String>();
		for (Repair repair : repairs) {
			startTime = TimeUtils.getMinTime(startTime, repair.getDecl_time());
			endTime = TimeUtils.getMaxTime(endTime, repair.getRep_end_time());
			Long time = TimeUtils.getDeltaTime(repair.getDecl_time(),
					repair.getRep_end_time());
			if (assetTime.get(repair.getAsset_id()) == null) {
				assetTime.put(repair.getAsset_id(), time);
			} else {
				long temp = assetTime.get(repair.getAsset_id());
				assetTime.put(repair.getAsset_id(), time + temp);
			}
		}
		if (model.equals(Model.Asset)) {
			for (Repair repair : repairs) {
				if (!set.contains(repair.getAsset_id())) {
					hql = "select asset.asset_dept from Asset asset where asset.id=?";
					query = sessionFactory.getCurrentSession().createQuery(hql);
					query.setString(0, type);
					String dept = (String) query.uniqueResult();
					set.add(repair.getAsset_id());
					long sumTime = TimeUtils.getSumTime(startTime, endTime,
							dept);
					double result;
					if (analyticType.equals(AnalyticType.Availability)) {
						result = assetTime.get(repair.getAsset_id()) / sumTime
								* 100;
					} else {
						result = (sumTime - assetTime.get(repair.getAsset_id()))
								/ sumTime * 100;
					}
					map.put(repair.getAsset_id(), result);
				}
			}
		} else if (model.equals(Model.Type)) {
			long sumTime = 0;
			long failTime = 0;
			for (Repair repair : repairs) {
				if (!set.contains(repair.getAsset_id())) {
					hql = "select asset.asset_dept from Asset asset where asset.id=?";
					query = sessionFactory.getCurrentSession().createQuery(hql);
					query.setString(0, type);
					String dept = (String) query.uniqueResult();
					set.add(repair.getAsset_id());
					sumTime = sumTime
							+ TimeUtils.getSumTime(startTime, endTime, dept);
					failTime = failTime + assetTime.get(repair.getAsset_id());
				}
			}
			double result;
			if (analyticType.equals(AnalyticType.Availability)) {
				result = failTime / sumTime * 100;
			} else {
				result = (sumTime - failTime) / sumTime * 100;
			}
			map.put(type, result);
		}

		return map;
	}

	public Map<String, Double> calculateDownLosses(Map<String, Double> map,
			String type, String model) {
		String hql = "from Operation op where op.asset_type=? ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, type);
		List<Operation> ops = query.list();
		Map<String, Cost> asset_cost = new HashMap<String, Cost>();
		for (Operation op : ops) {
			if (asset_cost.get(op.getAsset_id()) != null) {
				Cost cost = asset_cost.get(op.getAsset_id());
				cost.op_cost = cost.op_cost
						+ Double.parseDouble(op.getOp_cost());
				cost.op_time = cost.op_time
						+ TimeUtils.getDeltaTime(op.getOp_strt_time(),
								op.getOp_end_time());
				asset_cost.put(op.getAsset_id(), cost);
			} else {
				Cost cost = new Cost();
				cost.op_cost = Double.parseDouble(op.getOp_cost());
				cost.op_time = TimeUtils.getDeltaTime(op.getOp_strt_time(),
						op.getOp_end_time());
				asset_cost.put(op.getAsset_id(), cost);
			}
		}

		hql = "from Repair rapair where rapair.asset_type=?";
		query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, type);
		List<Repair> repairs = query.list();
		for (Repair repair : repairs) {
			if (asset_cost.get(repair.getAsset_id()) != null) {
				Cost cost = asset_cost.get(repair.getAsset_id());
				cost.fail_time = cost.fail_time
						+ TimeUtils.getDeltaTime(repair.getDecl_time(),
								repair.getRep_end_time());
				cost.repair_time = cost.repair_time
						+ TimeUtils.getDeltaTime(repair.getRep_strt_time(),
								repair.getRep_end_time());
				cost.repair_cost = cost.repair_cost
						+ Double.parseDouble(repair.getRep_cost());
				cost.repair_count = cost.repair_count + 1;
				asset_cost.put(repair.getAsset_id(), cost);
			} else {
				Cost cost = new Cost();
				cost.fail_time = TimeUtils.getDeltaTime(repair.getDecl_time(),
						repair.getRep_end_time());
				cost.repair_time = TimeUtils.getDeltaTime(
						repair.getRep_strt_time(), repair.getRep_end_time());
				cost.repair_cost = Double.parseDouble(repair.getRep_cost());
				cost.repair_count = 1;
				asset_cost.put(repair.getAsset_id(), cost);
			}
		}
		double op_cost = 0.0;
		long op_time = 0;
		double repair_cost = 0;
		long fail_time = 0;
		long repair_time = 0;
		int repair_count = 0;
		if (model.equals(Model.Asset)) {

			for (String id : asset_cost.keySet()) {
				op_cost = op_cost + asset_cost.get(id).op_cost;
				op_time = op_time + asset_cost.get(id).op_time;
				repair_cost = repair_cost + asset_cost.get(id).repair_cost;
				fail_time = fail_time + asset_cost.get(id).fail_time;
				repair_time = repair_time + asset_cost.get(id).repair_time;
				repair_count = repair_count + asset_cost.get(id).repair_count;
			}
			double result = (op_cost / op_time + repair_cost / repair_time)
					* fail_time / repair_count;
			map.put(type, result);

		} else {
			for (String id : asset_cost.keySet()) {
				op_cost = asset_cost.get(id).op_cost;
				op_time = asset_cost.get(id).op_time;
				repair_cost = asset_cost.get(id).repair_cost;
				fail_time = asset_cost.get(id).fail_time;
				repair_time = asset_cost.get(id).repair_time;
				repair_count = asset_cost.get(id).repair_count;

				double result = (op_cost / op_time + repair_cost / repair_time)
						* fail_time / repair_count;
				map.put(id, result);
			}
		}

		return map;
	}
}
