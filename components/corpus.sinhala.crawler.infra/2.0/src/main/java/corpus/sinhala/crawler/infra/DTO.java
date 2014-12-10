package corpus.sinhala.crawler.infra;

public class DTO {
	String url;
	int year;
	int month;
	int date;
	
	public DTO(String url, int year, int month, int date) {
		this.url = url;
		this.year = year;
		this.month = month;
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}
}
