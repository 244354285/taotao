package com.taotao.search.dao.impl;

import com.taotao.search.dao.SearchDao;
import com.taotao.search.pojo.SearchItem;
import com.taotao.search.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SearchDaoImpl implements SearchDao {

    @Autowired
    private SolrServer solrServer;

    @Override
    public SearchResult search(SolrQuery query) throws Exception {
        //执行查询
        QueryResponse response = solrServer.query(query);
        //获取查询结果列表
        SolrDocumentList documents = response.getResults();
        List<SearchItem> itemList = new ArrayList<>();
        for(SolrDocument document : documents){
            //创建一个SearchItem对象
            SearchItem item = new SearchItem();
            item.setCategory_name((String) document.get("item_category_name"));
            item.setId((String) document.get("id"));
            item.setImage((String) document.get("item_image"));
            item.setPrice((Long) document.get("item_price"));
            item.setSell_point((String) document.get("item_sell_point"));
            //获取高亮显示
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            List<String> list = highlighting.get(document.get("id")).get("item_title");
            String itemTitle = "";
            if (list != null && list.size() > 0){
                //获取高亮后的结果
                itemTitle = list.get(0);
            } else {
                itemTitle = (String) document.get("item_title");
            }
            item.setTitle(itemTitle);
            //添加到列表
            itemList.add(item);
        }
        SearchResult result = new SearchResult();
        result.setItemList(itemList);
        //查询结果总数量
        result.setRecordCount(documents.getNumFound());
        return result;
    }
}
