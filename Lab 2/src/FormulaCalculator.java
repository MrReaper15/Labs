import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormulaCalculator extends JFrame {
    private JTextField xField, yField, zField, resultField, sumField;
    private JButton calculateBtn1, calculateBtn2, mcBtn, mPlusBtn;
    private double sum = 0.0;

    public FormulaCalculator() {
        setTitle("Калькулятор формул");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Поля ввода
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("X:"), gbc);

        gbc.gridx = 1;
        xField = new JTextField(10);
        xField.setText("1.0");
        add(xField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Y:"), gbc);

        gbc.gridx = 1;
        yField = new JTextField(10);
        yField.setText("1.0");
        add(yField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Z:"), gbc);

        gbc.gridx = 1;
        zField = new JTextField(10);
        zField.setText("1.0");
        add(zField, gbc);

        // Кнопки вычисления
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        calculateBtn1 = new JButton("Вычислить формулу №1");
        add(calculateBtn1, gbc);

        gbc.gridy = 4;
        calculateBtn2 = new JButton("Вычислить формулу №2");
        add(calculateBtn2, gbc);

        // Кнопки памяти
        gbc.gridy = 5; gbc.gridwidth = 1;
        mcBtn = new JButton("MC");
        add(mcBtn, gbc);

        gbc.gridx = 1;
        mPlusBtn = new JButton("M+");
        add(mPlusBtn, gbc);

        // Поля результатов
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        add(new JLabel("Результат:"), gbc);

        gbc.gridy = 7;
        resultField = new JTextField(15);
        resultField.setEditable(false);
        add(resultField, gbc);

        gbc.gridy = 8;
        add(new JLabel("Сумма (M):"), gbc);

        gbc.gridy = 9;
        sumField = new JTextField(15);
        sumField.setEditable(false);
        sumField.setText("0.0");
        add(sumField, gbc);

        // Обработчики событий
        calculateBtn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateFormula1();
            }
        });

        calculateBtn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateFormula2();
            }
        });

        mcBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sum = 0.0;
                sumField.setText("0.0");
                resultField.setText("Память очищена");
            }
        });

        mPlusBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String text = resultField.getText();
                    if (text.startsWith("Ошибка") || text.startsWith("Сначала") || text.startsWith("Память")) {
                        resultField.setText("Нельзя добавить: " + text);
                        return;
                    }
                    double currentResult = Double.parseDouble(text);
                    sum += currentResult;
                    sumField.setText(String.format("%.6f", sum));
                    resultField.setText("Добавлено к сумме: " + String.format("%.6f", currentResult));
                } catch (NumberFormatException ex) {
                    resultField.setText("Ошибка: результат не является числом");
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void calculateFormula1() {
        try {
            double x = Double.parseDouble(xField.getText());
            double y = Double.parseDouble(yField.getText());
            double z = Double.parseDouble(zField.getText());

            // Проверка области определения
            if (x <= 0) {
                resultField.setText("Ошибка: x должен быть > 0");
                return;
            }

            if (1 + y <= 0) {
                resultField.setText("Ошибка: y должен быть > -1 для ln(1+y)");
                return;
            }

            // F(x, y, z) = (cos(e^x) + ln(1+y))^2 + sqrt(e^{cos x} + sin^2 x) + sqrt(1/x + cos(y^2)) * sin z
            // Часть 1: (cos(e^x) + ln(1+y))^2
            double cosExpX;
            try {
                cosExpX = Math.cos(Math.exp(x));
            } catch (Exception e) {
                resultField.setText("Ошибка: переполнение при вычислении e^x");
                return;
            }

            double ln1y = Math.log(1 + y);
            double part1 = cosExpX + ln1y;
            double part1Squared = part1 * part1;

            // Часть 2: sqrt(e^{cos x} + sin^2 x)
            double expCosX = Math.exp(Math.cos(x));
            double sin2x = Math.sin(x) * Math.sin(x);
            double arg2 = expCosX + sin2x;

            if (arg2 < 0) {
                resultField.setText("Ошибка: отрицательное значение под sqrt в части 2");
                return;
            }
            double part2 = Math.sqrt(arg2);

            // Часть 3: sqrt(1/x + cos(y^2)) * sin z
            double invX = 1.0 / x;
            double cosY2 = Math.cos(y * y);
            double arg3 = invX + cosY2;

            if (arg3 < 0) {
                resultField.setText("Ошибка: отрицательное значение под sqrt в части 3");
                return;
            }
            double part3 = Math.sqrt(arg3) * Math.sin(z);

            double result = part1Squared + part2 + part3;

            // Проверка на особые значения
            if (Double.isNaN(result) || Double.isInfinite(result)) {
                resultField.setText("Ошибка: результат не является числом или бесконечен");
                return;
            }

            resultField.setText(String.format("%.10f", result));

        } catch (NumberFormatException e) {
            resultField.setText("Ошибка ввода: введите числа");
        } catch (Exception e) {
            resultField.setText("Ошибка вычисления: " + e.getMessage());
        }
    }

    private void calculateFormula2() {
        try {
            double x = Double.parseDouble(xField.getText());
            double y = Double.parseDouble(yField.getText());
            double z = Double.parseDouble(zField.getText());

            // F(x, y, z) = кубический корень(1 + x^2) / e^{sin(z) + x}
            double x2 = x * x;
            double numerator = Math.cbrt(1 + x2);

            double expArg = Math.sin(z) + x;
            double denominator = Math.exp(expArg);

            if (denominator == 0) {
                resultField.setText("Ошибка: деление на ноль");
                return;
            }

            if (Double.isInfinite(denominator)) {
                resultField.setText("Ошибка: переполнение при вычислении экспоненты");
                return;
            }

            double result = numerator / denominator;

            if (Double.isNaN(result) || Double.isInfinite(result)) {
                resultField.setText("Ошибка: результат не является числом");
                return;
            }

            resultField.setText(String.format("%.10f", result));

        } catch (NumberFormatException e) {
            resultField.setText("Ошибка ввода: введите числа");
        } catch (Exception e) {
            resultField.setText("Ошибка вычисления: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormulaCalculator();
            }
        });
    }
}