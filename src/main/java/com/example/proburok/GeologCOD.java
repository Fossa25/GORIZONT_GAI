package com.example.proburok;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import java.io.File;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
public class GeologCOD extends Configs {

    @FXML private Button B1;
    @FXML private Button B2;
    @FXML private Button B3;
    @FXML private Button B4;
    @FXML private Button B5;
    @FXML private Button B6;

    @FXML private ImageView PlanVKL;
    @FXML private ImageView PlanVKLNe;
    @FXML private ImageView PoperVKL;
    @FXML private ImageView PoperVKLNe;
    @FXML private ImageView ProdolVKL;
    @FXML private ImageView ProdolVKLNe;



    @FXML private TextArea opisanie;
    @FXML private TextField date;
    @FXML private TextField katigoria;
    @FXML private TextField nomer;
    @FXML private Button singUpButtun;
    @FXML private ComboBox<String> gorbox;
    @FXML private ComboBox<String> namebox;

    @FXML private ComboBox<String> cbcloi;

    @FXML private TextField idi;
    @FXML private ImageView instr;
    @FXML private ImageView dokumGeolog;
    @FXML private ImageView dokumGeolog11;
    @FXML private TextField dlina;
    @FXML private ImageView tabl;
    private String faktor;
    private String opisFaktor;
    private String tippas;

    public String blak= " -fx-border-color: #00000000;-fx-background-color:#00000000;-fx-border-width: 0px";
    public String red = "-fx-border-color: #14b814;-fx-background-color:#00000000;-fx-border-width: 3px";

    List<String> soprigenii = Arrays.asList("12.8/12.8", "3.7/3.7", "3.7/2.5", "14.7/14.7", "8.5/8.5", "5.8/5.8", "12.8|12.8");

