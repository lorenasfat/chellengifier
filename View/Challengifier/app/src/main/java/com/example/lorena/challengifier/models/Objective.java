package com.example.lorena.challengifier.models;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Lorena on 08.01.2017.
 */

public class Objective implements Serializable {
    private UUID Id;
    private String Name;
    private String Description;
    private Date Deadline;
    private String ExpectedOutcome;
    private UUID ChallengeId;
    private int Status;
    private Date StartDate;
    private Date EndDate;
    private String UserId;
    private double Grade;
    private int Progress;

    public double getGrade() {
        return Grade;
    }

    public void setGrade(double grade) {
        this.Grade = grade;
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public Date getDeadline() {
        return Deadline;
    }

    public void setDeadline(Date deadline) {
        this.Deadline = deadline;
    }

    public String getExpectedOutcome() {
        return ExpectedOutcome;
    }

    public int getProgress() {
        return Progress;
    }

    public void setProgress(int progress) {
        Progress = progress;
    }

    public void setExpectedOutcome(String expectedOutcome) {
        this.ExpectedOutcome = expectedOutcome;
    }

    public UUID getChallengeId() {
        return ChallengeId;
    }

    public void setChallengeId(UUID challengeId) {
        this.ChallengeId = challengeId;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        this.Status = status;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        this.StartDate = startDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
        this.EndDate = endDate;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        this.UserId = userId;
    }
}
