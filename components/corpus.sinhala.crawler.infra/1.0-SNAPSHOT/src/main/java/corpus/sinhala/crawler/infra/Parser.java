package corpus.sinhala.crawler.infra;

public interface Parser {
	
	public String getContent() throws Exception;
	public String getTitle() throws Exception;
	public String getAuthor() throws Exception;
	public String getUrl() throws Exception;
	public String getYear() throws Exception;
	public String getMonth() throws Exception;
	public String getDate() throws Exception;
	public String getCategory() throws Exception;
}
