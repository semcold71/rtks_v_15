package ru.samcold.rtks._utils;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Component;
import padeg.lib.Padeg;
import ru.samcold.rtks.domain.Contract;
import ru.samcold.rtks.domain.Customer;
import ru.samcold.rtks.domain.Work;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

@Component
public class Worder {

    private final FWMoney fwMoney;

    public Worder(FWMoney fwMoney) {
        this.fwMoney = fwMoney;
    }

    // Создание и сохранение договорных документов в .docx
    /**
     Карточка предприятия
     */
    public void printCard(Customer customer) throws IOException {
        XWPFDocument document = new XWPFDocument(
                Objects.requireNonNull(getClass().getResourceAsStream("/docx/card.docx")));

        XWPFTable table = document.getTables().get(0);

        // fill
        document.getParagraphs().get(1).createRun().setText(replaceQuote(customer.getName()));

        table.getRow(0).getCell(1).getParagraphs().get(0).createRun().setText(replaceQuote(customer.getName()));
        table.getRow(1).getCell(1).getParagraphs().get(0).createRun().setText(replaceQuote(customer.getName()));
        table.getRow(2).getCell(1).getParagraphs().get(0).createRun().setText(replaceQuote(customer.getAddress()));
        table.getRow(3).getCell(1).getParagraphs().get(0).createRun().setText(replaceQuote(customer.getAddress()));
        table.getRow(4).getCell(1).getParagraphs().get(0).createRun().setText(customer.getInn());
        table.getRow(5).getCell(1).getParagraphs().get(0).createRun().setText(customer.getKpp());
        table.getRow(6).getCell(1).getParagraphs().get(0).createRun().setText(customer.getOgrn());
        table.getRow(7).getCell(1).getParagraphs().get(0).createRun().setText(customer.getRs());
        table.getRow(8).getCell(1).getParagraphs().get(0).createRun().setText(replaceQuote(customer.getBank()));
        table.getRow(9).getCell(1).getParagraphs().get(0).createRun().setText(customer.getKs());
        table.getRow(10).getCell(1).getParagraphs().get(0).createRun().setText(customer.getBik());
        table.getRow(11).getCell(1).getParagraphs().get(0).createRun().setText(customer.getPhone());
        table.getRow(12).getCell(1).getParagraphs().get(0).createRun().setText(customer.getEmail());
        table.getRow(13).getCell(1).getParagraphs().get(0).createRun().setText(customer.getWeb());
        table.getRow(14).getCell(1).getParagraphs().get(0).createRun().setText(replaceQuote(customer.getPost()));
        table.getRow(15).getCell(1).getParagraphs().get(0).createRun().setText(customer.getBoss());

        // Доверенность
        //table.getRow(16).getCell(1).getParagraphs().get(0).createRun().setText(customer.getWeb());

        table.getRow(17).getCell(1).getParagraphs().get(0).createRun().setText(customer.getNote());

        // save
        saveDocument(document);
    }

    /**
     Договор
     */
    public void printContract(Contract contract) throws IOException {
        XWPFDocument document = new XWPFDocument(Objects.requireNonNull(getClass().getResourceAsStream("/docx/contract.docx")));
        List<XWPFParagraph> paragraphs = document.getParagraphs();

        updateParagraph(paragraphs, "number", contract.getNumber());

        updateParagraph(paragraphs, "cust",
                replaceQuote(contract.getCustomer().getName2() + " (" + contract.getCustomer().getName() + ")"));

        updateParagraph(paragraphs, "post", Padeg.getAppointmentPadeg(contract.getCustomer().getPost(), 2).toLowerCase());
        updateParagraph(paragraphs, "boss", Padeg.getFIOPadegFSAS(contract.getCustomer().getBoss(), 2));
        updateParagraph(paragraphs, "start", contract.getStart() + " г.");
        updateParagraph(paragraphs, "finish", contract.getFinish() + " г.");
        updateParagraph(paragraphs, "total_digit", String.format("%,.2f", contract.getPrice()));
        updateParagraph(paragraphs, "total_string", moneyToString(contract.getPrice()));

        // prepay
        String prepay;
        if (contract.getPrepay() == 100) {
            prepay = "предоплата " + contract.getPrepay() + "%";
        } else {
            prepay = "предоплата " + contract.getPrepay() + "%. " + "Окончательный расчет производится " +
                    "в течение 10-ти календарных дней с даты подписания Сторонами Акта сдачи-приемки выполненных работ.";
        }
        updateParagraph(paragraphs, "prepay", prepay);

        // contract date
        document.getTables().get(0).getRows().get(0).getCell(1).getParagraphs().get(0).createRun()
                .setText(new SimpleDateFormat("dd MMMM yyyy").format(contract.getDate()) + " г.", 0);

        // works
        insertWorks(document, contract);

        // details
        insertDetails(document, contract.getCustomer());

        // signatures
        insertSignatures(document, contract.getCustomer());

        saveDocument(document);
    }

