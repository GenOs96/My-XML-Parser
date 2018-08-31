
public class CosmosBean implements Comparable{

	private String field_name;
	private String from;
	private String dlxml;
	private String to;
	
	
	public String getField_name() {
		return field_name;
	}
	public void setField_name(String field_name) {
		this.field_name = field_name;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getDlxml() {
		return dlxml;
	}
	public void setDlxml(String dlxml) {
		this.dlxml = dlxml;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
	@Override
	public int compareTo(Object o) {
		int index_bean= Integer.parseInt(((CosmosBean)o).getTo());
        /* For Ascending order*/
        return Integer.parseInt(this.to) - index_bean;

	}
	
}
