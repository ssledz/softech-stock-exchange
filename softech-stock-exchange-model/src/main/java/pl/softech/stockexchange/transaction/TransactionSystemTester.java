/*
 * Copyright 2013 Sławomir Śledź <slawomir.sledz@sof-tech.pl>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.softech.stockexchange.transaction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import pl.softech.stockexchange.transaction.ITransactionSystem.Signal;

/**
 *
 * @author Sławomir Śledź <slawomir.sledz@sof-tech.pl>
 * @since 1.0
 */
public class TransactionSystemTester {

    private double wallet;
    private double initWallet;
    private double overallTransactionCost;
    private int quantity;
    private ITestable transactionSystem;
    
    private int buySignals;
    private int sellSignals;
    
    public TransactionSystemTester(double wallet, ITestable transactionSystem) {
        this.initWallet = this.wallet = wallet;
        this.transactionSystem = transactionSystem;
    }
    
    
    
    
    @Override
    public String toString() {
        return "TransactionSystemTester [name=" + transactionSystem+ ", close=" +transactionSystem.getClose()+ ", buySignals=" + buySignals + ", sellSignals="
                + sellSignals + ", freeFounds=" + overallValue() + ", initWallet=" + initWallet + ", overallTransactionCost="
                + overallTransactionCost + "]";
    }


    

    private double freeFounds() {
        return wallet - overallTransactionCost;
    }
    

    public double overallValue() {
        
        double close = transactionSystem.getClose();
        
        return freeFounds() + quantity * close ;
    }
    
    private double transactionCost() {
        return 0;
    }
    
    private void buy() {
        
        buySignals++;
        
        double cost = transactionSystem.getClose();
        
        if(freeFounds() < cost) {
            return;
        }
        
        quantity = (int) (freeFounds() / cost);
        wallet -= quantity * cost;
        overallTransactionCost += transactionCost();
    }
    
    private void sell() {
        
        sellSignals++;
        
        if(quantity == 0) {
            return;
        }
        
        double cost = transactionSystem.getClose();
        
        wallet += quantity * cost;
        overallTransactionCost += transactionCost();
        quantity = 0;
    }
    
    
    public void test(String dateFrom) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = df.parse(dateFrom);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        test(date);
    }
    
    public void test(Date dateFrom) {
        
        if(!transactionSystem.start()) {
            return;
        }
        
        do {
        
            if(transactionSystem.getDate().equals(dateFrom) || transactionSystem.getDate().after(dateFrom)) {
                break;
            }
            
        }while(transactionSystem.next());
        
        runTest();
    }
    
    public void test() {
        
        if(!transactionSystem.start()) {
            return;
        }
        runTest();
    }
    
    private void runTest() {
        
        Signal lastSignal = Signal.NONE;
        
        do {
            
            Signal signal = transactionSystem.emit();
            
            if(signal == Signal.NONE || lastSignal == signal) {
                continue;
            }
            
            if(signal.isBuy()) {
                buy();
            } else {
                sell();
            }
            
        } while(transactionSystem.next());
        
    }
    
}
