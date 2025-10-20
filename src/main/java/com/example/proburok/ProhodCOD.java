package com.example.proburok;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;


public class ProhodCOD extends Configs{
    @FXML private DatePicker data;
    @FXML private ComboBox<String> sehenbox;
    @FXML private TextField gorizont;
    @FXML private TextField sehen;
    @FXML private TextField nameProhod;

    @FXML private TextField nomer;
    @FXML private TextField privizka;

    @FXML private TextField Wid;
    @FXML private TextField Hid;

    @FXML private TextField idi;
    @FXML private Button singUpButtun;
    private String NOMER;
    private Date DATA;
    private String SEHEN;
    private String GORIZONT;
    private String NAME;
    private String PRIVIZKA;
    private String HIFR;
    private String WID;
    private String HID;

    @FXML private ImageView instr;
    @FXML private ImageView dokumGeolog;
    @FXML private ImageView dokumGeolog11;
    @FXML private ImageView pethat;
    @FXML private ImageView tabl;
    @FXML private ImageView othet;
    @FXML private ComboBox<String> ushatok;
    @FXML private ComboBox<String> cbsehen;
    @FXML private CheckBox cb;
    @FXML private ImageView PlanVKL;
    @FXML private ImageView PlanVKLNe;
    @FXML private ImageView PoperVKL;
    @FXML private ImageView PoperVKLNe;
    @FXML private ImageView ProdolVKL;
    @FXML private ImageView ProdolVKLNe;
    @FXML private ImageView planVNS;
    @FXML private ImageView planVNSNE;
    @FXML private ImageView planobnov;
    @FXML private ImageView poperVNS;
    @FXML private ImageView poperVNSNE;
    @FXML private ImageView poperobnov;
    @FXML private ImageView prodolVNS;
    @FXML private ImageView prodolVNSNE;
    @FXML private ImageView prodolobnov;
    private ObservableList<String> originalItems;
    private FilteredList<String> filteredItems;
    @FXML
    void initialize() {
        cbsehen.visibleProperty().bind(cb.selectedProperty());
        sehen.visibleProperty().bind(cb.selectedProperty().not());


        setupTooltips(); // Добавляем подсказки
        setupCursor();   // Настраиваем курсор
        setupImageHandlers();
        cb.selectedProperty().addListener((observable, oldValue, newValue) -> {
            formatOnFocusLost(Wid);
            formatOnFocusLost(Hid);
        });
        Wid.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // когда поле теряет фокуc
                formatOnFocusLost(Wid);
                S(Wid.getText(),Hid.getText());

            }
        });

        Hid.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // когда поле теряет фокуc
                formatOnFocusLost(Hid);
                S(Wid.getText(),Hid.getText());
            }
        });

        Hid.promptTextProperty().bind(
                Bindings.when(cb.selectedProperty())
                        .then("ОСНОВНАЯ")
                        .otherwise("ВЫСОТА")
        );
        Wid.promptTextProperty().bind(
                Bindings.when(cb.selectedProperty())
                        .then("СОПРЯГАЕМАЯ")
                        .otherwise("ШИРИНА")
        );

        idi.setVisible(false);
        data.setValue(LocalDate.now());
        pethat.setOnMouseClicked(mouseEvent -> openNewScene("/com/example/proburok/Pehat.fxml"));
        tabl.setOnMouseClicked(mouseEvent -> openNewScene("/com/example/proburok/app.fxml"));

        dokumGeolog.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_geolog,"Геология Октябрьского месторождения_");
        });
        dokumGeolog11.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_albom,"АЛЬБОМ ТИПОВЫХ ПАСПОРТОВ КРЕПЛЕНИЯ_");
        });
        instr.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_instr,"Инструкция_");
        });
        othet.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_othet,"Отчет_");
        });


        ushatok.getItems().addAll("Участок ГКиПР №1", "Участок ГКиПР №2", "Участок ГКиПР №3");

        originalItems = FXCollections.observableArrayList(
                "16.0/14.8", "18.8/16.2", "18.8/14.0", "18.8/20.6",
                "12.7/20.6", "12.7/19.3"
                ,"21.0/21.1", "21.0/21.1", "21.0/16.4","21.0/19.8",
                "19.8/16.4", "19.8/19.8"
                ,"20.6/20.6", "20.6/19.8",
                "16.4/16.4","16.4/16.4",
                "20.8/17.3"
        );

        filteredItems = new FilteredList<>(originalItems);
        cbsehen.setItems(filteredItems);
        setupAdvancedFiltering();



        cbsehen.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
            String[] spS =  cbsehen.getValue().split("/");
                Wid.setText(spS[1]);
                Hid.setText(spS[0]);

            }
        });
        ushatok.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && data.getValue() != null) {
                String year = String.valueOf(data.getValue().getYear());
                String prefix = "";
                if (newValue.equals("Участок ГКиПР №1")) prefix = "1";
                else if (newValue.equals("Участок ГКиПР №2")) prefix = "2";
                else if (newValue.equals("Участок ГКиПР №3")) prefix = "3";


                // Получаем следующий порядковый номер из БД
                DatabaseHandler dbHandler = new DatabaseHandler();
                int nextNumber = dbHandler.getNextSequenceNumber(prefix, year);

                // Устанавливаем номер в формате "1-5-2025"
                nomer.setText(prefix + "-" + nextNumber + "-" + year);
                proverkaImage(Put + "/" + nomer.getText() + "/" + "План",planVNS,planVNSNE, PlanVKL,PlanVKLNe,planobnov);
                proverkaImage(Put + "/" + nomer.getText() + "/" + "Поперечный",poperVNS,poperVNSNE,PoperVKL,PoperVKLNe,poperobnov);
                proverkaImage(Put + "/" + nomer.getText() + "/" + "Продольный",prodolVNS,prodolVNSNE,ProdolVKL,ProdolVKLNe,prodolobnov);
            }
        });
        singUpButtun.setOnMouseClicked(mouseEvent -> {
            proverkaImage(Put + "/" + nomer.getText() + "/" + "План",planVNS,planVNSNE, PlanVKL,PlanVKLNe,planobnov);
            proverkaImage(Put + "/" + nomer.getText() + "/" + "Поперечный",poperVNS,poperVNSNE,PoperVKL,PoperVKLNe,poperobnov);
            proverkaImage(Put + "/" + nomer.getText() + "/" + "Продольный",prodolVNS,prodolVNSNE,ProdolVKL,ProdolVKLNe,prodolobnov);
            NOMER = nomer.getText() != null ? nomer.getText().trim() : "";
            SEHEN = sehen.getText() != null ? sehen.getText().trim() : "";
            GORIZONT = gorizont.getText() != null ? gorizont.getText().trim() : "";
            PRIVIZKA = privizka.getText() != null ? privizka.getText().trim() : "";
            NAME = nameProhod.getText() != null ? nameProhod.getText().trim() : "";
            WID = Wid.getText() != null ? Wid.getText().trim() : "";
            HID = Hid.getText() != null ? Hid.getText().trim() : "";
            idi.setText(getIdiP(HID+"_"+WID));
            System.out.println("Данные " + HID+"_"+WID);
            HIFR = idi.getText() != null ? idi.getText().trim() : "";
            LocalDate selectedDate = data.getValue();
            try {
                StringBuilder errors = new StringBuilder();
                if (NOMER.isEmpty()) errors.append("- Не заполнен номер\n");
                if (selectedDate == null) {
                    errors.append("- Не выбрана дата\n");
                } else {
                    DATA = Date.valueOf(selectedDate); // Преобразуем только если дата выбрана
                }
                if (GORIZONT.isEmpty()) errors.append("- Не заполнен горизонт\n");
                if (PRIVIZKA.isEmpty()) errors.append("- Не заполнена привязка\n");
                if (NAME.isEmpty()) errors.append("- Не заполнено название выработки\n");
                if (WID.isEmpty()) errors.append("- Не заполнена ширина\n");
                if (HID.isEmpty()) errors.append("- Не заполнена высота\n");
                if (SEHEN.isEmpty()) errors.append("- Не заполнено сечение\n");
                if (planVNS.isVisible() || planVNSNE.isVisible() ) {errors.append("- Не внесён план\n");}
               // if (poperVNS.isVisible() || poperVNSNE.isVisible() ) {errors.append("- Не внесён поперечный разрез\n");}
               // if (prodolVNS.isVisible() || prodolVNSNE.isVisible() ) {errors.append("- Не внесён продольный разрез\n");}
                if (errors.length() > 0) {
                    showAlert("Заполните обязательные поля:\n" + errors);
                    return;
                }
                String nameBD = NAME + " № " + NOMER;
                String prim =  "Требуется геологическое описание" ;

                System.out.println("Данные " + HIFR);
                // Все данные валидны - сохраняем
                DatabaseHandler Tabl = new DatabaseHandler();
                Tabl.Dobavlenie(NOMER, DATA, SEHEN, GORIZONT, PRIVIZKA, nameBD,NAME,HIFR,ushatok.getValue(),prim,WID,HID);
                ohistka();

            } catch (DateTimeException e) {
                showAlert("Ошибка в формате даты!");
            } catch (Exception e) {
                showAlert("Произошла ошибка: " + e.getMessage());
            }
        });
    }

    private void setupAdvancedFiltering() {
        TextField editor = cbsehen.getEditor();

        editor.textProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if (filteredItems.size() > 0) {
                    cbsehen.show();}
            });
            if (newVal == null || newVal.isEmpty()) {
                filteredItems.setPredicate(null);
                return;}
            final String filter = newVal.toLowerCase();
            filteredItems.setPredicate(item -> {
                if (item == null) return false;
                return item.toLowerCase().contains(filter);
            });
        });
        // Обработка клавиш для улучшения UX
        editor.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // При нажатии Enter выбираем первый подходящий элемент
                if (filteredItems.size() > 0) {
                    cbsehen.getSelectionModel().select(0);
                    cbsehen.hide();
                }
            } else if (event.getCode() == KeyCode.ESCAPE) {
                cbsehen.hide();
            }
        });
    }

    private void S(String W,String H){
        if (W.equals("") || H.equals(""))
        return;
        double WidS = Double.parseDouble(Wid.getText().trim().replace(',', '.') );
        double HidS = Double.parseDouble(Hid.getText().trim().replace(',', '.') );

        sehen.setText( String.format(Locale.US,"%.1f", (WidS*(HidS-0.0706*WidS))));
    }
    private void formatOnFocusLost(TextField textField) {
        String text = textField.getText();
        if (text != null && !text.isEmpty()) {
            try {
                // Заменяем запятую на точку для парсинга
                String normalized = text.replace(',', '.');
                double value = Double.parseDouble(normalized);

                // Форматируем до 2 знаков после запятой

                DecimalFormat df =cb.isSelected()
                        ? new DecimalFormat("#0.0")
                        : new DecimalFormat("#0.00");

                String formatted = df.format(value).replace(',', '.');

                textField.setText(formatted);

            } catch (NumberFormatException e) {
                // Если не число, оставляем как есть или очищаем
                textField.setText("");
            }
        }
    }
    private void ohistka() {
        ushatok.setValue("");
        nomer.setText("");
        sehen.setText("");
        gorizont.setText("");
        privizka.setText("");
        nameProhod.setText("");
        idi.setText("");
        Wid.setText("");
        Hid.setText("");
        PlanVKL.setVisible(false);PlanVKLNe.setVisible(true);planobnov.setVisible(false);
        planVNS.setVisible(false); planVNSNE.setVisible(true);
        PoperVKL.setVisible(false);PoperVKLNe.setVisible(true);poperobnov.setVisible(false);
        poperVNS.setVisible(false); poperVNSNE.setVisible(true);
        ProdolVKL.setVisible(false);ProdolVKLNe.setVisible(true);prodolobnov.setVisible(false);
        prodolVNS.setVisible(false); prodolVNSNE.setVisible(true);
    }


    public String getIdiP(String danni){

        return switch (danni) {
            case "2.20_2.00"-> "1";case "3.70_3.70"-> "2";case "3.70_4.00"-> "3";
            case "3.70_4.20"-> "4";case "3.95_4.30"-> "5";case "4.10_4.00"-> "6";
            case "4.10_4.10"-> "7";case "4.15_4.05"-> "8";case "4.15_4.15"-> "9";
            case "4.10_4.15"-> "10";case "4.00_4.40"-> "11";case "4.20_4.20"-> "12";
            case "3.70_5.10"-> "13";case "4.20_4.40"-> "14";case "4.15_4.50"-> "15";
            case "4.05_4.90"-> "16";case "4.00_5.10"-> "17";case "4.35_4.70"-> "18";
            case "4.35_4.85"-> "19";case "5.40_3.80"-> "20";case "4.55_4.70"-> "21";
            case "4.30_5.05"-> "22";case "4.50_4.90"-> "23";case "4.55_4.90"-> "24";
            case "5.70_3.80"-> "25";case "4.45_5.20"-> "26";case "4.50_5.10"-> "27";
            case "4.55_5.15"-> "28";case "4.55_5.30"-> "29";case "4.10_6.00"-> "30";
            case "4.30_6.00"-> "31";case "4.55_5.70"-> "32";case "4.75_5.60"-> "33";
            default -> "0";

        };

    }
    private void setupImageHandlers() {
        openImageHandler(PlanVKL, "План",PlanVKLNe);
        openImageHandler(PoperVKL, "Поперечный",PoperVKLNe);
        openImageHandler(ProdolVKL, "Продольный",ProdolVKLNe);

        obnovaImageHandler(planobnov, "План",planobnov);
        obnovaImageHandler(poperobnov, "Поперечный",planobnov);
        obnovaImageHandler(prodolobnov, "Продольный",planobnov);

        sozdaniiImageHandler(planVNS, "План",PlanVKL,PlanVKLNe,planVNS,planobnov,planobnov);
        sozdaniiImageHandler(poperVNS, "Поперечный",PoperVKL,PoperVKLNe,poperVNS,poperobnov,planobnov);
        sozdaniiImageHandler(prodolVNS, "Продольный",ProdolVKL,ProdolVKLNe,prodolVNS,prodolobnov,planobnov);
    }

    protected void openImageHandler(ImageView imageView, String folder, ImageView neimage) {
        imageView.setOnMouseClicked(e -> {
            if (nomer.getText().isEmpty()) return;
            openImage(Put + "/" + nomer.getText() + "/" + folder,imageView,neimage,"yes");
        });
    }
    protected void obnovaImageHandler(ImageView imageView, String folder,ImageView im) {
        imageView.setOnMouseClicked(e -> {
            if (nomer.getText().isEmpty()) return;
            Sosdatpapky(nomer.getText(),folder,im);
        });
    }
    protected void sozdaniiImageHandler(ImageView imageView, String folder, ImageView image1, ImageView image2, ImageView image3, ImageView image4,ImageView im) {
        imageView.setOnMouseClicked(e -> {
            if (nomer.getText().isEmpty()) return;
            Sosdatpapky(nomer.getText(),folder,im);
            image1.setVisible(true); image2.setVisible(false);
            image3.setVisible(false); image4.setVisible(true);
        });}
    public void Sosdatpapky(String gor, String papka,ImageView pl){

        String path = Put + "/" + gor + "/" + papka;
        File newFolder = new File(path);

        // Создаем папку, если её нет
        if (!newFolder.exists()) {
            newFolder.mkdirs();
        }

        // Удаляем все существующие файлы в папке
        File[] existingFiles = newFolder.listFiles();
        if (existingFiles != null) {
            for (File file : existingFiles) {
                if (!file.isDirectory()) {
                    file.delete();
                }
            }
        }
        // Диалог выбора файла
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Window mainWindow = pl.getScene().getWindow(); // Получаем текущее окно
        File selectedFile = fileChooser.showOpenDialog(mainWindow);

        if (selectedFile != null) {
            try {
                File destinationFile = new File(newFolder.getAbsolutePath() + "/" + selectedFile.getName());
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Файл скопирован: " + destinationFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Ошибка при копировании файла: " + e.getMessage());
            }
        }
    }
    protected void setupTooltips() {
        Tooltip.install(planVNS, createTooltip("Внести изображение плана"));
        Tooltip.install(PlanVKL, createTooltip("Показать план"));
        Tooltip.install(PlanVKLNe, createTooltip("План не внесён"));
        Tooltip.install(planobnov, createTooltip("Обновить изображение плана"));

        Tooltip.install(poperVNS, createTooltip("Внести изображение поперечного разреза"));
        Tooltip.install(PoperVKL, createTooltip("Показать поперечный разрез"));
        Tooltip.install(PoperVKLNe, createTooltip("Поперечный разрез не внесён"));
        Tooltip.install(poperobnov, createTooltip("Обновить изображение поперечного разреза"));

        Tooltip.install(prodolVNS, createTooltip("Внести изображение продольного разреза"));
        Tooltip.install(ProdolVKL, createTooltip("Показать продольный разрез"));
        Tooltip.install(ProdolVKLNe, createTooltip("Продольный разрез не внесён"));
        Tooltip.install(prodolobnov, createTooltip("Обновить изображение продольного разреза"));

    }
    protected Tooltip createTooltip(String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setShowDelay(Duration.millis(300));
        tooltip.setStyle("-fx-font-size: 14; -fx-background-color: #aa9455;");
        return tooltip;
    }
    protected void setupCursor() {
        String hoverStyle = "-fx-cursor: hand;";//рука
        planVNS.setStyle(hoverStyle);
        PlanVKL.setStyle(hoverStyle);
        planVNSNE.setStyle(hoverStyle);
        PlanVKLNe.setStyle(hoverStyle);
        planobnov.setStyle(hoverStyle);

        poperVNS.setStyle(hoverStyle);
        PoperVKL.setStyle(hoverStyle);
        poperVNSNE.setStyle(hoverStyle);
        PoperVKLNe.setStyle(hoverStyle);
        poperobnov.setStyle(hoverStyle);

        prodolVNS.setStyle(hoverStyle);
        ProdolVKL.setStyle(hoverStyle);
        prodolVNSNE.setStyle(hoverStyle);
        ProdolVKLNe.setStyle(hoverStyle);
        prodolobnov.setStyle(hoverStyle);
    }


}






