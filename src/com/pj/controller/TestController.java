package com.pj.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pj.model.Operation;
import com.pj.service.UserManager;
import com.pj.service.ViewManager;

@Controller  
@RequestMapping("/test")  
public class TestController {
	@Resource(name="viewManager")  
	private ViewManager viewManager;  
	
	@RequestMapping("/createoperation")  
	public String getAllUser(HttpServletRequest request){  
		Operation op=new Operation();
		op.setAsset_id("operation.asset_id");
		op.setAsset_type("operation.asset_type");
		op.setOp_dept("operation.op_dept");
		op.setOp_end_time("operation.op_end_tine");
		op.setOp_id("operation.op_id");
		op.setOp_strt_time("operation.op_strt_time");
		op.setTables("operation");
		
		Operation op1=new Operation();
		op1.setAsset_id("operation3.asset_id");
		op1.setAsset_type("operation3.asset_type");
		op1.setOp_dept("operation2.op_dept");
		op1.setOp_end_time("operation1.op_end_tine");
		op1.setOp_id("operation1.op_id");
		op1.setOp_strt_time("operation1.op_strt_time");
		op1.setTables("operation1,operation2,operation3");
		op1.setRules("operation1.op_id=operation2.op_id and operation2.op_id=operation3.op_id");
		
		Operation op2=new Operation();
		op2.setAsset_id("operation4.asset_id");
		op2.setAsset_type("operation4.asset_type");
		op2.setOp_dept("operation5.op_dept");
		op2.setOp_end_time("operation5.op_end_tine");
		op2.setOp_id("operation4.op_id");
		op2.setOp_strt_time("operation4.op_strt_time");
		op2.setTables("operation4,operation5");
		op2.setRules("operation4.op_id=operation5.op_id ");
		List<Operation>ops=new ArrayList<Operation>();
		ops.add(op);
		ops.add(op1);
		ops.add(op2);
	   viewManager.crateOperationView(ops);
	    return "/Index";  
	}  
}
