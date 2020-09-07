package com.yyj.framework.es.admin.tcp.dao.impl;

import com.alibaba.fastjson.JSON;
import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import com.yyj.framework.common.util.response.Response;
import com.yyj.framework.es.admin.tcp.constant.EsAdminStatus;
import com.yyj.framework.es.admin.tcp.dao.EsTcpAdminDao;
import com.yyj.framework.es.admin.tcp.model.Source;
import com.yyj.framework.es.admin.tcp.mapping.*;
import com.yyj.framework.es.core.client.tcp.EsTcpClient;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by yangyijun on 2017/12/28.
 */
@Repository
public class EsTcpAdminDaoImpl implements EsTcpAdminDao {

    private static final Log logger = LogFactory.getLogger(EsTcpAdminDaoImpl.class);

    @Autowired
    private EsTcpClient esClient;

    private TransportClient getClient(String nodes, String clusterName) {
        return esClient.getClient(nodes, clusterName);
    }

    @Override
    public Response existsIndex(String index, String nodes, String clusterName) {
        return Response.success(indexExists(index, nodes, clusterName));
    }

    @Override
    public Response existsType(String index, String type, String nodes, String clusterName) {
        return Response.success(typeExists(index, type, nodes, clusterName));
    }

    @Override
    public Response createIndex(IndexMappingQo indexMapping) {
        String index = indexMapping.getIndex();
        String nodes = indexMapping.getNodes();
        String clusterName = indexMapping.getClusterName();
        XContentBuilder builder = IndexMappingTemplate.buildSettings(indexMapping);
        if (null == builder) {
            return createIndex(index, nodes, clusterName);
        } else {
            return createIndex(index, nodes, clusterName, builder);
        }
    }

    @Override
    public Response createIndex(String index, String nodes, String clusterName) {
        return createIndex(index, nodes, clusterName, IndexMappingTemplate.defaultSettings());
    }

    @Override
    public Response deleteIndex(String index, String nodes, String clusterName) {
        try {
            if (indexExists(index, nodes, clusterName)) {
                this.getClient(nodes, clusterName).admin().indices().prepareDelete(index).get();
                logger.info("Success to delete index[{0}]", index);
            } else {
                logger.warn("Failed to delete index[{0}], no such index", index);
            }
            return Response.success();
        } catch (Exception e) {
            logger.error("Failed to delete index[{0}]\n", e);
        }
        return Response.error(EsAdminStatus.DELETE_INDEX_ERROR, index);
    }

    @Override
    public Response createType(String index, String type, String nodes, String clusterName) {
        return createType(index, type, nodes, clusterName, TypeMappingTemplate.defaultSettings());
    }

    @Override
    public Response createType(TypeMappingQo mapping) {
        String index = mapping.getIndex();
        String type = mapping.getType();
        String nodes = mapping.getNodes();
        String clusterName = mapping.getClusterName();
        Map<String, Field> properties = mapping.getProperties();
        XContentBuilder builder = TypeMappingTemplate.buildSettings(properties);
        if (null == builder) {
            return createType(index, type, nodes, clusterName);
        } else {
            return createType(index, type, nodes, clusterName, builder);
        }
    }

    @Override
    public Response deleteType(String index, String type, String nodes, String clusterName) {
        try {
            if (!typeExists(index, type, nodes, clusterName)) {
                logger.warn("Failed to delete type[{0}], no such index {1}", type, index);
            } else {
                QueryBuilder builder = QueryBuilders.typeQuery(type);//查询整个type
                DeleteByQueryAction.INSTANCE.newRequestBuilder(getClient(nodes, clusterName))
                        .filter(builder)
                        .source(index)
                        .get();
                logger.info("Success to delete type[{0}] in index[{1}]", type, index);
            }
            return Response.success();
        } catch (Exception e) {
            logger.error("Failed to delete type[{0}] in index {1}\n", e, type, index);
        }
        return Response.error(EsAdminStatus.DELETE_DATA_ERROR, index, type);
    }

