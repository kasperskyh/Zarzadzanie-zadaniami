package zolci.models;

import jakarta.persistence.*;

/**
 * Klasa reprezentująca powiązanie między zadaniem a zapasami.
 */
@Entity
@Table(name = "task_inventory")
public class TaskInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Tasks task;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "value")
    private double value;

    /**
     * Zwraca ilość zapasów.
     *
     * @return ilość zapasów
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Ustawia ilość zapasów.
     *
     * @param quantity ilość zapasów
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Zwraca identyfikator powiązania między zadaniem a zapasami.
     *
     * @return identyfikator powiązania
     */
    public int getId() {
        return id;
    }

    /**
     * Ustawia identyfikator powiązania między zadaniem a zapasami.
     *
     * @param id identyfikator powiązania
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Zwraca zadanie, z którym powiązane są zapasy.
     *
     * @return zadanie
     */
    public Tasks getTask() {
        return task;
    }

    /**
     * Ustawia zadanie, z którym powiązane są zapasy.
     *
     * @param task zadanie
     */
    public void setTask(Tasks task) {
        this.task = task;
    }

    /**
     * Zwraca zapas, który jest powiązany z zadaniem.
     *
     * @return zapas
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Ustawia zapas, który jest powiązany z zadaniem.
     *
     * @param inventory zapas
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Zwraca wartość powiązania między zadaniem a zapasami.
     *
     * @return wartość powiązania
     */
    public double getValue() {
        return value;
    }

    /**
     * Ustawia wartość powiązania między zadaniem a zapasami.
     *
     * @param value wartość powiązania
     */
    public void setValue(double value) {
        this.value = value;
    }
}
