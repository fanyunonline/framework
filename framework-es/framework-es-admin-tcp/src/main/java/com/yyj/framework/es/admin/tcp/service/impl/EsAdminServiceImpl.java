package com.yyj.framework.es.admin.tcp.service.impl;

import com.yyj.framework.common.util.constant.CrudType;
import com.yyj.framework.common.util.response.Response;
import com.yyj.framework.es.admin.tcp.dao.EsTcpAdminDao;
import com.yyj.framework.es.admin.tcp.mapping.IndexMappingQo;
import com.yyj.framework.es.admin.tcp.mapping.TypeMappingQo;
import com.yyj.framework.es.admin.tcp.model.DocQo;
import com.yyj.framework.es.admin.tcp.model.IndexAdminQo;
import com.yyj.framework.es.admin.tcp.model.Source;
import com.yyj.framework.es.admin.tcp.service.EsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yangyijun on 2018/12/3.
 */
@Service
public class EsAdminServiceImpl implements EsAdminService {

    @Autowired
    public EsTcpAdminDao esTcpAdminDao;

    @Override
    public Response existsIndex(IndexAdminQo indexAdminQo) {
        return esTcpAdminDao.existsIndex(indexAdminQo.getIndex(), indexAdminQo.getNodes(), indexAdminQo.getClusterName());
    }

    @Override
    public Response createIndex(IndexMappingQo indexMappingQo) {
        return esTcpAdminDao.createIndex(indexMappingQo);
    }

    @Override
    public Response deleteIndex(IndexAdminQo indexAdminQo) {
        return esTcpAdminDao.deleteIndex(indexAdminQo.getIndex(), indexAdminQo.getNodes(), indexAdminQo.getClusterName());
    }

    @Override
    public Response existsType(TypeMappingQo typeMappingQo) {
        return esTcpAdminDao.existsType(typeMappingQo.getIndex(), typeMappingQo.getType(), typeMappingQo.getNodes(), typeMappingQo.getClusterName());
    }

    @Override
    public Response createType(TypeMappingQo typeMappingQo) {
        return esTcpAdminDao.createType(typeMappingQo);
    }

    @Override
    public Response deleteType(TypeMappingQo typeMappingQo) {
        return esTcpAdminDao.deleteType(typeMappingQo.getIndex(),
                typeMappingQo.getType(), typeMappingQo.getNodes(),
                typeMappingQo.getClusterName());
    }

    @Override
    public Response bulkCud(DocQo docQo) {
        String index = docQo.getIndex();
        String type = docQo.getType();
        String nodes = docQo.getNodes();
        String clusterName = docQo.getClusterName();
        List<Source> datas = docQo.getSources();
        if (CrudType.D.equals(docQo.getCrudType())) {
            return esTcpAdminDao.delete(index, type, nodes, clusterName, datas);
        } else {
            return esTcpAdminDao.bulkUpsert(index, type, nodes, clusterName, datas);
        }
    }
}
