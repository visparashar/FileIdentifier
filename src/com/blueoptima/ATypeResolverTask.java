package blueoptima;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shivek on 4/4/17.
 */
public abstract class ATypeResolverTask implements ITypeResolver {

    @Getter @Setter private String baseURL;

	    @Getter @Setter private Status status = Status.NOT_INITIALIZED;

    @Getter @Setter private List<HashMap<String, String>> result;

    @Getter @Setter private boolean finished = false;

    @Getter @Setter private FileName fileName;

    @Getter private AWebPageParser parser;

    @Getter protected HttpURLConnection connection;

    @Getter protected StringBuilder content;

    @Getter protected Document doc;


    public ATypeResolverTask(String baseURL, AWebPageParser parser, FileName fileName) {
        this.baseURL = baseURL;
        this.parser = parser;
        this.fileName = fileName;
        setStatus(Status.INITIALIZED);
    }

    protected void connect() throws ConnectionException {
        setStatus(Status.CONNECTING);
        try {
            URL url = new URL(getUrl());
            connection = (HttpURLConnection) url.openConnection();
            if (getConnection().getResponseCode() != 200) {
                throw new IOException(String.format("Cannot connect to %s", getUrl()));
            }
        } catch (MalformedURLException e) {
            throw new ConnectionException("URL not valid");
        } catch (IOException e) {
            throw new ConnectionException("Unable to read");
        }
        this.setStatus(Status.CONNECTED);
    }

    protected void fetch()  {
        setStatus(Status.FETCHING);
        try {
            String line = null;
            content = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(getConnection().getInputStream()));
            while ((line = in.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            getConnection().disconnect();
        }
    }

    protected void parse() throws ParsingException {
        setStatus(Status.PARSING);
        doc = Jsoup.parse(getContent().toString());
        getParser().parse(getDoc(), fileName.getFullFileName());
        result = parser.getResult();
        finished = isfinished();
        setStatus(Status.COMPLETED);
    }

    public void execute() {
        try {
            do {
                connect();
                fetch();
                parse();
            }while(!finished);
        } catch (ParsingException e) {
            this.setStatus(Status.ERROR);
        } catch (ConnectionException e) {
            this.setStatus(Status.ERROR);
        } catch (Exception e) {
            this.setStatus(Status.ERROR);
        } finally {
            if (getStatus() != Status.COMPLETED) {
                this.setStatus(Status.ERROR);
            }
        }
    }

    @Override
    public List<HashMap<String, String>> fetchResult() {
        if (getStatus() == Status.COMPLETED) {
            return getResult();
        } else return null;
    }

    protected String getUrl() {
        return String.format(getBaseURL(), getFileName().getExt());
    }

    protected boolean isfinished(){
        return true;
    }

	

}
