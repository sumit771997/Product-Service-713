package com.jbk.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class SupplierModel {

	@Min(value = 1, message = "Invalid Supplier Id")
	private long supplierId;
	
	@NotBlank(message = "Supplier name should not be blank")
	@Pattern(regexp = "^[a-zA-z ]+$", message = "Supplier name should only contain alphabets and spaces")
	private String supplierName;
	
	@NotBlank(message = "City name should not be blank")
	@Pattern(regexp = "^[a-zA-z ]+$", message = "City name should only contain alphabets and spaces")
	private String city;
	
	@Min(value = 100000, message = "Invalid postal code")
	@Max(value = 999999, message = "Invalid postal code")
	private int postalCode;
	
	@NotBlank(message = "Country name should not be blank")
	@Pattern(regexp = "^[a-zA-z ]+$", message = "Country name should only contain alphabets and spaces")
	private String countryName;
	
	@Pattern(regexp = "^[1-9][0-9]{9}$", message = "Mobile number should only contains digits, not start with 0")
	private String mobileNo;
	
	public SupplierModel() {
		// TODO Auto-generated constructor stub
	}

	public SupplierModel(long supplierId, String supplierName, String city, int postalCode, String countryName,
			String mobileNo) {
		super();
		this.supplierId = supplierId;
		this.supplierName = supplierName;
		this.city = city;
		this.postalCode = postalCode;
		this.countryName = countryName;
		this.mobileNo = mobileNo;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	@Override
	public String toString() {
		return "SupplierModel [supplierId=" + supplierId + ", supplierName=" + supplierName + ", city=" + city
				+ ", postalCode=" + postalCode + ", countryName=" + countryName + ", mobileNo=" + mobileNo + "]";
	}
	
	
	
}
