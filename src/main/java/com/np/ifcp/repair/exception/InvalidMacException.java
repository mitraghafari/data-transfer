package com.np.ifcp.repair.exception;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class InvalidMacException extends Exception {
	private static final long serialVersionUID = -5477920392065828878L;
	private ArrayList<String> fields = new ArrayList<String>(), values = new ArrayList<String>();
	private String tbleName;
	private static final ArrayList<String> invalidColumns = new ArrayList<String>(Arrays.asList(new String[] {
			"VEHICLE_ID", "VIN", "CARD_ID", "PAN", "PERIOD", "OPERATION_TIME", "SEQ_NO", "RULE_ID", "CLAZZ" ,"STAGE"}));

	public InvalidMacException(ResultSet rset, String tbleName) throws SQLException {
		super("Invalid mac in table:" + tbleName);
		if (tbleName.indexOf("TBL") > -1)
			tbleName = tbleName.substring(0, tbleName.indexOf("_TBL"));
		this.tbleName = tbleName;
		for (int i = 1; i <= rset.getMetaData().getColumnCount(); i++)
			if (invalidColumns.contains(rset.getMetaData().getColumnName(i))) {
				fields.add(rset.getMetaData().getColumnName(i));
				values.add(rset.getString(i));
			}

	}

	

	public String getQuery() {
		String fieldsString = fields.toString();
		String query = "INSERT INTO USER_SWITCH.INVALIDS (ID,  TABLE_NAME, OPERATION_TIME,"
				+ fieldsString.substring(1, fieldsString.length() - 1)
				+ ") values (USER_SWITCH.INVALIDS_SEQ.NEXTVAL, '" + tbleName + "', SYSDATE";
		for (String value : this.values)
			query += ", '" + (value == null ? "" : value) + "'";
		query += ")";
		return query;
	}

}
