package com.yyj.framework.es.admin.tcp;

import com.yyj.framework.common.core.rest.RestService;
import com.yyj.framework.common.util.constant.CrudType;
import com.yyj.framework.common.util.json.JsonUtils;
import com.yyj.framework.common.util.response.Response;
import com.yyj.framework.es.admin.tcp.mapping.IndexMappingQo;
import com.yyj.framework.es.admin.tcp.mapping.TypeMappingQo;
import com.yyj.framework.es.admin.tcp.model.DocQo;
import com.yyj.framework.es.admin.tcp.model.IndexAdminQo;
import com.yyj.framework.es.admin.tcp.model.Source;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyijun on 2018/12/15.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EsAdminTest {
    private final static String BASE_URL = "http://localhost:8082/es/admin/api";

    @Autowired
    private RestService restService;

    @Test
    public void existsIndex() throws Exception {
        IndexAdminQo indexAdminQo = new IndexAdminQo();
        indexAdminQo.setIndex("graph_cmb_dev4");
        //indexAdminQo.setNodes("192.168.1.49,192.168.1.50:9300");
        //indexAdminQo.setClusterName("graph");
        Response response = restService.doPost(BASE_URL + "/existsIndex", indexAdminQo, new ParameterizedTypeReference<Response>() {
        });
        JsonUtils.print(response);
    }

    @Test
    public void createIndex() throws Exception {
        IndexMappingQo indexMappingQo = new IndexMappingQo();
        indexMappingQo.setIndex("graph_cmb_dev4");
        //indexMappingQo.setNodes("192.168.1.49,192.168.1.50:9300");
        //indexMappingQo.setClusterName("graph");
        //indexMappingQo.setNumberOfReplicas(1);
        //indexMappingQo.setNumberOfShards(5);
        Response response = restService.doPost(BASE_URL + "/createIndex", indexMappingQo, new ParameterizedTypeReference<Response>() {
        });
        JsonUtils.print(response);
    }

    @Test
    public void deleteIndex() throws Exception {
        IndexAdminQo indexAdminQo = new IndexAdminQo();
        indexAdminQo.setIndex("graph_cmb_dev3");
        //indexAdminQo.setNodes("192.168.1.49,192.168.1.50:9300");
        //indexAdminQo.setClusterName("graph");
        Response response = restService.doPost(BASE_URL + "/deleteIndex", indexAdminQo, new ParameterizedTypeReference<Response>() {
        });
        JsonUtils.print(response);
    }

    @Test
    public void existsType() throws Exception {
        TypeMappingQo typeMappingQo = new TypeMappingQo();
        typeMappingQo.setIndex("graph_cmb_dev2");
        typeMappingQo.setType("tv_user");
        //typeMappingQo.setProperties(null);
        //typeMappingQo.setNodes("192.168.1.49,192.168.1.50:9300");
        //typeMappingQo.setClusterName("graph");
        Response response = restService.doPost(BASE_URL + "/existsType", typeMappingQo, new ParameterizedTypeReference<Response>() {
        });
        JsonUtils.print(response);
    }

    @Test
    public void createType() throws Exception {
        TypeMappingQo typeMappingQo = new TypeMappingQo();
        typeMappingQo.setIndex("graph_cmb_dev4");
        typeMappingQo.setType("tv_user");
        //typeMappingQo.setProperties(null);
        //typeMappingQo.setNodes("192.168.1.49,192.168.1.50:9300");
        //typeMappingQo.setClusterName("graph");
        Response response = restService.doPost(BASE_URL + "/createType", typeMappingQo, new ParameterizedTypeReference<Response>() {
        });
        JsonUtils.print(response);
    }

    @Test
    public void deleteType() throws Exception {
        TypeMappingQo typeMappingQo = new TypeMappingQo();
        typeMappingQo.setIndex("graph_cmb_dev2");
        typeMappingQo.setType("tv_user");
        //typeMappingQo.setProperties(null);
        //typeMappingQo.setNodes("192.168.1.49,192.168.1.50:9300");
        //typeMappingQo.setClusterName("graph");
        Response response = restService.doPost(BASE_URL + "/deleteType", typeMappingQo, new ParameterizedTypeReference<Response>() {
        });
        JsonUtils.print(response);
    }

    @Test
    public void bulkCud() throws Exception {
        bulkCud(CrudType.C);
//        bulkCud(CrudType.U);
//        bulkCud(CrudType.D);
    }

    public void bulkCud(CrudType crudType) throws Exception {
        DocQo docQo = new DocQo();
        docQo.setIndex("graph_cmb_dev2");
        docQo.setType("tv_user");
        docQo.setCrudType(crudType);
        List<Source> sources = new ArrayList<>();
        String[] birthdays = {
                "2018-01-01 12:12:12",
                "2018-02-01 12:12:12",
                "2018-03-01 12:12:12",
                "2018-04-01 12:12:12",
                "2018-05-01 12:12:12",
                "2018-06-01 12:12:12",
                "2018-07-01 12:12:12",
                "2018-08-01 12:12:12",
                "2018-09-01 12:12:12",
                "2018-10-01 12:12:12",
                "2018-11-01 12:12:12",
                "2018-12-01 12:12:12",
                "2018-01-02 12:12:12",
                "2018-02-02 12:12:12",
                "2018-03-02 12:12:12",
                "2018-04-02 12:12:12",
                "2018-05-02 12:12:12",
                "2018-06-02 12:12:12",
                "2018-07-02 12:12:12",
                "2018-08-02 12:12:12",
                "2018-09-02 12:12:12",
                "2018-10-02 12:12:12",
                "2018-11-02 12:12:12",
                "2018-12-02 12:12:12",
        };
        String[] sexs = {"男", "女", "未知"};
        for (int i = 1; i < 25; i++) {
            Source source = new Source();
            source.setId(String.valueOf(i));

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("name", "用户" + i);
            data.put("sex", sexs[i % 3]);
            data.put("age", i + 10);
            data.put("idcard", "522625199107231175" + i);
            data.put("phone", "18798012752" + i);
            data.put("birthday", birthdays[i - 1]);
            data.put("amount", i * 100.23);
            data.put("gep_coordinate", new Double[]{170.23 - Math.random() * i, 23.44 + Math.random() * i});//[longitude, latitude][经度、纬度]
            source.setSource(data);
            sources.add(source);
        }
        docQo.setSources(sources);
        //docQo.setNodes("192.168.1.49,192.168.1.50:9300");
        //docQo.setClusterName("graph");
        Response response = restService.doPost(BASE_URL + "/bulkCud", docQo, new ParameterizedTypeReference<Response>() {
        });
        JsonUtils.print(response);
    }


}
