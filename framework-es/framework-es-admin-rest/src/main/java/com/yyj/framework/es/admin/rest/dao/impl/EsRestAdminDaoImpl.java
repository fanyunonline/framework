package com.yyj.framework.es.admin.rest.dao.impl;

import com.alibaba.fastjson.JSON;
import com.yyj.framework.common.util.log.Log;
import com.yyj.framework.common.util.log.LogFactory;
import com.yyj.framework.common.util.response.Response;
import com.yyj.framework.es.admin.rest.dao.EsRestAdminDao;
import com.yyj.framework.es.admin.rest.mapping.IndexMappingQo;
import com.yyj.framework.es.admin.rest.mapping.TypeMappingTemplate;
import com.yyj.framework.es.admin.rest.model.Source;
import com.yyj.framework.es.core.client.rest.EsRestClient;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

/**
 * Created by yangyijun on 2017/12/28.
 */
@Repository
public class EsRestAdminDaoImpl implements EsRestAdminDao {

    private static final Log logger = LogFactory.getLogger(EsRestAdminDaoImpl.class);

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private EsRestClient esRestClient;


    @Override
    public Response existsIndex(String index, String nodes) {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(index);
        try {
            if (StringUtils.isBlank(nodes)) {
                restHighLevelClient.indices().exists(request);
            } else {
                esRestClient.getClient(nodes).indices().exists(request);
            }
            return Response.success(true);
        } catch (IOException e) {
            logger.error("find index [{0}] error", e, index);
        }
        return Response.success(false);
    }

    @Override
    public Response createIndex(IndexMappingQo indexMapping) {
        return getResponse(indexMapping.getIndex(), indexMapping.getNodes(),
                indexMapping.getNumberOfShards(), indexMapping.getNumberOfReplicas());
    }

    @Override
    public Response createIndex(String index, String nodes) {
        return getResponse(index, nodes, null, null);
    }

    @Override
    public Response deleteIndex(String index, String nodes) {
        DeleteIndexRequest request = new DeleteIndexRequest();
        request.indices(index);
        try {
            if (StringUtils.isBlank(nodes)) {
                restHighLevelClient.indices().delete(request);
            } else {
                esRestClient.getClient(nodes).indices().delete(request);
            }
            return Response.success(true);
        } catch (IOException e) {
            logger.error("delete index [{0}] error", e, index);
        }
        return Response.success(false);
    }

    @Override
    public Response bulkUpsert(String index, String type, String nodes, List<Source> sources) {
        BulkRequest request = new BulkRequest();
        sources.forEach(s -> {
            IndexRequest indexRequest = new IndexRequest(index, type);
            String id = s.getId();
            if (StringUtils.isNotBlank(id)) {
                indexRequest.id(id);
            }
            indexRequest.source(JSON.toJSONString(s.getSource()), XContentType.JSON);
            request.add(indexRequest);
        });

        try {
            if (StringUtils.isBlank(nodes)) {
                restHighLevelClient.bulk(request);
            } else {
                esRestClient.getClient(nodes).bulk(request);
            }
            return Response.success(true);
        } catch (IOException e) {
            logger.error("bulk upsert index/type [{0}/{1}] error", e, index, type);
        }
        return Response.success(false);
    }

    @Override
    public Response delete(String index, String type, String nodes, List<Source> sources) {
        BulkRequest request = new BulkRequest();
        sources.forEach(s -> request.add(new DeleteRequest(index, type, s.getId())));
        try {
            if (StringUtils.isBlank(nodes)) {
                restHighLevelClient.bulk(request);
            } else {
                esRestClient.getClient(nodes).bulk(request);
            }
            return Response.success(true);
        } catch (IOException e) {
            logger.error("delete data [{0}] from index/type [{1}/{2}] error", e, sources, index, type);
        }
        return Response.success(false);
    }

    ///////////////////////
    // private functions
    ///////////////////////
    private Response getResponse(String index, String nodes, Integer numberOfShards, Integer numberOfReplicas) {
        try {
            CreateIndexRequest request = new CreateIndexRequest(index);
            request.mapping("doc", TypeMappingTemplate.defaultSettings());
            Settings.Builder builder = Settings.builder();
            if (null != numberOfShards) {
                builder.put("number_of_shards", numberOfShards);
            }
            if (null != numberOfReplicas) {
                builder.put("number_of_replicas", numberOfReplicas);
            }
            request.settings(builder);
            if (StringUtils.isBlank(nodes)) {
                restHighLevelClient.indices().create(request);
            } else {
                esRestClient.getClient(nodes).indices().create(request);
            }
            return Response.success(true);
        } catch (IOException e) {
            logger.error("create index [{0}] error", e, index);
        }
        return Response.success(false);
    }
}
