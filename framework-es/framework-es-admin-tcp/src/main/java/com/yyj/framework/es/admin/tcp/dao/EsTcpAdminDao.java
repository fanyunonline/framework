package com.yyj.framework.es.admin.tcp.dao;


import com.yyj.framework.common.util.response.Response;
import com.yyj.framework.es.admin.tcp.mapping.IndexMappingQo;
import com.yyj.framework.es.admin.tcp.mapping.TypeMappingQo;
import com.yyj.framework.es.admin.tcp.model.Source;

import java.util.List;

/**
 * Created by yangyijun on 2017/12/28.
 */
public interface EsTcpAdminDao {

    Response existsIndex(String index, String nodes, String clusterName);

    Response existsType(String index, String type, String nodes, String clusterName);

    Response createIndex(IndexMappingQo indexMapping);

    Response createIndex(String index, String nodes, String clusterName);

    Response deleteIndex(String index, String nodes, String clusterName);

    Response createType(String index, String type, String nodes, String clusterName);

    Response createType(TypeMappingQo mapping);

    Response deleteType(String index, String type, String nodes, String clusterName);

    Response bulkUpsert(String index, String type, String nodes, String clusterName, List<Source> sourceList);

    Response delete(String index, String type, String nodes, String clusterName, List<Source> sources);
}
