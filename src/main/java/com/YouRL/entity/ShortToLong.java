package com.YouRL.entity;


import javax.persistence.*;

@Table(name = "short_to_long")
@Entity
public class ShortToLong {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "shorturl")
    private String shortUrl;

    @Column(name = "longurl")
    private String longUrl;

    public ShortToLong() {
    }

    public ShortToLong(String shortUrl, String longUrl) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    @Override
    public String toString() {
        return "ShortToLong{" +
                "shortUrl='" + shortUrl + '\'' +
                ", longUrl='" + longUrl + '\'' +
                '}';
    }
}
