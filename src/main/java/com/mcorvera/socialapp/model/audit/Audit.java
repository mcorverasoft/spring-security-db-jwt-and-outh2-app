package com.mcorvera.socialapp.model.audit;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)	
public abstract class Audit<T> {
	@CreatedDate
	@Column(nullable = false)
	private Instant createdAt;
	@LastModifiedDate
	@Column(nullable = false)
	private Instant updatedAt;
	@CreatedBy
	private T createdBy;
	@LastModifiedBy
	private T lastModifiedBy;
	
	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	public Instant getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
	public T getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(T createdBy) {
		this.createdBy = createdBy;
	}
	public T getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(T lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
}
