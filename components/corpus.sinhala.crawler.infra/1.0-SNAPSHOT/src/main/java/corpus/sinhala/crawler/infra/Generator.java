package corpus.sinhala.crawler.infra;

import java.io.IOException;
import java.util.Observable;

import org.jsoup.nodes.Document;

public abstract class Generator extends Observable{
	public abstract Document fetchPage() throws Exception;
}
