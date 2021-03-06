package com.iderson.currencyguide.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import java.sql.SQLException;

public class CurrencyContentProvider extends ContentProvider {

    public static final String PROVIDER_NAME = "com.iderson.currencyguide.organiations";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/organiations" );
    private static final int CONVERTER = 1;
    private static final UriMatcher uriMatcher ;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "organiations", CONVERTER);
    }

    CurrencyDBHelper mCurrencyDBHelper;

    @Override
    public boolean onCreate() {
        mCurrencyDBHelper = new CurrencyDBHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = 0;
        long rowID2 = 0;
        ContentValues contentValues = new ContentValues();
        ContentValues contentValues2 = new ContentValues();
        contentValues.put(CurrencyDBHelper.FIELD_ROW_ID, (String) values.get(CurrencyDBHelper.FIELD_ROW_ID));
        contentValues2.put(CurrencyDBHelper.FIELD_ROW_ID, String.valueOf(values.get(CurrencyDBHelper.FIELD_ROW_ID)));
        for (String n : values.keySet()) {
            if (n.contains("ASK") || n.contains("BID") || n.contains("FULL")) {
                try {
                    mCurrencyDBHelper.addColumn(n);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                contentValues2.put(n, values.getAsString(n));
            } else
                contentValues.put(n, values.getAsString(n));

        }
        if(contentValues2.size()>1)
            rowID = mCurrencyDBHelper.insert(
                    CurrencyDBHelper.CURRENCY_TABLE,
                    contentValues2);
        rowID2 = mCurrencyDBHelper.insert(contentValues);
        Uri _uri=null;
        if(rowID>0){
            _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
        }else {
            try {
                throw new SQLException("Failed to insert : " + uri);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(rowID2>0){
            _uri = ContentUris.withAppendedId(CONTENT_URI, rowID2);
        }else {
            try {
                throw new SQLException("Failed to insert : " + uri);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return _uri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int cnt = 0;
        cnt = mCurrencyDBHelper.del();
        return cnt;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if(uriMatcher.match(uri)== CONVERTER){
            return mCurrencyDBHelper.getOrganizations();
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}