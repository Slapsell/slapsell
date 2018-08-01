package com.example.user.slapsell;

import com.example.user.slapsell.pojo_model.Products;

import java.util.ArrayList;
import java.util.List;

public class Stores {
    private static List<Products> stores ;
    static {
        stores =  new ArrayList<Products>();
    }

    public static List<Products> getStores(){
        return stores;
    }

    public static List<Products> filterData(String searchString){
        List<Products> searchResults =  new ArrayList<Products>();
        if(searchString != null){
            searchString = searchString.toLowerCase();

            for(Products rec :  stores){
                if(rec.getName().toLowerCase().contains(searchString)){
                    searchResults.add(rec);
                }
            }
        }
        return searchResults;
    }
}