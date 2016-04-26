package Test;

import java.util.ArrayList;
import java.util.List;

import com.pj.model.Operation;
import com.pj.service.ViewManager;

import junit.framework.TestCase;

public class TestViewManager extends TestCase{

	public void testcrateOperationView(){
		Operation op=new Operation();
		op.setAsset_id("operation.asset_id");
		op.setAsset_type("operation.asset_type");
		op.setOp_dept("operation.op_dept");
		op.setOp_end_time("operation.op_end_tine");
		op.setOp_id("operation.op_id");
		op.setOp_strt_time("operation.op_strt_time");
		List<Operation>ops=new ArrayList<Operation>();
		ops.add(op);
		ViewManager vm=new ViewManager();
		vm.crateOperationView(ops);
	}
}
