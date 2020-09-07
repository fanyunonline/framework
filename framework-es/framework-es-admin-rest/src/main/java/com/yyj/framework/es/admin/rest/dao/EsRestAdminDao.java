package com.yyj.framework.es.admin.rest.dao;


import com.yyj.framework.common.util.response.Response;
import com.yyj.framework.es.admin.rest.mapping.IndexMappingQo;
import com.yyj.framework.es.admin.rest.model.Source;

import java.util.List;

/**
 * Created by yangyijun on 2017/12/28.
 */
public interface EsRestAdminDao {

    Response existsIndex(String index, String nodes);

    Response createIndex(IndexMappingQo indexMapping);

    Response createIndex(String index, String nodes);

    Response deleteIndex(String index, String nodes);

    Response bulkUpsert(String index, String type, String nodes, List<Source> sourceList);

    Response delete(String index, String type, String nodes, List<Source> sources);
}
