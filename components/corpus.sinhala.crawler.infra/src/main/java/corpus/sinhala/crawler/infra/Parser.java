package corpus.sinhala.crawler.infra;

public interface Parser {
	
	public String getContent();
	public String getTitle();
	public String getAuthor();
	public String getUrl();
	public String getYear();
	public String getMonth();
	public String getDate();
}
