package com.mcorvera.socialapp.model;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mcorvera.socialapp.model.audit.Audit;

@Entity
@Table(name="users",uniqueConstraints= {
								@UniqueConstraint(columnNames={"email"}),
								@UniqueConstraint(columnNames={"username"})})
public class User extends Audit<Long> {
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Email
	@Column(nullable = false, length = 100)
	private String email;
	@NotBlank
	@Column(nullable = false, length = 50)
	private String name;
	
	@NotBlank
	@Column(nullable = false, length = 50)
	private String lastname;
	
	@NotBlank
	@Column(nullable = false, length = 100)
	private String username;
	
	@JsonIgnore
	@Column(length = 200)
	private String password;
	
	@Column(nullable = false, length = 25)
	private String phone;
	
	@Column(nullable = false, name= "isaccountnonexpired")
	private Boolean isaccountnonexpired=true;
	
	@Column(nullable = false, name= "isaccountnonlocked")
	private Boolean isaccountnonlocked=true;
	
	@Column(nullable = false, name = "iscredentialsnonexpired")
	private Boolean iscredentialsnonexpired=true;

	@Column(nullable = false, name="isenabled")
	private Boolean isenabled=true;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles",
				joinColumns = @JoinColumn(name="user_id"),
				inverseJoinColumns = @JoinColumn(name="role_id"))
	private List<Role> roles =new ArrayList<>();
	
	//Attributes for Social Provider
	@Column(length=100)
	private String providerId;
	
	@Column(length=30)
	@Enumerated(EnumType.STRING)
	private ProviderNameEnum provider;
	
	@Column(length=400)
	private String imageUrl;
	
	public User() {
		super();
	}
	

	
	public User( String providerId, ProviderNameEnum provider, @NotBlank @Email String email, @NotBlank String username, @NotBlank String name, @NotBlank String lastname, 
			String password,
			@NotBlank String phone,
			List<Role> roles) {
		super();
		this.providerId=providerId;
		this.provider=provider;
		this.email = email;
		this.username = username;
		this.name = name;
		this.lastname=lastname;
		this.password = password;
		this.phone=phone;
		this.roles=roles;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public Boolean getIsaccountnonexpired() {
		return isaccountnonexpired;
	}
	public void setIsaccountnonexpired(Boolean isaccountnonexpired) {
		this.isaccountnonexpired = isaccountnonexpired;
	}
	public Boolean getIsaccountnonlocked() {
		return isaccountnonlocked;
	}
	public void setIsaccountnonlocked(Boolean isaccountnonlocked) {
		this.isaccountnonlocked = isaccountnonlocked;
	}
	public Boolean getIscredentialsnonexpired() {
		return iscredentialsnonexpired;
	}
	public void setIscredentialsnonexpired(Boolean iscredentialsnonexpired) {
		this.iscredentialsnonexpired = iscredentialsnonexpired;
	}
	public Boolean getIsenabled() {
		return isenabled;
	}
	public void setIsenabled(Boolean isenabled) {
		this.isenabled = isenabled;
	}
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public void setCreatedBy(Long createdBy) {
		// TODO Auto-generated method stub
		if(createdBy==0)
			createdBy=this.id;
		super.setCreatedBy(createdBy);
	}
	@Override
	public void setLastModifiedBy(Long lastModifiedBy) {
		// TODO Auto-generated method stub
		if(lastModifiedBy==0)
			lastModifiedBy=this.id;
		super.setLastModifiedBy(lastModifiedBy);
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}


	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}



	public ProviderNameEnum getProvider() {
		return provider;
	}



	public void setProvider(ProviderNameEnum provider) {
		this.provider = provider;
	}

	
}
