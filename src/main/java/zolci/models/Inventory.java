package zolci.models;

import jakarta.persistence.*;

/**
 * Klasa reprezentująca przedmioty w inwentarzu.
 */
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "unitPrice")
    private double unitPrice;

    @Column(name = "supplier")
    private String supplier;

    /**
     * Zwraca identyfikator przedmiotu w inwentarzu.
     *
     * @return identyfikator przedmiotu
     */
    public int getId() {
        return id;
    }

    /**
     * Ustawia identyfikator przedmiotu w inwentarzu.
     *
     * @param id identyfikator przedmiotu
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Zwraca nazwę przedmiotu w inwentarzu.
     *
     * @return nazwa przedmiotu
     */
    public String getName() {
        return name;
    }

    /**
     * Ustawia nazwę przedmiotu w inwentarzu.
     *
     * @param name nazwa przedmiotu
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Zwraca ilość dostępnych sztuk przedmiotu w inwentarzu.
     *
     * @return ilość sztuk przedmiotu
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Ustawia ilość dostępnych sztuk przedmiotu w inwentarzu.
     *
     * @param quantity ilość sztuk przedmiotu
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Zwraca jednostkową cenę przedmiotu w inwentarzu.
     *
     * @return jednostkowa cena przedmiotu
     */
    public double getUnitPrice() {
        return unitPrice;
    }

    /**
     * Ustawia jednostkową cenę przedmiotu w inwentarzu.
     *
     * @param unitPrice jednostkowa cena przedmiotu
     */
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * Zwraca dostawcę przedmiotu w inwentarzu.
     *
     * @return dostawca przedmiotu
     */
    public String getSupplier() {
        return supplier;
    }

    /**
     * Ustawia dostawcę przedmiotu w inwentarzu.
     *
     * @param supplier dostawca przedmiotu
     */
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}
