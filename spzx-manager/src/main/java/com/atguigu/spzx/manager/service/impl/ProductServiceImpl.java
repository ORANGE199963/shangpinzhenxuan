package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.ProductDetailsMapper;
import com.atguigu.spzx.manager.mapper.ProductMapper;
import com.atguigu.spzx.manager.mapper.ProductSkuMapper;
import com.atguigu.spzx.manager.service.ProductService;
import com.atguigu.spzx.model.dto.product.ProductDto;
import com.atguigu.spzx.model.entity.base.ProductUnit;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.entity.product.ProductDetails;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    ProductSkuMapper productSkuMapper;
    @Autowired
    ProductDetailsMapper productDetailsMapper;
    @Override
    public PageInfo findByPage(Integer pageNum, Integer pageSize, ProductDto productDto) {
        PageHelper.startPage(pageNum,pageSize);
        List<Product> list = productMapper.selectList(productDto);
        return new PageInfo(list);
    }

    @Override
    public List<ProductUnit> findAllProductUnit() {
        return productMapper.findAllProductUnit();
    }


        @Override
        public void addProduct(Product product) {

            //1、添加spu，并返回id主键
            productMapper.addProduct(product);

            Long productId = product.getId();//当前spu的id

            //2、添加sku列表
            List<ProductSku> productSkuList = product.getProductSkuList();
            for (int i = 0; i < productSkuList.size(); i++) {
                ProductSku productSku = productSkuList.get(i);

                productSku.setSkuCode(productId + "_" + i);
                productSku.setSkuName(product.getName() + " " + productSku.getSkuSpec());//spu名称 + 当前sku的规格组合
                productSku.setProductId(productId);

                productSkuMapper.addProductSku(productSku);
            }

            //3、添加details
            String detailsImageUrls = product.getDetailsImageUrls();// url1,url2,url3...
            ProductDetails productDetails = new ProductDetails();
            productDetails.setProductId(productId);
            productDetails.setImageUrls(detailsImageUrls);
            productDetailsMapper.insertProductDetails(productDetails);

        }

    @Override
    public Product getByProductId(Long productId) {
        Product product = productMapper.getById(productId);

        //2、根据spuid，查询sku列表，赋值给product对象的productSkuList属性
        List<ProductSku> skuList = productSkuMapper.getByProductId(productId);
        product.setProductSkuList(skuList);

        //3、根据spuid，查询product_details，获取他的image_urls属性赋值给product对象的detailsImageUrls
        ProductDetails productDetails = productDetailsMapper.getByProductId(productId);
        product.setDetailsImageUrls(productDetails.getImageUrls());

        return product;
    }

    @Override
    public void deleteById(Long id) {
        productMapper.deleteById(id);
        productSkuMapper.deleteByProductId(id);
        productDetailsMapper.deleteByProductId(id);
    }

    @Override
    public void updateAuditStatus(Long productId, Integer auditStatus) {
        productMapper.updateAuditStatus(productId,auditStatus);
    }
}
