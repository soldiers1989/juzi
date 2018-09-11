package com.jzfq.retail.core.api.service;

import java.util.List;
import java.util.Map;

public interface DataTransferService {

    void initDataImport(List<Map<Integer, String>> sellers,List<Map<Integer, String>> products,
                        List<Map<Integer, String>> productCateBrandAreas,List<Map<Integer, String>> sellerTerms,
                        List<Map<Integer, String>> sellerLoginPermissions,
                        List<Map<Integer, String>> productCates,List<Map<Integer, String>> productBrands);

    void batchCallAccounts(List<Map<Integer, String>> sellers);

    void saveInitData(List<Map<Integer, String>> sellers, List<Map<Integer, String>> products, List<Map<Integer, String>> productCateBrandAreases, List<Map<Integer, String>> sellerTerms, List<Map<Integer, String>> sellerLoginPermissions, List<Map<Integer, String>> productCates, List<Map<Integer, String>> productBrands);
}
