package corpus.sinhala.crawler.infra;

import java.io.IOException;
import java.util.Observable;

public abstract class Generator extends Observable{
	public abstract DTO fetchPage() throws IOException;
}
