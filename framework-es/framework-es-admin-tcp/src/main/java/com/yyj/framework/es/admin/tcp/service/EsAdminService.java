package com.yyj.framework.es.admin.tcp.service;

import com.yyj.framework.common.util.response.Response;
import com.yyj.framework.es.admin.tcp.mapping.TypeMappingQo;
import com.yyj.framework.es.admin.tcp.model.DocQo;
import com.yyj.framework.es.admin.tcp.model.IndexAdminQo;
import com.yyj.framework.es.admin.tcp.mapping.IndexMappingQo;

/**
 * Created by yangyijun on 2018/12/3.
 */
public interface EsAdminService {
    Response existsIndex(IndexAdminQo indexAdminQo);

    Response createIndex(IndexMappingQo indexMappingQo);

    Response deleteIndex(IndexAdminQo indexAdminQo);

    Response existsType(TypeMappingQo typeMappingQo);

    Response createType(TypeMappingQo typeMappingQo);

    Response deleteType(TypeMappingQo typeMappingQo);

    Response bulkCud(DocQo docQo);
}
