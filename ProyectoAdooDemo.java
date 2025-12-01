import java.util.*;

public class ProyectoAdooDemo {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Store tienda = new Store();
        tienda.addProduct(new Product(1, "Cuaderno A5", 25.0, 10));
        tienda.addProduct(new Product(2, "Lapicero Azul", 7.5, 50));
        tienda.addProduct(new Product(3, "Resaltador Amarillo", 12.0, 20));

        Cart carrito = new Cart();

        int opcion = 0;

        do {
            System.out.println("\n===== MENÚ PAPELERÍA =====");
            System.out.println("1. Ver productos");
            System.out.println("2. Agregar producto al carrito");
            System.out.println("3. Ver carrito");
            System.out.println("4. Generar ticket");
            System.out.println("5. Salir");
            System.out.print("Elige una opción: ");
            opcion = leerInt(sc);

            switch (opcion) {
                case 1:
                    tienda.printProducts();
                    break;

                case 2:
                    System.out.print("Ingresa ID del producto: ");
                    int id = leerInt(sc);
                    Product p = tienda.findById(id);

                    if (p == null) {
                        System.out.println("Producto no encontrado.");
                        break;
                    }

                    System.out.print("Cantidad: ");
                    int qty = leerInt(sc);

                    if (!tienda.hasStock(id, qty)) {
                        System.out.println("No hay suficiente stock.");
                        break;
                    }

                    carrito.addItem(p, qty);
                    System.out.println("Agregado al carrito.");
                    break;

                case 3:
                    carrito.printCart();
                    break;

                case 4:
                    printTicket(carrito);
                    break;

                case 5:
                    System.out.println("Saliendo...");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 5);

        sc.close();
    }

    // ---- Ticket SENCILLO ----
    static void printTicket(Cart cart) {
        System.out.println("\n===============================");
        System.out.println("         TICKET DE COMPRA      ");
        System.out.println("===============================");
        System.out.println("Fecha: " + new Date());
        System.out.println("-------------------------------");

        for (CartItem item : cart.getItems()) {
            System.out.printf("%s  x%d   $%.2f\n",
                    item.product.name,
                    item.qty,
                    item.lineTotal());
        }

        System.out.println("-------------------------------");
        System.out.printf("TOTAL: $%.2f\n", cart.subtotal());
        System.out.println("===============================");
        System.out.println("¡Gracias por su compra!");
        System.out.println();
    }

    static int leerInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Ingresa un número válido: ");
            sc.next();
        }
        return sc.nextInt();
    }
}


class Product {
    public final int id;
    public final String name;
    public final double price;
    public int stock;

    public Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
}

class CartItem {
    public final Product product;
    public int qty;

    public CartItem(Product product, int qty) {
        this.product = product;
        this.qty = qty;
    }

    public double lineTotal() {
        return product.price * qty;
    }
}

class Cart {
    private final List<CartItem> items = new ArrayList<>();

    public void addItem(Product p, int qty) {
        items.add(new CartItem(p, qty));
    }

    public double subtotal() {
        double s = 0;
        for (CartItem i : items) {
            s += i.lineTotal();
        }
        return s;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void printCart() {
        if (items.isEmpty()) {
            System.out.println("Carrito vacío.");
            return;
        }

        System.out.println("\n--- CARRITO ---");
        for (CartItem it : items) {
            System.out.printf("%s x%d = $%.2f\n",
                    it.product.name,
                    it.qty,
                    it.lineTotal());
        }
        System.out.printf("Subtotal: $%.2f\n", subtotal());
    }
}

class Store {
    private final List<Product> catalog = new ArrayList<>();

    public void addProduct(Product p) {
        catalog.add(p);
    }

    public Product findById(int id) {
        for (Product p : catalog)
            if (p.id == id) return p;
        return null;
    }

    public boolean hasStock(int id, int qty) {
        Product p = findById(id);
        return p != null && p.stock >= qty;
    }

    public void printProducts() {
        System.out.println("\n--- PRODUCTOS ---");
        for (Product p : catalog) {
            System.out.printf("%d) %s - $%.2f Stock:%d\n",
                    p.id, p.name, p.price, p.stock);
        }
    }
}
