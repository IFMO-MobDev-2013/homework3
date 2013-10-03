package ru.itmo.homework3;

/**
 * Created with IntelliJ IDEA.
 * User: Mikhail
 * Date: 03.10.13
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */
public class GoogleImageBean
{
    private String thumbUrl;
    private String title;

    public String getThumbUrl()
    {
        return thumbUrl;
    }

    public void setThumbUrl(String url)
    {
        this.thumbUrl = url;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}