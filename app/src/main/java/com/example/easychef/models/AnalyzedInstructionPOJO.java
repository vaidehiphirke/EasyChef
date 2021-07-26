package com.example.easychef.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnalyzedInstructionPOJO {

    @SerializedName("steps")
    private List<StepPOJO> steps;
}
