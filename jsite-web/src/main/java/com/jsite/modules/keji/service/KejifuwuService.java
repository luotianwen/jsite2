/**
 * Copyright &copy; 2017-2019 <a href="https://gitee.com/baseweb/JSite">JSite</a> All rights reserved.
 */
package com.jsite.modules.keji.service;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.jsite.common.lang.StringUtils;
import com.jsite.modules.business.entity.Leave;
import com.jsite.modules.flowable.service.FlowTaskService;
import com.jsite.modules.flowable.utils.FlowableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jsite.common.persistence.Page;
import com.jsite.common.service.CrudService;
import com.jsite.modules.keji.entity.Kejifuwu;
import com.jsite.modules.keji.dao.KejifuwuDao;

/**
 * 科技服务表生成测试Service
 * @author 李乾仕
 * @version 2019-03-21
 */
@Service
@Transactional(readOnly = true)
public class KejifuwuService extends CrudService<KejifuwuDao, Kejifuwu> {
	@Autowired
	private FlowTaskService actTaskService;

	public Kejifuwu get(String id) {
		return super.get(id);
	}
	
	public List<Kejifuwu> findList(Kejifuwu kejifuwu) {
		return super.findList(kejifuwu);
	}
	
	public Page<Kejifuwu> findPage(Page<Kejifuwu> page, Kejifuwu kejifuwu) {
		return super.findPage(page, kejifuwu);
	}
	
	@Transactional(readOnly = false)
	public void save(Kejifuwu kejifuwu) {
		super.save(kejifuwu);
	}
	
	@Transactional(readOnly = false)
	public void delete(Kejifuwu kejifuwu) {
		super.delete(kejifuwu);
	}


	@Transactional(readOnly = false)
	public String save(Kejifuwu kejifuwu, Map<String, Object> variables) {
		String businessTable = FlowableUtils.getBusinessTable("leave");
		if (StringUtils.isBlank(businessTable)) {
			return "流程启动失败，请先设置业务表";
		}

		// 申请发起 保存业务数据
		if (StringUtils.isBlank(kejifuwu.getId())){
			kejifuwu.preInsert();
			dao.insert(kejifuwu);

			String procIns = actTaskService.startProcess("kejifuwu", businessTable, kejifuwu.getId(), variables);

			logger.debug("流程已启动，流程ID："+ procIns);

			return "流程已启动，流程ID："+ procIns;
		}

		// 重新编辑申请
		else{
			kejifuwu.preUpdate();
			dao.update(kejifuwu);

			kejifuwu.getAct().setComment(kejifuwu.getAct().isPass()?"[重申] ":"[销毁] " + kejifuwu.getAct().getComment());

			// 完成流程任务
			variables.put("auditPass", kejifuwu.getAct().isPass());
			actTaskService.complete(kejifuwu.getAct().getTaskId(), kejifuwu.getAct().getProcInsId(), kejifuwu.getAct().getComment(), variables);

			return "流程已" + (kejifuwu.getAct().isPass()?"[重申] ":"[销毁] ");
		}
	}
	/**
	 * 审核审批保存
	 * @param kejifuwu
	 */
	@Transactional(readOnly = false)
	public String auditSave(Kejifuwu kejifuwu) {

		// 设置意见
		kejifuwu.getAct().setComment((kejifuwu.getAct().isPass()?"[完成] ":"[未完成] ")+kejifuwu.getAct().getComment());

		kejifuwu.preUpdate();

		// 对不同环节的业务逻辑进行操作
		String taskDefKey = kejifuwu.getAct().getTaskDefKey();

		Map<String, Object> vars = Maps.newHashMap();
		// 部门领导审批
		if ("deptLeaderAudit".equals(taskDefKey)){
			kejifuwu.setDeptLeadText(kejifuwu.getAct().getComment());
			dao.updateLeadText(kejifuwu);
		}
		// HR审批
		else if ("hrAudit".equals(taskDefKey)){
			kejifuwu.setHrText(kejifuwu.getAct().getComment());
			dao.updateHRText(kejifuwu);
		}
		// 重新修改
		else if ("modifyApply".equals(taskDefKey)){
			dao.update(kejifuwu);
		}

		// 未知环节，直接返回
		else{
			return "未知环节";
		}

		vars.put("auditPass", kejifuwu.getAct().isPass());

		// 提交流程任务
		actTaskService.complete(kejifuwu.getAct().getTaskId(), kejifuwu.getAct().getProcInsId(), kejifuwu.getAct().getComment(), vars);

		return "处理成功";
	}

}