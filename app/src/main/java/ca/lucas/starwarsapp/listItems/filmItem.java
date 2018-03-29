package ca.lucas.starwarsapp.listItems;

/**
 * Created by lucas-asus on 2018-03-26.
 */

public class filmItem {
    private String title;
    private String episode;
    private String crawl;
    private String director;
    private String releaseDate;
    private String producer;
    private String url;


    public filmItem(String title, String episode, String crawl,String director, String date, String producer,String url)
    {
        this.title = title;
        this.episode =  episode;
        this.crawl = crawl;
        this.director = director;
        this.releaseDate = date;
        this.producer = producer;
        this.url = url;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getEpisode()
    {
        return this.episode;
    }

    public String getCrawl()
    {
        return this.crawl;
    }

    public String getDirector() { return this.director; }

    public String getPubDate()
    {
        return this.releaseDate;
    }

    public String getCreator()
    {
        return this.producer;
    }

    public String getUrl() { return this.url; }
}
