/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.BeforeClass;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.SQLiteDB;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest  {
    private static ExpenseManager expenseManager;

//    @BeforeClass
//    public static void addAccountTest(){
//
//        Context context = ApplicationProvider.getApplicationContext();
//        SQLiteDB.createInstance(context);
//        expenseManager = new PersistentExpenseManager();
//        expenseManager.addAccount("1965", "Commercial", "Amaya", 1000);
//    }
//
//    @Test
//    public void testAddedAccount(){
//        assertTrue(expenseManager.getAccountNumbersList().contains("1965"));
//    }

    @Test
    public void testTransaction() throws InvalidAccountException {
        Context context = ApplicationProvider.getApplicationContext();
        SQLiteDB.createInstance(context);
        expenseManager = new PersistentExpenseManager();


        int n = expenseManager.getTransactionLogs().size();
        expenseManager.updateAccountBalance("1965", 11,05,2022, ExpenseType.INCOME, "2500");
        int m = expenseManager.getTransactionLogs().size();

        assertTrue(n+1 == m);

    }
}