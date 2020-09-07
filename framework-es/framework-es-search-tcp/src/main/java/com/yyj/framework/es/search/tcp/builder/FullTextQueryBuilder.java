package com.yyj.framework.es.search.tcp.builder;

import com.yyj.framework.es.search.tcp.bean.Analyzer;
import com.yyj.framework.es.search.tcp.bean.Token;
import com.yyj.framework.es.search.tcp.bean.TokenResult;
import com.yyj.framework.es.search.tcp.model.FullTextSearchQo;
import com.yyj.framework.es.search.tcp.query.FullTextQuery;
import com.yyj.framework.es.search.tcp.query.Query;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by yangyijun on 2018/11/23.
 */
public class FullTextQueryBuilder implements RootQueryBuilder {

    @Override
    public BoolQueryBuilder build(Query query) {

        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        FullTextQuery fullTextQuery = (FullTextQuery) query;
        String keyword = fullTextQuery.getKeyword();
        Set<FullTextSearchQo.MatchField> matchFields = fullTextQuery.getMatchFields();

        //Matching multiple fields simultaneously with should
        builder.should(QueryBuilders.multiMatchQuery(keyword, matchFields.stream().map(matchField -> matchField.getName()).collect(Collectors.toList()).toArray(new String[]{})).slop(1).boost(1f));

        //phrase match with should
        mathKeyOrToken(builder, matchFields, keyword);

        //participle and then phrase match keys with should
        TokenResult tokenResult = Analyzer.parse(keyword);
        Set<String> keys = tokenResult.getKeys();
        for (String key : keys) {
            mathKeyOrToken(builder, matchFields, key);
        }

        //participle and then phrase match tokens with should
        Set<Token> tokens = tokenResult.getTokens();
        for (Token token : tokens) {
            mathKeyOrToken(builder, matchFields, token.getTerm());
        }
        return builder;
    }

    private void mathKeyOrToken(BoolQueryBuilder builder, Set<FullTextSearchQo.MatchField> matchFields, String key) {
        matchFields.forEach(field -> {
            builder.should(QueryBuilders.matchPhraseQuery(field.getName(), key).slop(field.getSlop()).boost(field.getBoost()));
        });
    }
}
