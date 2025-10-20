package com.example.proburok;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KrepController extends Configs {

    @FXML
    private TextField widthField;

    @FXML
    private TextField heightField;

    @FXML
    private Pane krepContainer;

    @FXML
    private Label infoLabel;

    @FXML
    private Button drawButton;

    @FXML
    private Button exportDxfButton; // Добавляем кнопку для экспорта

    private Canvas canvas;
    private GraphicsContext gc;

    // Текущие размеры выработки
    private double currentWidth = 0;
    private double currentHeight = 0;

    // Параметры для DXF экспорта
    private double lastOffsetX = 0;
    private double lastOffsetY = 0;
    private double lastWidthPixels = 0;
    private double lastHeightPixels = 0;
    private double lastPixelsPerMeter = 0;

    @FXML
    public void initialize() {
        System.out.println("Инициализация контроллера крепи...");

        // Создаем Canvas для рисования
        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();

        // Добавляем Canvas в контейнер
        krepContainer.getChildren().add(canvas);

        // Привязываем размеры Canvas к размерам контейнера
        canvas.widthProperty().bind(krepContainer.widthProperty());
        canvas.heightProperty().bind(krepContainer.heightProperty());

        // Добавляем слушатели изменений размеров
        krepContainer.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (currentWidth > 0 && currentHeight > 0) {
                drawKrep(currentWidth, currentHeight);
            }
        });

        krepContainer.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (currentWidth > 0 && currentHeight > 0) {
                drawKrep(currentWidth, currentHeight);
            }
        });

        // Устанавливаем начальные значения
        widthField.setText("2.0");
        heightField.setText("2.0");

        // Очищаем canvas при инициализации
        clearCanvas();
    }

    @FXML
    private void drawKrepAction() {
        try {
            double width = Double.parseDouble(widthField.getText());
            double height = Double.parseDouble(heightField.getText());

            if (width <= 0 || height <= 0) {
                infoLabel.setText("Ошибка: значения должны быть больше 0");
                return;
            }

            if (width > 8 || height > 8) {
                infoLabel.setText("Ошибка: значения не должны превышать 8 метров");
                return;
            }

            currentWidth = width;
            currentHeight = height;

            drawKrep(width, height);
            infoLabel.setText("Крепь нарисована. Ширина: " + width + " м, Высота: " + height + " м");
            exportToJpg();

        } catch (NumberFormatException e) {
            infoLabel.setText("Ошибка: введите числовые значения");
        }
    }

    @FXML
    private void exportToDxf() {
        if (currentWidth == 0 || currentHeight == 0) {
            infoLabel.setText("Ошибка: сначала нарисуйте крепь");
            return;
        }

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Сохранить DXF файл");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("DXF files (*.dxf)", "*.dxf")
            );

            // Генерируем имя файла по умолчанию
            String defaultName = "крепь_" + currentWidth + "x" + currentHeight + "м_" +
                    new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".dxf";
            fileChooser.setInitialFileName(defaultName);

            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                generateDxfFile(file.getAbsolutePath());
                infoLabel.setText("DXF файл сохранен: " + file.getName());
            }
        } catch (Exception e) {
            infoLabel.setText("Ошибка при сохранении DXF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void clearCanvas() {
        clearCanvasArea();
        infoLabel.setText("Введите параметры выработки и нажмите 'Нарисовать крепь'");
        currentWidth = 0;
        currentHeight = 0;
    }

    private void clearCanvasArea() {
        if (gc != null) {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            // Рисуем белый фон
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }

    private void drawKrep(double width, double height) {
        if (gc == null) {
            System.err.println("Графический контекст не инициализирован!");
            return;
        }

        // Очищаем Canvas
        clearCanvasArea();

        double PIXELS_PER_METER;
        if (height <= 4) {
            PIXELS_PER_METER = 100.0;
        } else {
            PIXELS_PER_METER = 70.0;
        }

        double widthPixels = width * PIXELS_PER_METER;
        double heightPixels = height * PIXELS_PER_METER;

        // Сохраняем параметры для DXF экспорта
        lastOffsetX = (canvas.getWidth() - widthPixels) / 2;
        lastOffsetY = (canvas.getHeight() - heightPixels) / 2;
        lastWidthPixels = widthPixels;
        lastHeightPixels = heightPixels;
        lastPixelsPerMeter = PIXELS_PER_METER;

        // Рассчитываем смещение для центрирования
        double offsetX = lastOffsetX;
        double offsetY = lastOffsetY;

        // Рисуем основную прямоугольную выработку
        drawMainTunnel(offsetX, offsetY, widthPixels, heightPixels);

        // Добавляем текстовые метки с размерами
        drawDimensionLabels(offsetX, offsetY, widthPixels, heightPixels, width, height);
    }

    // Рисование основной прямоугольной выработки
    private void drawMainTunnel(double offsetX, double offsetY, double widthPixels, double heightPixels) {
        double hr = widthPixels / 3.0;
        double alfaR = Math.atan((2.0 * hr) / widthPixels);
        double alfa = Math.toDegrees(alfaR);
        double beta = 90 - alfa;

        double sinA = Math.sin(alfaR);
        double cosA = Math.cos(alfaR);
        double sin2A = Math.sin(alfaR * 2);

        double r = (widthPixels * (((2.0/3.0) * sinA) + cosA - 1)) / (2 * (sinA + cosA - 1));
        double R = (widthPixels * ((1.0/3.0) - (cosA/(2*(1-sinA))))) / (1 - cosA - (sin2A/(2*(1-sinA))));

        // Установка цвета линии
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        // Рисуем стены и пол
        gc.strokeLine(offsetX, offsetY + hr, offsetX, offsetY + heightPixels);
        gc.strokeLine(offsetX + widthPixels, offsetY + hr, offsetX + widthPixels, offsetY + heightPixels);
        gc.strokeLine(offsetX, offsetY + heightPixels, offsetX + widthPixels, offsetY + heightPixels);

        // Рисуем дуги
        gc.strokeArc(offsetX, offsetY + hr - r, r * 2, r * 2, 180 - beta, beta, ArcType.OPEN);
        gc.strokeArc(offsetX + widthPixels - r * 2, offsetY + hr - r, r * 2, r * 2, 0, beta, ArcType.OPEN);
        gc.strokeArc(offsetX + (widthPixels/2) - R, offsetY, R * 2, R * 2, beta, 180 - 2 * beta, ArcType.OPEN);
    }

    // Добавление текстовых меток с размерами
    private void drawDimensionLabels(double offsetX, double offsetY, double widthPixels, double heightPixels, double width, double height) {
        gc.setFill(Color.BLACK);

        // Подпись ширины
        String widthText = String.format("Ширина: %.1f м", width);
        gc.fillText(widthText, offsetX + 10, offsetY - 15);

        // Подпись высоты
        String heightText = String.format("Высота: %.1f м", height);
        gc.fillText(heightText, offsetX - 70, offsetY + 25);

        // Стрелки для обозначения размеров
        drawDimensionArrow(offsetX, offsetY - 10, offsetX + widthPixels, offsetY - 10, true); // Ширина
        drawDimensionArrow(offsetX - 10, offsetY, offsetX - 10, offsetY + heightPixels, false); // Высота
    }

    // Рисование стрелок для обозначения размеров
    private void drawDimensionArrow(double startX, double startY, double endX, double endY, boolean isHorizontal) {
        gc.setStroke(Color.DARKBLUE);
        gc.setLineWidth(1);

        // Линия размера
        gc.strokeLine(startX, startY, endX, endY);

        // Стрелки на концах
        double arrowSize = 5;
        if (isHorizontal) {
            // Горизонтальная линия - вертикальные стрелки
            gc.strokeLine(startX, startY - arrowSize, startX, startY + arrowSize);
            gc.strokeLine(endX, endY - arrowSize, endX, endY + arrowSize);
        } else {
            // Вертикальная линия - горизонтальные стрелки
            gc.strokeLine(startX - arrowSize, startY, startX + arrowSize, startY);
            gc.strokeLine(endX - arrowSize, endY, endX + arrowSize, endY);
        }
    }

    // Генерация DXF файла
    // Генерация корректного DXF файла
    // Генерация корректного DXF файла
    // УЛЬТРА-ПРОСТОЙ гарантированно работающий DXF
    // Исправленная версия генерации DXF файла
    private void generateDxfFile(String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename);
             OutputStreamWriter writer = new OutputStreamWriter(fos, java.nio.charset.StandardCharsets.ISO_8859_1)) {

            // Минимальный корректный DXF файл в кодировке ISO-8859-1
            writer.write("0\nSECTION\n2\nHEADER\n");
            writer.write("9\n$ACADVER\n1\nAC1009\n");
            writer.write("0\nENDSEC\n");
            writer.write("0\nSECTION\n2\nTABLES\n");
            writer.write("0\nTABLE\n2\nLAYER\n70\n1\n");
            writer.write("0\nLAYER\n2\n0\n70\n0\n62\n7\n6\nCONTINUOUS\n");
            writer.write("0\nENDTAB\n");
            writer.write("0\nENDSEC\n");
            writer.write("0\nSECTION\n2\nENTITIES\n");

            // Создаем простую геометрию крепи
            double scale = 1000.0; // Масштаб для удобного отображения в AutoCAD
            double width = currentWidth * scale;
            double height = currentHeight * scale;

            // Расчет параметров крепи (аналогично JavaFX коду)
            double hr = width / 3.0;
            double hw = height-hr;

            double alfaR = Math.atan((2.0 * hr) / width);
            double alfa = Math.toDegrees(alfaR);
            double beta = 90 - alfa;

            double sinA = Math.sin(alfaR);
            double cosA = Math.cos(alfaR);
            double sin2A = Math.sin(alfaR * 2);

            double r = (width * (((2.0/3.0) * sinA) + cosA - 1)) / (2 * (sinA + cosA - 1));
            double R = (width * ((1.0/3.0) - (cosA/(2*(1-sinA))))) / (1 - cosA - (sin2A/(2*(1-sinA))));

            // Центрирование
            double startX = 0;
            double startY = 0;

            // Рисуем прямоугольную часть
            // Левая стена
            addLineToDxf(writer,startX,startY,startX,(startY + hw));
            // Правая стена
            addLineToDxf(writer,(startX + width),startY,(startX + width),(startY + hw));
            // Пол
            addLineToDxf(writer,startX,startY,(startX + width),startY );

            // Левая дуга
            addArcToDxf(writer,(startX + r),(startY + hw),r ,(180 - beta),180);
            // Правая дуга
            addArcToDxf(writer,(startX + width - r),(startY + hw),r ,0,beta);
            // Верхняя дуга
            addArcToDxf(writer,(startX + width/2),(startY+height - R),R ,beta,(180 - beta));

            // Добавляем текст на английском чтобы избежать проблем с кодировкой
            writer.write("0\nTEXT\n8\n0\n");
            writer.write("10\n" + (width/2 - 50) + "\n20\n" + (startY + height + 20) + "\n30\n0.0\n");
            writer.write("40\n40.0\n");
            writer.write("1\nWidth: " + currentWidth + "m Height: " + currentHeight + "m\n");
//            TEXT - тип объекта: текст;10 - X координата вставки текста;width/2 - 50 - значение X (центрирование);
//            20 - Y координата вставки текста;startY + height + 20 - значение Y (над чертежом); 40 - высота текста;
//            10.0 - значение высоты текста;1 - собственно текстовая строка;

            writer.write("0\nENDSEC\n");
            writer.write("0\nEOF\n");

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при создании DXF файла", e);
        }
    }
    // Вспомогательный метод для добавления линии в DXF
    private void addLineToDxf(OutputStreamWriter writer, double x1, double y1, double x2, double y2) throws IOException {
        writer.write("0\nLINE\n8\n0\n");
        writer.write("10\n" + x1 + "\n20\n" + y1 + "\n30\n0.0\n");
        writer.write("11\n" + x2 + "\n21\n" + y2 + "\n31\n0.0\n");
        //0 - начало нового объекта; LINE - тип объекта: линия; 8 - код группы для слоя; 0 - имя слоя (слой 0)
        //10 - X координата начальной точки;0.0 - значение X начальной точки; 20 - Y координата начальной точки
//      startY - значение Y начальной точки;30 - Z координата начальной точки;0.0 - значение Z (2D чертеж)
//11 - X координата конечной точки;0.0 - значение X конечной точки;21 - Y координата конечной точки;
//          startY + height - значение Y конечной точки;31 - Z координата конечной точки;0.0 - значение Z
    }

    // Вспомогательный метод для добавления дуги в DXF
    private void addArcToDxf(OutputStreamWriter writer, double cx, double cy, double radius,
                             double startAngle, double endAngle) throws IOException {
        writer.write("0\nARC\n8\n0\n");
        writer.write("10\n" + cx + "\n20\n" + cy + "\n30\n0.0\n");
        writer.write("40\n" + radius + "\n");
        writer.write("50\n" + startAngle + "\n51\n" + endAngle + "\n");
        //  10 - X координата центра дуги;centerX - значение X центра;20 - Y координата центра дуги;arcStartY - значение Y центра;
        //  40 - радиус дуги;arcRadius - значение радиуса;
        //  50 - начальный угол в градусах;0.0 - значение начального угла;51 - конечный угол в градусах;180.0 - значение конечного угла;
    }

    // Вспомогательный метод для добавления текста
    private void addTextToDxf(FileWriter writer, double x, double y, double height, String text) throws IOException {
        writer.write("0\nTEXT\n8\n0\n");
        writer.write("10\n" + x + "\n20\n" + y + "\n30\n0.0\n");
        writer.write("40\n" + height + "\n");
        writer.write("1\n" + text + "\n");
    }

    private void exportToJpg() {
        if (currentWidth == 0 || currentHeight == 0) {
            infoLabel.setText("Ошибка: сначала нарисуйте крепь");
            return;
        }

        try {

            String desktopPath = Put;
            String folderName = "/крепи_выработок";

            // Создаем папку, если ее нет
            File folder = new File(desktopPath + folderName);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Генерируем имя файла по умолчанию
            String defaultName = "крепь_" + currentWidth + "x" + currentHeight + "м_" +
                    new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";

            // Показываем диалог сохранения
            File file = new File(folder, defaultName);
            if (file != null) {
                saveCanvasAsJpg(file);
                infoLabel.setText("JPG файл сохранен: " + file.getName());
            }
        } catch (Exception e) {
            infoLabel.setText("Ошибка при сохранении JPG: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void saveCanvasAsJpg(File file) {
        try {
            // 1. Создаем WritableImage для захвата содержимого Canvas
            WritableImage writableImage = new WritableImage(
                    (int) canvas.getWidth(),
                    (int) canvas.getHeight()
            );
            // 2. Настраиваем параметры снимка
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.WHITE); // Устанавливаем белый фон
//            SnapshotParameters настраивает как делать снимок
//            setFill(Color.WHITE) гарантирует, что прозрачные области будут белыми

            // Делаем снимок
            WritableImage image = canvas.snapshot(params, writableImage);

            // Сохраняем через BufferedImage (более надежный способ)
            BufferedImage bufferedImage = new BufferedImage(
                    (int) image.getWidth(),
                    (int) image.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );

            // 5. Конвертируем пиксели из JavaFX в AWT формат
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    // Получаем цвет пикселя из JavaFX изображения
                    javafx.scene.paint.Color fxColor = image.getPixelReader().getColor(x, y);
                    // Конвертируем в AWT Color
                    java.awt.Color awtColor = new java.awt.Color(
                            (float) fxColor.getRed(),
                            (float) fxColor.getGreen(),
                            (float) fxColor.getBlue(),
                            (float) fxColor.getOpacity()
                    );
                    // Устанавливаем пиксель в BufferedImage
                    bufferedImage.setRGB(x, y, awtColor.getRGB());
                }
            }

            // Сохраняем как JPG
            ImageIO.write(bufferedImage, "jpg", file);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении JPG", e);
        }
    }
    public static void showKrepEditor() {
        try {
            FXMLLoader loader = new FXMLLoader(KrepController.class.getResource("krep_editor.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Редактор крепи горной выработки");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();

        } catch (IOException e) {
            System.err.println("Ошибка загрузки FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }
}