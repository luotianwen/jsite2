/**
 * Copyright &copy; 2017-2019 <a href="https://gitee.com/baseweb/JSite">JSite</a> All rights reserved.
 */
package com.jsite.modules.keji.web;

import com.google.common.collect.Maps;
import com.jsite.common.config.Global;
import com.jsite.common.lang.StringUtils;
import com.jsite.common.persistence.Page;
import com.jsite.common.web.BaseController;
import com.jsite.modules.keji.entity.Kejifuwu;
import com.jsite.modules.keji.service.KejifuwuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 科技服务表生成测试Controller
 * @author 李乾仕
 * @version 2019-03-21
 */
@Controller
@RequestMapping(value = "${adminPath}/keji/kejifuwu")
public class KejifuwuController extends BaseController {

	@Autowired
	private KejifuwuService kejifuwuService;

	@ModelAttribute
	public Kejifuwu get(@RequestParam(required=false) String id) {
		Kejifuwu entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = kejifuwuService.get(id);
		}
		if (entity == null){
			entity = new Kejifuwu();
		}
		return entity;
	}

	@RequiresPermissions("keji:kejifuwu:view")
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "modules/keji/kejifuwuList";
	}

	@RequiresPermissions("keji:kejifuwu:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<Kejifuwu> listData(Kejifuwu kejifuwu, HttpServletRequest request, HttpServletResponse response) {
		Page<Kejifuwu> page = kejifuwuService.findPage(new Page<>(request, response), kejifuwu);
		return page;
	}

	@RequiresPermissions("keji:kejifuwu:view")
	@RequestMapping(value = "form")
	public String form(Kejifuwu kejifuwu, Model model) {
		String view = "kejifuwuForm";
		if (StringUtils.isNotBlank(kejifuwu.getId())){
			String taskDefKey = kejifuwu.getAct().getTaskDefKey();

			// 查看工单
			if (kejifuwu.getAct().isFinishTask()) {
				view = "kejifuwuAudit";
			}

			// 部门领导审核环节
			else if ("deptLeaderAudit".equals(taskDefKey)) {
				view = "kejifuwuAudit";
			}

			// 人事审核环节
			else if ("hrAudit".equals(taskDefKey)) {
				view = "kejifuwuAudit";
			}

			// 调整申请
			else if ("modifyApply".equals(taskDefKey)) {
				view = "kejifuwuForm";
			}

		}
		model.addAttribute("kejifuwu", kejifuwu);
		return "modules/keji/"+ view;
	}

	@RequiresPermissions("keji:kejifuwu:edit")
	@RequestMapping(value = "save")
	@ResponseBody
	public String save(Kejifuwu kejifuwu) {
		try {
			Map<String, Object> variables = Maps.newHashMap();

			String message = kejifuwuService.save(kejifuwu, variables);

			return renderResult(Global.TRUE, message);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return renderResult(Global.FALSE, "系统内部错误！");
	}

	@RequiresPermissions("keji:kejifuwu:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Kejifuwu kejifuwu) {
		kejifuwuService.delete(kejifuwu);
		return renderResult(Global.TRUE, "删除科技服务表生成成功");
	}

	/**
	 * 工单执行（完成任务）
	 * @param
	 * @return
	 */
	@RequiresPermissions("keji:kejifuwu:edit")
	@RequestMapping(value = "saveAudit")
	@ResponseBody
	public String saveAudit(Kejifuwu kejifuwu) {
		String message = kejifuwuService.auditSave(kejifuwu);
		return renderResult(Global.TRUE, message);
	}

}