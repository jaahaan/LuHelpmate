package com.example.luhelpmate;

public class Object {



    /**
     * Faculty member
     */
    private String facultyName;
    private String rank;
    private String contactFaculty;
    private String email;
    private int image;

    public Object(String facultyName, String rank, String contactFaculty, String email, int image) {
        this.facultyName = facultyName;
        this.rank = rank;
        this.contactFaculty = contactFaculty;
        this.email = email;
        this.image = image;
    }

    /**
     * Returns the faculty members info.
     */
    public String getFacultyName() {
        return facultyName;
    }

    public String getRank() {
        return rank;
    }

    public String getContactFaculty() {
        return contactFaculty;
    }

    public String getEmail() {
        return email;
    }

    public int getImage() {
        return image;
    }


    /**
     * cr
     */
    private String crName;
    private String id;
    private String contact;
    private String batch;


    public Object(String batch, String crName, String id, String contact) {
        this.batch = batch;
        this.crName = crName;
        this.id = id;
        this.contact = contact;
    }

    /**
     * Returns the cr info
     */
    public String getBatch() {
        return batch;
    }

    public String getCrName() {
        return crName;
    }

    public String getId() {
        return id;
    }

    public String getContact() {
        return contact;
    }


    /**
     * Book
     */
    private String bookName;
    private String authorName;
    private String edition;
    private String courseCode;
    private String bUrl;

    public Object(String bookName, String authorName, String edition, String courseCode, String bUrl) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.edition = edition;
        this.courseCode = courseCode;
        this.bUrl = bUrl;
    }

    /**
     * Returns the book info.
     */
    public String getBookName() {
        return bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getEdition() {
        return edition;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getbUrl() {
        return bUrl;
    }

    /**
     * Notice
     */
    private String noticeDate;
    private String noticeTitle;
    private String nUrl;

    public Object(String noticeDate, String noticeTitle, String nUrl) {
        this.noticeDate = noticeDate;
        this.noticeTitle = noticeTitle;
        this.nUrl = nUrl;
    }

    /**
     * Returns the notice info.
     */
    public String getNoticeDate() {
        return noticeDate;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public String getnUrl() {
        return nUrl;
    }
}
