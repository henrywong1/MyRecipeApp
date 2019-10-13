package com.example.henry.myrecipeapp;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Henry on 10/12/2019.
 */

public class Recipe {


    private String recipeTitle;
    private String recipeImageURL;
    private String recipeURL;
    private Bitmap recipeImage;

    public Bitmap getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(Bitmap recipeImage) {
        this.recipeImage = recipeImage;
    }


    public Recipe(String title, String imgURL, String recipeURL) {
        this.recipeTitle = title;
        this.recipeImageURL = imgURL;
        this.recipeURL = recipeURL;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public String getRecipeImageURL() {
        return recipeImageURL;
    }

    public void setRecipeImageURL(String recipeImageURL) {
        this.recipeImageURL = recipeImageURL;
    }

    public String getRecipeURL() {
        return recipeURL;
    }

    public void setRecipeURL(String recipeURL) {
        this.recipeURL = recipeURL;
    }


}