    /**
     Счет на оплату
     */
    public void printOrder(Contract contract, int prepayKey) throws IOException {
        XWPFDocument document = new XWPFDocument(Objects.requireNonNull(getClass().getResourceAsStream("/docx/order.docx")));
        List<XWPFParagraph> paragraphs = document.getParagraphs();

        updateParagraph(paragraphs, "number", contract.getPrepay() != 100 ? contract.getNumber() + "/" + prepayKey : contract.getNumber());
        updateParagraph(paragraphs, "date", new SimpleDateFormat("dd MMMM yyyy").format(contract.getDate()) + " г.");

        XWPFTable table2 = document.getTables().get(1);
        XWPFRun run2 = table2.getRows().get(1).getCell(1).getParagraphs().get(0).createRun();
        run2.setText(contract.getCustomer().nameForOrder());
        run2.setBold(true);

        XWPFRun run3 = table2.getRows().get(2).getCell(1).getParagraphs().get(0).createRun();
        StringBuilder sb1 = getOrderFoundation(contract, prepayKey);
        run3.setText(String.valueOf(sb1));
        run3.setBold(true);

        XWPFTable worksTable = document.getTables().get(2);
        for (int i = 0; i < contract.getWorkList().size(); i++) {
            XWPFTableRow row = worksTable.getRow(i + 1);
            row.getCell(0).getParagraphs().get(0).createRun().setText(String.valueOf(i + 1));
            row.getCell(0).getParagraphs().get(0).setAlignment(ParagraphAlignment.CENTER);
            row.getCell(1).getParagraphs().get(0).createRun().setText(contract.getWorkList().get(i).getDescription());
            row.getCell(2).getParagraphs().get(0).createRun().setText(contract.getWorkList().get(i).getUnit());
            row.getCell(2).getParagraphs().get(0).setAlignment(ParagraphAlignment.CENTER);
            row.getCell(3).getParagraphs().get(0).createRun().setText(String.valueOf(contract.getWorkList().get(i).getCount()));
            row.getCell(3).getParagraphs().get(0).setAlignment(ParagraphAlignment.CENTER);

            double price = contract.getWorkList().get(i).getPrice();
            row.getCell(4).getParagraphs().get(0).createRun().setText(
                    String.format("%,.2f%n", getPrepayValue(price, contract.getPrepay(), prepayKey)));
            row.getCell(4).getParagraphs().get(0).setAlignment(ParagraphAlignment.RIGHT);

            double sum = contract.getWorkList().get(i).getTotal();
            row.getCell(5).getParagraphs().get(0).createRun().setText(
                    String.format("%,.2f%n", getPrepayValue(sum, contract.getPrepay(), prepayKey)));
            row.getCell(5).getParagraphs().get(0).setAlignment(ParagraphAlignment.RIGHT);

            if (i < contract.getWorkList().size() - 1) {
                worksTable.createRow();
            }
        }

        double total = getPrepayValue(contract.getPrice(), contract.getPrepay(), prepayKey);

        XWPFRun totalRun = document.getTables().get(3).getRows().get(0).getCell(1).getParagraphs().get(0).createRun();
        totalRun.setText(String.format("%,.2f%n", total));
        totalRun.setBold(true);

        XWPFRun nds = document.getTables().get(3).getRows().get(1).getCell(1).getParagraphs().get(0).createRun();
        nds.setText("-");
        nds.setBold(true);

        XWPFRun totalRun2 = document.getTables().get(3).getRows().get(2).getCell(1).getParagraphs().get(0).createRun();
        totalRun2.setText(String.format("%,.2f%n", total));
        totalRun2.setBold(true);

        updateParagraph(paragraphs, "count", String.valueOf(contract.getWorkList().size()));
        updateParagraph(paragraphs, "total", String.format("%,.2f%n", total));
        updateParagraph(paragraphs, "total_string", moneyToString(total));

        saveDocument(document);
    }

