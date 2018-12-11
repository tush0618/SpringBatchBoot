package com.bbuzz.us.cardreportreader.model;

import java.util.List;

public class ReportDTO {

	private String lineType;
	private String runDate ;
	private String reportDate;
	private String totalTrans;
	private String totalAmt;
	private List<RowDTO> rows;
	private String rejCheckfreeTrans;
	private String rejCheckfreeAmt;
	private String rejotherTrans;
	private String rejOtherAmt;
	private String fedTrans;
	private String fedAmt;
	public String getLineType() {
		return lineType;
	}
	public void setLineType(String lineType) {
		this.lineType = lineType;
	}
	public String getRunDate() {
		return runDate;
	}
	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getTotalTrans() {
		return totalTrans;
	}
	public void setTotalTrans(String totalTrans) {
		this.totalTrans = totalTrans;
	}
	public String getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(String totalAmt) {
		this.totalAmt = totalAmt;
	}
	public List<RowDTO> getRows() {
		return rows;
	}
	public void setRows(List<RowDTO> rows) {
		this.rows = rows;
	}
	public String getRejCheckfreeTrans() {
		return rejCheckfreeTrans;
	}
	public void setRejCheckfreeTrans(String rejCheckfreeTrans) {
		this.rejCheckfreeTrans = rejCheckfreeTrans;
	}
	public String getRejCheckfreeAmt() {
		return rejCheckfreeAmt;
	}
	public void setRejCheckfreeAmt(String rejCheckfreeAmt) {
		this.rejCheckfreeAmt = rejCheckfreeAmt;
	}
	public String getRejotherTrans() {
		return rejotherTrans;
	}
	public void setRejotherTrans(String rejotherTrans) {
		this.rejotherTrans = rejotherTrans;
	}
	public String getRejOtherAmt() {
		return rejOtherAmt;
	}
	public void setRejOtherAmt(String rejOtherAmt) {
		this.rejOtherAmt = rejOtherAmt;
	}
	public String getFedTrans() {
		return fedTrans;
	}
	public void setFedTrans(String fedTrans) {
		this.fedTrans = fedTrans;
	}
	public String getFedAmt() {
		return fedAmt;
	}
	public void setFedAmt(String fedAmt) {
		this.fedAmt = fedAmt;
	}
	@Override
	public String toString() {
		return "ReportDTO [lineType=" + lineType + ", runDate=" + runDate + ", reportDate=" + reportDate
				+ ", totalTrans=" + totalTrans + ", totalAmt=" + totalAmt + ", rows=" + rows + ", rejCheckfreeTrans="
				+ rejCheckfreeTrans + ", rejCheckfreeAmt=" + rejCheckfreeAmt + ", rejotherTrans=" + rejotherTrans
				+ ", rejOtherAmt=" + rejOtherAmt + ", fedTrans=" + fedTrans + ", fedAmt=" + fedAmt + "]";
	}
	
	
	
	
	
}
