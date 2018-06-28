package com.example.user.slapsell;
import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.user.slapsell.pojo_model.Products;

public class search_contentprovider extends ContentProvider {

    private static final String STORES = "stores/"+ SearchManager.SUGGEST_URI_PATH_QUERY+"/*";

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI("com.example.user.search", STORES, 1);
    }

    private static String[] matrixCursorColumns = {"_id",
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_ICON_1,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA };

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        String queryType = "";
        switch(uriMatcher.match(uri)){
            case 1 :
                String query = uri.getLastPathSegment().toLowerCase();
                return getSearchResultsCursor(query);
            default:
                return null;
        }
    }

    private MatrixCursor getSearchResultsCursor(String searchString){
        MatrixCursor searchResults =  new MatrixCursor(matrixCursorColumns);
        Object[] mRow = new Object[4];
        int counterId = 0;
        if(searchString != null){
            searchString = searchString.toLowerCase();

            for(Products rec :  Stores.getStores()){
                if(rec.getName().toLowerCase().contains(searchString)){
                    mRow[0] = ""+counterId++;
                    mRow[1] = rec.getName();

                    mRow[2] = getContext().getResources().getIdentifier(getStoreName(rec.getName()),
                            "drawable", getContext().getPackageName());
                    mRow[3] = ""+counterId++;

                    searchResults.addRow(mRow);
                }
            }
        }
        return searchResults;
    }

    private String getStoreName(String suggestion){
        String suggestionWords[] = suggestion.split(" ");
        return suggestionWords[0].toLowerCase();
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
