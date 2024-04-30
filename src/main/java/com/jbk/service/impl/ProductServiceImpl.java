package com.jbk.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.jbk.dao.ProductDao;
import com.jbk.dao.impl.ProductDaoImpl;
import com.jbk.entity.ProductEntity;
import com.jbk.exception.ResourceAlreadyExistsException;
import com.jbk.exception.ResourceNotExistsException;
import com.jbk.exception.SomethingWentWrongException;
import com.jbk.model.CategoryModel;
import com.jbk.model.ProductModel;
import com.jbk.model.Product_Supplier_Category;
import com.jbk.model.SupplierModel;
//import com.jbk.model.Product;
import com.jbk.service.ProductService;
import com.jbk.utility.ObjectValidator;

@Service
public class ProductServiceImpl implements ProductService 
{

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	ProductDao dao;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private ObjectValidator validator;
	
	Map<Integer, Map<String, String>> rowError= new HashMap<Integer, Map<String, String>>();
	
	Map<String, Object> finalMap=new HashMap<String, Object>();
	List<Integer> rowNumList=new ArrayList<Integer>();
	Map<String, String> validationMap=new HashMap<String, String>();
	int totalRecords=0;

	@Override
	public boolean addProduct(ProductModel productModel) {
		
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		productModel.setProductId(Long.parseLong(timeStamp));
		
		ProductEntity productEntity = mapper.map(productModel, ProductEntity.class);
		
		return dao.addProduct(productEntity);
	}

	@Override
	public ProductModel getProductById(long productId) {
		
		ProductEntity productEntity = dao.getProductById(productId);
		
		if(productEntity!=null)
		{
			ProductModel productModel = mapper.map(productEntity, ProductModel.class);
			
			return productModel;
		}else {
			throw new ResourceNotExistsException("Product Not Exists ID= "+productId);
		}
		
	}
	
	@Override
	public Product_Supplier_Category getProductWithSCById(long productId) {
		
		Product_Supplier_Category psc = null;
		
		ProductModel productModel = getProductById(productId);
		if(productModel!=null)
		{
			psc=new Product_Supplier_Category();
			psc.setProductModel(productModel);
			try {
				SupplierModel supplierModel = restTemplate.getForObject(
						"http://localhost:8082/supplier/get-supplier-by-id/"+productModel.getSupplierId(),
						SupplierModel.class);

				psc.setSupplierModel(supplierModel);
				
			} catch (ResourceAccessException e) {
				psc.setSupplierModel(null);
			}
			
			try {
				CategoryModel categoryModel = restTemplate.getForObject(
						"http://localhost:8083/category/get-category-by-id/"+productModel.getCategoryId(),
						CategoryModel.class);
			
				if(categoryModel.getCategoryId()<=0)
				{
					psc.setCategoryModel(null);
				}else {

					psc.setCategoryModel(categoryModel);
				}
				
				
			} catch (ResourceAccessException e) {
				psc.setCategoryModel(null);
			} 
						
		}else {
			
		}
			
		return psc;
	}

