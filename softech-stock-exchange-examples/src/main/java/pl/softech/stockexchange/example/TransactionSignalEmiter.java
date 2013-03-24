package pl.softech.stockexchange.example;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import pl.softech.stockexchange.dao.RemoteDbBossaDao;
import pl.softech.stockexchange.model.FInstrument;
import pl.softech.stockexchange.transaction.ITransactionSystem.Signal;
import pl.softech.stockexchange.transaction.SimpleTransactionSystem;
import pl.softech.stockexchange.transaction.TransactionSystemTester;

public class TransactionSignalEmiter {

    public static void main(String[] args) throws Exception {
        RemoteDbBossaDao bossaDao = new RemoteDbBossaDao();

        List<FInstrument> buy = new LinkedList<FInstrument>();
        List<FInstrument> sell = new LinkedList<FInstrument>();
        
        for (String name : bossaDao.findeAllInstrumentsName()) {

            Collection<FInstrument> data = bossaDao.findeInstrumentByName(name);
            FInstrument[] tmp = data.toArray(new FInstrument[0]);
            SimpleTransactionSystem sts = new SimpleTransactionSystem(tmp);
            Signal signal = sts.emit();
            
            FInstrument last = tmp[tmp.length - 1];
            if(signal.isBuy()) {
                buy.add(last);
            } else if(signal.isSell()) {
                sell.add(last);
            }
            
        }
        
        TransactionSystemTester[] testers = new TransactionSystemTester[buy.size()];
        int i = 0;
        Calendar calc = Calendar.getInstance();
        calc.add(Calendar.DAY_OF_YEAR, -7 * 8);
        Date dateFrom = calc.getTime();
        for (FInstrument fi : buy) {

            Collection<FInstrument> data = bossaDao.findeInstrumentByName(fi.getName());
            SimpleTransactionSystem sts = new SimpleTransactionSystem(data.toArray(new FInstrument[0]));
            TransactionSystemTester tester = new TransactionSystemTester(600, sts);
            tester.test(dateFrom);
            testers[i++] = tester;
            

        }
        
        Arrays.sort(testers, new Comparator<TransactionSystemTester>() {

            @Override
            public int compare(TransactionSystemTester o1, TransactionSystemTester o2) {
                
                return (int) (o1.overallValue() - o2.overallValue());
            }
        });
        
        
        System.out.println("Buy: " + buy.size());
        System.out.println("Sell: " + sell.size());
        
        System.out.println("--Buy--");
        for(TransactionSystemTester t : testers) {
            System.out.println("BUY " + t.toString());
        }
        System.out.println("--Sell--");
        for(FInstrument fi : sell) {
            System.out.println("SELL " + fi.getName());
        }

    }

}
