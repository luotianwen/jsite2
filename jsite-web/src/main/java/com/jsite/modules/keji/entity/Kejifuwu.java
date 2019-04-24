/**
 * Copyright &copy; 2017-2019 <a href="https://gitee.com/baseweb/JSite">JSite</a> All rights reserved.
 */
package com.jsite.modules.keji.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jsite.common.persistence.ActEntity;
import com.jsite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 科技服务Entity
 * @author liqianshi
 * @version 2019-04-06
 */
public class Kejifuwu extends ActEntity<Kejifuwu> {
	
	private static final long serialVersionUID = 1L;
	private String procInsId;		// 流程实例编号
	private Date startTime;		// 开始时间
	private Date endTime;		// 结束时间
	private String kejiType;		// 项目类型
	private String reason;		// 申请理由
	private String deptLeadText;		// 部门领导意见
	private String hrText;		// 人力资源意见
	private Date applyTime;		// 申请时间
	private String proname;		// 项目名字
	
	public Kejifuwu() {
		super();
	}

	public Kejifuwu(String id){
		super(id);
	}

	@Length(min=1, max=64, message="流程实例编号长度必须介于 1 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="开始时间不能为空")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="结束时间不能为空")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Length(min=1, max=20, message="项目类型长度必须介于 1 和 20 之间")
	public String getKejiType() {
		return kejiType;
	}

	public void setKejiType(String kejiType) {
		this.kejiType = kejiType;
	}
	
	@Length(min=1, max=255, message="申请理由长度必须介于 1 和 255 之间")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Length(min=1, max=200, message="部门领导意见长度必须介于 1 和 200 之间")
	public String getDeptLeadText() {
		return deptLeadText;
	}

	public void setDeptLeadText(String deptLeadText) {
		this.deptLeadText = deptLeadText;
	}
	
	@Length(min=1, max=200, message="人力资源意见长度必须介于 1 和 200 之间")
	public String getHrText() {
		return hrText;
	}

	public void setHrText(String hrText) {
		this.hrText = hrText;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	
	@Length(min=1, max=255, message="项目名字长度必须介于 1 和 255 之间")
	public String getProname() {
		return proname;
	}

	public void setProname(String proname) {
		this.proname = proname;
	}
	
}