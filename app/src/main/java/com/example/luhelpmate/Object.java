package com.example.luhelpmate;

public class Object {

    /** Faculty Name */
    private String facultyName;

    /** Rank */
    private String rank;

    /** contact*/
    private String contactFaculty;

    /** email*/
    private String email;

    /** Image*/
    private int image;


    public Object(String facultyName, String rank, String contactFaculty, String email, int image){
        this.facultyName = facultyName;
        this.rank = rank;
        this.contactFaculty = contactFaculty;
        this.email = email;
        this.image = image;
    }

    public String getFacultyName(){return facultyName;}
    public String getRank(){return rank;}
    public String getContactFaculty(){return contactFaculty;}
    public String getEmail(){return email;}
    public int getImage(){return image;}


    /** cr Name */
    private String crName;

    /** id */
    private String id;

    /** contact*/
    private String contact;

    private String batch;


    public Object(String batch,String crName, String id, String contact){
        this.batch = batch;
        this.crName = crName;
        this.id = id;
        this.contact = contact;
    }

    public String getBatch(){return batch;}
    public String getCrName(){return crName;}
    public String getId(){return id;}
    public String getContact(){return contact;}



    /** Book Name */
    private String bookName;

    /** Author Name */
    private String authorName;

    /** Edition */
    private String edition;

    /** Course Code */
    private String courseCode;

    /** Website URL of the book */
    private String bUrl;
    public Object(String bookName, String authorName, String edition,String courseCode, String bUrl) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.edition = edition;
        this.courseCode = courseCode;
        this.bUrl =bUrl;
    }

    /**
     * Returns the name of the book.
     */
    public String getBookName(){return bookName;}

    /**
     * Returns the Author of the book.
     */
    public String getAuthorName(){return authorName;}

    /**
     * Returns the Edition of the book.
     */
    public String getEdition(){return edition;}

    /**
     * Returns the Course Code of the book.
     */
    public String getCourseCode(){return courseCode;}

    /**
     * Returns the website URL to find more information about the book.
     */
    public String getbUrl() {
        return bUrl;
    }

}