    /**
     Акт сдачи-приемки выполненных работ
     */
    public void printAkt(Contract contract) throws IOException {
        XWPFDocument document = new XWPFDocument(Objects.requireNonNull(getClass().getResourceAsStream("/docx/akt.docx")));
        List<XWPFParagraph> paragraphs = document.getParagraphs();

        updateParagraph(paragraphs, "cust", contract.getCustomer().getName().replace(" \"", " «").replace("\"", "»"));
        updateParagraph(paragraphs, "post", Padeg.getAppointmentPadeg(contract.getCustomer().getPost(), 2).toLowerCase());
        updateParagraph(paragraphs, "boss", Padeg.getFIOPadegFSAS(contract.getCustomer().getBoss(), 2));
        updateParagraph(paragraphs, "number", contract.getNumber());
        updateParagraph(paragraphs, "date", new SimpleDateFormat("dd.MM.yyyy").format(contract.getDate()) + " г.");
        updateParagraph(paragraphs, "total", String.format("%,.2f", contract.getPrice()) + "руб.");
        updateParagraph(paragraphs, "prop", moneyToString(contract.getPrice()));

        insertWorks2(document, contract);
        insertDetails(document, contract.getCustomer());
        insertSignatures(document, contract.getCustomer());

        saveDocument(document);
    }


