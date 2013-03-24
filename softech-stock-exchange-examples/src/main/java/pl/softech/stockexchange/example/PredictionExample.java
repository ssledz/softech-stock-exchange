package pl.softech.stockexchange.example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;
import pl.softech.forecast.WintersModel;
import pl.softech.stockexchange.dao.RemoteDbBossaDao;
import pl.softech.stockexchange.model.FIHelper;
import pl.softech.stockexchange.model.FInstrument;
import pl.softech.swing.chart.AbstractDataSetModel;
import pl.softech.swing.chart.Chart;
import pl.softech.swing.chart.Plot;
import pl.softech.swing.chart.axis.DateTickLabelFormatter;

public class PredictionExample {

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

        @Override
        public Color getGraphColor() {
            return Color.CYAN;
        }
    }

    public static class KGHMPrediction extends AbstractDataSetModel {

        private List<Point2D> list;

        public KGHMPrediction(Collection<FInstrument> data, double[] prediction) {
            list = new LinkedList<Point2D>();

            FInstrument[] tmp = data.toArray(new FInstrument[0]);
            for (int i = 0; i < prediction.length; i++) {

                Date date = null;
                if (i < tmp.length) {

                    date = tmp[i].getDate();

                } else {
                    Calendar calc = Calendar.getInstance();
                    calc.setTime(tmp[tmp.length - 1].getDate());
                    calc.add(Calendar.DAY_OF_YEAR, i - tmp.length + 1);
                    date = calc.getTime();
                }
                list.add(new Point2D.Double(date.getTime(), prediction[i]));
            }


        }

        @Override
        public Collection<Point2D> getData() {
            return list;
        }
    }

    public static class KGHMPredictionError extends AbstractDataSetModel {

        private List<Point2D> list;

        public KGHMPredictionError(Collection<FInstrument> data, double[] prediction) {
            list = new LinkedList<Point2D>();

            FInstrument[] tmp = data.toArray(new FInstrument[0]);
            for (int i = 0; i < tmp.length; i++) {
                list.add(new Point2D.Double(tmp[i].getDate().getTime(),
                        tmp[i].getClose() - prediction[i]));
            }
        }

        @Override
        public Collection<Point2D> getData() {
            return list;
        }

        @Override
        public Color getGraphColor() {
            return Color.GRAY;
        }
    }

    public static void main(String[] args) throws Exception {

        RemoteDbBossaDao bossaDao = new RemoteDbBossaDao();
        Collection<FInstrument> data = bossaDao.findeInstrumentByName("KGHM");

        double[] close = FIHelper.toClose(data);
        WintersModel wm = new WintersModel(close);
        double[] pred = wm.compute(0.60000, 0.20000, 0.1).predict(4);

        Chart chart = new Chart();
        chart.getxAxis().setTickLabelFormatter(new DateTickLabelFormatter());

        final Plot plot = chart.getPlot();

        final KGHMPrediction prediction = new KGHMPrediction(data, pred);
        final KGHMPredictionError error = new KGHMPredictionError(data, pred);

        plot.addDataSeries(new KGHMData(data));
        plot.addDataSeries(prediction);
        plot.addDataSeries(error);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(chart);
        frame.setSize(new Dimension(1024, 768));
        frame.setVisible(true);

    }
}
