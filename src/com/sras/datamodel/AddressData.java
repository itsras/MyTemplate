package com.sras.datamodel;

public class AddressData extends WithId
{
	private String bldName;
	private String bldNo;
	private String street;
	private String locality;
	private String landmark;
	private String city;
	private String state;
	private String country;
	private String postalcode;
	private String mobileNum;
	private String landNum;
	private String areaCode;

	public String getBldName()
	{
		return bldName;
	}

	public void setBldName(String bldName)
	{
		this.bldName = bldName;
	}

	public String getBldNo()
	{
		return bldNo;
	}

	public void setBldNo(String bldNo)
	{
		this.bldNo = bldNo;
	}

	public String getLocality()
	{
		return locality;
	}

	public String getStreet()
	{
		return street;
	}

	public void setStreet(String street)
	{
		this.street = street;
	}

	public void setLocality(String locality)
	{
		this.locality = locality;
	}

	public String getLandmark()
	{
		return landmark;
	}

	public void setLandmark(String landmark)
	{
		this.landmark = landmark;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getPostalcode()
	{
		return postalcode;
	}

	public void setPostalcode(String postalcode)
	{
		this.postalcode = postalcode;
	}

	public String getMobileNum()
	{
		return mobileNum;
	}

	public void setMobileNum(String mobileNum)
	{
		this.mobileNum = mobileNum;
	}

	public String getLandNum()
	{
		return landNum;
	}

	public void setLandNum(String landNum)
	{
		this.landNum = landNum;
	}

	public String getAreaCode()
	{
		return areaCode;
	}

	public void setAreaCode(String areaCode)
	{
		this.areaCode = areaCode;
	}
}
