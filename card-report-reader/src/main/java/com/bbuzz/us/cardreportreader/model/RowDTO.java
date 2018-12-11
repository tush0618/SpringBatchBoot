package com.bbuzz.us.cardreportreader.model;

public class RowDTO {
	private String date;
	private String trans;
	private String amount;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTrans() {
		return trans;
	}
	public void setTrans(String trans) {
		this.trans = trans;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "RowDTO [date=" + date + ", trans=" + trans + ", amount=" + amount + "]";
	}
	
	
}
