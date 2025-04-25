package zolci.models;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Klasa reprezentująca projekty.
 */
@Entity
@Table(name = "projects")
public class Projects {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;

    @Column(name = "startDate")
    private LocalDate startDate;

    @Column(name = "endDate")
    private LocalDate endDate;

    @Column(name = "valuation")
    private double valuation;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status statusId;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Teams teamId;

    /**
     * Zwraca nazwę projektu.
     *
     * @return nazwa projektu
     */
    public String getName() {
        return name;
    }

    /**
     * Zwraca zespół przypisany do projektu.
     *
     * @return zespół przypisany do projektu
     */
    public Teams getTeamId() {
        return teamId;
    }

    /**
     * Ustawia zespół przypisany do projektu.
     *
     * @param teamId zespół przypisany do projektu
     */

    public void setTeamId(Teams teamId) {
        this.teamId = teamId;
    }

    /**
     * Zwraca identyfikator projektu.
     *
     * @return identyfikator projektu
     */
    public int getId() {
        return id;
    }

    /**
     * Ustawia identyfikator projektu.
     *
     * @param id identyfikator projektu
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Zwraca nazwę projektu.
     *
     * @return nazwa projektu
     */
    public String getProjectName() {
        return name;
    }

    /**
     * Ustawia nazwę projektu.
     *
     * @param name nazwa projektu
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Zwraca datę rozpoczęcia projektu.
     *
     * @return data rozpoczęcia projektu
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Ustawia datę rozpoczęcia projektu.
     *
     * @param startDate data rozpoczęcia projektu
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Zwraca datę zakończenia projektu.
     *
     * @return data zakończenia projektu
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Ustawia datę zakończenia projektu.
     *
     * @param endDate data zakończenia projektu
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Zwraca wycenę projektu.
     *
     * @return wycena projektu
     */
    public double getValuation() {
        return valuation;
    }

    /**
     * Ustawia wycenę projektu.
     *
     * @param valuation wycena projektu
     */
    public void setValuation(double valuation) {
        this.valuation = valuation;
    }

    /**
     * Zwraca status projektu.
     *
     * @return status projektu
     */
    public Status getStatusId() {
        return statusId;
    }

    /**
     * Ustawia status projektu.
     *
     * @param statusId status projektu
     */
    public void setStatusId(Status statusId) {
        this.statusId = statusId;
    }
}
