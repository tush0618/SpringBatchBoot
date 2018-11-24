package com.bbuzz.us.cardreportreader.model;

public class CreditCardBill {

	private String cardNumber;
	private String billDueDate;
	private Double dueAmount;
	private String cardType;
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getBillDueDate() {
		return billDueDate;
	}
	public void setBillDueDate(String billDueDate) {
		this.billDueDate = billDueDate;
	}
	public Double getDueAmount() {
		return dueAmount;
	}
	public void setDueAmount(Double dueAmount) {
		this.dueAmount = dueAmount;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	@Override
	public String toString() {
		return "CreditCardBill [cardNumber=" + cardNumber + ", billDueDate=" + billDueDate + ", dueAmount=" + dueAmount
				+ ", cardType=" + cardType + "]";
	}
	public CreditCardBill(String cardNumber, String billDueDate, Double dueAmount, String cardType) {
		super();
		this.cardNumber = cardNumber;
		this.billDueDate = billDueDate;
		this.dueAmount = dueAmount;
		this.cardType = cardType;
	}
	public CreditCardBill() {
		
	}
	
	
	
}
