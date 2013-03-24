package pl.softech.stockexchange.example;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import pl.softech.stockexchange.dao.RemoteDbBossaDao;
import pl.softech.stockexchange.model.FInstrument;
import pl.softech.stockexchange.transaction.SimpleTransactionSystem;
import pl.softech.stockexchange.transaction.TransactionSystemTester;

public class TransactionTesterExample {

    public static void main(String[] args) throws Exception {
        RemoteDbBossaDao bossaDao = new RemoteDbBossaDao();
        Collection<String> names = bossaDao.findeAllInstrumentsName();
        TransactionSystemTester[] testers = new TransactionSystemTester[names.size()];
        int i = 0;
        
        for (String name : names) {

            Collection<FInstrument> data = bossaDao.findeInstrumentByName(name);
            SimpleTransactionSystem sts = new SimpleTransactionSystem(data.toArray(new FInstrument[0]));
            TransactionSystemTester tester = new TransactionSystemTester(500, sts);
            tester.test("2012-01-01");
            testers[i++] = tester;
            

        }
        
        
        Arrays.sort(testers, new Comparator<TransactionSystemTester>() {

            @Override
            public int compare(TransactionSystemTester o1, TransactionSystemTester o2) {
                
                return (int) (o1.overallValue() - o2.overallValue());
            }
        });
        
        for(TransactionSystemTester t : testers) {
            System.out.println(t.toString());
        }
    }
    
}
