import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PolynomialTableApp extends JFrame {
    private PolynomialTableModel tableModel;
    private JTable table;

    public PolynomialTableApp() {
        setTitle("Таблица значений многочлена");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Создание модели таблицы
        tableModel = new PolynomialTableModel();
        table = new JTable(tableModel);

        // Настройка отображения чекбоксов в третьем столбце
        table.getColumnModel().getColumn(2).setCellRenderer(table.getDefaultRenderer(Boolean.class));
        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JCheckBox()));

        // Создание главного меню
        JMenuBar menuBar = new JMenuBar();

        JMenu helpMenu = new JMenu("Справка");
        JMenuItem aboutItem = new JMenuItem("О программе");

        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAboutDialog();
            }
        });

        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        // Добавление компонентов
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Панель с кнопками для управления
        JPanel controlPanel = new JPanel();
        JButton addButton = new JButton("Добавить точку");
        JButton removeButton = new JButton("Удалить выбранную");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.addRow();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    tableModel.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(PolynomialTableApp.this,
                            "Выберите строку для удаления",
                            "Внимание",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        controlPanel.add(addButton);
        controlPanel.add(removeButton);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
                "Автор: Иванов И.И.\n" + // ← ЗАМЕНИТЕ на свою фамилию
                        "Группа: ПИ-123\n" +     // ← ЗАМЕНИТЕ на свою группу
                        "Программа для расчета значений многочлена",
                "О программе",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Модель таблицы с тремя столбцами
    class PolynomialTableModel extends AbstractTableModel {
        private String[] columnNames = {"X", "Значение многочлена", "Значение больше нуля?"};
        private Object[][] data = {
                {0.0, calculatePolynomial(0.0), calculatePolynomial(0.0) > 0},
                {1.0, calculatePolynomial(1.0), calculatePolynomial(1.0) > 0},
                {2.0, calculatePolynomial(2.0), calculatePolynomial(2.0) > 0},
                {3.0, calculatePolynomial(3.0), calculatePolynomial(3.0) > 0}
        };

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 2) {
                return Boolean.class; // Третий столбец - булевские значения
            }
            return Double.class; // Первые два столбца - числа
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 0; // Разрешаем редактирование только первого столбца (X)
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                try {
                    double x = Double.parseDouble(aValue.toString());
                    data[rowIndex][0] = x;
                    double value = calculatePolynomial(x);
                    data[rowIndex][1] = value;
                    data[rowIndex][2] = value > 0;
                    fireTableRowsUpdated(rowIndex, rowIndex);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(PolynomialTableApp.this,
                            "Введите корректное число",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        public void addRow() {
            Object[][] newData = new Object[data.length + 1][3];
            // Копируем существующие данные
            for (int i = 0; i < data.length; i++) {
                newData[i][0] = data[i][0];
                newData[i][1] = data[i][1];
                newData[i][2] = data[i][2];
            }

            // Добавляем новую строку
            double x = data.length > 0 ? (Double)data[data.length - 1][0] + 1 : 0;
            double value = calculatePolynomial(x);
            newData[data.length][0] = x;
            newData[data.length][1] = value;
            newData[data.length][2] = value > 0;

            data = newData;
            fireTableRowsInserted(data.length - 1, data.length - 1);
        }

        public void removeRow(int row) {
            if (data.length <= 1) {
                JOptionPane.showMessageDialog(PolynomialTableApp.this,
                        "Нельзя удалить последнюю строку",
                        "Внимание",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Object[][] newData = new Object[data.length - 1][3];
            for (int i = 0, k = 0; i < data.length; i++) {
                if (i == row) {
                    continue;
                }
                newData[k][0] = data[i][0];
                newData[k][1] = data[i][1];
                newData[k][2] = data[i][2];
                k++;
            }
            data = newData;
            fireTableRowsDeleted(row, row);
        }

        // Пример функции многочлена: f(x) = x^2 - 2x + 1
        private double calculatePolynomial(double x) {
            return x * x - 2 * x + 1; // Можно заменить на любой другой многочлен
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new PolynomialTableApp().setVisible(true);
            }
        });
    }
}