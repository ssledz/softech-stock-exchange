package pl.softech.stockexchange.example;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;
import pl.softech.stockexchange.dao.RemoteDbBossaDao;
import pl.softech.stockexchange.index.Adx;
import pl.softech.stockexchange.model.FIHelper;
import pl.softech.stockexchange.model.FInstrument;
import pl.softech.swing.chart.AbstractDataSetModel;
import pl.softech.swing.chart.Chart;
import pl.softech.swing.chart.Plot;
import pl.softech.swing.chart.axis.DateTickLabelFormatter;

public class ChartExample {

    private static final int NUMBER_OF_PERIODS = 45;
    private static final double EMA_WEIGHT = 0.9;

    public static class KGHMData extends AbstractDataSetModel {

        private List<Point2D> list;

        public KGHMData(Collection<FInstrument> data) {
            list = new LinkedList<Point2D>();
            for (FInstrument f : data) {
                list.add(new Point2D.Double(f.getDate().getTime(), f.getClose()));
            }
        }

        @Override
        public Collection<Point2D> getData() {
            return list;
        }
    }

    public static class KGHMAdx extends AbstractDataSetModel {

        private List<Point2D> list;

        public KGHMAdx(Collection<FInstrument> data) {
            list = new LinkedList<Point2D>();

            double[] adx = new Adx().count(FIHelper.toLow(data), FIHelper.toHigh(data), FIHelper.toClose(data), EMA_WEIGHT, NUMBER_OF_PERIODS);

            int i = 0;
            for (FInstrument f : data) {
                double tmp = adx[i++];

                if (Double.isNaN(tmp)) {
                    continue;
                }

                list.add(new Point2D.Double(f.getDate().getTime(), tmp));
            }
        }

        @Override
        public Collection<Point2D> getData() {
            return list;
        }
    }

    public static void main(String[] args) throws Exception {

        RemoteDbBossaDao bossaDao = new RemoteDbBossaDao();
        Collection<FInstrument> data = bossaDao.findeInstrumentByName("KGHM", "2010-01-01");

        Chart chart = new Chart();
        chart.getxAxis().setTickLabelFormatter(new DateTickLabelFormatter());

        final Plot plot = chart.getPlot();

        final KGHMAdx adx = new KGHMAdx(data);

        plot.addDataSeries(new KGHMData(data));
        plot.addDataSeries(adx);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(chart);
        frame.setSize(new Dimension(1024, 768));
        frame.setVisible(true);

    }
}
