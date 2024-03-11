package com.atguigu.spzx.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.atguigu.spzx.model.dto.h5.ProductSkuDto;
import com.atguigu.spzx.model.entity.product.Brand;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.entity.product.ProductDetails;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.vo.h5.ProductItemVo;
import com.atguigu.spzx.product.mapper.ProductMapper;
import com.atguigu.spzx.product.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductMapper productMapper;


    @Override
    public List<ProductSku> findIndexSkuList() {
        return productMapper.findIndexSkuList();
    }

    @Override
    public PageInfo findByPage(Integer pageNum, Integer pageSize, ProductSkuDto productSkuDto) {
        PageHelper.startPage(pageNum,pageSize);
        List<ProductSku> list = productMapper.findByPage(productSkuDto);

        return new PageInfo(list);
    }

    @Cacheable(value = "brand",key = "'allBrand'")
    @Override
    public List<Brand> findAllBrand() {
        return productMapper.findAllBrand();
    }

    @Override
    public ProductItemVo item(Long skuId) {
        ProductItemVo productItemVo = new ProductItemVo();
        ProductSku productSku = productMapper.getSkuBySkuId(skuId);
        Product product = productMapper.getSpuBySpuId(productSku.getProductId());

        List<String> sliderUrlList = null;
        String sliderUrls =  product.getSliderUrls();
        if (!StringUtils.isEmpty(sliderUrls)){
            String[] split = sliderUrls.split(",");
            sliderUrlList = Arrays.asList(split);
        }

        List<String> detailsImageUrlList = null;
        ProductDetails productDetails = productMapper.getProductDetailsByProductId(productSku.getProductId());
        String imageUrls  = productDetails.getImageUrls();
        if (!StringUtils.isEmpty(imageUrls)){
            String[] split = imageUrls.split(",");
            detailsImageUrlList  = Arrays.asList(split);
        }

        String specValue = product.getSpecValue();
        JSONArray jsonArray = JSONArray.parseArray(specValue);

        HashMap<String, Object> skuSpecValueMap = new HashMap<>();
        List<ProductSku> skuList = productMapper.getSkuListByProductId(productSku.getProductId());
        skuList.forEach(sku ->{
            skuSpecValueMap.put(sku.getSkuSpec(),sku.getId());
        });

        productItemVo.setProduct(product);
        productItemVo.setProductSku(productSku);
        productItemVo.setSliderUrlList(sliderUrlList);
        productItemVo.setDetailsImageUrlList(detailsImageUrlList);
        productItemVo.setSkuSpecValueMap(skuSpecValueMap);
        productItemVo.setSpecValueList(jsonArray);


        return productItemVo;
    }

    @Override
    public ProductSku getBySkuId(Long skuId) {
        ProductSku productSku = productMapper.getSkuBySkuId(skuId);

        return productSku;
    }


}
