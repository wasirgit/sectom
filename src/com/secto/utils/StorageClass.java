package com.secto.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.secto.model.ActualHourDto;
import com.secto.model.DetailsDto;
import com.secto.model.WorkListDto;

public class StorageClass {

	private static  ArrayList<WorkListDto> worklist;
	private static  ArrayList<ActualHourDto> actualhourList;
	private static Map<String, ArrayList<DetailsDto>> detailsMap ;

	public static void setWorklist(ArrayList<WorkListDto> worklist) {
		StorageClass.worklist = worklist;
	}
	public static ArrayList<WorkListDto> getWorklist() {
		if(worklist == null){
			worklist = new ArrayList<WorkListDto>();
		}
		return worklist;
	}
	public static ArrayList<ActualHourDto> getActualhourList() {
		if(actualhourList == null){
			actualhourList = new ArrayList<ActualHourDto>();
		}
		return actualhourList;
	}
	public static void setActualhourList(ArrayList<ActualHourDto> actualhourList) {
		StorageClass.actualhourList = actualhourList;
	}
	public static Map<String, ArrayList<DetailsDto>> getDetailsMap() {
		if(detailsMap == null){
			detailsMap = new HashMap<String, ArrayList<DetailsDto>>();
		}
		return detailsMap;
	}
	public static void setDetailsMap(Map<String, ArrayList<DetailsDto>> detailsMap) {
		StorageClass.detailsMap = detailsMap;
	}
	
	
	
}
