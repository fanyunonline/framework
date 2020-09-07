package com.yyj.framework.es.admin.rest.service;

import com.yyj.framework.common.util.response.Response;
import com.yyj.framework.es.admin.rest.mapping.IndexMappingQo;
import com.yyj.framework.es.admin.rest.model.DocQo;
import com.yyj.framework.es.admin.rest.model.IndexAdminQo;

/**
 * Created by yangyijun on 2018/12/3.
 */
public interface EsAdminService {
    Response existsIndex(IndexAdminQo indexAdminQo);

    Response createIndex(IndexMappingQo indexMappingQo);

    Response deleteIndex(IndexAdminQo indexAdminQo);

    Response bulkCud(DocQo docQo);
}
