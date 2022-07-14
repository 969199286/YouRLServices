package com.YouRL.Entity;


import javax.persistence.*;

@Table(name = "long_to_short")
@Entity
public class LongToShort {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "longurl")
    private String longUrl;
    @Column(name = "shorturl")
    private String shortUrl;

    public LongToShort() {
    }

    public LongToShort(String longUrl, String shortUrl) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    @Override
    public String toString() {
        return "LongToShort{" +
                "longUrl='" + longUrl + '\'' +
                ", shortUrl='" + shortUrl + '\'' +
                '}';
    }
}
