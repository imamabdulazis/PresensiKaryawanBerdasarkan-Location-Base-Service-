package gi.pelatihan.odt.presensikaryawan.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ConverterUtils {

    public static String convertIDR(String jumlah) {
        double price = Double.parseDouble(jumlah);

        Locale localeID = new Locale("in", "ID");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(localeID);
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        String pattern = "Rp #,##0.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);

        return String.valueOf(decimalFormat.format(price));
    }
}