    @Override
    public Response bulkUpsert(String index, String type, String nodes, String clusterName, List<Source> sources) {
        try {
            TransportClient client = getClient(nodes, clusterName);
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            for (Source source : sources) {
                UpdateRequestBuilder urb = client.prepareUpdate(index, type, source.getId())
                        .setDocAsUpsert(true).setDoc(source.getSource());
                bulkRequest.add(urb);
            }
            BulkResponse bulkResponse = bulkRequest.get();
            if (bulkResponse.hasFailures()) {
                logger.error("one or more sources upsert failed with type[{0}/{1}] due to:\n{2}\n{3} ", index, type,
                        bulkResponse.buildFailureMessage(), JSON.toJSONString(sources, true));
            } else {
                logger.info("Success to bulk upsert [{0}] records with type[{1}/{2}]", sources.size(), index, type);
            }
            return Response.success();
        } catch (Exception e) {
            logger.error("Failed to bulk upsert with type[{0}/{1}]", e, index, type, JSON.toJSONString(sources));
        }
        return Response.error(EsAdminStatus.UPSERT_DATA_ERROR, index, type);
    }

    @Override
    public Response delete(String index, String type, String nodes, String clusterName, List<Source> sources) {
        try {
            TransportClient client = getClient(nodes, clusterName);
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            for (Source source : sources) {
                DeleteRequestBuilder dr = client.prepareDelete(index, type, source.getId());
                bulkRequest.add(dr);
            }
            BulkResponse bulkResponse = bulkRequest.get();
            if (bulkResponse.hasFailures()) {
                logger.error("One or more indexes delete failed with type[{0}/{1}] due to:\n{2}\n{3}",
                        index, type, bulkResponse.buildFailureMessage(), JSON.toJSONString(sources, true));
            } else {
                logger.info("Success to bulk delete [{0}] records for type[{1}/{2}].", sources.size(), index,
                        type);
            }
            return Response.success();
        } catch (Exception e) {
            logger.error("Failed to bulk delete [{0}] records with type[{1}/{2}],data:\n{3}", e,
                    sources.size(), index, type, JSON.toJSONString(sources, true));
        }
        return Response.error(EsAdminStatus.DELETE_DATA_ERROR, index, type);
    }

    ///////////////////////
    // private functions
    ///////////////////////
    private boolean indexExists(String index, String nodes, String clusterName) {
        IndicesExistsResponse response = this.getClient(nodes, clusterName).admin().indices()
                .exists(new IndicesExistsRequest(index)).actionGet();
        return response.isExists();
    }

    private Response createIndex(String index, String nodes, String clusterName, XContentBuilder builder) {
        try {
            if (indexExists(index, nodes, clusterName)) {
                logger.info("The index[{0}] already exists", index);
            } else {
                this.getClient(nodes, clusterName).admin().indices().prepareCreate(index)
                        .setSettings(builder).get();
                logger.info("Success to create index[{0}]", index);
            }
            return Response.success();
        } catch (Exception e) {
            logger.error("Failed to create index[{0}]\n", e, index);
        }
        return Response.error(EsAdminStatus.CREATE_INDEX_ERROR, index);
    }

    private boolean typeExists(String index, String type, String nodes, String clusterName) {
        TypesExistsResponse response = this.getClient(nodes, clusterName).admin().indices()
                .typesExists(new TypesExistsRequest(new String[]{index}, type)).actionGet();
        return response.isExists();
    }

    private Response createType(String index, String type, String nodes, String clusterName, XContentBuilder builder) {
        try {
            if (typeExists(index, type, nodes, clusterName)) {
                logger.info("The type[{0}/{1}] already exists", index, type);
            } else {
                this.getClient(nodes, clusterName).admin().indices()
                        .preparePutMapping(index).setType(type)
                        .setSource(builder).get();
                logger.info("Success to create type[{0}/{1}]", index, type);
            }
            return Response.success();
        } catch (Exception e) {
            logger.error("Failed to create type[{0}/{1}]\n", e, index, type);
        }
        return Response.error(EsAdminStatus.CREATE_TYPE_ERROR, index, type);
    }
}
