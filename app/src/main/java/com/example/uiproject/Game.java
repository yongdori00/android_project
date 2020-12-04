package com.example.uiproject;

public class Game {

    public String name;

    public String personnel;

    public String genre; // 장르

    public String explain; // 설명

    public byte[] gameimage;


    

    // TODO : get,set 함수 생략

    public void setName(String string) {
        name = string;
    }

    public void setPersonnel(String string) {
        personnel = string;
    }

    public void setGenre(String string) {
        genre = string;
    }

    public void setExplain(String string) {
        explain = string;
    }

    public void setImage(byte[] bitmap){gameimage = bitmap;}


    public String getName() {
        return this.name;
    }

    public String getPersonnel() {
        return this.personnel;
    }

    public String getGenre() {
        return this.genre;
    }

    public String getExplain() {
        return this.explain;
    }

    public byte[] getImage() {return this.gameimage;}
}

