package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class SQLiteDB extends SQLiteOpenHelper {

    private static SQLiteDB sqLiteDB = null;

    public static void createInstance (Context context) {

        sqLiteDB = new SQLiteDB(context);
    }

    public static SQLiteDB getInstance() {

        return sqLiteDB;
    }

    private SQLiteDB(@Nullable Context context) {
        super(context, "190377T", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `account` (" +
                "`accountNo` VARCHAR(255) NOT NULL," +
                " `bankName` VARCHAR(255) NOT NULL, " +
                "`accountHolderName` VARCHAR(255) NOT NULL, " +
                "`balance` DOUBLE NOT NULL," +
                " PRIMARY KEY (`accountNo`))");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `transaction` (" +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                "`date` DATE NOT NULL," +
                "`accountNo` VARCHAR(255) NOT NULL,"+
                "`expenseType` VARCHAR(255) NOT NULL," +
                "`amount` DOUBLE NOT NULL," +
                "CONSTRAINT fk_account FOREIGN KEY (`accountNo`) REFERENCES `account`(`accountNo`))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS `account`");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS `transaction`");

        onCreate(sqLiteDatabase);

    }
}
