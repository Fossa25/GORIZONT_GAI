package com.example.proburok;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class GeomexCOD extends Configs {


    @FXML private ImageView PlanVKL;
    @FXML private ImageView PlanVKLNe;
    @FXML private ImageView PoperVKL;
    @FXML private ImageView PoperVKLNe;
    @FXML private ImageView ProdolVKL;
    @FXML private ImageView ProdolVKLNe;
    @FXML private Button butnA;
    @FXML private Button butnB;

    @FXML private Button butnN;
    @FXML private Button butnV;
    @FXML private Button butnG;
    @FXML private Button butnD;
    @FXML private TextField privaz;
    @FXML private TextField dlina;
    @FXML private TextField idi;
    @FXML private ComboBox<String> gorbox;
    @FXML private TextField katigoria;
    @FXML private ComboBox<String> namebox;
    @FXML private TextField nomer;
    @FXML private TextArea opisanie;
    @FXML private Button singUpButtun;
    @FXML private TextField texfak;

    @FXML private TextArea opisanieA;

    @FXML private TextArea opisanieB;

    @FXML private TextArea opisanieN;

    @FXML private TextArea opisanieV;
    @FXML private TextArea opisanieG;
    @FXML private TextArea opisanieD;

    @FXML private ImageView instr;
    @FXML private ImageView dokumGeolog;
    @FXML private ImageView dokumGeolog11;
    @FXML private ImageView tabl;
    public String textopisaniA = "Проходка в разуплотненном горном массиве после его подработки. (Укажите конкретную причину).";
    public String textopisaniB = "Разуплотнение горного массива при сейсмическом воздействии от взрывных работ.";
    public String textopisaniV = "Проходка горных выработок в условиях высокого горного давления. (Укажите конкретную причину).";
    public String textopisaniG = "Взаимная ориентация направления проходки и слоистости горных пород .";
    public String textopisaniD = "Увеличенный срок эксплуатации выработки. (Укажите конкретную причину).";

public String seri= "-fx-background-radius: 50px; -fx-background-color: #5e5d5d;";
public String zoloto = "-fx-background-color: aa9455";
    public String tippas;
    public String tfopis;

    List<String> soprigenii = Arrays.asList("27","28","29","30","31","32");
    @FXML
    void initialize() {
        idi.setVisible(false);
// Создаем фильтр, запрещающий точку с запятой
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (change.getText().contains(";")) {
                return null; // отклоняем изменение
            }
            return change;
        };

// Применяем фильтр к полям описания
        opisanieA.setTextFormatter(new TextFormatter<>(filter));
        opisanieB.setTextFormatter(new TextFormatter<>(filter));
        opisanieV.setTextFormatter(new TextFormatter<>(filter));

        tabl.setOnMouseClicked(mouseEvent -> openNewScene("/com/example/proburok/app.fxml"));

        dokumGeolog.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_geolog,"Геология Юбилейного месторождения_");
        });
        dokumGeolog11.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_texusovia,"Технологические условия_");
        });
        instr.setOnMouseClicked(mouseEvent -> {
            OpenDok(Put_instr,"Инструкция_");
        });

        butnA.setOnMouseClicked(mouseEvent -> {
            String stari =texfak.getText() ;
            if(stari == null) stari = "";
            if (butnA.getStyle().equals(seri)){
            butnA.setStyle(zoloto);
            opisanieA.setText(textopisaniA);
            opisanieA.setEditable(true);

                switch (stari) {

                    case "Б","БД" ->  {
                        texfak.setText("А"+stari);butnV.setStyle(seri);butnV.setDisable(true);opisanieV.setDisable(true);
                    }
                    default -> {
                        texfak.setText("А"+stari);butnV.setStyle(seri);butnV.setDisable(false);opisanieV.setDisable(false);
                    }
                }

            } else if (butnA.getStyle().equals(zoloto)) {
                butnA.setStyle(seri);
                String nowi = stari.replace("А","");
                texfak.setText(nowi);
                butnV.setStyle(seri);butnV.setDisable(false);opisanieV.setDisable(false);
                opisanieA.setText(null);
                opisanieA.setEditable(false);
            }
        });
        butnB.setOnMouseClicked(mouseEvent -> {
            String stari =texfak.getText() ;
            if(stari == null) stari = "";
            if (butnB.getStyle().equals(seri)){
                butnB.setStyle(zoloto);
                opisanieB.setText(textopisaniB);
                opisanieB.setEditable(true);

                switch (stari) {
                    case "" -> texfak.setText("Б");
                    case "А" -> texfak.setText(stari + "Б");
                    case "Г", "В","Д","ВГ","ВД","ГД","ВГД" -> texfak.setText("Б"+stari);
                    case "АВ" -> texfak.setText("АБВ");
                    case "АГ" -> texfak.setText("АБГ");
                    case "АД" -> texfak.setText("АБД");
                    case "АВГ" -> texfak.setText("АБВГ");
                    case "АВД" -> texfak.setText("АБВД");
                    case "АГД" -> texfak.setText("АБГД");
                    case "АВГД" -> texfak.setText("АБВГД");
                }
            } else if (butnB.getStyle().equals(zoloto)) {
                butnB.setStyle(seri);
                String nowi = stari.replace("Б","");
                texfak.setText(nowi);
                opisanieB.setText(null);
                opisanieB.setEditable(false);
            }
        });
        butnV.setOnMouseClicked(mouseEvent -> {
            String stari =texfak.getText() ;
            if(stari == null) stari = "";
            if (butnV.getStyle().equals(seri)){
                butnV.setStyle(zoloto);
                opisanieV.setText(textopisaniV);
                opisanieV.setEditable(true);

                switch (stari) {
                    case "Б" -> {
                        texfak.setText(stari + "В");butnA.setStyle(seri);butnA.setDisable(true);opisanieA.setDisable(true);
                    }
                    case "БД" -> {
                        texfak.setText("БВД");butnA.setStyle(seri);butnA.setDisable(true);opisanieA.setDisable(true);
                    }

                    case "" -> {
                        texfak.setText("В");butnA.setDisable(false);opisanieA.setDisable(false);
                    }
                    case "А","АБ" -> {
                        texfak.setText(stari + "В");butnA.setDisable(false);opisanieA.setDisable(false);
                    }
                    case "Г","Д","ГД" -> {
                        texfak.setText("В"+stari );butnA.setDisable(false);opisanieA.setDisable(false);
                    }
                    case "АГ" -> {
                        texfak.setText("АВГ");butnA.setDisable(false);opisanieA.setDisable(false);
                    }
                    case "АД" ->{
                        texfak.setText("АВД");butnA.setDisable(false);opisanieA.setDisable(false);
                    }
                    case "БГ" -> {
                        texfak.setText("БВГ");butnA.setDisable(false);opisanieA.setDisable(false);
                    }
                    case "АБГ" -> {
                        texfak.setText("АБВГ");butnA.setDisable(false);opisanieA.setDisable(false);
                    }
                    case "АБД" -> {
                        texfak.setText("АБВД");butnA.setDisable(false);opisanieA.setDisable(false);
                    }
                    case "БГД" -> {
                        texfak.setText("БВГД");butnA.setDisable(false);opisanieA.setDisable(false);
                    }
                    case "АБГД" -> {
                        texfak.setText("АБВГД");butnA.setDisable(false);opisanieA.setDisable(false);
                    }
                }
            } else if (butnV.getStyle().equals(zoloto)) {
                butnV.setStyle(seri);
                String nowi = stari.replace("В","");
                texfak.setText(nowi);
                butnA.setDisable(false);opisanieA.setDisable(false);
                opisanieV.setText(null);
                opisanieV.setEditable(false);
            }
        });
        butnG.setOnMouseClicked(mouseEGent -> {
            String stari =texfak.getText() ;
            if(stari == null) stari = "";
            if (butnG.getStyle().equals(seri)){
                butnG.setStyle(zoloto);
                opisanieG.setText(textopisaniG);
                opisanieG.setEditable(true);

                switch (stari) {
                    case "" -> texfak.setText("Г");
                    case "А", "Б","В","АБ","АВ","БВ","АБВ" -> texfak.setText(stari + "Г");
                    case "Д" -> texfak.setText("Г"+stari );
                    case "АД" -> texfak.setText("АГД");
                    case "БД" -> texfak.setText("БГД");
                    case "ВД" -> texfak.setText("ВГД");
                    case "АБД" -> texfak.setText("АБГД");
                    case "БВД" -> texfak.setText("БВГД");
                    case "АБВД" -> texfak.setText("АБВГД");
                }
            } else if (butnG.getStyle().equals(zoloto)) {
                butnG.setStyle(seri);
                String nowi = stari.replace("Г","");
                texfak.setText(nowi);
                opisanieG.setText(null);
                opisanieG.setEditable(false);
            }
        });
        butnD.setOnMouseClicked(mouseEDent -> {
            String stari =texfak.getText() ;
            if(stari == null) stari = "";
            if (butnD.getStyle().equals(seri)){
                butnD.setStyle(zoloto);
                opisanieD.setText(textopisaniD);
                opisanieD.setEditable(true);
                if (stari.isEmpty()) {
                    texfak.setText("Д");
                } else {
                    texfak.setText(stari + "Д");
                }
            } else if (butnD.getStyle().equals(zoloto)) {
                butnD.setStyle(seri);
                String nowi = stari.replace("Д","");
                texfak.setText(nowi);
                opisanieD.setText(null);
                opisanieD.setEditable(false);
            }
        });


        butnN.setOnMouseClicked(mouseEvent -> {
            String stari =texfak.getText() ;
            if(stari == null) stari = "";
            if (butnN.getStyle().equals(seri)){
                butnN.setStyle(zoloto);
                butnA.setDisable(true);butnB.setDisable(true);butnV.setDisable(true);
                butnA.setStyle(seri);butnB.setStyle(seri);butnV.setStyle(seri);
                butnG.setDisable(true);butnD.setDisable(true);
                butnG.setStyle(seri);butnD.setStyle(seri);
                texfak.setText("-");
                opisanieA.setText(null);opisanieA.setEditable(false);
                opisanieB.setText(null);opisanieB.setEditable(false);
                opisanieV.setText(null);opisanieV.setEditable(false);
                opisanieG.setText(null);opisanieG.setEditable(false);
                opisanieD.setText(null);opisanieD.setEditable(false);
                opisanieN.setText("технологические факторы не будут оказывать влияние.");

            } else if (butnN.getStyle().equals(zoloto)) {
                butnN.setStyle(seri);
                butnA.setDisable(false);butnB.setDisable(false);butnV.setDisable(false);
                butnG.setDisable(false);butnD.setDisable(false);
                texfak.setText(null);
                opisanieN.setText(null);
            }
        });
        DatabaseHandler dbHandler = new DatabaseHandler();
        ObservableList<Baza> bazas = dbHandler.getAllBaza();
        List<String> nom = bazas.stream() //заполнение базы
                .map(Baza::getGORIZONT)
                .distinct()
                .collect(Collectors.toList());

        gorbox.setItems(FXCollections.observableArrayList(nom));

        // Слушатель изменений выбора в gorbox
        gorbox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
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
            if (newValue != null ) {

                poisk(gorbox.getValue(),newValue);
                poiskFAKTOR();
            }
        });

        singUpButtun.setOnMouseClicked(mouseEvent -> {
            String selectedGor = gorbox.getValue();
            String selectedName = namebox.getValue();
            String kategoriyaValue = katigoria.getText();
            String opisanieValue = opisanie.getText();
            String selectPrivizka = privaz.getText();
            String dlinaValue = dlina.getText();
            String faktorValue = texfak.getText();
            String AValue= opisanieA.getText();
            String BValue= opisanieB.getText();
            String VValue= opisanieV.getText();
            String GValue= opisanieG.getText();
            String DValue= opisanieD.getText();

            openImage( Put+"/"+ nomer.getText() +"/План",PlanVKL,PlanVKLNe,"no");
            openImage(Put+"/"+ nomer.getText() +"/Поперечный",PoperVKL,PoperVKLNe,"no");
            openImage(Put+"/"+ nomer.getText() +"/Продольный" ,ProdolVKL,ProdolVKLNe,"no");
            try {
                // Проверка полей по очереди
                StringBuilder errors = new StringBuilder();
                if (selectedGor == null || selectedGor.isEmpty()) {errors.append("- Не выбран горизонт\n");}
                if (selectedName == null || selectedName.isEmpty()) {errors.append("- Не выбрано название выработки\n");}
                if (selectPrivizka == null || selectPrivizka.isEmpty()) {errors.append("- Не заполнена привязка\n");}
                if (dlinaValue == null || dlinaValue.isEmpty()) {errors.append("- Не заполнена длина выработки\n");}
                if (kategoriyaValue == null || kategoriyaValue.isEmpty()) { errors.append("- Не заполнено поле категория \n"); }
                if (opisanieValue == null || opisanieValue.isEmpty()) {errors.append("- Не заполнено поле описание\n");}
                if (faktorValue == null || faktorValue.isEmpty()) {errors.append("- Влияющий на устойчивость тех.фактор не выбран\n");}

                if (butnA.getStyle().equals(zoloto)) {
                        if( AValue == null || AValue.isEmpty() || AValue.equals(textopisaniA) ) {errors.append("- Укажите конкретную причину фактора А\n");}}

                if (butnB.getStyle().equals(zoloto)) {
                    if( BValue == null || BValue.isEmpty()  ) {errors.append("- Поле описание для фактора Б пустое\n");}}

                if (butnV.getStyle().equals(zoloto)) {
                    if( VValue == null || VValue.isEmpty() || VValue.equals(textopisaniV) ) {errors.append("- Укажите конкретную причину фактора В\n");}}
                if (butnG.getStyle().equals(zoloto)) {
                    if( GValue == null || GValue.isEmpty()  ) {errors.append("- Поле описание для фактора Г пустое\n");}}

                if (butnD.getStyle().equals(zoloto)) {
                    if( DValue == null || DValue.isEmpty() || DValue.equals(textopisaniD) ) {errors.append("- Укажите конкретную причину фактора Д\n");}}


                if (PlanVKLNe.isVisible()  ) {errors.append("- Не внесён план\n");}
                //if (PlanVKLNe.isVisible() ) {errors.append("- Не внесён поперечный разрез\n");}
               // if (ProdolVKLNe.isVisible()  ) {errors.append("- Не внесён продольный разрез\n");}


                // Если есть ошибки - показываем их
                if (errors.length() > 0) {showAlert("Заполните обязательные поля:\n" + errors.toString());
                    return;
                }
                assert faktorValue != null;
                tfopis = switch (faktorValue) {
                                    case "А" -> AValue;
                                    case "Б" -> BValue;
                                    case "В" -> VValue;
                                    case "Г" -> GValue;
                                    case "Д" -> DValue;
                                    case "АБ" -> AValue + ";\n"+ BValue;
                                    case "АВ" -> AValue + ";\n"+ VValue;
                                    case "АГ" -> AValue + ";\n"+ GValue;
                                    case "АД" -> AValue + ";\n"+ DValue;
                                    case "БВ" -> BValue +";\n"+ VValue;
                                    case "БГ" -> BValue +";\n"+ GValue;
                                    case "БД" -> BValue +";\n"+ DValue;
                                    case "ВГ" -> VValue +";\n"+ GValue;
                                    case "ВД" -> VValue +";\n"+ DValue;
                                    case "ГД" -> GValue +";\n"+ DValue;
                                    case "АБВ" -> AValue + ";\n"+ BValue +";\n"+ VValue;
                                    case "АБГ" -> AValue + ";\n"+ BValue +";\n"+ GValue;
                                    case "АБД" -> AValue + ";\n"+ BValue +";\n"+ DValue;
                                    case "АВГ" -> AValue + ";\n"+ VValue +";\n"+ GValue;
                                    case "АВД" -> AValue + ";\n"+ VValue +";\n"+ DValue;
                                    case "АГД" -> AValue + ";\n"+ GValue +";\n"+ DValue;
                                    case "БВГ" -> BValue + ";\n"+ VValue +";\n"+ GValue;
                                    case "БВД" -> BValue + ";\n"+ VValue +";\n"+ DValue;
                                    case "ВГД" -> VValue + ";\n"+ GValue +";\n"+ DValue;
                    case "АБВГ" -> AValue + ";\n"+ BValue +";\n"+ VValue+";\n"+ GValue;
                    case "АБВД" -> AValue + ";\n"+ BValue +";\n"+ VValue+";\n"+ DValue;
                    case "БВГД" -> BValue +";\n"+ VValue+";\n"+";\n"+ GValue+";\n"+ DValue;
                    case "АБВГД" -> AValue + ";\n"+ BValue +";\n"+ VValue+";\n"+ GValue+";\n"+ DValue;

                                    case "-" -> opisanieN.getText();
                    default -> throw new IllegalStateException("Unexpected value: " + faktorValue);
                };

                getPas();
                String prim =  "Все данные внесены" ;
                new DatabaseHandler().DobavlenieGEOMEX(faktorValue, tfopis,tippas,selectedGor, selectedName,prim);
                System.out.println(tfopis);
                System.out.println("ДЛЯ idi - "+idi.getText()+" НОМЕР - "+tippas);
                ohistka();
                //gorbox.setValue(null);
            }  catch (Exception e) {
                showAlert("Произошла ошибка: " + e.getMessage());
            }

        });
        PlanVKL.setOnMouseClicked(mouseEvent -> {
            String imagePath = Put+"/"+ nomer.getText() +"/План"; // Замените на ваш путь
            openImage(imagePath,PlanVKL,PlanVKLNe,"yes");
        });
        PoperVKL.setOnMouseClicked(mouseEvent -> {
            String imagePath = Put+"/"+ nomer.getText() +"/Поперечный"; // Замените на ваш путь
            openImage(imagePath,PoperVKL,PoperVKLNe,"yes");
        });
        ProdolVKL.setOnMouseClicked(mouseEvent -> {
            String imagePath = Put+"/"+ nomer.getText() +"/Продольный"; // Замените на ваш путь
            openImage(imagePath,ProdolVKL,ProdolVKLNe,"yes");
        });
    }

    private void poisk (String viborGOR, String viborName) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        Baza poluh = dbHandler.danii(viborGOR, viborName);
        opisanieN.setText("");
        opisanieA.setText("");
        opisanieB.setText("");
        opisanieV.setText("");
        opisanieG.setText("");
        opisanieD.setText("");

        if (poluh != null) {
            nomer.setText(poluh.getNOMER());
            privaz.setText(poluh.getPRIVIZKA());
            idi.setText(poluh.getIDI());
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
            String faktor = poluh.getFAKTOR();
            texfak.setText(faktor != null ? faktor : "");
            if (faktor != null && faktor.equals("-")){
                opisanieN.setText(poluh.getTFOPISANIE());
             } else { DroblenieTEXT(poluh.getTFOPISANIE(), poluh.getFAKTOR());}

            PlanVKL.setVisible(true);PlanVKLNe.setVisible(false);
            PoperVKL.setVisible(true);PoperVKLNe.setVisible(false);
            ProdolVKL.setVisible(true);ProdolVKLNe.setVisible(false);
            poiskFAKTOR();
        } else {
            nomer.clear();        }
    }
    public void DroblenieTEXT (String tfopis, String faktorValue) {
        // Очищаем поля перед загрузкой
        opisanieA.setText("");
        opisanieB.setText("");
        opisanieV.setText("");
        opisanieG.setText("");
        opisanieD.setText("");

        // Проверяем наличие данных
        if (tfopis == null || tfopis.isEmpty() || faktorValue == null) {
            return;
        }

        // Разделяем строку с учётом разделителя ";\n"
        String[] parts = tfopis.split(";\\s*\\n");
        int index = 0;

        // Распределяем части согласно порядку факторов
        if (faktorValue.contains("А")) {
            if (index < parts.length) {
                opisanieA.setText(parts[index].trim());
                index++;
            }
        }

        if (faktorValue.contains("Б")) {
            if (index < parts.length) {
                opisanieB.setText(parts[index].trim());
                opisanieB.setDisable(true);
                index++;
            }
        }

        if (faktorValue.contains("В")) {
            if (index < parts.length) {
                opisanieV.setText(parts[index].trim());
                index++;
            }
        }
        if (faktorValue.contains("Г")) {
            if (index < parts.length) {
                opisanieG.setText(parts[index].trim());
                opisanieG.setDisable(true);
                index++;
            }
        }
        if (faktorValue.contains("Д")) {
            if (index < parts.length) {
                opisanieD.setText(parts[index].trim());
                index++;
            }
        }
    }

    private void poiskFAKTOR(){
        if ((texfak.getText() ==null) || texfak.getText().isEmpty() ){

            butnN.setStyle(seri);butnN.setDisable(false);opisanieN.setDisable(false);
            butnA.setStyle(seri);butnB.setStyle(seri);butnV.setStyle(seri);
            butnG.setStyle(seri);butnD.setStyle(seri);
            butnA.setDisable(false);butnB.setDisable(true);butnV.setDisable(false);
            butnG.setDisable(true);butnD.setDisable(false);
        } else {
            switch (texfak.getText()){
                case "А" -> butstail(zoloto,seri,seri,seri,seri);
                case "Б" -> butstail(seri,zoloto,seri,seri,seri);
                case "В" -> butstail(seri,seri,zoloto,seri,seri);
                case "Г" -> butstail(seri,seri,seri,zoloto,seri);
                case "Д" ->  butstail(seri,seri,seri,seri,zoloto);
                case "АБ" -> butstail(zoloto,zoloto,seri,seri,seri);
                case "АВ" -> butstail(zoloto,seri,zoloto,seri,seri);
                case "АГ" -> butstail(zoloto,seri,seri,zoloto,seri);
                case "АД" -> butstail(zoloto,seri,seri,seri,zoloto);
                case "БВ" ->butstail(seri,zoloto,zoloto,seri,seri);
                case "БГ" -> butstail(seri,zoloto,seri,zoloto,seri);
                case "БД" -> butstail(seri,zoloto,seri,seri,zoloto);
                case "ВГ" -> butstail(seri,seri,zoloto,zoloto,seri);
                case "ВД" ->  butstail(seri,seri,zoloto,seri,zoloto);
                case "ГД" ->  butstail(seri,seri,seri,zoloto,zoloto);
                case "АБВ" ->  butstail(zoloto,zoloto,zoloto,seri,seri);
                case "АБГ" -> butstail(zoloto,zoloto,seri,zoloto,seri);
                case "АБД" -> butstail(zoloto,zoloto,seri,seri,zoloto);
                case "АВГ" -> butstail(zoloto,seri,zoloto,zoloto,seri);
                case "АВД" -> butstail(zoloto,seri,zoloto,seri,zoloto);
                case "АГД" -> butstail(zoloto,seri,seri,zoloto,zoloto);
                case "БВГ" -> butstail(seri,zoloto,zoloto,zoloto,seri);
                case "БВД" -> butstail(seri,zoloto,zoloto,seri,zoloto);
                case "ВГД" -> butstail(seri,seri,zoloto,zoloto,zoloto);
                case "АБВГ" -> butstail(zoloto,zoloto,zoloto,zoloto,seri);
                case "АБВД" -> butstail(zoloto,zoloto,zoloto,seri,zoloto);
                case "БВГД" -> butstail(seri,zoloto,zoloto,zoloto,zoloto);
                case "АБВГД" -> butstail(zoloto,zoloto,zoloto,zoloto,zoloto);
                case "-" -> butstail(seri,seri,seri,seri,seri);
    }}}
    private void butstail(String As,String Bs,String Vs,String Gs,String Ds){
        if (texfak.getText().equals(("-"))){
            butnN.setStyle(zoloto);butnN.setDisable(false);opisanieN.setDisable(false);
            butnA.setStyle(As);butnB.setStyle(Bs);butnV.setStyle(Vs);
            butnG.setStyle(Gs);butnD.setStyle(Ds);
            butnA.setDisable(true);butnB.setDisable(true);butnV.setDisable(true);
            butnG.setDisable(true);butnD.setDisable(true);
            System.out.println("ДЛЯ  - ");
            return;
        }
        else if(texfak.getText().contains("Б")||texfak.getText().contains("Г")){
            butnN.setStyle(seri); butnN.setDisable(true);opisanieN.setDisable(true);
            butnA.setStyle(As);butnB.setStyle(Bs);butnV.setStyle(Vs);
            butnG.setStyle(Gs);butnD.setStyle(Ds);
            butnA.setDisable(false);butnB.setDisable(true);butnV.setDisable(false);
            butnG.setDisable(true);butnD.setDisable(false);
            System.out.println("ДЛЯ  B ");
            return;
        }
        butnN.setStyle(seri);butnN.setDisable(false);opisanieN.setDisable(false);
        butnA.setStyle(As);butnB.setStyle(Bs);butnV.setStyle(Vs);
        butnG.setStyle(Gs);butnD.setStyle(Ds);
        butnA.setDisable(false);butnB.setDisable(true);butnV.setDisable(false);
        butnG.setDisable(true);butnD.setDisable(false);
        System.out.println("ДЛЯ  d ");
    }
    private void ohistka(){

        namebox.setValue("");
        nomer.setText("");
        privaz.setText("");
        katigoria.setText("");
        opisanie.setText("");
        dlina.setText("");
        idi.setText("");
        texfak.setText("");
        opisanieA.setText("");
        opisanieB.setText("");
        opisanieV.setText("");
        opisanieB.setText("");
        opisanieG.setText("");
        opisanieD.setText("");
        PlanVKL.setVisible(false);PlanVKLNe.setVisible(true);
        PoperVKL.setVisible(false);PoperVKLNe.setVisible(true);
        ProdolVKL.setVisible(false);ProdolVKLNe.setVisible(true);
        butnN.setStyle(seri);
        butnA.setStyle(seri);butnB.setStyle(seri);butnV.setStyle(seri);
        butnG.setStyle(seri);butnD.setStyle(seri);
        butnA.setDisable(false);butnB.setDisable(false);butnV.setDisable(false);
        butnG.setDisable(false);butnD.setDisable(false);
    }
    private void getPas () {
        String kat = katigoria.getText();
        String fakt = texfak.getText();
        String nomer=idi.getText();
        if (!fakt.isEmpty()){
            if (soprigenii.contains(nomer)) {
                getFAKT27_32(kat,fakt,nomer);
        }else {
            switch (fakt) {
                case "-" -> getFAKT_NOY(kat,nomer,fakt);
                case "АБВ" -> getFAKT_ABV(nomer);
                case "АБ" -> getFAKT_AB(kat,nomer);
                case "Б" -> getFAKT_B(kat,nomer);
                case "БВ" -> getFAKT_BV(kat,nomer);

            }}
    }}
    private void getFAKT_BV (String kat,String nomer) {
        tippas = switch (kat) {
            case "6" -> switch (nomer) {
                case "3" -> "123";
                case "5" -> "25";
                case "6" -> "129";
                case "7" -> "135";
                case "8" -> "141";
                case "9" -> "147";
                case "10" -> "153";
                case "15" -> "72";
                case "16" -> "79";
                case "17" -> "159";
                case "18" -> "165";
                default -> "Типовой паспорт не разработан";
            };
            case "7" -> switch (nomer) {
                case "3" -> "126";
                case "5" -> "28";
                case "6" -> "132";
                case "7" -> "138";
                case "8" -> "144";
                case "9" -> "150";
                case "10" -> "156";
                case "15" -> "75";
                case "16" -> "82";
                case "17" -> "162";
                case "18" -> "168";
                default -> "Типовой паспорт не разработан";
            };
            default -> "Типовой паспорт не разработан";
        };
    }
    private void getFAKT_B (String kat,String nomer) {
        tippas = switch (kat) {
            case "6" -> switch (nomer) {
                case "3" -> "122";
                case "5" -> "24";
                case "6" -> "128";
                case "7" -> "134";
                case "8" -> "140";
                case "9" -> "146";
                case "10" -> "152";
                case "15" -> "71";
                case "16" -> "78";
                case "17" -> "158";
                case "18" -> "164";
                default -> "Типовой паспорт не разработан";
            };
            case "7" -> switch (nomer) {
                case "3" -> "125";
                case "5" -> "27";
                case "6" -> "131";
                case "7" -> "137";
                case "8" -> "143";
                case "9" -> "149";
                case "10" -> "155";
                case "15" -> "74";
                case "16" -> "81";
                case "17" -> "161";
                case "18" -> "167";
                default -> "Типовой паспорт не разработан";
            };
            default -> "Типовой паспорт не разработан";
        };
    }
    private void getFAKT_AB (String kat,String nomer) {
        tippas = switch (kat) {
            case "6" -> switch (nomer) {
                case "3" -> "121";
                case "5" -> "23";
                case "6" -> "127";
                case "7" -> "133";
                case "8" -> "139";
                case "9" -> "145";
                case "10" -> "151";
                case "15" -> "70";
                case "16" -> "77";
                case "17" -> "157";
                case "18" -> "163";
                default -> "Типовой паспорт не разработан";
            };
            case "7" -> switch (nomer) {
                case "3" -> "124";
                case "5" -> "26";
                case "6" -> "130";
                case "7" -> "136";
                case "8" -> "142";
                case "9" -> "148";
                case "10" -> "154";
                case "15" -> "73";
                case "16" -> "80";
                case "17" -> "160";
                case "18" -> "166";
                default -> "Типовой паспорт не разработан";
            };
            default -> "Типовой паспорт не разработан";
        };
    }

    private void getFAKT_ABV (String nomer) {
        tippas = switch (nomer) {

            case "4" -> "17";
            case "5" -> "22";
            case "15" -> "69";
            case "16" -> "76";

            default -> "Типовой паспорт не разработан";
        };
    }
    private void getFAKT_NOY (String kat,String nomer,String fakt) {
        if (fakt.equals("-")){
        tippas = switch (nomer) {
            case "1" -> switch (kat) {
                case "1" -> "1";case "2" -> "2";case "3" -> "3";case "4" -> "4";
                default -> "Типовой паспорт не разработан";
            };
            case "2" -> switch (kat) {
                case "1" -> "5";case "2" -> "6";case "3" -> "7";case "4" -> "8";
                default -> "Типовой паспорт не разработан";
            };
            case "3" -> switch (kat) {
                case "1" -> "9";case "2" -> "10";case "3" -> "11";case "4" -> "12";
                default -> "Типовой паспорт не разработан";
            };

            case "4" -> switch (kat) {
                 case "1" -> "13";case "2" -> "14";case "3" -> "15";case "4" -> "16";
                default -> "Типовой паспорт не разработан";
            };
            case "5" -> switch (kat) {
                case "1" -> "18";case "2" -> "19";case "3" -> "20";case "4" -> "21";
                default -> "Типовой паспорт не разработан";
            };

            case "6" -> switch (kat) {
                case "1" -> "29";case "2" -> "30";case "3" -> "31";case "4" -> "32";
                default -> "Типовой паспорт не разработан";
            };
            case "7" -> switch (kat) {
                case "1" -> "33";case "2" -> "34";case "3" -> "35";case "4" -> "36";
                default -> "Типовой паспорт не разработан";
            };
            case "8" -> switch (kat) {
                case "1" -> "37";case "2" -> "38";case "3" -> "39";case "4" -> "40";
                default -> "Типовой паспорт не разработан";
            };
            case "9" -> switch (kat) {
                case "1" -> "41";case "2" -> "42";case "3" -> "43";case "4" -> "44";
                default -> "Типовой паспорт не разработан";
            };
            case "10" -> switch (kat) {
                case "1" -> "45";case "2" -> "46";case "3" -> "47";case "4" -> "48";
                default -> "Типовой паспорт не разработан";
            };
            case "11" -> switch (kat) {
                case "1" -> "49";case "2" -> "50";case "3" -> "51";case "4" -> "52";
                default -> "Типовой паспорт не разработан";
            };
            case "12" -> switch (kat) {
                case "1" -> "53";case "2" -> "54";case "3" -> "55";case "4" -> "56";
                default -> "Типовой паспорт не разработан";
            };
            case "13" -> switch (kat) {
                case "1" -> "57";case "2" -> "58";case "3" -> "59";case "4" -> "60";
                default -> "Типовой паспорт не разработан";
            };
            case "14" -> switch (kat) {
                case "1" -> "61";case "2" -> "62";case "3" -> "63";case "4" -> "64";
                default -> "Типовой паспорт не разработан";
            };
            case "15" -> switch (kat) {
                case "1" -> "65";case "2" -> "66";case "3" -> "67";case "4" -> "68";
                default -> "Типовой паспорт не разработан";
            };

            case "17" -> switch (kat) {
                case "1" -> "83";case "2" -> "84";case "3" -> "85";case "4" -> "86";
                default -> "Типовой паспорт не разработан";
            };
            case "18" -> switch (kat) {
                case "1" -> "87";case "2" -> "88";case "3" -> "89";case "4" -> "90";
                default -> "Типовой паспорт не разработан";
            };
            case "19" -> switch (kat) {
                case "1" -> "91";case "2" -> "92";case "3" -> "93";case "4" -> "94";
                default -> "Типовой паспорт не разработан";
            };
            case "20" -> switch (kat) {
                case "1" -> "95";case "2" -> "96";case "3" -> "97";case "4" -> "98";
                default -> "Типовой паспорт не разработан";
            };
            case "21" -> switch (kat) {
                case "1" -> "99";case "2" -> "100";case "3" -> "101";case "4" -> "102";
                default -> "Типовой паспорт не разработан";
            };
            case "22" -> switch (kat) {
                case "1" -> "103";case "2" -> "104";case "3" -> "105";case "4" -> "106";
                default -> "Типовой паспорт не разработан";
            };
            case "23" -> switch (kat) {
                case "1" -> "107";case "2" -> "108";case "3" -> "109";case "4" -> "110";
                default -> "Типовой паспорт не разработан";
            };
            case "24" -> switch (kat) {
                case "1" -> "111";case "2" -> "112";case "3" -> "113";case "4" -> "114";
                default -> "Типовой паспорт не разработан";
            };
            case "25" -> switch (kat) {
                case "1" -> "115";case "2" -> "116";case "4" -> "117";
                default -> "Типовой паспорт не разработан";
            };
            case "26" -> switch (kat) {
                case "1" -> "118";case "2" -> "119";case "4" -> "120";
                default -> "Типовой паспорт не разработан";
            };
            default -> "Типовой паспорт не разработан";
        };
    }else{tippas="Типовой паспорт не разработан";}
    }


    private void getFAKT27_32 (String kat,String fakt,String hifr) {
        tippas = switch (kat) {
            case "1" -> switch (fakt) {
                case "-" -> switch (hifr) {
                    case "27" -> "1";
                    case "28" -> "10";
                    case "29" -> "19";
                    case "30" -> "28";
                    case "31" -> "37";
                    case "32" -> "46";
                    default -> "Типовой паспорт не разработан";
                };
                default ->"Типовой паспорт не разработан";
            };
            case "2" -> switch (fakt) {
                case "-" -> switch (hifr) {
                    case "27" -> "2";
                    case "28" -> "11";
                    case "29" -> "20";
                    case "30" -> "29";
                    case "31" -> "38";
                    case "32" -> "47";
                    default -> "Типовой паспорт не разработан";
                };default ->"Типовой паспорт не разработан";};
            case "4" -> switch (fakt) {
                case "-" -> switch (hifr) {
                    case "27" -> "3";
                    case "28" -> "12";
                    case "29" -> "21";
                    case "30" -> "30";
                    case "31" -> "39";
                    case "32" -> "48";
                    default -> "Типовой паспорт не разработан";
                };default ->"Типовой паспорт не разработан";};
            case "6" -> switch (fakt) {
                case "АБ" -> switch (hifr) {
                    case "27" -> "4";
                    case "28" -> "13";
                    case "29" -> "22";
                    case "30" -> "31";
                    case "31" -> "40";
                    case "32" -> "49";
                    default -> "Типовой паспорт не разработан";
                };
                case "Б" -> switch (hifr) {
                    case "27" -> "5";
                    case "28" -> "14";
                    case "29" -> "23";
                    case "30" -> "32";
                    case "31" -> "41";
                    case "32" -> "50";
                    default -> "Типовой паспорт не разработан";
                };
                case "БВ" -> switch (hifr) {
                    case "27" -> "6";
                    case "28" -> "15";
                    case "29" -> "24";
                    case "30" -> "33";
                    case "31" -> "42";
                    case "32" -> "51";
                    default -> "Типовой паспорт не разработан";
                };default ->"Типовой паспорт не разработан";};
            case "7" -> switch (fakt) {
                case "АБ" -> switch (hifr) {
                    case "27" -> "7";
                    case "28" -> "16";
                    case "29" -> "25";
                    case "30" -> "34";
                    case "31" -> "43";
                    case "32" -> "52";
                    default -> "Типовой паспорт не разработан";
                };
                case "Б" -> switch (hifr) {
                    case "27" -> "8";
                    case "28" -> "17";
                    case "29" -> "26";
                    case "30" -> "35";
                    case "31" -> "44";
                    case "32" -> "53";
                    default -> "Типовой паспорт не разработан";
                };
                case "БВ" -> switch (hifr) {
                    case "27" -> "9";
                    case "28" -> "18";
                    case "29" -> "27";
                    case "30" -> "36";
                    case "31" -> "45";
                    case "32" -> "54";
                    default -> "Типовой паспорт не разработан";
                };default ->"Типовой паспорт не разработан";};
            default -> "Типовой паспорт не разработан";
        };}

}
