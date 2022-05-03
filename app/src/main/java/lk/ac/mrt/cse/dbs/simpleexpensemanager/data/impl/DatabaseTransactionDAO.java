package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.SQLiteDB;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DatabaseTransactionDAO implements TransactionDAO {

    private SQLiteDB sqLiteDB;

    public DatabaseTransactionDAO() {
        sqLiteDB = SQLiteDB.getInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = sqLiteDB.getWritableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String DateString= dateFormat.format(date);

        ContentValues contentValues = new ContentValues();
        contentValues.put("date", DateString);
        contentValues.put("accountNo", accountNo);
        contentValues.put("expenseType", expenseType.toString());
        contentValues.put("amount", amount);

        db.insert("`transaction`", null, contentValues);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase db = sqLiteDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM `transaction`", null);
        return getLogsList(cursor);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase db = sqLiteDB.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM `transaction` LIMIT " + limit, null);

        return getLogsList(cursor);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Transaction> getLogsList(Cursor cursor) {

        Map<String,Integer> columns = new HashMap<>();
        columns.put("id",cursor.getColumnIndex("id"));
        columns.put("date",cursor.getColumnIndex("date"));
        columns.put("accountNo",cursor.getColumnIndex("accountNo"));
        columns.put("expenseType",cursor.getColumnIndex("expenseType"));
        columns.put("amount",cursor.getColumnIndex("amount"));

        List<Transaction> logsList = new LinkedList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            try {
                logsList.add(new Transaction(
                        new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(columns.get("date"))),
                        cursor.getString(columns.get("accountNo")),
                        ExpenseType.valueOf(cursor.getString(columns.get("expenseType"))),
                        cursor.getDouble(columns.get("amount"))
                ));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return logsList;
    }
}
