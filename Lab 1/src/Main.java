// 1. Основной абстрактный класс Food
abstract class Food {
    protected String name;

    public Food(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        // Сравниваем только по имени класса
        if (this == obj) return true;
        if (obj == null) return false;
        return this.getClass() == obj.getClass(); // Исправлено: было getClass() != obj.getClass()
    }

    @Override
    public String toString() {
        return name;
    }

    // Добавим getter для имени
    public String getName() {
        return name;
    }
}

// 2. Классы продуктов (остаются без изменений, но добавим аннотации)
class Tea extends Food {
    private String color; // чёрный, зелёный

    public Tea(String color) {
        super("Чай");
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + color + ")";
    }
}

class Pie extends Food {
    private String filling; // вишнёвая, клубничная, яблочная

    public Pie(String filling) {
        super("Пирог");
        this.filling = filling;
    }

    public String getFilling() {
        return filling;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + filling + " начинка)";
    }
}

class Milk extends Food {
    private String fat; // 1.5%, 2.5%, 5%

    public Milk(String fat) {
        super("Молоко");
        this.fat = fat;
    }

    public String getFat() {
        return fat;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + fat + " жирность)";
    }
}

class Potatoes extends Food {
    private String type; // жареная, вареная, фри

    public Potatoes(String type) {
        super("Картошка");
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + type + ")";
    }
}

class Burger extends Food {
    private String size; // малый, большой, средний

    public Burger(String size) {
        super("Гамбургер");
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + size + ")";
    }
}

class Coffee extends Food {
    private String aroma; // насыщенный, горький, восточный

    public Coffee(String aroma) {
        super("Кофе");
        this.aroma = aroma;
    }

    public String getAroma() {
        return aroma;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + aroma + " аромат)";
    }
}

class IceCream extends Food {
    private String sirup; // карамель, шоколад

    public IceCream(String sirup) {
        super("Мороженое");
        this.sirup = sirup;
    }

    public String getSirup() {
        return sirup;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + sirup + " сироп)";
    }
}

class ChewingGum extends Food {
    private String flavour; // мята, арбуз, вишня

    public ChewingGum(String flavour) {
        super("Жевательная резинка");
        this.flavour = flavour;
    }

    public String getFlavour() {
        return flavour;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + flavour + " вкус)";
    }
}

class Eggs extends Food {
    private String number; // одно, два, три

    public Eggs(String number) {
        super("Яйца");
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + number + " яйца)";
    }
}

class Lemonade extends Food {
    private String taste; // лимон, апельсин, клубника

    public Lemonade(String taste) {
        super("Лимонад");
        this.taste = taste;
    }

    public String getTaste() {
        return taste;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + taste + " вкус)";
    }
}

class Cake extends Food {
    private String icing; // шоколадная, сливочная, карамель

    public Cake(String icing) {
        super("Пирожное");
        this.icing = icing;
    }

    public String getIcing() {
        return icing;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + icing + " глазурь)";
    }
}

class Beef extends Food {
    private String preparedness; // с кровью, норма, прожаренное

    public Beef(String preparedness) {
        super("Мясо");
        this.preparedness = preparedness;
    }

    public String getPreparedness() {
        return preparedness;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + preparedness + " прожарка)";
    }
}

// 3. Класс завтрака
class Breakfast {
    private Food[] foods;

    public Breakfast(Food[] foods) {
        this.foods = foods;
    }

    // Метод подсчета продуктов заданного типа без сравнения по внутренним полям
    public int countFoodType(Class<? extends Food> foodType) {
        int count = 0;
        for (Food food : foods) {
            if (food.getClass().equals(foodType)) {
                count++;
            }
        }
        return count;
    }

    // Метод для отображения завтрака
    public void displayBreakfast() {
        System.out.println("Завтрак состоит из:");
        for (Food food : foods) {
            System.out.println("  - " + food);
        }
        System.out.println();
    }

    // Метод для подсчета с использованием equals()
    public int countFoodTypeUsingEquals(Food sample) {
        int count = 0;
        for (Food food : foods) {
            if (food.equals(sample)) {
                count++;
            }
        }
        return count;
    }
}

// 4. Основная программа
public class Main {
    public static void main(String[] args) {
        // Создаем продукты для завтрака
        Food[] breakfastFoods = {
                new Tea("чёрный"),
                new Tea("зелёный"),
                new Pie("яблочная"),
                new Milk("2.5%"),
                new Pie("вишнёвая"),
                new Tea("чёрный"),
                new Eggs("два"),
                new Coffee("насыщенный"),
                new Pie("клубничная"),
                new Milk("5%")
        };

        // Создаем завтрак
        Breakfast breakfast = new Breakfast(breakfastFoods);

        // Отображаем завтрак
        breakfast.displayBreakfast();

        // Подсчитываем продукты заданного типа
        System.out.println("Подсчет продуктов (через сравнение классов):");
        System.out.println("Чай: " + breakfast.countFoodType(Tea.class));
        System.out.println("Пирог: " + breakfast.countFoodType(Pie.class));
        System.out.println("Молоко: " + breakfast.countFoodType(Milk.class));
        System.out.println("Кофе: " + breakfast.countFoodType(Coffee.class));
        System.out.println("Яйца: " + breakfast.countFoodType(Eggs.class));

        System.out.println("\nПодсчет продуктов (через equals()):");
        System.out.println("Чай: " + breakfast.countFoodTypeUsingEquals(new Tea("чёрный")));
        System.out.println("Пирог: " + breakfast.countFoodTypeUsingEquals(new Pie("яблочная")));
        System.out.println("Молоко: " + breakfast.countFoodTypeUsingEquals(new Milk("2.5%")));

        // Демонстрация работы equals() без учета внутренних полей
        System.out.println("\nДемонстрация работы equals():");
        Tea tea1 = new Tea("чёрный");
        Tea tea2 = new Tea("зелёный");
        Tea tea3 = new Tea("чёрный");
        Pie pie1 = new Pie("яблочная");

        System.out.println("tea1.equals(tea2): " + tea1.equals(tea2)); // true (один класс)
        System.out.println("tea1.equals(tea3): " + tea1.equals(tea3)); // true (один класс)
        System.out.println("tea1.equals(pie1): " + tea1.equals(pie1)); // false (разные классы)

        // Проверка с null
        System.out.println("tea1.equals(null): " + tea1.equals(null)); // false

        // Проверка рефлексивности
        System.out.println("tea1.equals(tea1): " + tea1.equals(tea1)); // true
    }
}