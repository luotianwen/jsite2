/**
 * Copyright &copy; 2017-2019 <a href="https://gitee.com/baseweb/JSite">JSite</a> All rights reserved.
 */
package com.jsite.modules.keji.dao;

import com.jsite.common.persistence.CrudDao;
import com.jsite.common.persistence.annotation.MyBatisDao;
import com.jsite.modules.keji.entity.Kejifuwu;

/**
 * 科技服务表生成测试DAO接口
 * @author 李乾仕
 * @version 2019-03-21
 */
@MyBatisDao
public interface KejifuwuDao extends CrudDao<Kejifuwu> {

    void updateLeadText(Kejifuwu kejifuwu);

    void updateHRText(Kejifuwu kejifuwu);
}