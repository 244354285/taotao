package com.taotao.search.service;

import com.taotao.search.pojo.SearchResult;

public interface SearchService {
    SearchResult search(String qureyString,int page,int rows) throws Exception;
}
