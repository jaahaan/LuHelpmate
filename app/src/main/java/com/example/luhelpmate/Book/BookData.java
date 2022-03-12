package com.example.luhelpmate.Book;

public class BookData {
    String bookName, author, edition, code, pdf, key;

    public BookData(){}

    public BookData(String bookName, String author, String edition, String code, String pdf, String key) {
        this.bookName = bookName;
        this.author = author;
        this.edition = edition;
        this.code = code;
        this.pdf = pdf;
        this.key = key;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
