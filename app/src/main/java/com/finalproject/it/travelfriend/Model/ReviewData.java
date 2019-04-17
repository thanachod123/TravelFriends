package com.finalproject.it.travelfriend.Model;

public class ReviewData {
    String comment, rating;

    public ReviewData(){
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "ReviewData{" +
                "comment='" + comment + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }
}