    // Утилиты
    /**
     Сохранение документа на диск
     */
    private void saveDocument(XWPFDocument doc) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                "Microsoft Word (.docx)", "*.docx"));
        File res = fileChooser.showSaveDialog(new Stage());

        if (res != null) {
            try (FileOutputStream out = new FileOutputStream(res)) {
                doc.write(out);
            }
        }
    }

    /**
     Замена простых кавычек на ёлочки
     */
    private String replaceQuote(String original) {
        return original
                .replace(" \"", " «")
                .replace("\"", "»");
    }

    /**
     Вставка в документ переменных
     */
    private void updateParagraph(List<XWPFParagraph> paragraphs, String target, String replacement) {
        boolean success = false;
        for (XWPFParagraph paragraph : paragraphs) {
            for (XWPFRun run : paragraph.getRuns()) {
                if (run.getText(0).equals(target)) {
                    String text = run.getText(0);
                    text = text.replace(target, replacement);
                    run.setText(text, 0);
                    success = true;
                    break;
                }
            }
            if (success)
                break;
        }
    }

    /**
     Число прописью и замена первой буквы на заглавную
     */
    private String moneyToString(double value) {
        String strMoney = fwMoney.num2str(value);
        strMoney = strMoney.substring(0, 0) + Character.toUpperCase(strMoney.charAt(0)) + strMoney.substring(1);
        return strMoney;
    }

    /**
     Вставка в документ списка работ (для Договора и Счета)
     */
    private void insertWorks(XWPFDocument doc, Contract contract) {

        XWPFTable table = doc.getTables().get(1);
        String style = table.getRow(0).getCell(0).getParagraphs().get(0).getStyleID();
        List<Work> works = contract.getWorkList();

        for (int i = 0; i < works.size(); i++) {
            XWPFTableRow row = table.getRow(i);
            XWPFTableCell cell = row.getCell(0);
            XWPFParagraph paragraph = cell.getParagraphs().get(0);
            XWPFRun run = paragraph.createRun();
            run.setText(works.get(i).getDescription());
            paragraph.setStyle(style);
            if (i < works.size() - 1) {
                table.createRow();
            }
        }
    }

    /**
     Вставка в документ списка работ (для Акта сдачи-приемки работ)
     */
    private void insertWorks2(XWPFDocument document, Contract contract) {

        List<Work> works = contract.getWorkList();

        for (int i = 0; i < works.size(); i++) {
            XWPFTable table = document.getTableArray(1);
            XWPFTableRow row = table.getRow(i + 1);
            row.getCell(0).getParagraphs().get(0).createRun().setText(String.valueOf(i + 1));
            row.getCell(0).getParagraphs().get(0).setAlignment(ParagraphAlignment.CENTER);
            row.getCell(1).getParagraphs().get(0).createRun().setText(works.get(i).getDescription());
            row.getCell(2).getParagraphs().get(0).createRun().setText(works.get(i).getUnit());
            row.getCell(2).getParagraphs().get(0).setAlignment(ParagraphAlignment.CENTER);
            row.getCell(3).getParagraphs().get(0).createRun().setText(String.valueOf(works.get(i).getCount()));
            row.getCell(3).getParagraphs().get(0).setAlignment(ParagraphAlignment.CENTER);

            String price = String.format("%,.2f", works.get(i).getPrice());
            row.getCell(4).getParagraphs().get(0).createRun().setText(price);
            row.getCell(4).getParagraphs().get(0).setAlignment(ParagraphAlignment.RIGHT);

            String sum = String.format("%,.2f", works.get(i).getTotal());
            row.getCell(5).getParagraphs().get(0).createRun().setText(sum);
            row.getCell(5).getParagraphs().get(0).setAlignment(ParagraphAlignment.RIGHT);

            if (i < works.size() - 1) {
                table.createRow();
            }
        }
    }

    /**
     Вставка в документ реквизитов контрагента
     */
    private void insertDetails(XWPFDocument doc, Customer customer) {

        XWPFTable table = doc.getTables().get(2);
        XWPFTableRow row = table.getRow(1);
        XWPFTableCell cell = row.getCell(1);
        List<XWPFParagraph> paragraphs = cell.getParagraphs();

        paragraphs.forEach(p -> {
            p.getRuns().forEach(run -> {
                String text = run.getText(0);
                if (text.contains("customer")) {
                    String custName = customer.getName().replace(" \"", " «").replace("\"", "»");
                    run.setText(text.replace("customer", custName), 0);
                }
                if (run.getText(0).contains("address")) {
                    String address = customer.getAddress();
                    run.setText(text.replace("address", address), 0);
                }
                if (run.getText(0).contains("innkpp")) {
                    String inn_kpp = !customer.getKpp().isEmpty() && customer.getKpp() != null ?
                            customer.getInn() + "/" + customer.getKpp() :
                            customer.getInn();
                    run.setText(text.replace("innkpp", inn_kpp), 0);
                }
                if (run.getText(0).contains("ogrn")) {
                    String ogrn = customer.getOgrn();
                    run.setText(text.replace("ogrn", ogrn), 0);
                }
                if (run.getText(0).contains("rs")) {
                    String rs = customer.getRs();
                    run.setText(text.replace("rs", rs), 0);
                }
                if (run.getText(0).contains("bank")) {
                    String bank = customer.getBank();
                    run.setText(text.replace("bank", bank), 0);
                }
                if (run.getText(0).contains("bik")) {
                    String bik = customer.getBik();
                    run.setText(text.replace("bik", bik), 0);
                }
                if (run.getText(0).contains("ks")) {
                    String ks = customer.getKs();
                    run.setText(text.replace("ks", ks), 0);
                }
            });
        });
    }

    /**
     Вставка в документ блока с подписями
     */
    private void insertSignatures(XWPFDocument doc, Customer customer) {

        XWPFTable table = doc.getTables().get(3);

        XWPFParagraph postP = table.getRow(1).getCell(1).getParagraphs().get(0);
        XWPFRun postR = postP.getRuns().get(0);
        String postStr = postR.getText(0);
        postStr = postStr.replace("post", customer.getPost());
        postR.setText(postStr, 0);

        XWPFParagraph bossP = table.getRow(3).getCell(1).getParagraphs().get(0);
        for (XWPFRun run : bossP.getRuns()) {
            String bossStr = run.getText(0);
            bossStr = bossStr.replace("boss", customer.shortBossName());
            run.setText(bossStr, 0);
        }
    }

    /**
     Формирование строки "Основание" в счете
     */
    private StringBuilder getOrderFoundation(Contract contract, int prepayKey) {

        StringBuilder sb1 = new StringBuilder();

        sb1.append("Договор № ")
                .append(contract.getNumber())
                .append(" от ")
                .append(new SimpleDateFormat("dd.MM.yyyy").format(contract.getDate()))
                .append(" г.");

        if (prepayKey != 0) {

            if (prepayKey == 1) {
                sb1.append(" (Предоплата ").append(contract.getPrepay()).append("%)");

            } else if (prepayKey == 2) {
                sb1.append(" (Окончательный расчет ").append(100 - contract.getPrepay()).append("%)");
            }
        }

        return sb1;
    }

    /**
     Формирование суммы предоплаты, если она предусмотрена договором
     */
    private double getPrepayValue(double value, int prepay, int prepayKey) {
        return prepayKey == 1 ? value * prepay / 100.0 : value * (100 - prepay) / 100.0;
    }
}