    public String tex1 = "Очень прочный, крупноблочный, угловатый массив. После отпалки поверхность рваная." +
            " Поверхность трещин шероховатая, цепкая, с редким заполнителем. Блоки зажаты. При простукивании геологическим молотком или оборке заколов издается резкий звенящий звук.";
    public String tex2 = "Непрочный слоистый массив. Наличие крупных разломов, пересекающих все сечение выработки. Разломы и трещины заполнены глинистым чешуйчатым материалом. " +
            "Заполнитель набухает и размягчается при обводнении.  Склонен к заколообразованию.";
    public String tex3 = "Мягкий тонкослоистый массив. Пластины легко отделяются друг от друга под ударами геологического молотка. " +
            "Большая доля в составе пород чешуйчатых минералов. Четко выраженные анизотропные свойства.";
    public String tex4 = "Сплошной или блочный массив. Плотная зажатая структура.  При простукивании геологическим молотком или оборке заколов издается резкий звенящий звук.";
    public String tex5 = "Разноориентированные разломы на коротком интервале горной выработки. Разломы с мягким чешуйчатым заполнителем, при обводнении высыпаются в выработку.";
    public String tex6 = "Сыпучая структура, плохо связанные минеральные зерна. Зеркала скольжения.";
    @FXML
    void initialize() {

        setupImageHandlers();
        cbcloi.getItems().addAll("Вдоль", "Вкрест");
        cbcloi.setVisible(false);
        dlina.setTextFormatter(new TextFormatter<>(change -> {
            // Всегда корректируем start и end
            int start = Math.min(change.getRangeStart(), change.getRangeEnd());
            int end = Math.max(change.getRangeStart(), change.getRangeEnd());
            change.setRange(start, end);

            String newText = change.getControlNewText();

            if (newText.isEmpty() || newText.matches("[0-9]*([.,][0-9]*)?")) {
                return change;
            }

            // Отклоняем невалидные изменения
            return null;
        }));
        viborKatigorii();
        tabl.setOnMouseClicked(mouseEvent -> openNewScene("/com/example/proburok/app.fxml"));


        DatabaseHandler dbHandler = new DatabaseHandler();
        ObservableList<Baza> bazas = dbHandler.getAllBaza();
        List<String> nom = bazas.stream() //заполнение базы
                .map(Baza::getGORIZONT)
                .distinct()
                .collect(Collectors.toList());

        gorbox.setItems(FXCollections.observableArrayList(nom));
        gorbox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                ObservableList<Baza> namespisok = dbHandler.poiskName(newValue);
                List<String> imi = namespisok.stream() //заполнение базы
                        .map(Baza::getNAME)
                        .distinct()
                        .toList();
                ohistka();
                namebox.setItems(FXCollections.observableArrayList(imi));
            }
        });
        namebox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null  && !newValue.equals(oldValue)) {
                poisk(gorbox.getValue(),newValue);
                viborKatigorii();
            }
        });

        singUpButtun.setOnMouseClicked(mouseEvent -> {
            String selectedGor = gorbox.getValue();
            String selectedName = namebox.getValue();
            String kategoriyaValue = katigoria.getText();
            String opisanieValue = opisanie.getText();
            String selectPrivizka = date.getText();
            String dlinaValue = dlina.getText();

            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "План", PlanVKL,PlanVKLNe);
            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Поперечный",PoperVKL,PoperVKLNe);
            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Продольный",ProdolVKL,ProdolVKLNe);
            try {
                // Проверка полей по очереди
                StringBuilder errors = new StringBuilder();
                if (selectedGor == null || selectedGor.isEmpty()) {errors.append("- Не выбран горизонт\n");}
                if (selectedName == null || selectedName.isEmpty()) {errors.append("- Не выбрано название выработки\n");}
                if (selectPrivizka == null || selectPrivizka.isEmpty()) {errors.append("- Не заполнена привязка\n");}
                if (dlinaValue == null || dlinaValue.isEmpty()) {errors.append("- Не заполнена длина выработки\n");}

                if (kategoriyaValue == null || kategoriyaValue.isEmpty()) { errors.append("- Не заполнено поле категория \n"); }
                if (opisanieValue == null || opisanieValue.isEmpty()) {errors.append("- Не заполнено поле описание\n");}
                if (kategoriyaValue.equals("3") && cbcloi.getValue()==null || cbcloi.getValue().isEmpty()){errors.append("- Не выбрана слоистость \n");}

                // Если есть ошибки - показываем их
                if (errors.length() > 0) {showAlert("Заполните обязательные поля:\n" + errors.toString());
                    return;
                }
                // Все данные валидны - выполняем сохранение
                faktorpoisk(kategoriyaValue);
                String prim =  "Требуется геомеханическое описание" ;
                new DatabaseHandler().DobavlenieGEOLOG(kategoriyaValue, opisanieValue,cbcloi.getValue(),dlinaValue, selectedGor, selectedName,this.faktor,this.opisFaktor,prim)    ;
                System.out.println("ТИПОВОЙ ПАСПОРТ= "+this.faktor+this.opisFaktor);
                ohistka();
              // gorbox.setValue(null);
            }  catch (Exception e) {
                showAlert("Произошла ошибка: " + e.getMessage());
            }
        });
        dokumGeolog.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_geolog,"Геология Октябрьского месторождения_");
        });
        dokumGeolog11.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_albom,"АЛЬБОМ ТИПОВЫХ ПАСПОРТОВ КРЕПЛЕНИЯ_");
        });
        instr.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_instr,"Инструкция_");
        });
    }
    private void viborKatigorii(){
        if (soprigenii.contains(idi.getText())) {
            klikKatigorii(B1,"1","no",tex1,"no");
            klikKatigorii(B2,"2","no",tex2,"yes");
            klikKatigorii(B3,"3","no",tex3,"yes");
            klikKatigorii(B4,"4","no",tex4,"no");
            klikKatigorii(B5,"5","no",tex5,"no");
            klikKatigorii(B6,"6","no",tex6,"no");
        }else {
            klikKatigorii(B1,"1","no",tex1,"no");
            klikKatigorii(B2,"2","no",tex2,"yes");
            klikKatigorii(B3,"3","yes",tex3,"yes");
            klikKatigorii(B4,"4","no",tex4,"no");
            klikKatigorii(B5,"5","no",tex5,"no");
            klikKatigorii(B6,"6","no",tex6,"no");
        }
    }

    private void klikKatigorii(Button imageView, String kat, String x, String tx,String krigik  ) {
        imageView.setOnMouseClicked(e -> {
            obnul(kat,tx);
            imageView.setStyle(red);
            if (x.equals("yes")){
                cbcloi.setVisible(true);
                cbcloi.setValue("");
            }else {
                cbcloi.setVisible(false);
                cbcloi.setValue("-");
            }
        });
    }

    private void setupImageHandlers() {
        openImageHandler(PlanVKL, "План",PlanVKLNe);
        openImageHandler(PoperVKL, "Поперечный",PoperVKLNe);
        openImageHandler(ProdolVKL, "Продольный",ProdolVKLNe);

    }

    protected void openImageHandler(ImageView imageView, String folder, ImageView neimage) {
        imageView.setOnMouseClicked(e -> {
            if (nomer.getText().isEmpty()) return;
            openImage(Put + "/" + nomer.getText() + "/" + folder,imageView,neimage,"yes");
        });
    }

    public void obnul(String x,String tx){
        if (x.equals("x")) {
            B1.setStyle(blak);
            B2.setStyle(blak);
            B3.setStyle(blak);
            B4.setStyle(blak);
            B5.setStyle(blak);
            B6.setStyle(blak);

            opisanie.setText(tx);

        }else {
            B1.setStyle(blak);
            B2.setStyle(blak);
            B3.setStyle(blak);
            B4.setStyle(blak);
            B5.setStyle(blak);
            B6.setStyle(blak);

            katigoria.setText(x);
            opisanie.setText(tx);
        }
    }

    private void poisk (String viborGOR, String viborName) {

        Platform.runLater(() -> {
        DatabaseHandler dbHandler = new DatabaseHandler();
        Baza poluh = dbHandler.danii(viborGOR, viborName);

        if (poluh != null) {
            nomer.setText(poluh.getNOMER());
            idi.setText(poluh.getIDI());
            date.setText(poluh.getPRIVIZKA());

            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "План", PlanVKL,PlanVKLNe);
            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Поперечный",PoperVKL,PoperVKLNe);
            proverkaImageGeolg(Put + "/" + nomer.getText() + "/" + "Продольный",ProdolVKL,ProdolVKLNe);


            if (poluh.getDLINA() != null) {
                dlina.setText(poluh.getDLINA());
            } else {
                dlina.setText("");
            }
            if (poluh.getOPISANIE() != null) {
                opisanie.setText(poluh.getOPISANIE());
            } else {
                opisanie.setText("");
            }
            if (poluh.getKATEGORII() != null) {
                katigoria.setText(poluh.getKATEGORII());
            } else {
                katigoria.setText("");
            }
            if (poluh.getSLOI() != null) {
                cbcloi.setValue(poluh.getSLOI());
            } else {
                cbcloi.setValue("");
            }

            if ((katigoria.getText() ==null) || katigoria.getText().isEmpty() ){
                obnul("x","");
                System.out.println("Категории нет ");
            }else {
                switch (katigoria.getText()){
                    case"1" -> {obnul("x",opisanie.getText());B1.setStyle(red);cbcloi.setVisible(false);}
                    case"2" -> {obnul("x",opisanie.getText());B2.setStyle(red);cbcloi.setVisible(false);}
                    case"3" -> {obnul("x",opisanie.getText());B3.setStyle(red);cbcloi.setVisible(true);}
                    case"4" -> {obnul("x",opisanie.getText());B4.setStyle(red);cbcloi.setVisible(false);}
                    case"5" ->{obnul("x",opisanie.getText());B5.setStyle(red);cbcloi.setVisible(false);}
                    case"6" -> {obnul("x",opisanie.getText());B6.setStyle(red);cbcloi.setVisible(false);}

                }}

        } else {

            System.out.println("Данные не найдены для " + viborGOR + " - " + viborName);
            nomer.clear();
            date.clear(); // Пользователь не найден
        }
        });

    }
    private void ohistka(){
        obnul("x","");
        //namebox.setValue(null);
        nomer.setText("");
        date.setText("");
        katigoria.setText("");
        opisanie.setText("");
        dlina.setText("");
        tippas="";
        PlanVKL.setVisible(false);PlanVKLNe.setVisible(true);
        PoperVKL.setVisible(false);PoperVKLNe.setVisible(true);
        ProdolVKL.setVisible(false);ProdolVKLNe.setVisible(true);
        cbcloi.setVisible(false);
        this.faktor="";
        this.opisFaktor="";
    }

    void proverkaImageGeolg(String imagePath,ImageView VKL,ImageView VKLNE) {
        File folder = new File(imagePath);
        // Проверяем, существует ли папка
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Папка не найдена: " + imagePath);

            VKL.setVisible(false);VKLNE.setVisible(true);
            return;
        }
        // Получаем список файлов в папке
        File[] files = folder.listFiles((dir, name) -> {
            // Фильтруем файлы по расширению (изображения)
            String lowerCaseName = name.toLowerCase();
            return lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".png") ||
                    lowerCaseName.endsWith(".jpeg") || lowerCaseName.endsWith(".gif");
        });
        // Проверяем, есть ли изображения в папке
        if (files != null && files.length > 0) {


            VKL.setVisible(true);VKLNE.setVisible(false);

        } else {
            System.err.println("В папке нет изображений.");

            VKL.setVisible(false);VKLNE.setVisible(true);
        }
    }
    private void faktorpoisk (String kat) throws ParseException {
        // Проверка входных данных
        if (kat == null || kat.isEmpty()) {
            throw new IllegalArgumentException("Значение list не может быть пустым");
        }
        switch (kat) {

            case "3" -> {this.faktor = "Г";this.opisFaktor = "Взаимная ориентация направления проходки и слоистости горных пород.";}
            case "4","5","6" -> {this.faktor = "Б";this.opisFaktor = "Разуплотнение горного массива при сейсмическом воздействии от взрывных работ.\n";}



        }
    }
}

