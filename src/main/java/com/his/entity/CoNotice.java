package com.his.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="HIS_CO_NOTICE")
public class CoNotice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer coNoticeId;
	private String coNoticeStatus;
	private String s3Url;
	private LocalDate coGenDate;
	private LocalDate coStartDate;
	private LocalDate coEndDate;
	private Integer benefitAmount;
	private String isGenerated;
	private LocalDate coPrintDate;	
	private Integer appNumber;;
	public Integer getCoNoticeId() {
		return coNoticeId;
	}
	public void setCoNoticeId(Integer coNoticeId) {
		this.coNoticeId = coNoticeId;
	}
	public String getCoNoticeStatus() {
		return coNoticeStatus;
	}
	public void setCoNoticeStatus(String coNoticeStatus) {
		this.coNoticeStatus = coNoticeStatus;
	}
	public String getS3Url() {
		return s3Url;
	}
	public void setS3Url(String s3Url) {
		this.s3Url = s3Url;
	}
	public LocalDate getCoGenDate() {
		return coGenDate;
	}
	public void setCoGenDate(LocalDate coGenDate) {
		this.coGenDate = coGenDate;
	}
	public LocalDate getCoStartDate() {
		return coStartDate;
	}
	public void setCoStartDate(LocalDate coStartDate) {
		this.coStartDate = coStartDate;
	}
	public LocalDate getCoEndDate() {
		return coEndDate;
	}
	public void setCoEndDate(LocalDate coEndDate) {
		this.coEndDate = coEndDate;
	}
	public Integer getBenefitAmount() {
		return benefitAmount;
	}
	public void setBenefitAmount(Integer benefitAmount) {
		this.benefitAmount = benefitAmount;
	}
	public String getIsGenerated() {
		return isGenerated;
	}
	public void setIsGenerated(String isGenerated) {
		this.isGenerated = isGenerated;
	}
	public LocalDate getCoPrintDate() {
		return coPrintDate;
	}
	public void setCoPrintDate(LocalDate coPrintDate) {
		this.coPrintDate = coPrintDate;
	}
	public Integer getAppNumber() {
		return appNumber;
	}
	public void setAppNumber(Integer appNumber) {
		this.appNumber = appNumber;
	}

}
