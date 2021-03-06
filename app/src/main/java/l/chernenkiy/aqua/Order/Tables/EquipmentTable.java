package l.chernenkiy.aqua.Order.Tables;

import java.util.ArrayList;
import java.util.HashMap;

import l.chernenkiy.aqua.Helpers.CartHelper;

public final class EquipmentTable {

    public static String generateHtml(ArrayList<HashMap<String, String> > cartEquipmentItem) {
        StringBuilder result = new StringBuilder("" +
                "<h3>Данные о заказе на оборудование</h3>" +
                "<table style=\"width:100%; border:1px solid #999; border-collapse: collapse;\">" +
                "<tr align=\"left\" style=\"background-color:#f4f4f4\">" +
                "<th style=\"padding: 5px;border:1px solid #999;\">№</th>" +
                "<th style=\"padding: 5px;border:1px solid #999;\">Артикул</th>" +
                "<th style=\"padding: 5px;border:1px solid #999;\">Название</th>" +
                "<th style=\"padding: 5px;border:1px solid #999;\">Количество</th>" +
                "<th style=\"padding: 5px;border:1px solid #999;\">Цена</th>" +
                "<th style=\"padding: 5px;border:1px solid #999;\">Сумма</th>" +
                "</tr>");

        for (int i = 0, c = 1; i < cartEquipmentItem.size(); i++, c++) {
            result.append("" + "<tr>" + "<td style=\"padding: 5px;border:1px solid #999;\">").append(c).append("</td>").append("<td style=\"padding: 5px;border:1px solid #999;\">").append(cartEquipmentItem.get(i).get("article")).append("</td>").append("<td style=\"padding: 5px;border:1px solid #999;\">").append(cartEquipmentItem.get(i).get("name")).append("</td>").append("<td style=\"padding: 5px;border:1px solid #999;\">").append(cartEquipmentItem.get(i).get("quantity")).append("</td>").append("<td style=\"padding: 5px;border:1px solid #999;\">").append(cartEquipmentItem.get(i).get("price")).append("</td>").append("<td style=\"padding: 5px;border:1px solid #999;\">").append(CartHelper.itemSumEquip(cartEquipmentItem.get(i))).append(" грн.").append("</td>").append("</tr>");
        }
        return result + "</table>" +
                "<h3 style=\"margin-top:10px;text-align:right;\">Сумма заказа на оборудование: "
                +CartHelper.countFinalSumEquip (cartEquipmentItem)+" грн.</h3>";
    }
}
