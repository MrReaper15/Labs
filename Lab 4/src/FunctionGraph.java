import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class FunctionGraph extends JPanel {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int MARKER_SIZE = 11;
    private static final int POINT_COUNT = 200;
    private static final double X_MIN = -10;
    private static final double X_MAX = 10;

    // Функция для отображения (пример: y = x^2 + 2x + 1)
    private double function(double x) {
        return x * x + 2 * x + 1;
    }

    // Проверка условия: целая часть значения функции является квадратом целого числа
    private boolean isIntegerSquare(double y) {
        // Берем целую часть (не floor для отрицательных!)
        int intPart = (int) y;

        if (intPart < 0) {
            // Для отрицательных чисел берем модуль
            intPart = Math.abs(intPart);
        }

        if (intPart < 0) return false; // на случай переполнения

        int sqrt = (int) Math.sqrt(intPart);
        return sqrt * sqrt == intPart;
    }

    // Создание маркера в виде квадрата со стрелкой
    private Shape createMarker(double x, double y) {
        Path2D.Double marker = new Path2D.Double();

        // Проверяем, чтобы маркер не выходил за границы
        if (x < 0 || x > getWidth() || y < 0 || y > getHeight()) {
            return new Rectangle2D.Double(0, 0, 0, 0); // пустой маркер
        }

        // Квадрат 11x11 с центром в (x, y)
        marker.moveTo(x - 5.5, y - 5.5);
        marker.lineTo(x + 5.5, y - 5.5);
        marker.lineTo(x + 5.5, y + 5.5);
        marker.lineTo(x - 5.5, y + 5.5);
        marker.closePath();

        // Стрелка сверху
        marker.moveTo(x, y - 5.5);
        marker.lineTo(x - 3, y - 8.5);
        marker.lineTo(x + 3, y - 8.5);
        marker.closePath();

        return marker;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Очистка фона
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Находим минимальное и максимальное значение функции для масштабирования
        double yMin = Double.MAX_VALUE;
        double yMax = Double.MIN_VALUE;

        for (int i = 0; i <= POINT_COUNT; i++) {
            double x = X_MIN + (X_MAX - X_MIN) * i / POINT_COUNT;
            double y = function(x);
            if (y < yMin) yMin = y;
            if (y > yMax) yMax = y;
        }

        // Добавляем отступы
        double yRange = yMax - yMin;
        yMin -= yRange * 0.1;
        yMax += yRange * 0.1;

        // Масштабирование
        double xScale = getWidth() / (X_MAX - X_MIN);
        double yScale = getHeight() / (yMax - yMin);

        // Оси координат
        g2d.setColor(Color.LIGHT_GRAY);

        // Ось X
        double zeroY = getHeight() - (-yMin) * yScale;
        if (zeroY >= 0 && zeroY <= getHeight()) {
            g2d.drawLine(0, (int)zeroY, getWidth(), (int)zeroY);
        }

        // Ось Y
        double zeroX = -X_MIN * xScale;
        if (zeroX >= 0 && zeroX <= getWidth()) {
            g2d.drawLine((int)zeroX, 0, (int)zeroX, getHeight());
        }

        // Отображение графика функции
        Path2D.Double path = new Path2D.Double();
        boolean first = true;

        for (int i = 0; i <= POINT_COUNT; i++) {
            double x = X_MIN + (X_MAX - X_MIN) * i / POINT_COUNT;
            double y = function(x);

            // Преобразование координат
            double screenX = (x - X_MIN) * xScale;
            double screenY = getHeight() - (y - yMin) * yScale;

            // Проверяем границы
            if (screenY < 0) screenY = 0;
            if (screenY > getHeight()) screenY = getHeight();

            // Рисование линии
            if (first) {
                path.moveTo(screenX, screenY);
                first = false;
            } else {
                path.lineTo(screenX, screenY);
            }
        }

        // Рисуем линию графика
        g2d.setColor(new Color(30, 144, 255));
        g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.draw(path);

        // Рисуем маркеры в точках
        for (int i = 0; i <= POINT_COUNT; i += 5) { // Каждую 5-ю точку
            double x = X_MIN + (X_MAX - X_MIN) * i / POINT_COUNT;
            double y = function(x);

            double screenX = (x - X_MIN) * xScale;
            double screenY = getHeight() - (y - yMin) * yScale;

            // Проверяем, чтобы точка была в пределах видимой области
            if (screenX >= 0 && screenX <= getWidth() &&
                    screenY >= 0 && screenY <= getHeight()) {

                // Создаем маркер
                Shape marker = createMarker(screenX, screenY);

                // Проверяем условие
                if (isIntegerSquare(y)) {
                    // Точки, удовлетворяющие условию - красным цветом
                    g2d.setColor(new Color(220, 20, 60));
                    g2d.fill(marker);
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(1f));
                    g2d.draw(marker);
                } else {
                    // Остальные точки - синим цветом
                    g2d.setColor(new Color(30, 144, 255, 200));
                    g2d.fill(marker);
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(1f));
                    g2d.draw(marker);
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("График функции с модифицированными маркерами");
            FunctionGraph graph = new FunctionGraph();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(graph);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}