package com.jbk.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.jbk.model.ProductModel;
import com.jbk.model.Product_Supplier_Category;

public interface ProductService 
{

	public boolean addProduct(ProductModel productModel);
	
	public ProductModel getProductById(long productId);
	
	public Product_Supplier_Category getProductWithSCById(long productId);
	
	public boolean deleteProductById(long productId);

	public boolean updateProduct(ProductModel productModel);
	
	public List<ProductModel> getAllProducts();

	public List<ProductModel> sortProduct(String orderType, String property);
	
	public double getMaxProductPrice();

	public List<ProductModel> getMaxPriceProduct();

	public ProductModel getProductByName(String productName);
	
	public List<ProductModel> getAllProducts(double low, double high);
	
	public List<ProductModel> getProductStartWith(String expression);
	
	public double productPriceAverage();
	
	public double countOfTotalProducts();
	
	
	public Map<String, Object> uploadSheet(MultipartFile myFile);

	
}
