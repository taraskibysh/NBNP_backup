package com.nbnp.trip_to_go.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title", length = 250, nullable = false)
    private String title;

    @Column(name = "trip_description", columnDefinition = "TEXT")
    private String tripDescription;

    @Column(name = "avatar_image", length = 100)
    private String avatarImage;

    @Column(name = "group_link", length = 250)
    private String groupLink;

    @Column(name = "finance_link", length = 250)
    private String financeLink;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTrip> userTrips;

    public Trip() {
    }

    public Trip(Integer id, String title, String tripDescription, String avatarImage, String groupLink, String financeLink,
                LocalDateTime startDate, LocalDateTime endDate, Boolean isActive, List<UserTrip> userTrips) {
        this.id = id;
        this.title = title;
        this.tripDescription = tripDescription;
        this.avatarImage = avatarImage;
        this.groupLink = groupLink;
        this.financeLink = financeLink;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.userTrips = userTrips;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTripDescription() {
        return tripDescription;
    }

    public void setTripDescription(String tripDescription) {
        this.tripDescription = tripDescription;
    }

    public String getGroupLink() {
        return groupLink;
    }

    public void setGroupLink(String groupLink) {
        this.groupLink = groupLink;
    }

    public String getFinanceLink() {
        return financeLink;
    }

    public void setFinanceLink(String financeLink) {
        this.financeLink = financeLink;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<UserTrip> getUserTrips() {
        return userTrips;
    }

    public void setUserTrips(List<UserTrip> userTrips) {
        this.userTrips = userTrips;
    }

    public String getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }
}
