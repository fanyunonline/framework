package com.yyj.framework.es.admin.rest.service.impl;

import com.yyj.framework.common.util.constant.CrudType;
import com.yyj.framework.common.util.response.Response;
import com.yyj.framework.es.admin.rest.dao.EsRestAdminDao;
import com.yyj.framework.es.admin.rest.model.DocQo;
import com.yyj.framework.es.admin.rest.mapping.IndexMappingQo;
import com.yyj.framework.es.admin.rest.model.IndexAdminQo;
import com.yyj.framework.es.admin.rest.model.Source;
import com.yyj.framework.es.admin.rest.service.EsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yangyijun on 2018/12/3.
 */
@Service
public class EsAdminServiceImpl implements EsAdminService {

    @Autowired
    public EsRestAdminDao esRestAdminDao;

    @Override
    public Response existsIndex(IndexAdminQo indexAdminQo) {
        return esRestAdminDao.existsIndex(indexAdminQo.getIndex(), indexAdminQo.getNodes());
    }

    @Override
    public Response createIndex(IndexMappingQo indexMappingQo) {
        return esRestAdminDao.createIndex(indexMappingQo);
    }

    @Override
    public Response deleteIndex(IndexAdminQo indexAdminQo) {
        return esRestAdminDao.deleteIndex(indexAdminQo.getIndex(), indexAdminQo.getNodes());
    }


    @Override
    public Response bulkCud(DocQo docQo) {
        String index = docQo.getIndex();
        String type = docQo.getType();
        String nodes = docQo.getNodes();
        List<Source> datas = docQo.getSources();
        if (CrudType.D.equals(docQo.getCrudType())) {
            return esRestAdminDao.delete(index, type, nodes, datas);
        } else {
            return esRestAdminDao.bulkUpsert(index, type, nodes, datas);
        }
    }
}
