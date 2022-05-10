package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.SQLiteDB;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class DatabaseAccountDAO implements AccountDAO {
    private SQLiteDB sqLiteDB;

    public DatabaseAccountDAO() {
         sqLiteDB = SQLiteDB.getInstance();
    }

    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase db = sqLiteDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT `accountNo` FROM `account`", null);

        List <String> accountNumbers = new LinkedList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String accountNo = cursor.getString(cursor.getColumnIndexOrThrow("accountNo"));
            accountNumbers.add(accountNo);
        }
        return accountNumbers;

    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db = sqLiteDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM `account`", null);

        List <Account> accountList = new LinkedList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            String accountNo = cursor.getString(cursor.getColumnIndexOrThrow("accountNo"));
            String bankName = cursor.getString(cursor.getColumnIndexOrThrow("bankName"));
            String accountHolderName = cursor.getString(cursor.getColumnIndexOrThrow("accountHolderName"));
            double balance = cursor.getDouble(cursor.getColumnIndexOrThrow("balance"));

            Account account = new Account(accountNo, bankName, accountHolderName, balance);

            accountList.add(account);
        }
        return accountList;

    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = sqLiteDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM `account` WHERE accountNo=" + accountNo + "", null);

        if(cursor.moveToFirst()) {
            return new Account(
                    cursor.getString(cursor.getColumnIndexOrThrow("accountNo")),
                    cursor.getString(cursor.getColumnIndexOrThrow("bankName")),
                    cursor.getString(cursor.getColumnIndexOrThrow("accountHolderName")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("balance"))
            );
        }else {
            throw new InvalidAccountException(accountNo + "not found.");
        }
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = sqLiteDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("accountNo", account.getAccountNo());
        contentValues.put("bankName", account.getBankName());
        contentValues.put("accountHolderName", account.getAccountHolderName());
        contentValues.put("balance", account.getBalance());

        db.insert("`account`", null, contentValues);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = sqLiteDB.getWritableDatabase();

        String[] args = {accountNo};
        db.delete("`account`", "accountNo = ?", args);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = sqLiteDB.getWritableDatabase();

        Account account = getAccount(accountNo);
        ContentValues contentValues = new ContentValues();

        if (expenseType == ExpenseType.EXPENSE) {
            contentValues.put("balance", account.getBalance() - amount);
        } else if (expenseType == ExpenseType.INCOME){
            contentValues.put("balance", account.getBalance() + amount);
        }

        db.update("`account`", contentValues, "`accountNo` = ?", new String[] {accountNo});
    }
}
