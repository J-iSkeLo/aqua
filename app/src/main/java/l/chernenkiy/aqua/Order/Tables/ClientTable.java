package l.chernenkiy.aqua.Order.Tables;

import java.util.HashMap;

public final class ClientTable {

    public static String generateHtml(HashMap<String, String> clientData)
    {
       return "" +
                "<h3>Данные о клиенте</h3>" +
                "<table style=\"width:100%; border:1px solid #999; border-collapse: collapse;\">" +
                "<tr align=\"left\" style=\"background-color:#f4f4f4\">" +
                "<th style=\"padding: 5px;border:1px solid #999;\">Имя Фамилия</th>" +
                "<th style=\"padding: 5px;border:1px solid #999;\">Город</th>" +
                "<th style=\"padding: 5px;border:1px solid #999;\">Номер Телефона</th>" +
                "<th style=\"padding: 5px;border:1px solid #999;\">Email</th>" +
                "<th style=\"padding: 5px;border:1px solid #999;\">Комментарий</th>" +
                "</tr>" +
                "<tr>" +
                "<td style=\"padding: 5px;border:1px solid #999;\">"+clientData.get("name")+"</td>" +
                "<td style=\"padding: 5px;border:1px solid #999;\">"+clientData.get("city")+"</td>" +
                "<td style=\"padding: 5px;border:1px solid #999;\">"+clientData.get("number")+"</td>" +
                "<td style=\"padding: 5px;border:1px solid #999;\">"+clientData.get("email")+"</td>" +
                "<td style=\"padding: 5px;border:1px solid #999;\">"+clientData.get("comment")+"</td>" +
                "</tr>"+
                "</table><br>";
    }

}
