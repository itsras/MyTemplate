package com.sras.datamodel;

public class UserData extends WithId
{
	private String userName;
	private String firstName;
	private String lastName;
	private String middleName;
	private String dob;
	private int sex;
	private String maritalStatus;
	private AddressData address;
	private long addressId;
	private Object image;
	private String nationality;
	private transient String password;
	private String mailId;
	private boolean isActive;
	private String activationKey;

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getMiddleName()
	{
		return middleName;
	}

	public void setMiddleName(String middleName)
	{
		this.middleName = middleName;
	}

	public String getDob()
	{
		return dob;
	}

	public void setDob(String dob)
	{
		this.dob = dob;
	}

	public int getSex()
	{
		return sex;
	}

	public void setSex(int sex)
	{
		this.sex = sex;
	}

	public String getMaritalStatus()
	{
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus)
	{
		this.maritalStatus = maritalStatus;
	}

	public AddressData getAddress()
	{
		return address;
	}

	public void setAddress(AddressData address)
	{
		this.address = address;
	}

	public long getAddressId()
	{
		return addressId;
	}

	public void setAddressId(long addressId)
	{
		this.addressId = addressId;
	}

	public Object getImage()
	{
		return image;
	}

	public void setImage(Object image)
	{
		this.image = image;
	}

	public String getNationality()
	{
		return nationality;
	}

	public void setNationality(String nationality)
	{
		this.nationality = nationality;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getMailId()
	{
		return mailId;
	}

	public void setMailId(String mailId)
	{
		this.mailId = mailId;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getActivationKey() {
		return activationKey;
	}

	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}
}
