package com.atguigu.spzx.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.atguigu.spzx.model.dto.h5.ProductSkuDto;
import com.atguigu.spzx.model.dto.product.SkuSaleDto;
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
import java.util.Map;

@Service
public class ProductServiceImpl  implements ProductService {


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

        //spu + sku
        ProductSku productSku = productMapper.getSkuBySkuId(skuId);
        Product product = productMapper.getSpuBySpuId(productSku.getProductId());


        //轮播图
        List<String> sliderUrlList = null;
        String sliderUrls = product.getSliderUrls();
        if(!StringUtils.isEmpty(sliderUrls)){
            String[] split = sliderUrls.split(",");
            sliderUrlList = Arrays.asList(split);
        }

        //详情图
        List<String> detailsImageUrlList = null;
        ProductDetails productDetails = productMapper.getProductDetailsByProductId(productSku.getProductId());
        String imageUrls = productDetails.getImageUrls();
        if(!StringUtils.isEmpty(imageUrls)){
            String[] split = imageUrls.split(",");
            detailsImageUrlList = Arrays.asList(split);
        }

        //商品规格信息(当前spu使用的规则)
        // [{"key":"颜色","valueList":["黑色","白色"]},{"key":"续航","valueList":["500km","800km"]}]   在java中可以一个JSONArray类型
        String specValue = product.getSpecValue();
        JSONArray jsonArray = JSONArray.parseArray(specValue);//注意：类名  com.alibaba.fastjson.JSONArray

        //sku的规则组合（sku_spec）  +   sku的id(id)
        Map<String,Object> skuSpecValueMap = new HashMap<>();
        //根据spu的id查询对应的sku列表
        List<ProductSku> skuList = productMapper.getSkuListByProductId(productSku.getProductId());
        skuList.forEach(sku->{
            skuSpecValueMap.put(sku.getSkuSpec(),sku.getId());//当前sku的skuSpec和id属性作为一组k-v
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

    @Override
    public void updateSkuStockNumAndSaleNum(List<SkuSaleDto> skuSaleDtoList) {
        for (SkuSaleDto skuSaleDto : skuSaleDtoList) {
            //UPDATE `product_sku` SET stock_num = stock_num - ?   , sale_num=sale_num  + ? WHERE id = ?
            productMapper.updateSku(skuSaleDto);
        }
    }


}
