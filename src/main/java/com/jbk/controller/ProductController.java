package com.jbk.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jbk.exception.ResourceAlreadyExistsException;
import com.jbk.exception.ResourceNotExistsException;
import com.jbk.exception.SomethingWentWrongException;
import com.jbk.model.ProductModel;
import com.jbk.model.Product_Supplier_Category;
import com.jbk.service.ProductService;
import com.jbk.service.impl.ProductServiceImpl;
import jakarta.validation.Valid;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("product")
public class ProductController 
{

	@Autowired
	ProductService service;
	
	@PostMapping("/add-product")
	public ResponseEntity<String> addProduct(@RequestBody @Valid ProductModel productModel) 
	{
		service.addProduct(productModel);
		return ResponseEntity.ok("Product Added !!");
		
	}
	
	@GetMapping("/get-product-by-id/{productId}")
	public ResponseEntity<ProductModel> getProductById(@PathVariable long productId) 
	{
		ProductModel productModel = service.getProductById(productId);
		
		return ResponseEntity.ok(productModel);
		
	}
	
	@GetMapping("/get-product-with-sc/{productId}")
	public ResponseEntity<Product_Supplier_Category> getProductByIdWithSC(@PathVariable long productId) 
	{
		Product_Supplier_Category psc = service.getProductWithSCById(productId);
		
		return ResponseEntity.ok(psc);
		
	}
	
	@DeleteMapping("/delete-product/{productId}") // my code
	public String deleteProduct(@PathVariable long productId)
	{
		return null;
		
	}
	

	@PutMapping("/update-product")
	public String updateProduct() 
	{
		return null;

	}
	
	// ***************************
	
	@GetMapping("/get-all-products")
	public ResponseEntity<List<ProductModel>> getAllProducts() 
	{
		
		return ResponseEntity.ok(service.getAllProducts());
	}
	
	@GetMapping("/sort-products")
	public ResponseEntity<List<ProductModel>> sortProducts(@RequestParam String orderType, @RequestParam String property) 
	{
		return ResponseEntity.ok(service.sortProduct(orderType, property));
	}
	
	@GetMapping("/max-price")
	public ResponseEntity<Double> getMaxProductPrice(@RequestParam String param) 
	{
		return ResponseEntity.ok(service.getMaxProductPrice());
	}
	
	@GetMapping("max-price-product")
	public ResponseEntity<Object> getMaxPriceProduct() 
	{
		return ResponseEntity.ok(service.getMaxPriceProduct());
	}
	
	@GetMapping("/get-product-by-name/{productName}")
	public ResponseEntity<Object> getProductByName(@PathVariable String productName) 
	{
		return ResponseEntity.ok(service.getProductByName(productName));
	}
	
	@PostMapping("upload-sheet")
	public ResponseEntity<Map<String, Object>> uploadSheet(@RequestParam MultipartFile myFile) {
		
		Map<String, Object> map = service.uploadSheet(myFile);
		return ResponseEntity.ok(map);
	}
	
	
}