	@Override
	public boolean deleteProductById(long productId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateProduct(ProductModel productModel) {
		
		return dao.updateProduct(mapper.map(productModel, ProductEntity.class));
	}

	
	@Override
	public List<ProductModel> getAllProducts() {
		
		List<ProductEntity> entityList = dao.getAllProducts();
		List<ProductModel> modelList=new ArrayList<ProductModel>();
		
		if(!entityList.isEmpty())
		{
			for (ProductEntity productEntity : entityList) 
			{
				ProductModel productModel = mapper.map(productEntity, ProductModel.class);
				modelList.add(productModel);
			}
			
			return modelList;
		}else {
			throw new ResourceNotExistsException("Product Not Exists");
		}
		
	}

	@Override
	public List<ProductModel> sortProduct(String orderType, String property) {
		
		List<ProductEntity> entityList = dao.sortProduct(orderType, property);
		List<ProductModel> modelList=new ArrayList<ProductModel>();
		
		if(!entityList.isEmpty())
		{
			for (ProductEntity productEntity : entityList) 
			{
				ProductModel productModel = mapper.map(productEntity, ProductModel.class);
				modelList.add(productModel);
			}
			
			return modelList;
		}else {
			throw new ResourceNotExistsException("Product Not Exists");
		}
		
	}
	
	@Override
	public double getMaxProductPrice() {
		double maxProductPrice = dao.getMaxProductPrice();
		if (maxProductPrice > 0) {
			return maxProductPrice;
		} else {
			throw new ResourceNotExistsException("Product Not Exists");
		}

	}
	
	@Override
	public List<ProductModel> getMaxPriceProduct() {
		List<ProductEntity> list = dao.getMaxPriceProduct();

		List<ProductModel> modelList = new ArrayList<ProductModel>();
//		
//		for (ProductEntity productEntity : list) {
//			ProductModel productModel = mapper.map(productEntity, ProductModel.class);
//			modelList.add(productModel);
//		}

		modelList = list.stream().map(productEntity-> mapper.map(productEntity, ProductModel.class)).collect(Collectors.toList());

		return modelList;
	}

	@Override
	public ProductModel getProductByName(String productName) {
		
		ProductEntity dbProduct = dao.getProductByName(productName);
		
		if(dbProduct!=null)
		{
			return mapper.map(dao.getProductByName(productName), ProductModel.class);
		}
		else {
			return null;
		}
		
	}

	@Override
	public List<ProductModel> getAllProducts(double low, double high) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductModel> getProductStartWith(String expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double productPriceAverage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double countOfTotalProducts() {
		// TODO Auto-generated method stub
		return 0;
	}

	private List<ProductModel> readExcel(String filePath){
		List<ProductModel> list= new ArrayList<>();
		// pointout file
		try {
			FileInputStream fis= new FileInputStream(filePath);
			
			// workbook
			Workbook workbook = WorkbookFactory.create(fis);
			
			// sheet
			Sheet sheet = workbook.getSheetAt(0);
			
			totalRecords = sheet.getLastRowNum();
			// rows
			Iterator<Row> rows = sheet.rowIterator();
			
			// iterate rows and pointout every row
			
			while (rows.hasNext()) {
				Row row = rows.next();
				int rowNum = row.getRowNum();
				if(rowNum==0)
				{
					continue;
				}
				// for every row will create one ProductModel object
				ProductModel productModel=new ProductModel();
				
				String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
				productModel.setProductId(Long.parseLong(timeStamp)+rowNum);

				// iterate row and pointout every cell
				Iterator<Cell> cells = row.cellIterator();
				
				while (cells.hasNext()) {
					Cell cell = cells.next();
					
					// get data
					int columnIndex = cell.getColumnIndex();
					switch (columnIndex) {
					case 0:{
						String productName = cell.getStringCellValue();
						productModel.setProductName(productName);
						break;
					}
					case 1:{
						double supplierId = cell.getNumericCellValue();
												
						productModel.setSupplierId((long) supplierId);
						break;
					}
					case 2:{
						double categoryId = cell.getNumericCellValue();
												
						productModel.setCategoryId((long) categoryId);
						break;
					}
					case 3:{
						double productQty = cell.getNumericCellValue();
						productModel.setProductQty((int) productQty);
						break;
					}
					case 4:{
						double productPrice = cell.getNumericCellValue();
						productModel.setProductPrice(productPrice);
						break;
					}
					case 5:{
						double charges = cell.getNumericCellValue();
						productModel.setDeliveryCharges((int) charges);
						break;
					}
				
					}
					// add product into list
//					list.add(productModel);
				}
				// check data is proper or not
				validationMap = validator.validateProduct(productModel);
				
				if(!validationMap.isEmpty())
				{
					// add validationMap with rowNum
					rowError.put(rowNum+1, validationMap);
					
					
				}else {
					// check its exist or not in DB
					ProductModel dbProduct = getProductByName(productModel.getProductName());
					if(dbProduct!=null) {
						rowNumList.add(rowNum+1);
					}else {
						list.add(productModel);
					}
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	@Override
	public Map<String, Object> uploadSheet(MultipartFile myFile) {

		int isAddedCounter=0;
		int alreadyExistCounter=0;
		int issueCounter=0;
		
		try {
			String path="uploaded";
			String fileName=myFile.getOriginalFilename();
			FileOutputStream fos= new FileOutputStream(path+File.separator+fileName);
			
			byte[] data = myFile.getBytes();
			
			fos.write(data);
			
			// read excel
			List<ProductModel> list = readExcel(path+File.separator+fileName);
			int rowNum=0;
			for (ProductModel productModel : list) {
				rowNum=rowNum+1;
				ProductEntity productEntity = mapper.map(productModel, ProductEntity.class);
				
				try {
					boolean isAdded = dao.addProduct(productEntity);
					if(isAdded)
					{
						isAddedCounter=isAddedCounter+1;
					}
					
				}catch (SomethingWentWrongException e) {
					issueCounter=issueCounter+1;
					
				}
			}
			
			finalMap.put("Total Records In Sheet", totalRecords);
			finalMap.put("Uploaded Records In DB", isAddedCounter);
			finalMap.put("Total Exists Records In DB", rowNumList.size());
			finalMap.put("Row Num, Exists Records In DB", rowNumList);
			finalMap.put("Total Excluded Record Count", rowError.size());
			finalMap.put("Bad Record Row Number", rowError);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return finalMap;
	}

	
	

	
}